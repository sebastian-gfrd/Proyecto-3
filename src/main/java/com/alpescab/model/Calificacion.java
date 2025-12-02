package com.alpescab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Calificaciones")
public class Calificacion {

    @Id
    @Field("calificacion_id")
    private String calificacionId;

    @Field("usuario_id")
    private String usuarioId;

    @Field("conductor_id")
    private String conductorId;

    @Field("puntuacion")
    private Double puntuacion;

    @Field("comentario")
    private String comentario;

    @CreatedDate
    @Field("fecha_creada")
    private LocalDateTime fechaCreada;
}
