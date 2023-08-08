package com.example.hotelreservations.controller.exceptions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(StandardError.class)
public class StandardErrorTest {

    @Test
    public void testGetTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        StandardError error = new StandardError(timestamp, 400, "Bad Request");
        assertEquals(timestamp, error.getTimestamp());
    }

    @Test
    public void testGetStatus() {
        StandardError error = new StandardError(LocalDateTime.now(), 404, "Not Found");
        assertEquals(404, error.getStatus());
    }

    @Test
    public void testGetMessage() {
        StandardError error = new StandardError(LocalDateTime.now(), 500, "Internal Server Error");
        assertEquals("Internal Server Error", error.getMessage());
    }

    @Test
    public void testSetTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        StandardError error = new StandardError(null, 200, "OK");
        error.setTimestamp(timestamp);
        assertEquals(timestamp, error.getTimestamp());
    }

    @Test
    public void testSetStatus() {
        StandardError error = new StandardError(LocalDateTime.now(), 201, "Created");
        error.setStatus(202);
        assertEquals(202, error.getStatus());
    }

    @Test
    public void testSetMessage() {
        StandardError error = new StandardError(LocalDateTime.now(), 204, "No Content");
        error.setMessage("Updated");
        assertEquals("Updated", error.getMessage());
    }
}
