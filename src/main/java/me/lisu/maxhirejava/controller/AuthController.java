package me.lisu.maxhirejava.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import me.lisu.maxhirejava.model.User;
import me.lisu.maxhirejava.record.MessageResponse;
import me.lisu.maxhirejava.repository.UserRepository;
import me.lisu.maxhirejava.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public record LoginRequest(String email, String password) {}
    public record UserDataObject(String id, String email, String role, String name, String surname) {}
    public record AuthResponse(String message, UserDataObject user) {}

    public record RegisterRequest(String email, String password, String name, String surname) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            String token = jwtService.generateToken(user.getId());

            Cookie cookie = new Cookie("token_auth", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(2592000);

            response.addCookie(cookie);
            response.addHeader("Set-Cookie", "token_auth=" + token + "; Max-Age=2592000; Path=/; HttpOnly; SameSite=Strict");

            UserDataObject userData = new UserDataObject(user.getId(), user.getEmail(), user.getRole(), user.getName(), user.getSurname());
            return ResponseEntity.ok(new AuthResponse("Zalogowano pomyślnie", userData));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nieprawidłowe dane");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Użytkownik już istnieje");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(hashedPassword);
        newUser.setName(request.name());
        newUser.setSurname(request.surname());

        User savedUser;
        try {
            savedUser = userRepository.save(newUser);
        } catch (Exception e) {
            System.err.println("User creation error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas zapisywania użytkownika");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Dodano użytkownika o numerze id: " + savedUser.getId());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token_auth", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok(new MessageResponse("Wylogowano pomyślnie"));
    }
}
