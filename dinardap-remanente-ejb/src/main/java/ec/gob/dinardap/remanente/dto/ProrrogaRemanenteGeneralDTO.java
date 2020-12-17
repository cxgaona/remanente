/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;

/**
 *
 * @author christian.gaona
 */
public class ProrrogaRemanenteGeneralDTO {

    private ProrrogaRemanenteMensual prorrogaRemanenteMensual;
    private String mesStr;

    public ProrrogaRemanenteGeneralDTO() {
        this.prorrogaRemanenteMensual = new ProrrogaRemanenteMensual();
    }

    public ProrrogaRemanenteGeneralDTO(ProrrogaRemanenteMensual prorrogaRemanenteMensual) {
        this.prorrogaRemanenteMensual = prorrogaRemanenteMensual;
        this.mesStr = getMesStr(this.prorrogaRemanenteMensual.getMes());
    }

    private String getMesStr(Integer mes) {
        String mesAux = "";
        switch (mes) {
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
                mesAux = "DICIEMBRE";
                break;
            default:
                mesAux = "Fecha sin Definir";
                break;
        }
        return mesAux;
    }

    public ProrrogaRemanenteMensual getProrrogaRemanenteMensual() {
        return prorrogaRemanenteMensual;
    }

    public void setProrrogaRemanenteMensual(ProrrogaRemanenteMensual prorrogaRemanenteMensual) {
        this.prorrogaRemanenteMensual = prorrogaRemanenteMensual;
    }

    public String getMesStr() {
        return mesStr;
    }

    public void setMesStr(String mesStr) {
        this.mesStr = mesStr;
    }

}
