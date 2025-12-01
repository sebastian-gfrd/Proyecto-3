package com.alpescab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetodosPago {
    private Integer metodo_pago_id;
    private String tipo;
    private String ultimos4;
    private String proveedor;
}
