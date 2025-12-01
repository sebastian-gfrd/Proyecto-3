package com.alpescab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * Modelo de documento para la colección de Viajes en MongoDB.
 * Representa un viaje en el sistema de ride-hailing ALPESCAB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Viajes")
public class Viajes {

    @Id
    private String id;

    @NotBlank(message = "El ID del conductor es obligatorio")
    private String conductorId;

    @NotBlank(message = "El ID del pasajero es obligatorio")
    private String pasajeroId;

    @NotBlank(message = "La ubicación de origen es obligatoria")
    private String origenUbicacion;

    @NotBlank(message = "La ubicación de destino es obligatoria")
    private String destinoUbicacion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    @NotNull(message = "El estado del viaje es obligatorio")
    private EstadoViaje estado;

    @Positive(message = "La tarifa debe ser un valor positivo")
    private Double tarifa;

    @Positive(message = "La distancia debe ser un valor positivo")
    private Double distanciaKm;

    private String comentarios;

    /**
     * Enum para representar los estados posibles de un viaje
     */
    public enum EstadoViaje {
        SOLICITADO,
        ACEPTADO,
        EN_CURSO,
        COMPLETADO,
        CANCELADO
    }
}
