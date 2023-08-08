package com.example.hotelreservations.service.exceptions;

public class ReservationUpdateException extends RuntimeException {
    public ReservationUpdateException(String message) {
        super(message);
    }

    public ReservationUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

}
