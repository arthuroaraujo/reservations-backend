package com.example.hotelreservations.controller;

import com.example.hotelreservations.domain.Reservation;
import com.example.hotelreservations.dto.ReservationDTO;
import com.example.hotelreservations.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/reservas")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation createdReservation = reservationService.createReservation(reservationDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdReservation.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdReservation);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Integer id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok().body(reservation);
    }

    @GetMapping("/confirmadas")
    public ResponseEntity<List<Reservation>> getConfirmedReservations() {
        List<Reservation> reservations = reservationService.findAllConfirmedReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<Reservation>> getPendingReservations() {
        List<Reservation> reservations = reservationService.findAllPendingReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/canceladas")
    public ResponseEntity<List<Reservation>> getCancelledReservations() {
        List<Reservation> reservations = reservationService.findAllCancelledReservations();
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Integer id,
            @RequestBody ReservationDTO reservationDTO
    ) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok().body(updatedReservation);
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Integer id) {
        Reservation canceledReservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(canceledReservation);
    }
}
