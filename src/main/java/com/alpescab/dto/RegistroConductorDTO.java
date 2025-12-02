package com.alpescab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroConductorDTO {
    private String usuarioId;
    private String nombreCompleto;
    private String ciudadId;
    private String numeroLicencia;
    private Double latitud;
    private Double longitud;
}
