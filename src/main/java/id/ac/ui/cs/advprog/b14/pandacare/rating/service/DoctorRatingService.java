package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public DoctorRatingService(DoctorRatingRepository doctorRatingRepository, UserRepository userRepository) {
        this.doctorRatingRepository = doctorRatingRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public DoctorRating createRating(String caregiverId, String pacilianId, int value, String comment) {
        Caregiver caregiver = getCaregiverById(caregiverId);
        Pacilian pacilian = getPacilianById(pacilianId);

        DoctorRating base = new DoctorRating(caregiver, pacilian, value, comment);

        ConcreteDoctorRating decorated = new ConcreteDoctorRating(base);
        decorated.setValue(value);
        decorated.setComment(comment);

        return doctorRatingRepository.save(decorated);
    }

    // READ (by ID)
    public DoctorRating getRatingById(String id) {
        DoctorRating rating = doctorRatingRepository.findById(id);
        if (rating == null) {
            throw new IllegalArgumentException("Rating not found for ID: " + id);
        }
        return rating;
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
        Caregiver caregiver = getCaregiverById(doctorId);
        return CompletableFuture.supplyAsync(() -> doctorRatingRepository.findByCaregiver(caregiver));
    }

    // Private helper methods
    private Caregiver getCaregiverById(String caregiverId) {
        User user = userRepository.findById(caregiverId)
                .orElseThrow(() -> new IllegalArgumentException("Caregiver not found with ID: " + caregiverId));

        if (!(user instanceof Caregiver)) {
            throw new IllegalArgumentException("User with ID: " + caregiverId + " is not a Caregiver");
        }

        return (Caregiver) user;
    }

    private Pacilian getPacilianById(String pacilianId) {
        User user = userRepository.findById(pacilianId)
                .orElseThrow(() -> new IllegalArgumentException("Pacilian not found with ID: " + pacilianId));

        if (!(user instanceof Pacilian)) {
            throw new IllegalArgumentException("User with ID: " + pacilianId + " is not a Pacilian");
        }

        return (Pacilian) user;
    }

    public List<Caregiver> getAllDoctors() {
        return doctorRatingRepository.findAllDoctors();
    }
}
