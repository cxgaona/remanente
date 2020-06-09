/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;

/**
 *
 * @author christian.gaona
 */
public class SolicitudCambioDTO {

    private InstitucionRequerida institucion;
    private String institucionNombre;
    private RemanenteMensual remanenteMensual;
    private Integer mesInt;
    private Integer año;
    private String mesStr;
    private String estado;

    public SolicitudCambioDTO() {

    }

    public SolicitudCambioDTO(RemanenteMensual remanenteMensual) {
        this.remanenteMensual = remanenteMensual;
        this.institucionNombre = remanenteMensual.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getNombre();
        this.mesInt = this.remanenteMensual.getMes();
        this.año = this.remanenteMensual.getRemanenteCuatrimestral().getRemanenteAnual().getAnio();
        this.mesStr = getMesStr(mesInt);
        this.estado = this.remanenteMensual.getEstadoRemanenteMensualList().get(this.remanenteMensual.getEstadoRemanenteMensualList().size() - 1).getDescripcion();
    }

    public SolicitudCambioDTO(InstitucionRequerida institucion, String institucionNombre, RemanenteMensual remanenteMensual, Integer mesInt, Integer año) {
        this.institucion = institucion;
        this.institucionNombre = institucionNombre;
        this.remanenteMensual = remanenteMensual;
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
                mesAux = "DICIEMRE";
                break;
            default:
                mesAux = "Fecha sin Definir";
                break;
        }
        return mesAux;
    }

    public InstitucionRequerida getInstitucion() {
        return institucion;
    }

    public void setInstitucion(InstitucionRequerida institucion) {
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

    public RemanenteMensual getRemanenteMensual() {
        return remanenteMensual;
    }

    public void setRemanenteMensual(RemanenteMensual remanenteMensual) {
        this.remanenteMensual = remanenteMensual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
