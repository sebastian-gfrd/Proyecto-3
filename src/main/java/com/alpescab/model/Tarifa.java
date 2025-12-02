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
@Document(collection = "Tarifas")
public class Tarifa {

    @Id
    @Field("tarifa_id")
    private String tarifaId;

    @Field("ciudad_id")
    private String ciudadId;

    @Field("tipo_servicio")
    private String tipoServicio;

    @Field("tipo_vehiculo")
    private String tipoVehiculo;

    @Field("precio_km")
    private Double precioKm;

    @Field("tarifa_base")
    private Double tarifaBase;
}
