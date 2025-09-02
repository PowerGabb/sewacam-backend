package com.project.java.sewacam.controller;

import com.project.java.sewacam.dto.UploadResponseDTO;
import com.project.java.sewacam.model.Camera;
import com.project.java.sewacam.model.Category;
import com.project.java.sewacam.model.User;
import com.project.java.sewacam.repository.CameraRepository;
import com.project.java.sewacam.repository.CategoryRepository;
import com.project.java.sewacam.repository.UserRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/cameras")
public class CameraController {

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public List<Camera> getAllCameras() {
        return cameraRepository.findAll();
    }

    @GetMapping("/category/{categoryId}")
    public List<Camera> getCamerasByCategory(@PathVariable Integer categoryId) {
        return cameraRepository.findByCategoryId(categoryId);
    }

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> addCamera(
            @RequestParam("name") String name,
            @RequestParam("pricePerDay") Long pricePerDay,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("ownerId") Integer ownerId,
            @RequestParam("role") String role,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            // validasi role (tanpa authorization header)
            if (role == null || !"OWNER".equalsIgnoreCase(role.trim())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied: only OWNER can add camera");
            }
            if (image == null || image.isEmpty()) {
                return ResponseEntity.badRequest().body("Image is required");
            }

            if (name == null || name.isBlank()) {
                return ResponseEntity.badRequest().body("Name is required");
            }

            if (pricePerDay == null || pricePerDay <= 0) {
                return ResponseEntity.badRequest().body("price Per Day must be > 0");
            }
            Optional<User> ownerOptional = userRepository.findById(ownerId);
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

            // Check if owner and category exist in the database
            if (ownerOptional.isEmpty()) {
                return new ResponseEntity<>("Owner with ID " + ownerId + " not found.",
                        HttpStatus.BAD_REQUEST);
            }
            if (categoryOptional.isEmpty()) {
                return new ResponseEntity<>("Category with ID " + categoryId + " not found.",
                        HttpStatus.BAD_REQUEST);
            }

            // Subfolder by date (opsional)
            LocalDate today = LocalDate.now();
            Path baseDir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path datedDir = baseDir.resolve(Paths.get(
                    String.format("%04d", today.getYear()),
                    String.format("%02d", today.getMonthValue())
            ));
            Files.createDirectories(datedDir);

            // generate filename
            String ext = FilenameUtils.getExtension(StringUtils.cleanPath(image.getOriginalFilename()));
            if (ext == null || ext.isBlank()) {
                ext = "jpg";
            }
            String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;

            Path target = datedDir.resolve(filename);
            Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // relative path untuk disimpan (misal 2025/08/uuid.jpg)
            String relativePath = today.getYear() + "/" + String.format("%02d", today.getMonthValue()) + "/" + filename;

            // URL publik (via ResourceHandler /uploads/** )
            String publicUrl = "/uploads/" + relativePath;

            Camera newCamera = new Camera();
            newCamera.setOwner(ownerOptional.get());
            newCamera.setCategory(categoryOptional.get());
            newCamera.setName(name);
            newCamera.setDescription(description);
            newCamera.setPricePerDay(pricePerDay);
            newCamera.setImageUrl(publicUrl);
            newCamera.setLocation(location);
            newCamera.setIsAvailable(true);

            Camera savedCamera = cameraRepository.save(newCamera);

            return ResponseEntity.ok(new UploadResponseDTO<>("Camera uploaded successfully", publicUrl, savedCamera));

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
    
}
