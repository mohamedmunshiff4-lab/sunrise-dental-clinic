package com.sunrise.dental.service;

import com.sunrise.dental.entity.Staff;
import com.sunrise.dental.repository.StaffRepository;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class AuthService {
    private final StaffRepository staffRepository;

    public AuthService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    /** Validates credentials and returns the staff member if authorised. */
    public Optional<Staff> login(String username, String password) {
        return staffRepository.findByUsername(username)
                .filter(s -> s.getPasswordHash().equals(hash(password)));
    }

    /** SHA-256 hashing - passwords are never stored or compared in plain text. */
    public static String hash(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte x : b) sb.append(String.format("%02x", x));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
