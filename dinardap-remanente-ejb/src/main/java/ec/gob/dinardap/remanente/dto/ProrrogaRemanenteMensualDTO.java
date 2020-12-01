/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import ec.gob.dinardap.seguridad.modelo.Institucion;

/**
 *
 * @author christian.gaona
 */
public class ProrrogaRemanenteMensualDTO {

    private ProrrogaRemanenteMensual prorrogaRemanenteMensual;
    private Institucion institucion;
    private String institucionNombre;
    private Integer mesInt;
    private Integer año;
    private String mesStr;

    public ProrrogaRemanenteMensualDTO() {

    }

    public ProrrogaRemanenteMensualDTO(ProrrogaRemanenteMensual prorrogaRemanenteMensual, Institucion institucion, String institucionNombre, Integer mesInt, Integer año) {
        this.prorrogaRemanenteMensual = prorrogaRemanenteMensual;
        this.institucion = institucion;
        this.institucionNombre = institucionNombre;
        this.mesInt = mesInt;
        this.año = año;
        this.mesStr = getMesStr(mesInt);
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

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public String getInstitucionNombre() {
        return institucionNombre;
    }

    public void setInstitucionNombre(String institucionNombre) {
        this.institucionNombre = institucionNombre;
    }

    public Integer getMesInt() {
        return mesInt;
    }

    public void setMesInt(Integer mesInt) {
        this.mesInt = mesInt;
    }

    public String getMesStr() {
        return mesStr;
    }

    public void setMesStr(String mesStr) {
        this.mesStr = mesStr;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

}
