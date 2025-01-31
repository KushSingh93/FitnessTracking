//package in.ongrid.fitnesstracker.controller;
//
//import in.ongrid.fitnesstracker.service.ReportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/reports")
//public class ReportController {
//
//    @Autowired
//    private ReportService reportService;
//
//    // ✅ Get user's overall workout report
//    @GetMapping("/{userId}")
//    public ResponseEntity<String> getWorkoutReport(@PathVariable Long userId) {
//        return ResponseEntity.ok(reportService.getWorkoutReport(userId));
//    }
//}
