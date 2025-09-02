/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.java.sewacam.controller;

import com.project.java.sewacam.dto.ConversationDTO;
import com.project.java.sewacam.dto.MessageDTO;
import com.project.java.sewacam.dto.MessagePayload;
import com.project.java.sewacam.dto.SendRenterReq;
import com.project.java.sewacam.model.Conversations;
import com.project.java.sewacam.model.Messages;
import com.project.java.sewacam.model.User;
import com.project.java.sewacam.repository.ConversationRepo;
import com.project.java.sewacam.repository.MessagesRepo;
import com.project.java.sewacam.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author hilal
 */
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ConversationRepo convRepo;
    private final MessagesRepo msgRepo;
    private final UserRepository userRepo;

    // GET /api/chats/conversations?role=admin|renter[&renterId=]
    @GetMapping("/conversations")
    public ResponseEntity<?> list(@RequestParam String role,
            @RequestParam(required = false) Integer renterId) {
        if ("owner".equalsIgnoreCase(role)) {
            // ambil semua conversations terbaru (dengan fetch join)
            List<ConversationDTO> out = convRepo.findAllWithUsersOrderByLastTimeDesc()
                    .stream().map(ConversationDTO::from).collect(Collectors.toList());
            return ResponseEntity.ok(out);
        }

        if ("renter".equalsIgnoreCase(role)) {
            if (renterId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "renterId required");
            }
            // pastikan conversation ada; kalau belum, buat
            Conversations c = convRepo.findByRenter_Id(renterId).orElseGet(() -> {
                Conversations nc = new Conversations();
                User renterRef = userRepo.findById(renterId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Renter not found"));
                nc.setRenter(renterRef);
                nc.setLastTime(Instant.now());
                nc.setLastMessage(null);
                return convRepo.save(nc);
            });
            return ResponseEntity.ok(List.of(ConversationDTO.from(c)));
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role");
    }

    // GET /api/chats/{cid}/messages[?since=ISO_INSTANT]
    @GetMapping("/{cid}/messages")
    public ResponseEntity<List<MessageDTO>> messages(@PathVariable Integer cid,
            @RequestParam(required = false) String since) {
        List<Messages> list;
        if (since == null || since.isBlank()) {
            list = msgRepo.findByConversation_IdOrderByCreatedAtAsc(cid);
        } else {
            Instant t;
            try {
                t = Instant.parse(since);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid 'since' format; use ISO-8601 Instant");
            }
            list = msgRepo.findByConversation_IdAndCreatedAtAfterOrderByCreatedAtAsc(cid, t);
        }
        return ResponseEntity.ok(list.stream().map(MessageDTO::from).collect(Collectors.toList()));
    }

    // POST /api/chats/{cid}/messages
    @PostMapping("/{cid}/messages")
    public ResponseEntity<MessageDTO> send(@PathVariable Integer cid,
            @RequestBody MessagePayload body) {
        if (body == null || body.getSenderId() == null || body.getContent() == null || body.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "senderId & content are required");
        }

        Conversations c = convRepo.findById(cid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"));

        User sender = userRepo.findById(body.getSenderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));

        Messages m = new Messages();
        m.setConversation(c);
        m.setSender(sender);
        m.setContent(body.getContent());
        m.setCreatedAt(Instant.now());
        m = msgRepo.save(m);

        c.setLastMessage(body.getContent());
        c.setLastTime(m.getCreatedAt());
        convRepo.save(c);

        return ResponseEntity.status(HttpStatus.CREATED).body(MessageDTO.from(m));
    }

    @PostMapping("/renter/send")
    public MessageDTO sendFromRenter(@RequestBody SendRenterReq body) {
        if ((body.getText() == null || body.getText().isBlank()) && body.getImageUrl() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text or imageUrl required");
        }
        if (body.getRenterId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "renterId required");
        }

        var renter = userRepo.getReferenceById(body.getRenterId());
        var owner = (body.getOwnerId() != null ? userRepo.getReferenceById(body.getOwnerId()) : null);

        // cari conversation; kalau tidak ada -> buat baru
        Conversations conv = (owner != null)
                ? convRepo.findByRenter_IdAndOwner_Id(body.getRenterId(), body.getOwnerId())
                        .orElseGet(() -> {
                            var c = new Conversations();
                            c.setRenter(renter);
                            c.setOwner(owner);
                            c.setLastTime(Instant.now());
                            return convRepo.save(c);
                        })
                : convRepo.findByRenter_Id(body.getRenterId())
                        .orElseGet(() -> {
                            var c = new Conversations();
                            c.setRenter(renter);
                            c.setLastTime(Instant.now());
                            return convRepo.save(c);
                        });

        // simpan pesan
        var msg = new Messages();
        msg.setConversation(conv);
        msg.setSender(renter);
        msg.setContent(body.getText() != null ? body.getText() : "[image] " + body.getImageUrl());
        msg.setCreatedAt(Instant.now());
        msg = msgRepo.save(msg);

        // update ringkasan percakapan
        conv.setLastMessage(body.getText());
        conv.setLastTime(msg.getCreatedAt());
        convRepo.save(conv);

        return MessageDTO.from(msg);
    }

}
