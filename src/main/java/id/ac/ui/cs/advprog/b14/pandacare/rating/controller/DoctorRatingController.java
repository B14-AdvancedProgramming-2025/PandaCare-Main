package id.ac.ui.cs.advprog.b14.pandacare.rating.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.rating.dto.DoctorRatingSummaryDto;
import id.ac.ui.cs.advprog.b14.pandacare.rating.dto.RatingRequest;
import id.ac.ui.cs.advprog.b14.pandacare.rating.dto.RatingResponse;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.service.DoctorRatingService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ratings")
public class DoctorRatingController {

    private final DoctorRatingService doctorRatingService;
    private final UserRepository userRepository;

    @Autowired
    public DoctorRatingController(DoctorRatingService doctorRatingService, UserRepository userRepository) {
        this.doctorRatingService = doctorRatingService;
        this.userRepository = userRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody RatingRequest request) {
        try {
            DoctorRating created = doctorRatingService.createRating(
                    request.getCaregiverId(),
                    request.getPacilianEmail(),
                    request.getValue(),
                    request.getComment()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(true, "Rating created successfully", new RatingResponse(created))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<?> getAllRatings() {
        List<RatingResponse> responses = doctorRatingService.getAllRatings().stream()
                .map(RatingResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Ratings retrieved successfully", responses)
        );
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRatingById(@PathVariable String id) {
        try {
            DoctorRating rating = doctorRatingService.getRatingById(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Rating retrieved successfully", new RatingResponse(rating))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRating(@PathVariable String id, @RequestBody RatingRequest request) {
        try {
            DoctorRating updated = doctorRatingService.updateRating(
                    id, request.getValue(), request.getComment()
            );
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Rating updated successfully", new RatingResponse(updated))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable String id) {
        try {
            doctorRatingService.deleteRating(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Rating deleted successfully", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/doctor/{doctorIdOrEmail}")
    public CompletableFuture<ResponseEntity<ApiResponse<List<RatingResponse>>>> getByDoctor(@PathVariable String doctorIdOrEmail) {
        String doctorId;

        // Check if the input is an email
        if (doctorIdOrEmail.contains("@")) {
            try {
                User user = userRepository.findByEmail(doctorIdOrEmail);
                doctorId = user.getId();
            } catch (IllegalArgumentException e) {
                return CompletableFuture.completedFuture(
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, e.getMessage(), null))
                );
            }
        } else {
            doctorId = doctorIdOrEmail;
        }

        return doctorRatingService.findByDoctorId(doctorId)
                .thenApply(ratings -> ratings.stream()
                        .map(RatingResponse::new)
                        .collect(Collectors.toList()))
                .thenApply(ratingResponses ->
                        ResponseEntity.ok(new ApiResponse<>(true, "Ratings retrieved successfully", ratingResponses)))
                .exceptionally(e ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, e.getCause().getMessage(), null))
                );
    }

    // Inner Class API Response
    @Getter
    public static class ApiResponse<T> {
        private final boolean success;
        private final String message;
        private final T data;

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorRatingSummaryDto>> getDoctorsAverageRatings() {
        List<DoctorRatingSummaryDto> summaries =
                doctorRatingService.getAllDoctorsAverageRatings();
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/id/{email}")
    public ResponseEntity<?> getIdByEmail(@PathVariable String email) {
        try {
            User user = userRepository.findByEmail(email);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "User ID retrieved successfully", user.getId())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }


}
