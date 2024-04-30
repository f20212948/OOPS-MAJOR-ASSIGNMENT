package com.major.Major_Assignment.Service;

import com.major.Major_Assignment.Model.Room;
import org.springframework.http.ResponseEntity;

public interface RoomService {
    public ResponseEntity<?> addNewRoom(Room room);
    public ResponseEntity<?> editRoom(Room room);
    public ResponseEntity<?> deleteRoom(int roomID);
}
