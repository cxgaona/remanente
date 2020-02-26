/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

import ec.gob.dinardap.remanente.modelo.RemanenteMensual;

/**
 *
 * @author christian.gaona
 */
public class RemanenteMensualDTO {

    private RemanenteMensual remanenteMensual;
    private String mesStr;
    private String ultimoEstado;
    private String descEstadoMesStr;

    public RemanenteMensualDTO() {

    }

    public RemanenteMensualDTO(RemanenteMensual remanenteMensual) {
        this.remanenteMensual = remanenteMensual;
        String mesAux;
        switch (this.remanenteMensual.getMes()) {
            case 1:
                mesAux = "ENERO";
                break;
            case 2:
                mesAux = "FEBRERO";
                break;
            case 3:
                mesAux = "MARZO";
                break;
            case 4:
                mesAux = "ABRIL";
                break;
            case 5:
                mesAux = "MAYO";
                break;
            case 6:
                mesAux = "JUNIO";
                break;
            case 7:
                mesAux = "JULIO";
                break;
            case 8:
                mesAux = "AGOSTO";
                break;
            case 9:
                mesAux = "SEPTIEMBRE";
                break;
            case 10:
                mesAux = "OCTUBRE";
                break;
            case 11:
                mesAux = "NOVIEMBRE";
                break;
            case 12:
                mesAux = "DICIEMRE";
                break;
            default:
                mesAux = "Fecha sin Definir";
                break;
        }
        this.mesStr = mesAux;
        this.descEstadoMesStr = this.mesStr;
        if (this.remanenteMensual.getEstadoRemanenteMensualList().get(0).getDescripcion().equals("GeneradoNuevaVersion")) {
            this.descEstadoMesStr = this.mesStr + " (NUEVA VERSIÓN)";
        }

    }

    public RemanenteMensual getRemanenteMensual() {
        return remanenteMensual;
    }

    public void setRemanenteMensual(RemanenteMensual remanenteMensual) {
        this.remanenteMensual = remanenteMensual;
    }

    public String getMesStr() {
        return mesStr;
    }

    public void setMesStr(String mesStr) {
        this.mesStr = mesStr;
    }

    public String getUltimoEstado() {
        return ultimoEstado;
    }

    public void setUltimoEstado(String ultimoEstado) {
        this.ultimoEstado = ultimoEstado;
    }

    public String getDescEstadoMesStr() {
        return descEstadoMesStr;
    }

    public void setDescEstadoMesStr(String descEstadoMesStr) {
        this.descEstadoMesStr = descEstadoMesStr;
    }

}
