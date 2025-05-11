//package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;
//
//import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
//
//import java.util.UUID;
//
//public interface AccountService {
//    Account getAccountById(UUID id);
//    Account updateAccount(Account account);
//    void deleteAccount(UUID id);
//}

package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;

public interface AccountService {
    User getProfileById(String id);
    User updateProfile(String id, User updatedUser);
    void deleteProfile(String id);
}

