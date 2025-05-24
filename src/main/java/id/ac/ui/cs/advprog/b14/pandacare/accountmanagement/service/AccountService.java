package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;

public interface AccountService {
    UserProfileDTO getProfileById(String id);
    UserProfileDTO updateProfile(String id, UpdateProfileDTO dto);
    void deleteProfile(String id);
}

