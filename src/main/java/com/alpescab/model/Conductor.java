package com.alpescab.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.mapping.Document;

@Data 
@NoArgsConstructor // Constructor vacío (Obligatorio para que MongoDB pueda instanciar la clase)
@AllArgsConstructor // Constructor con todos los argumentos (Útil si usas el Builder o pruebas)
@Document(collection = "Conductores")
public class Conductor {
    

    @Id
    private String id; // Mapea al _id de Mongo (ObjectId)

    private Integer usuarioId; // Referencia al Usuario
    private Integer ciudadId;
    
    // Patrón Extended Reference: Guardamos nombre para no hacer join cada vez
    private String nombreCompleto; 

    private String numeroLicencia;
    
    // La calificacion se inicializa en 
    private Double calificacionAvg = 0.0;
    
    // Patron Computed  para el RFC2: Contador acumulativo de viajes completados
    private Integer totalViajes = 0;

    private EstadoConductor estado = EstadoConductor.ACTIVO; // "activo", "inactivo", "ocupado"

    //  CONFIGURACIÓN GEOESPACIAL (RF6) 
    // Índice geoespacial para búsquedas "Near"
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint ubicacionActual;

    
    
    public Conductor(Integer usuarioId, String nombreCompleto, Integer ciudadId, String numeroLicencia, Double latitud, Double longitud) {
        this.usuarioId = usuarioId;
        this.nombreCompleto = nombreCompleto;
        this.ciudadId = ciudadId;
        this.numeroLicencia = numeroLicencia;
        
    
        
        // Aqui se crea el punto en formato GeoJason
        this.ubicacionActual = new GeoJsonPoint(longitud, latitud);
    }
}