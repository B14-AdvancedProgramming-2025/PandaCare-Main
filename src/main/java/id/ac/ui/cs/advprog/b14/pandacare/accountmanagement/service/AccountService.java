package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.UserDTO;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;

public interface AccountService {
    UserDTO getProfileById(String id);
    UserDTO updateProfile(String id, UpdateProfileDTO dto);
    void deleteProfile(String id);
}

