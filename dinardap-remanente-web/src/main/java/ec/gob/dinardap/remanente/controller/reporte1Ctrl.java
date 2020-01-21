package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
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

    //Variables de negocio
    private InstitucionRequerida direccionRegionalSelected;

    //Listas
    private List<InstitucionRequerida> direccionRegionalList;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;
//
//    private List<InstitucionRequerida> direccionRegionalList;
//    private List<InstitucionRequerida> gadList;
//    private List<InstitucionRequerida> registroMixtoList;

    @PostConstruct
    protected void init() {
        tituloPagina = "Reporte: Cumplimiento e incumplimiento de Registro de Información";
        direccionRegionalList = new ArrayList<InstitucionRequerida>();
        direccionRegionalList = institucionRequeridaServicio.getDireccionRegionalList();
//        direccionRegionalList = new ArrayList<InstitucionRequerida>();
//        direccionRegionalList = institucionRequeridaServicio.getDireccionRegionalList();
//        gadList = new ArrayList<InstitucionRequerida>();
//        gadList = institucionRequeridaServicio.getGADList();
//        registroMixtoList = new ArrayList<InstitucionRequerida>();
//        registroMixtoList = institucionRequeridaServicio.getRegistroMixtoList();
    }

    public void seleccionarDireccionRegional() {
        System.out.println("Dirección Regional Seleccionada");
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

    public List<InstitucionRequerida> getDireccionRegionalList() {
        return direccionRegionalList;
    }

    public void setDireccionRegionalList(List<InstitucionRequerida> direccionRegionalList) {
        this.direccionRegionalList = direccionRegionalList;
    }

    public InstitucionRequerida getDireccionRegionalSelected() {
        return direccionRegionalSelected;
    }

    public void setDireccionRegionalSelected(InstitucionRequerida direccionRegionalSelected) {
        this.direccionRegionalSelected = direccionRegionalSelected;
    }

}
