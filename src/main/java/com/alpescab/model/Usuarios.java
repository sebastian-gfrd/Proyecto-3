package com.alpescab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Usuarios")
public class Usuarios {

    @Id
    private String usuario_id;

    private String nombre;

    @Indexed(unique = true)
    private String correo;

    private String telefono;

    private String rol;

    @CreatedDate
    private Date fecha_creado;

    private List<MetodosPago> metodosPago;
}
