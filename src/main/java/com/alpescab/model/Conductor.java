package com.alpescab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Conductores")
public class Conductor {

    @Id
    private String conductor_id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("ciudad_id")
    private String ciudadId;

    @Field("numero_licencia")
    private String numeroLicencia;

    @Field("calificacion_avg")
    private Double calificacionAvg;

    private String estado;
}