package id.ac.ui.cs.advprog.b14.pandacare.authentication.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;

public interface AuthenticationService {
    User registerPacilian(Pacilian pacilian);
    User registerCaregiver(Caregiver caregiver);
    User login(String email, String password);
    void logout(String userId);
}
