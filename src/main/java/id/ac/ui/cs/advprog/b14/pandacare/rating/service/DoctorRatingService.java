package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.rating.decorator.ConcreteDoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.dto.DoctorRatingSummaryDto;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.repository.DoctorRatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class DoctorRatingService {

    private final DoctorRatingRepository doctorRatingRepository;
    private final UserRepository userRepository;

    public DoctorRatingService(DoctorRatingRepository doctorRatingRepository,
                               UserRepository userRepository) {
        this.doctorRatingRepository = doctorRatingRepository;
        this.userRepository         = userRepository;
    }

    // CREATE
    public DoctorRating createRating(String caregiverId,
                                     String pacilianEmail,
                                     int value,
                                     String comment) {
        Caregiver caregiver = getCaregiverById(caregiverId);

        User user = userRepository.findByEmail(pacilianEmail);
        if (user == null) {
            throw new IllegalArgumentException(
                    "Pacilian dengan email " + pacilianEmail + " tidak ditemukan"
            );
        }
        Pacilian pacilian = getPacilianById(user.getId());

        // 1) buat entity murni
        DoctorRating base = new DoctorRating(caregiver, pacilian, value, comment);
        // 2) kalau perlu validasi/dekorasi, bungkus di memori
        ConcreteDoctorRating decorated = new ConcreteDoctorRating(base);
        decorated.setValue(value);
        decorated.setComment(comment);
        // 3) persist **entity base**, bukan decorator
        return doctorRatingRepository.save(base);
    }

    // READ by ID
    public DoctorRating getRatingById(String id) {
        return doctorRatingRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Rating not found for ID: " + id)
                );
    }

    // READ all
    public List<DoctorRating> getAllRatings() {
        return doctorRatingRepository.findAll();
    }

    // UPDATE
    public DoctorRating updateRating(String id, int value, String comment) {
        DoctorRating existing = doctorRatingRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Rating not found for ID: " + id)
                );
        // validasi melalui decorator
        ConcreteDoctorRating decorated = new ConcreteDoctorRating(existing);
        decorated.setValue(value);
        decorated.setComment(comment);
        // save **entity asli** yang sudah termutasi
        return doctorRatingRepository.save(existing);
    }

    // DELETE
    public void deleteRating(String id) {
        if (!doctorRatingRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Cannot delete. Rating not found for ID: " + id
            );
        }
        doctorRatingRepository.deleteById(id);
    }

    // READ by caregiver (async)
    @Async
    public CompletableFuture<List<DoctorRating>> findByDoctorId(String doctorId) {
        List<DoctorRating> ratings =
                doctorRatingRepository.findByCaregiver_Id(doctorId);
        return CompletableFuture.completedFuture(ratings);
    }

    // rata-rata per dokter
    public List<DoctorRatingSummaryDto> getAllDoctorsAverageRatings() {
        return doctorRatingRepository.findAll().stream()
                .collect(Collectors.groupingBy(r -> r.getCaregiver().getId()))
                .entrySet().stream()
                .map(e -> {
                    String cid   = e.getKey();
                    String cname = e.getValue().get(0).getCaregiver().getName();
                    double avg   = e.getValue().stream()
                            .mapToInt(DoctorRating::getValue)
                            .average().orElse(0.0);
                    return new DoctorRatingSummaryDto(cid, cname, avg);
                })
                .collect(Collectors.toList());
    }

    // helper load & validasi Caregiver
    private Caregiver getCaregiverById(String caregiverId) {
        User user = userRepository.findById(caregiverId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Caregiver not found: " + caregiverId)
                );
        if (!(user instanceof Caregiver)) {
            throw new IllegalArgumentException(
                    "User is not a Caregiver: " + caregiverId
            );
        }
        return (Caregiver) user;
    }

    // helper load & validasi Pacilian
    private Pacilian getPacilianById(String pacilianId) {
        User user = userRepository.findById(pacilianId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pacilian not found: " + pacilianId)
                );
        if (!(user instanceof Pacilian)) {
            throw new IllegalArgumentException(
                    "User is not a Pacilian: " + pacilianId
            );
        }
        return (Pacilian) user;
    }
}
