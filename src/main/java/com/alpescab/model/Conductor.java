package com.alpescab.model;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.geo.Point;

@Document(collection = "Conductores")
public class Conductor {

    @Id
    private String id; // Este mapea al "conductor_id": { bsonType: "ObjectId" }

    private Integer usuarioId; // Referencia al usuario (int según tu schema anterior)
    private Integer ciudadId;
    
    // Campo del patrón Extended Reference
    private String nombreCompleto; 

    private String numeroLicencia;
    
    private Double calificacionAvg;
    
    // Aqui se muestra el acumulado de viajes completados del conductor (RFC2)
    private Integer totalViajes;

    private String estado; // "activo", "inactivo", "ocupado"

    // --- CONFIGURACIÓN GEOESPACIAL (RF6) ---
    // Esto permite usar repositorio.findByUbicacionActualNear(...)
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint ubicacionActual;

    public Conductor() {}

    public Conductor(Integer usuarioId, String nombreCompleto, Integer ciudadId, Double latitud, Double longitud) {
        this.usuarioId = usuarioId;
        this.nombreCompleto = nombreCompleto;
        this.ciudadId = ciudadId;
        this.calificacionAvg = 5.0;
        this.totalViajes = 0;
        this.estado = "activo";
        
        // Inicializar ubicación (Longitud, Latitud)
        this.ubicacionActual = new GeoJsonPoint(longitud, latitud);
    }}
