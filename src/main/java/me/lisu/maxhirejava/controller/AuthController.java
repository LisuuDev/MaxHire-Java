package me.lisu.maxhirejava.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.lisu.maxhirejava.model.User;
import me.lisu.maxhirejava.record.MessageResponse;
import me.lisu.maxhirejava.repository.UserRepository;
import me.lisu.maxhirejava.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @Value("${app.jwt.expiration}")
    private long expirationTime;

    public record LoginRequest(String email, String password) {}
    public record LoginWithEmailRequest(String email) {}
    public record VerifyEmailLoginRequest(String token) {}
    public record UserDataObject(String id, String email, String role, String name, String surname) {}
    public record AuthResponse(String message, UserDataObject user) {}
    public record RecoverPassRequest(String email) {}
    public record ChangePassRequest(String token, String password) {}
    public record RegisterRequest(String email, String password, String name, String surname) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.email()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Nie znaleziono użytkownika"));
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Nieprawidłowe dane"));
        }

        return createAuthResponse(user, response);
    }

    @PostMapping("/login/email/loginUser")
    public ResponseEntity<?> requestEmailLogin(@RequestBody LoginWithEmailRequest request) {
        User user = userRepository.findByEmail(request.email()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Nie znaleziono użytkownika o podanym emailu"));
        }

        String token = jwtService.generateRecoveryToken(request.email());

        log.info("Link do logowania użytkownika {}: http://localhost:5173/login/{}", request.email(), token);

        return ResponseEntity.ok(new MessageResponse("Wysłano link do logowania na podany adres email"));
    }

    @PostMapping("/login/email/login")
    public ResponseEntity<?> verifyEmailLogin(@RequestBody VerifyEmailLoginRequest request, HttpServletResponse response) {
        String userEmail = jwtService.extractRecoveryEmail(request.token());

        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Nieprawidłowy lub wygasły token"));
        }

        User user = userRepository.findByEmail(userEmail).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Nie znaleziono użytkownika"));
        }

        return createAuthResponse(user, response);
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
            log.error("Błąd rejestracji użytkownika: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas rejestracji       użytkownika");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Dodano użytkownika o numerze id: " + savedUser.getId());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();

        ResponseCookie cookie = jwtService.cleanJwtCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("Wylogowano pomyślnie"));
    }

    @PostMapping("/edit/user/forgetPassword")
    public ResponseEntity<?> requestPassChange(@RequestBody RecoverPassRequest request) {
        User user = userRepository.findByEmail(request.email()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(new MessageResponse("Nie znaleziono użytkownika o podanym emailu"));
        }

        String token = jwtService.generateRecoveryToken(request.email());

        log.info("Recovery link użytkownika {}: http://localhost:5173/recover/{}", request.email(), token);

        return ResponseEntity.ok(new MessageResponse("Wysłano link do zmiany hasła na podany adres email"));
    }

    @PatchMapping("/edit/user/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassRequest request) {
        String userEmail = jwtService.extractRecoveryEmail(request.token());

        if (userEmail == null) {
            return ResponseEntity.status(400).body(new MessageResponse("Nieprawidłowy lub wygasły token"));
        }

        User user = userRepository.findByEmail(userEmail).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(new MessageResponse("Nie znaleziono użytkownika"));
        }

        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Hasło zostało zmienione pomyślnie"));
    }

    private ResponseEntity<AuthResponse> createAuthResponse(User user, HttpServletResponse response) {
        String token = jwtService.generateToken(user.getId());

        ResponseCookie cookie = ResponseCookie.from("token_auth", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(expirationTime)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        UserDataObject userData = new UserDataObject(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getName(),
                user.getSurname()
        );

        return ResponseEntity.ok(new AuthResponse("Zalogowano pomyślnie", userData));
    }
}
