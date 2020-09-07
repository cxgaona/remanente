package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "institucionCtrl")
@ViewScoped
public class InstitucionCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloPagina;
    private Boolean renderEdition;

    //Variables de negocio
    private Institucion institucionSelected;

    //Listas
    private List<Institucion> institucionList;

    @EJB
    private InstitucionServicio institucionServicio;
//
//    private List<InstitucionRequerida> direccionRegionalList;
//    private List<InstitucionRequerida> gadList;
//    private List<InstitucionRequerida> registroMixtoList;

    @PostConstruct
    protected void init() {
        tituloPagina = "Institución";
        renderEdition = Boolean.FALSE;
        institucionList = new ArrayList<Institucion>();
        institucionList = institucionServicio.getInstitucion();
//        direccionRegionalList = new ArrayList<InstitucionRequerida>();
//        direccionRegionalList = institucionRequeridaServicio.getDireccionRegionalList();
//        gadList = new ArrayList<InstitucionRequerida>();
//        gadList = institucionRequeridaServicio.getGADList();
//        registroMixtoList = new ArrayList<InstitucionRequerida>();
//        registroMixtoList = institucionRequeridaServicio.getRegistroMixtoList();
    }

    public void seleccionarInstitucion() {
        renderEdition = Boolean.TRUE;
//        onCreate = Boolean.FALSE;
//        onEdit = Boolean.TRUE;
//        btnGuardar = "Actualizar";
//        disabledRegistrador = Boolean.TRUE;
//        disabledVerificador = Boolean.TRUE;
//        disabledValidador = Boolean.TRUE;
//        disabledAdministrador = Boolean.TRUE;
//        disabledRestablecer = Boolean.FALSE;
//        restablecer = Boolean.FALSE;       
    }

    public void nuevaInstitucion() {
        System.out.println("Nueva Institucion");
    }

    public void deshabilitarInstitucion() {
        System.out.println("Deshabilitar Institucion");
    }

//Getters & Setters
    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<Institucion> getInstitucionList() {
        return institucionList;
    }

    public void setInstitucionList(List<Institucion> institucionList) {
        this.institucionList = institucionList;
    }

    public Institucion getInstitucionSelected() {
        return institucionSelected;
    }

    public void setInstitucionSelected(Institucion institucionSelected) {
        this.institucionSelected = institucionSelected;
    }

}
