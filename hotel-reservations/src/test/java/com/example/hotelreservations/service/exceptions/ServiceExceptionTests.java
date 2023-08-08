package com.example.hotelreservations.service.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ServiceExceptionTests {

    @Test
    public void testDateInPastException() {
        String message = "Date is in the past.";
        Throwable cause = new RuntimeException("Cause");

        DateInPastException exception1 = new DateInPastException(message);
        DateInPastException exception2 = new DateInPastException(message, cause);

        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    public void testDateIsAlreadyBookedException() {
        String message = "Date is already booked.";
        Throwable cause = new RuntimeException("Cause");

        DateIsAlreadyBookedException exception1 = new DateIsAlreadyBookedException(message);
        DateIsAlreadyBookedException exception2 = new DateIsAlreadyBookedException(message, cause);

        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    public void testInvalidDateRangeException() {
        String message = "Invalid date range.";
        Throwable cause = new RuntimeException("Cause");

        InvalidDateRangeException exception1 = new InvalidDateRangeException(message);
        InvalidDateRangeException exception2 = new InvalidDateRangeException(message, cause);

        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    public void testObjectNotFoundException() {
        String message = "Reservation not found.";
        Throwable cause = new RuntimeException("Cause");

        ObjectNotFoundException exception1 = new ObjectNotFoundException(message);
        ObjectNotFoundException exception2 = new ObjectNotFoundException(message, cause);

        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    public void testReservationUpdateException() {
        String message = "Error updating reservation.";
        Throwable cause = new RuntimeException("Cause");

        ReservationUpdateException exception1 = new ReservationUpdateException(message);
        ReservationUpdateException exception2 = new ReservationUpdateException(message, cause);

        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertEquals(cause, exception2.getCause());
    }
}
