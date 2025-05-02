package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.repository.DoctorRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorRatingService {

    private final DoctorRatingRepository doctorRatingRepository;

    @Autowired
    public DoctorRatingService(DoctorRatingRepository doctorRatingRepository) {
        this.doctorRatingRepository = doctorRatingRepository;
    }

    public void saveRating(DoctorRating doctorRating) {
        doctorRatingRepository.save(doctorRating);
    }
}
