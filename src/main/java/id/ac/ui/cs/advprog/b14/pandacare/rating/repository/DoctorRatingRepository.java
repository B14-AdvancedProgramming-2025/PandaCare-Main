package id.ac.ui.cs.advprog.b14.pandacare.rating.repository;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DoctorRatingRepository {

    private final Map<String, DoctorRating> storage = new HashMap<>();

    public DoctorRating save(DoctorRating rating) {
        storage.put(rating.getId(), rating);
        return rating;
    }

    public DoctorRating findById(String id) {
        return storage.get(id);
    }

    public List<DoctorRating> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteById(String id) {
        storage.remove(id);
    }

    public boolean existsById(String id) {
        return storage.containsKey(id);
    }
}
