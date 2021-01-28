package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.dao.ReporteDao;
import ec.gob.dinardap.remanente.dto.ConteoTramitesDTO;
import ec.gob.dinardap.remanente.dto.UltimoEstadoDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "reporte2Ctrl")
@ViewScoped
public class reporte2Ctrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual    
    private String tituloPagina;
    //Variables de Negocio
    private Integer año;

    //Listas
    private List<ConteoTramitesDTO> conteoTramitesDTOList;
    private List<ConteoTramitesDTO> conteoTramitesDTOListFiltrado;

    //EJB's
    @EJB
    ReporteDao dao;

    @PostConstruct
    protected void init() {
        tituloPagina = "Reporte";
        conteoTramitesDTOList = new ArrayList<ConteoTramitesDTO>();
    }

    public void buscarAño() {
        conteoTramitesDTOList = dao.getConteoTramites(año);
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

    public List<ConteoTramitesDTO> getConteoTramitesDTOList() {
        return conteoTramitesDTOList;
    }

    public void setConteoTramitesDTOList(List<ConteoTramitesDTO> conteoTramitesDTOList) {
        this.conteoTramitesDTOList = conteoTramitesDTOList;
    }

    public List<ConteoTramitesDTO> getConteoTramitesDTOListFiltrado() {
        return conteoTramitesDTOListFiltrado;
    }

    public void setConteoTramitesDTOListFiltrado(List<ConteoTramitesDTO> conteoTramitesDTOListFiltrado) {
        this.conteoTramitesDTOListFiltrado = conteoTramitesDTOListFiltrado;
    }

}
