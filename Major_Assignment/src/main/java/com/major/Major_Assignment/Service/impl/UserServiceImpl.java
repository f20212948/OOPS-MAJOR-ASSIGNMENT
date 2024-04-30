package com.major.Major_Assignment.Service.impl;

import com.major.Major_Assignment.Dao.BookingDao;
import com.major.Major_Assignment.Dao.RoomsDao;
import com.major.Major_Assignment.Dao.UserDao;
import com.major.Major_Assignment.Model.Booking;
import com.major.Major_Assignment.Model.Room;
import com.major.Major_Assignment.Model.User;
import com.major.Major_Assignment.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomsDao roomsDao;
    @Autowired
    private BookingDao bookingDao;// instantiate the repository interacting with the service layer

    @Override
    public ResponseEntity<?> signupUser(User newUser) {
        User existingUser = userDao.findByEmail(newUser.getEmail());
        if (existingUser != null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Forbidden, Account already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else
        {
            userDao.save(newUser);
            return ResponseEntity.ok("Account Creation Successful");
        }
    }

    @Override
    public ResponseEntity<?> loginUser(User user) {
        User existingUser = userDao.findByEmail(user.getEmail());
        if (existingUser == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else if (!existingUser.getPassword().equals(user.getPassword())) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Invalid Password");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok("Login Successful");
        }
    }

    @Override
    public ResponseEntity<?> getUserDetails(int userID) {
        User existingUser = userDao.findByUserID(userID);
        if (existingUser == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(existingUser);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public ResponseEntity<?> getHistory(Integer userID) {
        User user = userDao.findByUserID(userID);
        if (user == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            List<Booking> bookings = bookingDao.findByUserID(userID);
            List<Map<String, Object>> response = bookings.stream()
                    .filter(booking -> booking.getDateOfBooking().isBefore(LocalDate.now()) || booking.getDateOfBooking().isEqual(LocalDate.now()))
                    .map(booking -> {
                        Map<String, Object> bookingMap = new HashMap<>();
                        Room room = roomsDao.findByRoomID(booking.getRoomID());
                        bookingMap.put("roomName", room.getRoomName());
                        bookingMap.put("roomID", room.getRoomID());
                        bookingMap.put("bookingID", booking.getBookingID());
                        bookingMap.put("dateOfBooking", booking.getDateOfBooking());
                        bookingMap.put("timeFrom", booking.getTimeFrom());
                        bookingMap.put("timeTo", booking.getTimeTo());
                        bookingMap.put("purpose", booking.getPurpose());
                        return bookingMap;
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        }
    }

    @Override
    public ResponseEntity<?> upComing(Integer userID) {
       User user = userDao.findByUserID(userID);
       if (user == null) {
           Map<String , String> errorResponse = new LinkedHashMap<>();
           errorResponse.put("Error" , "User does not exist");
           return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
       } else {
           List<Booking> bookings = bookingDao.findByUserID(userID);
           List<Map<String, Object>> response = bookings.stream()
                   .filter(booking -> booking.getDateOfBooking().isAfter(LocalDate.now()))
                   .map(booking -> {
                       Map<String, Object> bookingMap = new HashMap<>();
                       Room room = roomsDao.findByRoomID(booking.getRoomID());
                       bookingMap.put("roomName", room.getRoomName());
                       bookingMap.put("roomID", room.getRoomID());
                       bookingMap.put("bookingID", booking.getBookingID());
                       bookingMap.put("dateOfBooking", booking.getDateOfBooking());
                       bookingMap.put("timeFrom", booking.getTimeFrom());
                       bookingMap.put("timeTo", booking.getTimeTo());
                       bookingMap.put("purpose", booking.getPurpose());
                       return bookingMap;
                   }).collect(Collectors.toList());
           return ResponseEntity.ok(response);
       }
    }

}
