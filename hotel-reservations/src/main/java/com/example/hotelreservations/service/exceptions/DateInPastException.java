package com.example.hotelreservations.service.exceptions;

public class DateInPastException extends RuntimeException{
    public DateInPastException(String message) {
        super(message);
    }

    public DateInPastException(String message, Throwable cause) {
        super(message, cause);
    }
}
