package com.major.Major_Assignment.Service.impl;

import com.major.Major_Assignment.Dao.BookingDao;
import com.major.Major_Assignment.Dao.RoomsDao;
import com.major.Major_Assignment.Dao.UserDao;
import com.major.Major_Assignment.ErrorResponse.ErrorResponse;
import com.major.Major_Assignment.Model.Booking;
import com.major.Major_Assignment.Model.Room;
import com.major.Major_Assignment.Model.User;
import com.major.Major_Assignment.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomsDao roomsDao;

    @Override
    public Object createNewBooking(Booking newBooking) {
        // Check if the user exists
        User user = userDao.findByUserID(newBooking.getUserID());
        if (user == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Check if the room exists
        Room room = roomsDao.findByRoomID(newBooking.getRoomID());
        if (room == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Room does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Check if the booking already exists on the same date and time
        List<Booking> existingBookings = bookingDao.findBookingsByDateAndTime(newBooking.getRoomID(),newBooking.getDateOfBooking(), newBooking.getTimeFrom(), newBooking.getTimeTo());
        if (!existingBookings.isEmpty()) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Room Unavailable");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // invalid date/time
        if (newBooking.getDateOfBooking().isBefore(LocalDate.now())) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Invalid Date / Time");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }


        try {
            LocalDate.parse(newBooking.getDateOfBooking().toString());
        } catch (DateTimeParseException _) {

        }

        // If all checks pass, save the new booking
        bookingDao.save(newBooking);
        return ResponseEntity.ok("Booking created Successfully");
    }

    @Override
    public ResponseEntity<?> editBooking(Booking booking) {
        // Fetch the user
        User user = userDao.findByUserID(booking.getUserID());
        if (user == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Fetch the room
        Room room = roomsDao.findByRoomID(booking.getRoomID());
        if (room == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Room does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Fetch the existing booking
        Booking existingBooking = bookingDao.findByBookingID(booking.getBookingID());
        if (existingBooking == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Booking does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Check if the room is available at the given date and time
        List<Booking> existingBookings = bookingDao.findBookingsByDateAndTime(booking.getRoomID(), booking.getDateOfBooking(), booking.getTimeFrom(), booking.getTimeTo());
        if (!existingBookings.isEmpty() && !existingBookings.contains(existingBooking)) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Room Unavailable");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // invalid date/time
        if (booking.getDateOfBooking().isBefore(LocalDate.now())) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Invalid Date / Time");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            LocalDate.parse(booking.getDateOfBooking().toString());
        } catch (DateTimeParseException _) {

        }



        // If all checks pass, update the booking details and save the booking
        existingBooking.setUserID(booking.getUserID());
        existingBooking.setRoomID(booking.getRoomID());
        existingBooking.setDateOfBooking(booking.getDateOfBooking());
        existingBooking.setTimeFrom(booking.getTimeFrom());
        existingBooking.setTimeTo(booking.getTimeTo());
        existingBooking.setPurpose(booking.getPurpose());
        bookingDao.save(existingBooking);

        return ResponseEntity.ok("Booking modified successfully");
    }

    @Override
    public ResponseEntity<?> deleteBooking(int bookingID) {
        // Fetch the existing booking
        Booking existingBooking = bookingDao.findByBookingID(bookingID);
        if (existingBooking == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Booking does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // If the booking exists, delete it
        bookingDao.delete(existingBooking);

        return ResponseEntity.ok("Booking deleted successfully");
    }
}
