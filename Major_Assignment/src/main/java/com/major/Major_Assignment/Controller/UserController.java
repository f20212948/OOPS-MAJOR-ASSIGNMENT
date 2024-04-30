package com.major.Major_Assignment.Controller;


import com.major.Major_Assignment.Model.User;
import com.major.Major_Assignment.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/oopmajroom")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get user Info for one user
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestParam("userID") int userID) // user email is the input
    {
        return userService.getUserDetails(userID);
    }

    // get info for all the users
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody User user)
    {
        return userService.signupUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user)
    {
        return userService.loginUser(user);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam("userID") Integer userID)
    {
        return userService.getHistory(userID);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> upComing(@RequestParam("userID") Integer userID)
    {
        return userService.upComing(userID);
    }











}
