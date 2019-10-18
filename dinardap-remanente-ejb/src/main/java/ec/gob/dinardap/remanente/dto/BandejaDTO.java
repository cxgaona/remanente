/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Usuario;
import java.util.Date;

/**
 *
 * @author cristian.remache
 */
public class BandejaDTO {

    private Usuario usuarioAsignado;
    private Usuario usuarioSolicitante;
    private Integer añoRegistro, mesRegistro, diaRegistro;
    private Integer año, mes, dia;
    private String descripcion;
    private String estado;
    private Integer bandejaID;
    private Boolean leido;
    private Date fechaLeido;
    private Date fechaRegistro;
    private RemanenteCuatrimestral remanenteCuatrimestral;
    private RemanenteMensual remanenteMensual;

    public BandejaDTO() {
    }

    public Usuario getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public void setUsuarioAsignado(Usuario usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

    public Usuario getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public RemanenteCuatrimestral getRemanenteCuatrimestral() {
        return remanenteCuatrimestral;
    }

    public void setRemanenteCuatrimestral(RemanenteCuatrimestral remanenteCuatrimestral) {
        this.remanenteCuatrimestral = remanenteCuatrimestral;
    }

    public RemanenteMensual getRemanenteMensual() {
        return remanenteMensual;
    }

    public void setRemanenteMensual(RemanenteMensual remanenteMensual) {
        this.remanenteMensual = remanenteMensual;
    }

    public Integer getAñoRegistro() {
        return añoRegistro;
    }

    public void setAñoRegistro(Integer añoRegistro) {
        this.añoRegistro = añoRegistro;
    }

    public Integer getMesRegistro() {
        return mesRegistro;
    }

    public void setMesRegistro(Integer mesRegistro) {
        this.mesRegistro = mesRegistro;
    }

    public Integer getDiaRegistro() {
        return diaRegistro;
    }

    public void setDiaRegistro(Integer diaRegistro) {
        this.diaRegistro = diaRegistro;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getBandejaID() {
        return bandejaID;
    }

    public void setBandejaID(Integer bandejaID) {
        this.bandejaID = bandejaID;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public Date getFechaLeido() {
        return fechaLeido;
    }

    public void setFechaLeido(Date fechaLeido) {
        this.fechaLeido = fechaLeido;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
