package com.alpescab.dto;

import lombok.Data;

@Data
public class SolicitudViajeDTO {
    private String pasajeroId;
    private String ciudadId;
    private String origenUbicacion;
    private String destinoUbicacion;
    private String tipoServicio; // e.g., "STANDARD", "PREMIUM"
    private Double distanciaKm;
}
