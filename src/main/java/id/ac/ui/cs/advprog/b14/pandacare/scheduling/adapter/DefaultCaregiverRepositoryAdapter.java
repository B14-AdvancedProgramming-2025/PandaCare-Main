package id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.SchedulingCaregiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DefaultCaregiverRepositoryAdapter implements CaregiverRepositoryAdapter {

    private final SchedulingCaregiverRepository caregiverRepository;
    
    @Autowired
    public DefaultCaregiverRepositoryAdapter(SchedulingCaregiverRepository caregiverRepository) {
        this.caregiverRepository = caregiverRepository;
    }
    
    @Override
    public Optional<Caregiver> findById(String id) {
        return caregiverRepository.findById(id);
    }
    
    @Override
    public Caregiver save(Caregiver caregiver) {
        return caregiverRepository.save(caregiver);
    }

    @Override
    public List<Caregiver> findAll() {
        return caregiverRepository.findAll();
    }
}