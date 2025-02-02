package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dto.ReportRequest;
import in.ongrid.fitnesstracker.service.ReportsService;
import in.ongrid.fitnesstracker.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;
    private final JwtUtil jwtUtil;

    public ReportsController(ReportsService reportsService, JwtUtil jwtUtil) {
        this.reportsService = reportsService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Get overall workout summary report
    @GetMapping("/summary")
    public ResponseEntity<ReportRequest> getWorkoutSummary(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "period", required = true) String period) {

        // Extract JWT Token (Remove "Bearer ")
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        ReportRequest report = reportsService.getWorkoutSummary(userEmail, period);
        return ResponseEntity.ok(report);
    }
}
