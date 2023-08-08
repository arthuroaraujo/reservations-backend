package com.example.hotelreservations.service.exceptions;

public class DateIsAlreadyBookedException extends RuntimeException {
    public DateIsAlreadyBookedException(String message) {
        super(message);
    }

    public DateIsAlreadyBookedException(String message, Throwable cause) {
        super(message, cause);
    }

}
