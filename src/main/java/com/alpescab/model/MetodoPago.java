package com.alpescab.model;

public class MetodoPago {
    
    private Integer metodoPagoId;
    private String tipo;      // "tarjeta", "efectivo"
    private String ultimos4;
    private String proveedor; // "VISA", "MASTERCARD"
    private String fechaVencimiento; 

    public MetodoPago() {}

    public MetodoPago(Integer metodoPagoId, String tipo, String ultimos4, String proveedor) {
        this.metodoPagoId = metodoPagoId;
        this.tipo = tipo;
        this.ultimos4 = ultimos4;
        this.proveedor = proveedor;
    }}
