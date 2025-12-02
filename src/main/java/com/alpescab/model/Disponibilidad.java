package com.alpescab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidad {

    private String diaSemana; // e.g., "Lunes", "Martes"
    private String horaInicio; // Format "HH:mm"
    private String horaFin; // Format "HH:mm"
}
