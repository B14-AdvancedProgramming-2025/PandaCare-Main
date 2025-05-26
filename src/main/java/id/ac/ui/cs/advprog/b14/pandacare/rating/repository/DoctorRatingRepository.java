package id.ac.ui.cs.advprog.b14.pandacare.rating.repository;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRatingRepository extends JpaRepository<DoctorRating, String> {
    List<DoctorRating> findByCaregiver_Id(String caregiverId);
    List<DoctorRating> findByPacilian_Id(String pacilianId);
}
