package com.major.Major_Assignment.Controller;


import com.major.Major_Assignment.Model.Booking;
import com.major.Major_Assignment.Service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookingController {

    BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public Object createNewBooking(@RequestBody Booking booking)
    {
        return bookingService.createNewBooking(booking);
    }

    @PatchMapping
    public ResponseEntity<?> editBooking(@RequestBody Booking booking)
    {
        return bookingService.editBooking(booking);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteBooking(@RequestParam("bookingID") int bookingID)
    {
        return bookingService.deleteBooking(bookingID);
    }


}
