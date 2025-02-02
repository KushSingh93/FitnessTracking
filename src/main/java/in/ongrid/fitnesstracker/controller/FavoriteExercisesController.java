package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dto.FavoriteRequest;
import in.ongrid.fitnesstracker.model.entities.FavoriteExercises;
import in.ongrid.fitnesstracker.service.FavoriteExercisesService;
import in.ongrid.fitnesstracker.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteExercisesController {

    private final FavoriteExercisesService favoriteExercisesService;
    private final JwtUtil jwtUtil;

    public FavoriteExercisesController(FavoriteExercisesService favoriteExercisesService, JwtUtil jwtUtil) {
        this.favoriteExercisesService = favoriteExercisesService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Retrieve favorite exercises of a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteExercises>> getFavoritesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteExercisesService.getFavoritesByUserId(userId));
    }

    // ✅ Add an exercise to favorites
    @PostMapping("/add")
    public ResponseEntity<FavoriteExercises> addFavoriteExercise(
            @Valid @RequestBody FavoriteRequest favoriteRequest,
            @RequestHeader("Authorization") String token) {

        // Extract actual JWT token (remove "Bearer " prefix)
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken); // Extract email from token

        FavoriteExercises savedFavorite = favoriteExercisesService.addFavoriteExercise(favoriteRequest, userEmail);
        return ResponseEntity.ok(savedFavorite);
    }

    // ✅ Remove an exercise from favorites
    @DeleteMapping("/remove/{exerciseId}")
    public ResponseEntity<Void> removeFavoriteExercise(
            @PathVariable Long exerciseId,
            @RequestHeader("Authorization") String token) {

        // Extract actual JWT token (remove "Bearer " prefix)
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken); // Extract email from token

        favoriteExercisesService.deleteFavoriteExercise(exerciseId, userEmail);
        return ResponseEntity.noContent().build();
    }
}