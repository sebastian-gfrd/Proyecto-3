package com.alpescab.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Component
@Scope("prototype")
@Data
@Document("Ciudades")
public class Ciudades {
    @Id
    private String ciudad_id;
    private String nombre;
    private String pais;
}