package com.project.java.sewacam.controller;

import com.project.java.sewacam.dto.BookingDto;
import com.project.java.sewacam.dto.BookingResponseDTO;
import com.project.java.sewacam.dto.StatusUpdateDTO;
import com.project.java.sewacam.model.Booking;
import com.project.java.sewacam.model.Camera;
import com.project.java.sewacam.model.User;
import com.project.java.sewacam.repository.BookingRepository;
import com.project.java.sewacam.repository.CameraRepository;
import com.project.java.sewacam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingDto bookingDto) {
        // Cari objek Camera dan User berdasarkan ID dari DTO
        Optional<Camera> cameraOptional = cameraRepository.findById(bookingDto.getCameraId());
        Optional<User> renterOptional = userRepository.findById(bookingDto.getRenterId());

        // Periksa apakah Camera dan User ada
        if (cameraOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BookingResponseDTO(null, "Camera tidak ditemukan"));
        }
        if (renterOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BookingResponseDTO(null, "User (renter) tidak ditemukan"));
        }

        // Buat objek Booking baru dan isi dengan data dari DTO dan entitas
        Booking newBooking = new Booking();
        newBooking.setCamera(cameraOptional.get());
        newBooking.setRenter(renterOptional.get());
        newBooking.setStartDate(bookingDto.getStartDate()); // PENTING: Mengisi startDate
        newBooking.setEndDate(bookingDto.getEndDate()); // PENTING: Mengisi endDate
        newBooking.setBookingDate(bookingDto.getBookingDate());
        newBooking.setDurationDays(bookingDto.getDurationDays());
        newBooking.setMethod(bookingDto.getMethod());
        newBooking.setStatus("Proses Cek Pembayaran");
        newBooking.setTotalPrice(bookingDto.getTotalPrice());

        // Simpan booking ke DB
        Booking savedBooking = bookingRepository.save(newBooking);

        // Ambil ID hasil generate
        Integer bookingId = savedBooking.getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(new BookingResponseDTO(bookingId, "Booking berhasil dibuat"));
    }

    @GetMapping("/")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/{renterId}")
    public List<Booking> getAllBookingsByRenter(@PathVariable Integer renterId) {
        return bookingRepository.findByRenterId(renterId);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatusBooking(@PathVariable Integer id, @RequestBody StatusUpdateDTO body) {

        if (body == null || body.getStatus() == null || body.getStatus().isBlank()) {
            return ResponseEntity.badRequest().body("Field 'status' wajib diisi");
        }

        // Normalisasi & validasi sederhana
        String statusIn = normalize(body.getStatus());
        if (!isAllowed(statusIn)) {
            return ResponseEntity.badRequest().body("Status tidak dikenali: " + body.getStatus());
        }

        Booking b = bookingRepository.findById(id)
                .orElse(null);
        if (b == null) {
            return ResponseEntity.badRequest().body("Booking tidak ditemukan");
        }

        // Update status
        b.setStatus(mapDisplay(statusIn));

        Booking saved = bookingRepository.save(b);
        return ResponseEntity.ok(saved);
       
    }

    private String normalize(String s) {
        return s.trim()
                .toLowerCase()
                .replace(' ', '_');
    }

    private boolean isAllowed(String s) {
        return s.equals("proses_cek_pembayaran")
                || s.equals("pembayaran_diterima")
                || s.equals("sedang_disewa")
                || s.equals("selesai");
    }

    // Kembalikan bentuk yang “rapi” untuk disimpan/ditampilkan
    private String mapDisplay(String norm) {
        return switch (norm) {
            case "proses_cek_pembayaran" ->
                "Proses Cek Pembayaran";
            case "pembayaran_diterima" ->
                "Pembayaran Diterima";
            case "sedang_disewa" ->
                "Sedang disewa";
            case "selesai" ->
                "Selesai";
            default ->
                norm; // fallback
        };
    }
}
