package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.repository.DoctorRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorRatingService {

    private final DoctorRatingRepository doctorRatingRepository;

    @Autowired
    public DoctorRatingService(DoctorRatingRepository doctorRatingRepository) {
        this.doctorRatingRepository = doctorRatingRepository;
    }

    // CREATE
    public DoctorRating createRating(String caregiverId, String pacilianId, int value, String comment) {
        return null;
    }

    // READ (by ID)
    public DoctorRating getRatingById(String id) {
        return null;
    }

    // READ (all)
    public List<DoctorRating> getAllRatings() {
        return null;
    }

    // UPDATE
    public DoctorRating updateRating(String id, int value, String comment) {
        return null;
    }

    // DELETE
    public void deleteRating(String id) {
    }
}
