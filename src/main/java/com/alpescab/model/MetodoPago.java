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
    }

    public Integer getMetodoPagoId() {
        return metodoPagoId;
    }

    public void setMetodoPagoId(Integer metodoPagoId) {
        this.metodoPagoId = metodoPagoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUltimos4() {
        return ultimos4;
    }

    public void setUltimos4(String ultimos4) {
        this.ultimos4 = ultimos4;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }


}

