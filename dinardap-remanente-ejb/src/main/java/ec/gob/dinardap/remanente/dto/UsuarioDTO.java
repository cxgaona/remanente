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
public class UsuarioDTO {

    private String perfil;
    private Integer usuarioID;
    private Integer institucionID;
    private Integer gadID;
    private Boolean registrador;
    private Boolean verificador;
    private Boolean validador;
    private Boolean administrador;
    private String tipo;

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Integer getInstitucionID() {
        return institucionID;
    }

    public void setInstitucionID(Integer institucionID) {
        this.institucionID = institucionID;
    }

    public Integer getGadID() {
        return gadID;
    }

    public void setGadID(Integer gadID) {
        this.gadID = gadID;
    }

    public Boolean getRegistrador() {
        return registrador;
    }

    public void setRegistrador(Boolean registrador) {
        this.registrador = registrador;
    }

    public Boolean getVerificador() {
        return verificador;
    }

    public void setVerificador(Boolean verificador) {
        this.verificador = verificador;
    }

    public Boolean getValidador() {
        return validador;
    }

    public void setValidador(Boolean validador) {
        this.validador = validador;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
