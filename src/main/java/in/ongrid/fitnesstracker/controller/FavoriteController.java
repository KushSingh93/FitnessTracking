package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // ✅ Get user's favorite exercises
    @GetMapping("/{userId}")
    public ResponseEntity<List<Long>> getFavoriteExercises(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoriteExercises(userId));
    }

    // ✅ Add an exercise to favorites
    @PostMapping("/{userId}/{exerciseId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Long userId, @PathVariable Long exerciseId) {
        favoriteService.addFavorite(userId, exerciseId);
        return ResponseEntity.ok().build();
    }

    // ✅ Remove an exercise from favorites
    @DeleteMapping("/{userId}/{exerciseId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long userId, @PathVariable Long exerciseId) {
        favoriteService.removeFavorite(userId, exerciseId);
        return ResponseEntity.noContent().build();
    }
}
