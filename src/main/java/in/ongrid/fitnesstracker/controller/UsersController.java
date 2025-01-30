package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.model.entities.Users;
import in.ongrid.fitnesstracker.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // ✅ Register a new user
    @PostMapping("/signup")
    public ResponseEntity<Users> signUp(@RequestBody Users user) {
        return ResponseEntity.ok(usersService.createUser(user));
    }

    // ✅ Login user (dummy implementation, JWT needed)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = usersService.authenticateUser(email, password);
        return isAuthenticated ? ResponseEntity.ok("Login successful") : ResponseEntity.badRequest().body("Invalid credentials");
    }

    // ✅ Get all users
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    // ✅ Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
        Optional<Users> user = usersService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Update user profile
    @PutMapping("/{userId}")
    public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody Users user) {
        return ResponseEntity.ok(usersService.updateUser(userId, user));
    }

    // ✅ Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
