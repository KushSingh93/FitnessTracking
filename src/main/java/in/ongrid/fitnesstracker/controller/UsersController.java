package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dto.LoginRequest;
import in.ongrid.fitnesstracker.dto.SignupRequest;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.service.AuthService;
import in.ongrid.fitnesstracker.service.UsersService;
import in.ongrid.fitnesstracker.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private UsersService usersService;
    private AuthService authService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsersController(UsersService usersService, AuthService authService, JwtUtil jwtUtil) {
        this.usersService = usersService;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // sign up
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
//        User user = authService.signup(request);
        String token = authService.signup(request);
        return ResponseEntity.ok("Generated successfully" + token);
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok("User logged in successfully! Token: " + token);
    }

    //  Get all users
    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    //  Get all admin users
    @GetMapping("/allAdmins")
    public ResponseEntity<List<User>> getAllAdmins() {
        return ResponseEntity.ok(usersService.getAllAdmins());
    }

    //  Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = usersService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Fetch User Profile (via JWT Token)
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {

            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
            }
            String email = jwtUtil.extractEmail(token.substring(7));

            Optional<User> user = usersService.getUserByEmail(email);

            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    //  Update User Profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
            }

            String email = jwtUtil.extractEmail(token.substring(7));

            Optional<User> userOpt = usersService.getUserByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = userOpt.get();
            user.setName(updatedUser.getName());
            user.setWeightKg(updatedUser.getWeightKg());
            user.setHeightCm(updatedUser.getHeightCm());
            user.setAge(updatedUser.getAge());
            user.setGender(updatedUser.getGender());

            User updated = usersService.updateUser(user.getUserId(), user);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
        }
    }

    //  Logout user (front end just removes token from local storage)
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("User logged out successfully.");
    }
}
