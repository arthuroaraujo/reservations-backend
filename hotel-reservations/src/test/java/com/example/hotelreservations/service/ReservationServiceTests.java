package com.example.hotelreservations.service;

import com.example.hotelreservations.controller.ReservationStatus;
import com.example.hotelreservations.domain.Reservation;
import com.example.hotelreservations.dto.ReservationDTO;
import com.example.hotelreservations.repository.ReservationRepository;
import com.example.hotelreservations.service.exceptions.ObjectNotFoundException;
import com.example.hotelreservations.service.exceptions.ReservationUpdateException;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReservationServiceTests {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void testCreateReservation() {
        ReservationDTO reservationDTO = new ReservationDTO("Maria", LocalDate.now(), LocalDate.now().plusDays(5), 2, ReservationStatus.CONFIRMADA);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        Reservation createdReservation = reservationService.createReservation(reservationDTO);

        assertNotNull(createdReservation);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testUpdateReservation() {
        ReservationDTO reservationDTO = new ReservationDTO("Maria", LocalDate.now(), LocalDate.now().plusDays(5), 2, ReservationStatus.CONFIRMADA);
        Reservation existingReservation = new Reservation(1, "João", LocalDate.now(), LocalDate.now().plusDays(6), 3, ReservationStatus.PENDENTE);
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(existingReservation);

        Reservation updatedReservation = reservationService.updateReservation(1, reservationDTO);

        assertNotNull(updatedReservation);
        verify(reservationRepository, times(1)).findById(eq(1));
        verify(reservationRepository, times(1)).save(eq(existingReservation));

        assertEquals("Maria", updatedReservation.getNomeHospede());
        assertEquals(LocalDate.now(), updatedReservation.getDataInicio());
        assertEquals(LocalDate.now().plusDays(5), updatedReservation.getDataFim());
        assertEquals(2, updatedReservation.getQuantidadePessoas());
        assertEquals(ReservationStatus.CONFIRMADA, updatedReservation.getStatus());
    }

    @Test
    public void testUpdateReservationWithCancelledStatus() {
        Integer reservationId = 1;
        Reservation oldReservation = new Reservation(reservationId, "Maria", LocalDate.now(), LocalDate.now().plusDays(5), 2, ReservationStatus.CANCELADA);
        ReservationDTO reservationDTO = new ReservationDTO("João", LocalDate.now(), LocalDate.now().plusDays(6), 3, ReservationStatus.PENDENTE);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(oldReservation));

        assertThrows(ReservationUpdateException.class, () -> reservationService.updateReservation(reservationId, reservationDTO));

        verify(reservationRepository, never()).save(any(Reservation.class));
    }


    @Test
    public void testGetAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CONFIRMADA));
        reservations.add(new Reservation(2, "Maria", LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), 4, ReservationStatus.CONFIRMADA));

        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(2, result.size());

        Reservation reservation1 = result.get(0);
        assertEquals(1, reservation1.getId());
        assertEquals("Joao", reservation1.getNomeHospede());
        assertEquals(LocalDate.now(), reservation1.getDataInicio());
        assertEquals(LocalDate.now().plusDays(3), reservation1.getDataFim());
        assertEquals(2, reservation1.getQuantidadePessoas());
        assertEquals(ReservationStatus.CONFIRMADA, reservation1.getStatus());

        Reservation reservation2 = result.get(1);
        assertEquals(2, reservation2.getId());
        assertEquals("Maria", reservation2.getNomeHospede());
        assertEquals(LocalDate.now().plusDays(4), reservation2.getDataInicio());
        assertEquals(LocalDate.now().plusDays(7), reservation2.getDataFim());
        assertEquals(4, reservation2.getQuantidadePessoas());
        assertEquals(ReservationStatus.CONFIRMADA, reservation2.getStatus());
    }

    @Test
    public void testGetReservationByIdExistente() {
        Reservation reservation = new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CONFIRMADA);
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Joao", result.getNomeHospede());
        assertEquals(LocalDate.now(), result.getDataInicio());
        assertEquals(LocalDate.now().plusDays(3), result.getDataFim());
        assertEquals(2, result.getQuantidadePessoas());
        assertEquals(ReservationStatus.CONFIRMADA, result.getStatus());
    }

    @Test
    public void testGetReservationByIdNaoExistente() {
        when(reservationRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> reservationService.getReservationById(99));

        verify(reservationRepository, times(1)).findById(99);
    }

    @Test
    public void testFindAllConfirmedReservations() {
        List<Reservation> confirmedReservations = List.of(
                new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CONFIRMADA),
                new Reservation(2, "Maria", LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), 4, ReservationStatus.CONFIRMADA)
        );
        when(reservationRepository.findAllConfirmed()).thenReturn(confirmedReservations);

        List<Reservation> result = reservationService.findAllConfirmedReservations();

        assertEquals(2, result.size());
        assertEquals(ReservationStatus.CONFIRMADA, result.get(0).getStatus());
        assertEquals(ReservationStatus.CONFIRMADA, result.get(1).getStatus());
    }

    @Test
    public void testFindAllPendingReservations() {
        List<Reservation> pendingReservations = List.of(
                new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.PENDENTE),
                new Reservation(2, "Maria", LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), 4, ReservationStatus.PENDENTE)
        );
        when(reservationRepository.findAllPending()).thenReturn(pendingReservations);

        List<Reservation> result = reservationService.findAllPendingReservations();

        assertEquals(2, result.size());
        assertEquals(ReservationStatus.PENDENTE, result.get(0).getStatus());
        assertEquals(ReservationStatus.PENDENTE, result.get(1).getStatus());
    }

    @Test
    public void testFindAllCancelledReservations() {
        List<Reservation> cancelledReservations = List.of(
                new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CANCELADA),
                new Reservation(2, "Maria", LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), 4, ReservationStatus.CANCELADA)
        );
        when(reservationRepository.findAllCancelled()).thenReturn(cancelledReservations);

        List<Reservation> result = reservationService.findAllCancelledReservations();

        assertEquals(2, result.size());
        assertEquals(ReservationStatus.CANCELADA, result.get(0).getStatus());
        assertEquals(ReservationStatus.CANCELADA, result.get(1).getStatus());
    }

    @Test
    public void testCancelReservation() {
        Integer reservationId = 1;
        Reservation reservation = new Reservation(reservationId, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CONFIRMADA);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.cancelReservation(reservationId);

        assertNotNull(result);
        assertEquals(ReservationStatus.CANCELADA, result.getStatus());
        verify(reservationRepository, times(1)).save(result);
    }

}
