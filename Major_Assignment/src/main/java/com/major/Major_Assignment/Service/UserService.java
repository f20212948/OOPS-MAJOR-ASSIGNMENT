package com.major.Major_Assignment.Service;

import com.major.Major_Assignment.Model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    public ResponseEntity<?> signupUser(User newUser);
    public ResponseEntity<?> loginUser(User user);
    public ResponseEntity<?> getUserDetails(int userID);
    public List<User> getAllUsers();

    ResponseEntity<?> getHistory(Integer userID);
    ResponseEntity<?> upComing(Integer userID);
}
