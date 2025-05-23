package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

import id.ac.ui.cs.advprog.b14.pandacare.rating.decorator.ConcreteDoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.repository.DoctorRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DoctorRatingService {

    private final DoctorRatingRepository doctorRatingRepository;

    @Autowired
    public DoctorRatingService(DoctorRatingRepository doctorRatingRepository) {
        this.doctorRatingRepository = doctorRatingRepository;
    }

    // CREATE
    public DoctorRating createRating(String caregiverId, String pacilianId, int value, String comment) {
        DoctorRating base = new DoctorRating(caregiverId, pacilianId, value, comment);

        // Gunakan decorator untuk validasi dan normalisasi
        ConcreteDoctorRating decorated = new ConcreteDoctorRating(base);
        decorated.setValue(value);               // validasi 1–5
        decorated.setComment(comment);           // trim komentar

        return doctorRatingRepository.save(decorated);
    }

    // READ (by ID)
    public DoctorRating getRatingById(String id) {
        return doctorRatingRepository.findById(id);
    }

    // READ (all)
    public List<DoctorRating> getAllRatings() {
        return doctorRatingRepository.findAll();
    }

    // UPDATE
    public DoctorRating updateRating(String id, int value, String comment) {
        DoctorRating existing = doctorRatingRepository.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Rating not found for ID: " + id);
        }
        // menggunakan decorator
        ConcreteDoctorRating decorated = new ConcreteDoctorRating(existing);
        decorated.setValue(value);
        decorated.setComment(comment);

        return doctorRatingRepository.save(decorated);
    }

    // DELETE
    public void deleteRating(String id) {
        if (!doctorRatingRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete. Rating not found for ID: " + id);
        }

        doctorRatingRepository.deleteById(id);
    }

    @Async
    public CompletableFuture<List<DoctorRating>> findByDoctorId(String doctorId) {
        return CompletableFuture.supplyAsync(() -> doctorRatingRepository
                .findByCaregiverId(doctorId));
    }
}
