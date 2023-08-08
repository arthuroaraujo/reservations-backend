package com.example.hotelreservations.dto;

import com.example.hotelreservations.controller.ReservationStatus;

import java.time.LocalDate;

public class ReservationDTO {
    private String nomeHospede;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int quantidadePessoas;
    private ReservationStatus status;

    // Getters, setters e construtor

    public ReservationDTO(String nomeHospede, LocalDate dataInicio, LocalDate dataFim, int quantidadePessoas, ReservationStatus status) {
        this.nomeHospede = nomeHospede;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidadePessoas = quantidadePessoas;
        this.status = status;
    }

    public ReservationDTO() {
    }

    public String getNomeHospede() {
        return nomeHospede;
    }

    public void setNomeHospede(String nomeHospede) {
        this.nomeHospede = nomeHospede;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

}
