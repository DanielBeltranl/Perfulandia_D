package com.example.perfulandia.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "pago")
@NoArgsConstructor
@AllArgsConstructor

public class Pago {

    @Id
    @Column(name = "id_pago")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column (name = "run_cliente")
    private String runCliente ;

    @Column(name = "monto_pago", nullable = false)
    private int monto;

    @Column (name = "fecha_pago", nullable = false)
    private Date fecha;

    @Column(name = "metodo_pago", nullable = false)
    private String metodo;

    @Column(name = "banco_pago", nullable = false)
    private String banco;


    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getRunCliente() {
        return runCliente;
    }

    public void setRunCliente(String runCliente) {
        this.runCliente = runCliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
