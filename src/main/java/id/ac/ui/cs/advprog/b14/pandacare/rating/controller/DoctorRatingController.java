package id.ac.ui.cs.advprog.b14.pandacare.rating.controller;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.service.DoctorRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ratings")
public class DoctorRatingController {

    private final DoctorRatingService doctorRatingService;

    @Autowired
    public DoctorRatingController(DoctorRatingService doctorRatingService) {
        this.doctorRatingService = doctorRatingService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<DoctorRating> createRating(@RequestBody DoctorRating body) {
        DoctorRating created = doctorRatingService.createRating(
                body.getCaregiverId(),
                body.getPacilianId(),
                body.getValue(),
                body.getComment()
        );
        return ResponseEntity.ok(created);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<DoctorRating>> getAllRatings() {
        return ResponseEntity.ok(doctorRatingService.getAllRatings());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorRating> getRatingById(@PathVariable String id) {
        return ResponseEntity.ok(doctorRatingService.getRatingById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<DoctorRating> updateRating(@PathVariable String id,
                                                     @RequestBody DoctorRating body) {
        DoctorRating updated = doctorRatingService.updateRating(
                id, body.getValue(), body.getComment()
        );
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable String id) {
        doctorRatingService.deleteRating(id);
        return ResponseEntity.ok("Rating deleted successfully.");
    }

    @GetMapping("/doctor/{doctorId}")
    public CompletableFuture<ResponseEntity<List<DoctorRating>>> getByDoctor(@PathVariable String doctorId) {
              return doctorRatingService.findByDoctorId(doctorId).thenApply(ResponseEntity::ok);
    }
}
