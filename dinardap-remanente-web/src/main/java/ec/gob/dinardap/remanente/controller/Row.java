/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller;

import java.math.BigDecimal;

/**
 *
 * @author christian.gaona
 */
public class Row {

    private String nombre;
    private String tipo;
    private String tipoIE;
    private BigDecimal valorMes1;
    private BigDecimal valorMes2;
    private BigDecimal valorMes3;
    private BigDecimal valorMes4;
    private BigDecimal valorTotal;

    public Row() {
        this.valorMes1=BigDecimal.ZERO;
        this.valorMes2=BigDecimal.ZERO;
        this.valorMes3=BigDecimal.ZERO;
        this.valorMes4=BigDecimal.ZERO;
        this.valorTotal=BigDecimal.ZERO;
        this.nombre="";
        this.tipo="";
        this.tipoIE="";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getValorMes1() {
        return valorMes1;
    }

    public void setValorMes1(BigDecimal valorMes1) {
        this.valorMes1 = valorMes1;
    }

    public BigDecimal getValorMes2() {
        return valorMes2;
    }

    public void setValorMes2(BigDecimal valorMes2) {
        this.valorMes2 = valorMes2;
    }

    public BigDecimal getValorMes3() {
        return valorMes3;
    }

    public void setValorMes3(BigDecimal valorMes3) {
        this.valorMes3 = valorMes3;
    }

    public BigDecimal getValorMes4() {
        return valorMes4;
    }

    public void setValorMes4(BigDecimal valorMes4) {
        this.valorMes4 = valorMes4;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoIE() {
        return tipoIE;
    }

    public void setTipoIE(String tipoIE) {
        this.tipoIE = tipoIE;
    }

}
