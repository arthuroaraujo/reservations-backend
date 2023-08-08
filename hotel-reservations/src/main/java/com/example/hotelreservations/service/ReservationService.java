package com.example.hotelreservations.service;

import com.example.hotelreservations.controller.ReservationStatus;
import com.example.hotelreservations.domain.Reservation;
import com.example.hotelreservations.dto.ReservationDTO;
import com.example.hotelreservations.repository.ReservationRepository;
import com.example.hotelreservations.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationDTO reservationDTO) {
        validateDateRange(reservationDTO.getDataInicio(), reservationDTO.getDataFim());
        validateDateNotInPast(reservationDTO.getDataInicio(), reservationDTO.getDataFim());

        if (!isDateAvailable(reservationDTO.getDataInicio(), reservationDTO.getDataFim())) {
            throw new DateIsAlreadyBookedException("Data indisponível para reserva.");
        }

        Reservation reservation = new Reservation();
        reservation.setNomeHospede(reservationDTO.getNomeHospede());
        reservation.setDataInicio(reservationDTO.getDataInicio());
        reservation.setDataFim(reservationDTO.getDataFim());
        reservation.setQuantidadePessoas(reservationDTO.getQuantidadePessoas());
        reservation.setStatus(ReservationStatus.CONFIRMADA);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation updateReservation(Integer id, ReservationDTO reservationDTO) {
        Reservation oldReservation = getReservationById(id);

        ReservationStatus newStatus = reservationDTO.getStatus();

        if (ReservationStatus.CANCELADA.equals(oldReservation.getStatus())) {
            throw new ReservationUpdateException("Não é possível atualizar uma reserva cancelada.");
        }

        if (ReservationStatus.CANCELADA.equals(newStatus)) {
            throw new ReservationUpdateException("Não é possível mudar o status para CANCELADA através desta operação.");
        }

        LocalDate newStartDate = reservationDTO.getDataInicio();
        LocalDate newEndDate = reservationDTO.getDataFim();

        validateDateRange(newStartDate, newEndDate);
        validateDateNotInPast(newStartDate, newEndDate);

        if (!newStartDate.isEqual(oldReservation.getDataInicio()) || !newEndDate.isEqual(oldReservation.getDataFim())) {
            if (!isDateAvailable(newStartDate, newEndDate)) {
                throw new DateIsAlreadyBookedException("Data indisponível para atualização.");
            }
        }

        oldReservation.setDataInicio(newStartDate);
        oldReservation.setDataFim(newEndDate);
        oldReservation.setNomeHospede(reservationDTO.getNomeHospede());
        oldReservation.setQuantidadePessoas(reservationDTO.getQuantidadePessoas());
        oldReservation.setStatus(newStatus);

        return reservationRepository.save(oldReservation);
    }


    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reservation getReservationById(Integer id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        return optionalReservation.orElseThrow(() -> new ObjectNotFoundException("Reserva não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllConfirmedReservations() {
        return reservationRepository.findAllConfirmed();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllPendingReservations() {
        return reservationRepository.findAllPending();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllCancelledReservations() {
        return reservationRepository.findAllCancelled();
    }

    @Transactional
    public Reservation cancelReservation(Integer id) {
        Reservation reservation = getReservationById(id);
        reservation.setStatus(ReservationStatus.CANCELADA);
        return reservationRepository.save(reservation);
    }

    public boolean isDateAvailable(LocalDate startDate, LocalDate endDate) {
        List<Reservation> overlappingReservations = reservationRepository.findByDataInicioLessThanEqualAndDataFimGreaterThanEqualAndStatus(
                endDate, startDate, ReservationStatus.CONFIRMADA);

        List<Reservation> overlappingPendingReservations = reservationRepository.findByDataInicioLessThanEqualAndDataFimGreaterThanEqualAndStatus(
                endDate, startDate, ReservationStatus.PENDENTE);

        return overlappingReservations.isEmpty() && overlappingPendingReservations.isEmpty();
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("A data de início da viagem não pode ser posterior à data de fim da viagem.");
        }
    }

    private void validateDateNotInPast(LocalDate... dates) {
        LocalDate currentDate = LocalDate.now();
        for (LocalDate date : dates) {
            if (date.isBefore(currentDate)) {
                throw new DateInPastException("Não é possível adicionar/atualizar reservas com datas no passado.");
            }
        }
    }
}
