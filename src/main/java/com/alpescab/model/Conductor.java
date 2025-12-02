package com.alpescab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.geo.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Conductores")
public class Conductor {

    @Id
    private String conductor_id; 

    
    @Field("usuarioId")
    private String usuarioId; 

    @Field("ciudad_id")
    private String ciudadId;

    
    @Field("nombre_completo")
    private String nombreCompleto;

    @Field("numero_licencia")
    private String numeroLicencia;

   
    @Field("calificacion_avg")
    private Double calificacionAvg = 5.0;

    // Contador para el RFC2
    @Field("total_viajes")
    private Integer totalViajes = 0;

    
    private EstadoConductor estado = EstadoConductor.ACTIVO;

   
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    @Field("ubicacion_actual")
    private GeoJsonPoint ubicacionActual;

    
    public Conductor(String usuarioId, String nombreCompleto, String ciudadId, String numeroLicencia, Double latitud, Double longitud) {
        this.usuarioId = usuarioId;
        this.nombreCompleto = nombreCompleto;
        this.ciudadId = ciudadId;
        this.numeroLicencia = numeroLicencia;
        
        
        this.calificacionAvg = 5.0;
        this.totalViajes = 0;
        this.estado = EstadoConductor.ACTIVO;
        
        
        this.ubicacionActual = new GeoJsonPoint(longitud, latitud);
    }
}