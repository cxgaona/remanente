/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;

/**
 *
 * @author christian.gaona
 */
public class RemanenteCuatrimestralDTO {

    private RemanenteCuatrimestral remanenteCuatrimestral;
    private String periodo;
    private String ultimoEstado;

    public RemanenteCuatrimestralDTO() {

    }

    public RemanenteCuatrimestralDTO(RemanenteCuatrimestral remanenteCuatrimestral) {
        this.remanenteCuatrimestral = remanenteCuatrimestral;
        String periodoAux;
        switch (this.remanenteCuatrimestral.getCuatrimestre()) {
            case 1:
                periodoAux = "Enero-Abril";
                break;
            case 2:
                periodoAux = "Mayo-Agosto";
                break;
            case 3:
                periodoAux = "Septiembre-Diciembre";
                break;
            default:
                periodoAux = "Período sin Definir";
                break;
        }
        this.periodo = periodoAux;
        if (!this.remanenteCuatrimestral.getEstadoRemanenteCuatrimestralList().isEmpty()) {
            this.ultimoEstado = this.remanenteCuatrimestral.getEstadoRemanenteCuatrimestralList().get(
                    this.remanenteCuatrimestral.getEstadoRemanenteCuatrimestralList().size() - 1).getDescripcion();
        }
    }

    public RemanenteCuatrimestral getRemanenteCuatrimestral() {
        return remanenteCuatrimestral;
    }

    public void setRemanenteCuatrimestral(RemanenteCuatrimestral remanenteCuatrimestral) {
        this.remanenteCuatrimestral = remanenteCuatrimestral;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getUltimoEstado() {
        return ultimoEstado;
    }

    public void setUltimoEstado(String ultimoEstado) {
        this.ultimoEstado = ultimoEstado;
    }

}
