package com.major.Major_Assignment.Service.impl;


import com.major.Major_Assignment.Dao.BookingDao;
import com.major.Major_Assignment.Dao.RoomsDao;
import com.major.Major_Assignment.Dao.UserDao;
import com.major.Major_Assignment.Model.Room;
import com.major.Major_Assignment.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.major.Major_Assignment.Model.Booking;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomsDao roomsDao;
    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private UserDao userDao;

    @Override
    public ResponseEntity<?> addNewRoom(Room room) {
        Room existingRoom = roomsDao.findByRoomName(room.getRoomName());
        if (existingRoom != null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Room already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else if (room.getRoomCapacity() < 0) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Invalid Capacity");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            roomsDao.save(room);
            return ResponseEntity.ok("Room created successfully");
        }
    }

    @Override
    public ResponseEntity<?> editRoom(Room room) {
        Room existingRoom = roomsDao.findByRoomID(room.getRoomID());
        if (existingRoom == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Room does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            Room roomWithSameName = roomsDao.findByRoomName(room.getRoomName());
            if (roomWithSameName != null && roomWithSameName.getRoomID() != room.getRoomID()) {
                Map<String , String> errorResponse = new LinkedHashMap<>();
                errorResponse.put("Error" , "Room with given name already exists");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else if (room.getRoomCapacity() <= 0) {
                Map<String , String> errorResponse = new LinkedHashMap<>();
                errorResponse.put("Error" , "Invalid Capacity");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else {
                existingRoom.setRoomName(room.getRoomName());
                existingRoom.setRoomCapacity(room.getRoomCapacity());
                roomsDao.save(existingRoom);
                return ResponseEntity.ok("Room edited successfully");
            }
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteRoom(int roomID) {
        Room existingRoom = roomsDao.findByRoomID(roomID);
        if (existingRoom == null) {
            Map<String , String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error" , "Room does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            bookingDao.deleteByRoomID(roomID);
            roomsDao.delete(existingRoom);
            return ResponseEntity.ok("Room deleted successfully");
        }
    }

}
