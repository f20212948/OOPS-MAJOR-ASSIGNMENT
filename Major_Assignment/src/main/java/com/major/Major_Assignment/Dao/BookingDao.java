package com.major.Major_Assignment.Dao;

import com.major.Major_Assignment.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingDao extends JpaRepository <Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.roomID = :roomID AND b.dateOfBooking = :dateOfBooking AND ((b.timeFrom <= :timeFrom AND b.timeTo > :timeFrom) OR (b.timeFrom < :timeTo AND b.timeTo >= :timeTo) OR (b.timeFrom >= :timeFrom AND b.timeTo <= :timeTo))")
    List<Booking> findBookingsByDateAndTime(@Param("roomID") int roomID, @Param("dateOfBooking") LocalDate dateOfBooking, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);


    Booking findByBookingID(int bookingID);
    Booking findByRoomID(int roomID);
    void deleteByRoomID(int roomID);

    List<Booking> findListByRoomID(int roomID);

    List<Booking> findByUserID(Integer userID);
}
