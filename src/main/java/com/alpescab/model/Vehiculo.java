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
@Document(collection = "Vehiculos")
public class Vehiculo {

    @Id
    @Field("vehiculo_id")
    private String id;

    @Field("ciudad_id")
    private String ciudadId;

    @Field("conductor_id")
    private String conductorId;

    @Field("placa")
    private String placa;

    @Field("modelo")
    private String modelo;

    @Field("tipo_vehiculo")
    private String tipoVehiculo;

    @Field("capacidad")
    private Integer capacidad;

    @Field("anio")
    private Integer anio;
}