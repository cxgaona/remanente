package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.dao.ReporteDao;
import ec.gob.dinardap.remanente.dto.UltimoEstadoDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "reporte1Ctrl")
@ViewScoped
public class reporte1Ctrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual    
    private String tituloPagina;
    //Variables de Negocio
    private Integer año;

    //Listas
    private List<UltimoEstadoDTO> ultimoEstadoDTOList;
    private List<UltimoEstadoDTO> ultimoEstadoDTOListFiltrado;

    //EJB's
    @EJB
    ReporteDao dao;

    @PostConstruct
    protected void init() {
        tituloPagina = "Reporte";
        ultimoEstadoDTOList = new ArrayList<UltimoEstadoDTO>();
    }

    public void buscarAño() {
        ultimoEstadoDTOList = dao.getUltimoEstado(año);
    }

    //Getters & Setters
    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public List<UltimoEstadoDTO> getUltimoEstadoDTOList() {
        return ultimoEstadoDTOList;
    }

    public void setUltimoEstadoDTOList(List<UltimoEstadoDTO> ultimoEstadoDTOList) {
        this.ultimoEstadoDTOList = ultimoEstadoDTOList;
    }

    public List<UltimoEstadoDTO> getUltimoEstadoDTOListFiltrado() {
        return ultimoEstadoDTOListFiltrado;
    }

    public void setUltimoEstadoDTOListFiltrado(List<UltimoEstadoDTO> ultimoEstadoDTOListFiltrado) {
        this.ultimoEstadoDTOListFiltrado = ultimoEstadoDTOListFiltrado;
    }

}
