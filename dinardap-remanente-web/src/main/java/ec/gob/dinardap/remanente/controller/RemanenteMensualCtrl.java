package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "remanenteMensualCtrl")
@ViewScoped
public class RemanenteMensualCtrl extends BaseCtrl implements Serializable {

    private String tituloPagina;
    private List<RemanenteMensual> remanenteMensualList;
    private Integer institucionId;
    private String nombreInstitucion;
    private RemanenteMensual remanenteMensualSelected;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @PostConstruct
    protected void init() {
        tituloPagina = "Gestión Remanente Mensual";
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        nombreInstitucion = institucionRequeridaServicio.getInstitucionById(institucionId).getNombre();
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId);
    }

    public void onRowSelect() {
        System.out.println("Seleccionado");
        nombreInstitucion= remanenteMensualSelected.getComentarios();
        
        System.out.println("Fin del seleccionado");

    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public RemanenteMensual getRemanenteMensualSelected() {
        return remanenteMensualSelected;
    }

    public void setRemanenteMensualSelected(RemanenteMensual remanenteMensualSelected) {
        this.remanenteMensualSelected = remanenteMensualSelected;
    }
    
    

}
