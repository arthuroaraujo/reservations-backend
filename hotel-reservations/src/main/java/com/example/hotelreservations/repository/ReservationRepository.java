package com.example.hotelreservations.repository;

import com.example.hotelreservations.controller.ReservationStatus;
import com.example.hotelreservations.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT obj FROM Reservation obj WHERE obj.status = 'CONFIRMADA'")
    List<Reservation> findAllConfirmed();

    // Consulta para reservas com status PENDENTE
    @Query("SELECT obj FROM Reservation obj WHERE obj.status = 'PENDENTE'")
    List<Reservation> findAllPending();

    // Consulta para reservas com status CANCELADO
    @Query("SELECT obj FROM Reservation obj WHERE obj.status = 'CANCELADA'")
    List<Reservation> findAllCancelled();

    @Query("SELECT obj FROM Reservation obj WHERE obj.dataInicio <= :endDate AND obj.dataFim >= :startDate AND obj.status = :status")
    List<Reservation> findByDataInicioLessThanEqualAndDataFimGreaterThanEqualAndStatus(
            @Param("endDate") LocalDate endDate,
            @Param("startDate") LocalDate startDate,
            @Param("status") ReservationStatus status
    );

}
