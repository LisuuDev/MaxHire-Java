package me.lisu.maxhirejava.controller;

import me.lisu.maxhirejava.model.User;
import me.lisu.maxhirejava.record.MessageResponse;
import me.lisu.maxhirejava.record.UserInfo;
import me.lisu.maxhirejava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    public record EditProfileRequest(String email, String phone) {}
    public record ProfileResponse(List<UserInfo> message) {}

    @PatchMapping({"/edit/user/{id}", "/admin/update/user/{id}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editProfile(@RequestBody EditProfileRequest request, Authentication authentication, @PathVariable("id") String id) {
        String userId = authentication.getName();
        boolean isAdmin = userRepository.isAdmin(userId);

        if (!isAdmin && !userId.equals(id)) {
            return ResponseEntity.status(403).body(new MessageResponse("Brak odpowiednich uprawnień"));
        }

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(new MessageResponse("Nie znaleziono użytkownika"));
        }

        if (request.email() != null && !request.email().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.findByEmail(request.email()).isPresent()) {
                return ResponseEntity.status(400).body(new MessageResponse("Ten adres email jest już zajęty"));
            }
            user.setEmail(request.email());
        }

        if (request.phone() != null) user.setPhone(request.phone());

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Zaktualizowano profil"));
    }

    @GetMapping("/profile/getProfile/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProfile(@PathVariable("id") String profileId) {
        User user = userRepository.findById(profileId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(new MessageResponse("Nie znaleziono użytkownika"));
        }

        UserInfo userInfo = new UserInfo(user);

        List<UserInfo> messageArray = List.of(userInfo);

        return ResponseEntity.ok(new ProfileResponse(messageArray));
    }
}
