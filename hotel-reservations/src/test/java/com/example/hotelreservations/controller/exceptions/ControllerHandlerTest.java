package com.example.hotelreservations.controller.exceptions;

import com.example.hotelreservations.controller.ReservationStatus;
import com.example.hotelreservations.dto.ReservationDTO;
import com.example.hotelreservations.service.ReservationService;
import com.example.hotelreservations.service.exceptions.DateIsAlreadyBookedException;
import com.example.hotelreservations.service.exceptions.ReservationUpdateException;
import com.example.hotelreservations.service.exceptions.ObjectNotFoundException;
import com.example.hotelreservations.service.exceptions.DateInPastException;
import com.example.hotelreservations.service.exceptions.InvalidDateRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ControllerHandlerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        ReservationDTO reservationDTO = new ReservationDTO("Maria", LocalDate.now(), LocalDate.now().plusDays(5), 3, ReservationStatus.CONFIRMADA);
    }

    @Test
    public void testHandleDateIsAlreadyBookedException() throws Exception {
        when(reservationService.createReservation(any(ReservationDTO.class)))
                .thenThrow(new DateIsAlreadyBookedException("Date is already booked."));

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeHospede\":\"Maria\",\"dataInicio\":\"2023-08-10\",\"dataFim\":\"2023-08-15\",\"quantidadePessoas\":2,\"status\":\"CONFIRMADA\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Date is already booked."));
    }

    @Test
    public void testHandleReservationUpdateException() throws Exception {
        when(reservationService.updateReservation(anyInt(), any(ReservationDTO.class)))
                .thenThrow(new ReservationUpdateException("Error updating reservation."));

        mockMvc.perform(put("/reservas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeHospede\":\"Maria\",\"dataInicio\":\"2023-08-10\",\"dataFim\":\"2023-08-15\",\"quantidadePessoas\":2,\"status\":\"CONFIRMADA\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error updating reservation."));
    }

    @Test
    public void testHandleObjectNotFoundException() throws Exception {
        when(reservationService.getReservationById(anyInt()))
                .thenThrow(new ObjectNotFoundException("Reservation not found."));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Reservation not found."));
    }

    @Test
    public void testHandleDateInPastException() throws Exception {
        when(reservationService.createReservation(any(ReservationDTO.class)))
                .thenThrow(new DateInPastException("Date is in the past."));

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeHospede\":\"Maria\",\"dataInicio\":\"2022-01-01\",\"dataFim\":\"2022-01-15\",\"quantidadePessoas\":2,\"status\":\"CONFIRMADA\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Date is in the past."));
    }

    @Test
    public void testHandleInvalidDateRangeException() throws Exception {
        when(reservationService.createReservation(any(ReservationDTO.class)))
                .thenThrow(new InvalidDateRangeException("Invalid date range."));

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeHospede\":\"Maria\",\"dataInicio\":\"2023-08-10\",\"dataFim\":\"2023-08-05\",\"quantidadePessoas\":2,\"status\":\"CONFIRMADA\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid date range."));
    }
}
