/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

/**
 *
 * @author christian.gaona
 */
public class ConteoTramitesDTO {

    private String nombreInstitucion;
    private Integer mes;
    private String mesStr;
    private String actividadRegistral;
    private String tipoTramite;
    private Integer conteo;

    public ConteoTramitesDTO() {
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

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
        this.mesStr = getMesStr(mes);
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public String getMesStr() {
        return mesStr;
    }

    public void setMesStr(String mesStr) {
        this.mesStr = mesStr;
    }

    public String getActividadRegistral() {
        return actividadRegistral;
    }

    public void setActividadRegistral(String actividadRegistral) {
        this.actividadRegistral = actividadRegistral;
    }

    public String getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(String tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Integer getConteo() {
        return conteo;
    }

    public void setConteo(Integer conteo) {
        this.conteo = conteo;
    }

}
