package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dto.LoginRequest;
import in.ongrid.fitnesstracker.dto.SignupRequest;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.service.AuthService;
import in.ongrid.fitnesstracker.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private UsersService usersService;
    private AuthService authService;

    @Autowired
    public UsersController(UsersService usersService, AuthService authService) {
        this.usersService = usersService;
        this.authService = authService;
    }

    // ✅ Register a new user
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
//        User user = authService.signup(request);
        String token = authService.signup(request);
        return ResponseEntity.ok("Generated successfully" + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok("User logged in successfully! Token: " + token);
    }

    // ✅ Get all users
    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    // ✅ Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = usersService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Update user profile
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        return ResponseEntity.ok(usersService.updateUser(userId, user));
    }

    // ✅ Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
