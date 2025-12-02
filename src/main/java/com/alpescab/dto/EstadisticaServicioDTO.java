package com.alpescab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaServicioDTO {
    private String tipoServicio;
    private Long cantidadServicios;
    private Double porcentaje;
}
