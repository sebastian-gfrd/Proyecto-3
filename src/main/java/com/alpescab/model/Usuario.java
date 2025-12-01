package com.alpescab.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "Usuarios") // Debe coincidir con db.createCollection
public class Usuario {

    @Id
    private String id; // El id generado por MOnGo

    // Este es tu ID de negocio (int) definido en el schema
    @Indexed(unique = true) 
    private Integer usuarioId; 

    private String nombre;
    private String correo;
    private String telefono;
    private String rol;
    private Date fechaCreado;

    
    private List<MetodoPago> metodosPago; 

    public Usuario() {
        this.metodosPago = new ArrayList<>();
    }

    // Constructor con datos básicos
    public Usuario(Integer usuarioId, String nombre, String correo, String rol) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.fechaCreado = new Date();
        this.metodosPago = new ArrayList<>();
    }}
