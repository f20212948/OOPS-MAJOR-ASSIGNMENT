package com.major.Major_Assignment.Controller;


import com.major.Major_Assignment.Dao.BookingDao;
import com.major.Major_Assignment.Dao.RoomsDao;
import com.major.Major_Assignment.Model.Booking;
import com.major.Major_Assignment.Model.Room;
import com.major.Major_Assignment.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController{

    RoomService roomService;
    @Autowired
    RoomsDao roomsDao;
    @Autowired
    BookingDao bookingDao;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping()
    public ResponseEntity<?> addNewRoom(@RequestBody Room room)
    {
        return roomService.addNewRoom(room);
    }

    @PatchMapping()
    public ResponseEntity<?> editRoom(@RequestBody Room room)
    {
        return roomService.editRoom(room);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteRoom(@RequestParam("roomID") int roomID)
    {
        return roomService.deleteRoom(roomID);
    }

    @GetMapping()
    public ResponseEntity<?> getRooms(@RequestParam(required = false) Integer roomCapacity) {
        List<Room> rooms;
        if (roomCapacity == null) {
            rooms = roomsDao.findAll();
        } else {
            if(roomCapacity <= 0){
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("errors", "Invalid Parameters");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            rooms = roomsDao.findByRoomCapacityGreaterThanEqual(roomCapacity);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Room room : rooms) {
            Map<String, Object> roomMap = new HashMap<>();
            roomMap.put("roomID", room.getRoomID());
            roomMap.put("capacity", room.getRoomCapacity());
            roomMap.put("roomName" , room.getRoomName());

            List<Booking> bookings = bookingDao.findListByRoomID(room.getRoomID());
            List<Map<String, Object>> booked = new ArrayList<>();
            bookings.forEach(booking -> {
                Map<String, Object> bookingMap = new HashMap<>();
                bookingMap.put("bookingID", booking.getBookingID());
                bookingMap.put("dateOfBooking", booking.getDateOfBooking());
                bookingMap.put("timeFrom", booking.getTimeFrom());
                bookingMap.put("timeTo", booking.getTimeTo());
                bookingMap.put("purpose", booking.getPurpose());
                bookingMap.put("userID", booking.getUserID());
                booked.add(bookingMap);
            });

            roomMap.put("booked", booked);
            response.add(roomMap);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }





}
