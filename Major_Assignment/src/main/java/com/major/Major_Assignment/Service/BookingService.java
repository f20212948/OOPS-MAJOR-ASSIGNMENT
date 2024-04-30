package com.major.Major_Assignment.Service;

import com.major.Major_Assignment.Model.Booking;
import org.springframework.http.ResponseEntity;

public interface BookingService{

    public Object createNewBooking(Booking newBooking);
    public ResponseEntity<?> editBooking(Booking booking);
    public ResponseEntity<?> deleteBooking(int bookingID);



}
