package com.example.hotelreservations.controller;

import com.example.hotelreservations.domain.Reservation;
import com.example.hotelreservations.dto.ReservationDTO;
import com.example.hotelreservations.service.ReservationService;
import com.example.hotelreservations.service.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    public void setup() {
        ReservationDTO reservationDTO = new ReservationDTO("Maria", LocalDate.now(), LocalDate.now().plusDays(5), 3, ReservationStatus.CONFIRMADA);
    }

    @Test
    public void testCreateReservation() throws Exception {
        Reservation reservation = new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CONFIRMADA);

        when(reservationService.createReservation(any(ReservationDTO.class))).thenReturn(reservation);

        mockMvc.perform(post("/reservas")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"nomeHospede\": \"Joao\", \"dataInicio\": \"2023-08-06\", \"dataFim\": \"2023-08-09\", \"quantidadePessoas\": 2, \"status\": \"CONFIRMADA\"}"))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", "http://localhost/reservas/1"));
    }

    @Test
    public void testCancelReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservas/1/cancelar"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindReservationByIdExistente() throws Exception {
        Integer idReservation = 1;

        Reservation reservationEncontrada = new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CONFIRMADA);

        when(reservationService.getReservationById(idReservation)).thenReturn(reservationEncontrada);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeHospede").value("Joao"))
                .andExpect(jsonPath("$.dataInicio").value(reservationEncontrada.getDataInicio().toString()))
                .andExpect(jsonPath("$.dataFim").value(reservationEncontrada.getDataFim().toString()))
                .andExpect(jsonPath("$.quantidadePessoas").value(2))
                .andExpect(jsonPath("$.status").value("CONFIRMADA"));
    }

    @Test
    public void testUpdateReservation() throws Exception {
        Reservation reservation = new Reservation(1, "Maria", LocalDate.now(), LocalDate.now().plusDays(3), 3, ReservationStatus.PENDENTE);

        when(reservationService.updateReservation(eq(1), any(ReservationDTO.class))).thenReturn(reservation);

        mockMvc.perform(put("/reservas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeHospede\": \"Maria\", \"dataInicio\": \"2023-08-06\", \"dataFim\": \"2023-08-11\", \"quantidadePessoas\": 3, \"status\": \"PENDENTE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeHospede").value("Maria"))
                .andExpect(jsonPath("$.dataInicio").value(reservation.getDataInicio().toString()))
                .andExpect(jsonPath("$.dataFim").value(reservation.getDataFim().toString()))
                .andExpect(jsonPath("$.quantidadePessoas").value(3))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    public void testGetConfirmedReservations() throws Exception {
        Reservation reservation1 = new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CONFIRMADA);
        Reservation reservation2 = new Reservation(2, "Maria", LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), 4, ReservationStatus.CONFIRMADA);

        List<Reservation> confirmedReservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.findAllConfirmedReservations()).thenReturn(confirmedReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/confirmadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomeHospede").value(reservation1.getNomeHospede()))
                .andExpect(jsonPath("$[0].dataInicio").value(reservation1.getDataInicio().toString()))
                .andExpect(jsonPath("$[0].dataFim").value(reservation1.getDataFim().toString()))
                .andExpect(jsonPath("$[0].quantidadePessoas").value(reservation1.getQuantidadePessoas()))
                .andExpect(jsonPath("$[0].status").value(reservation1.getStatus().toString()))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nomeHospede").value(reservation2.getNomeHospede()))
                .andExpect(jsonPath("$[1].dataInicio").value(reservation2.getDataInicio().toString()))
                .andExpect(jsonPath("$[1].dataFim").value(reservation2.getDataFim().toString()))
                .andExpect(jsonPath("$[1].quantidadePessoas").value(reservation2.getQuantidadePessoas()));
    }

    @Test
    public void testGetPendingReservations() throws Exception {
        Reservation reservation1 = new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.PENDENTE);
        Reservation reservation2 = new Reservation(2, "Maria", LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), 4, ReservationStatus.PENDENTE);

        List<Reservation> pendingReservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.findAllPendingReservations()).thenReturn(pendingReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/pendentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation1.getId()))
                .andExpect(jsonPath("$[0].nomeHospede").value(reservation1.getNomeHospede()))
                .andExpect(jsonPath("$[0].dataInicio").value(reservation1.getDataInicio().toString()))
                .andExpect(jsonPath("$[0].dataFim").value(reservation1.getDataFim().toString()))
                .andExpect(jsonPath("$[0].quantidadePessoas").value(reservation1.getQuantidadePessoas()))
                .andExpect(jsonPath("$[0].status").value(reservation1.getStatus().toString()))

                .andExpect(jsonPath("$[1].id").value(reservation2.getId()))
                .andExpect(jsonPath("$[1].nomeHospede").value(reservation2.getNomeHospede()))
                .andExpect(jsonPath("$[1].dataInicio").value(reservation2.getDataInicio().toString()))
                .andExpect(jsonPath("$[1].dataFim").value(reservation2.getDataFim().toString()))
                .andExpect(jsonPath("$[1].quantidadePessoas").value(reservation2.getQuantidadePessoas()))
                .andExpect(jsonPath("$[1].status").value(reservation2.getStatus().toString()));
    }


    @Test
    public void testGetCancelledReservations() throws Exception {
        Reservation reservation1 = new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CANCELADA);
        Reservation reservation2 = new Reservation(2, "Maria", LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), 4, ReservationStatus.CANCELADA);

        List<Reservation> cancelledReservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.findAllCancelledReservations()).thenReturn(cancelledReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/canceladas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation1.getId()))
                .andExpect(jsonPath("$[0].nomeHospede").value(reservation1.getNomeHospede()))
                .andExpect(jsonPath("$[0].dataInicio").value(reservation1.getDataInicio().toString()))
                .andExpect(jsonPath("$[0].dataFim").value(reservation1.getDataFim().toString()))
                .andExpect(jsonPath("$[0].quantidadePessoas").value(reservation1.getQuantidadePessoas()))
                .andExpect(jsonPath("$[0].status").value(reservation1.getStatus().toString()))

                .andExpect(jsonPath("$[1].id").value(reservation2.getId()))
                .andExpect(jsonPath("$[1].nomeHospede").value(reservation2.getNomeHospede()))
                .andExpect(jsonPath("$[1].dataInicio").value(reservation2.getDataInicio().toString()))
                .andExpect(jsonPath("$[1].dataFim").value(reservation2.getDataFim().toString()))
                .andExpect(jsonPath("$[1].quantidadePessoas").value(reservation2.getQuantidadePessoas()))
                .andExpect(jsonPath("$[1].status").value(reservation2.getStatus().toString()));
    }


    @Test
    public void testGetAllReservations() throws Exception {
        Reservation reservation1 = new Reservation(1, "Joao", LocalDate.now(), LocalDate.now().plusDays(3), 2, ReservationStatus.CONFIRMADA);
        Reservation reservation2 = new Reservation(2, "Maria", LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), 4, ReservationStatus.CANCELADA);

        List<Reservation> allReservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.getAllReservations()).thenReturn(allReservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation1.getId()))
                .andExpect(jsonPath("$[0].nomeHospede").value(reservation1.getNomeHospede()))
                .andExpect(jsonPath("$[0].dataInicio").value(reservation1.getDataInicio().toString()))
                .andExpect(jsonPath("$[0].dataFim").value(reservation1.getDataFim().toString()))
                .andExpect(jsonPath("$[0].quantidadePessoas").value(reservation1.getQuantidadePessoas()))
                .andExpect(jsonPath("$[0].status").value(reservation1.getStatus().toString()))

                .andExpect(jsonPath("$[1].id").value(reservation2.getId()))
                .andExpect(jsonPath("$[1].nomeHospede").value(reservation2.getNomeHospede()))
                .andExpect(jsonPath("$[1].dataInicio").value(reservation2.getDataInicio().toString()))
                .andExpect(jsonPath("$[1].dataFim").value(reservation2.getDataFim().toString()))
                .andExpect(jsonPath("$[1].quantidadePessoas").value(reservation2.getQuantidadePessoas()))
                .andExpect(jsonPath("$[1].status").value(reservation2.getStatus().toString()));
    }

}
