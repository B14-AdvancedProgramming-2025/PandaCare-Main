package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;

public interface AccountService {
    User findUserByEmail(String email);
    UserProfileDTO getProfileById(String id);
    UserProfileDTO updateProfile(String id, UpdateProfileDTO dto);
    void deleteProfile(String id);
}
