package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.dao.ReporteDao;
import ec.gob.dinardap.remanente.dto.ValoresTransaccionesDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "reporte3Ctrl")
@ViewScoped
public class reporte3Ctrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual    
    private String tituloPagina;
    //Variables de Negocio
    private Integer año;

    //Listas
    private List<ValoresTransaccionesDTO> valoresTransaccionesDTOList;
    private List<ValoresTransaccionesDTO> valoresTransaccionesDTOListFiltrado;

    //EJB's
    @EJB
    ReporteDao dao;

    @PostConstruct
    protected void init() {
        tituloPagina = "Reporte";
        valoresTransaccionesDTOList = new ArrayList<ValoresTransaccionesDTO>();
    }

    public void buscarAño() {
        valoresTransaccionesDTOList = dao.getValoresTransacciones(año);
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

    public List<ValoresTransaccionesDTO> getValoresTransaccionesDTOList() {
        return valoresTransaccionesDTOList;
    }

    public void setValoresTransaccionesDTOList(List<ValoresTransaccionesDTO> valoresTransaccionesDTOList) {
        this.valoresTransaccionesDTOList = valoresTransaccionesDTOList;
    }

    public List<ValoresTransaccionesDTO> getValoresTransaccionesDTOListFiltrado() {
        return valoresTransaccionesDTOListFiltrado;
    }

    public void setValoresTransaccionesDTOListFiltrado(List<ValoresTransaccionesDTO> valoresTransaccionesDTOListFiltrado) {
        this.valoresTransaccionesDTOListFiltrado = valoresTransaccionesDTOListFiltrado;
    }

    

}
