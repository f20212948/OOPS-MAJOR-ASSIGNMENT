package com.major.Major_Assignment.Dao;

import com.major.Major_Assignment.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomsDao extends JpaRepository<Room, Integer> {
     List<Room> findByRoomCapacityGreaterThanEqual(int roomCapacity);

    Room findByRoomName(String roomName);

    Room findByRoomID(int roomID);

}