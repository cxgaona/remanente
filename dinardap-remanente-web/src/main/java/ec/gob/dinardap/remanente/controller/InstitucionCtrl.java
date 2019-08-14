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

@Named(value = "institucionCtrl")
@ViewScoped
public class InstitucionCtrl extends BaseCtrl implements Serializable {

    private static final long serialVersionUID = 3066578283525294119L;
    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    private List<InstitucionRequerida> institucionList;
    private List<InstitucionRequerida> direccionRegionalList;
    private List<InstitucionRequerida> gadList;
    private List<InstitucionRequerida> registroMixtoList;

    @PostConstruct
    protected void init() {
//        institucionList = new ArrayList<InstitucionRequerida>();
//        institucionList = institucionRequeridaServicio.getInstitucion();
        direccionRegionalList = new ArrayList<InstitucionRequerida>();
        direccionRegionalList = institucionRequeridaServicio.getDireccionRegionalList();
        gadList = new ArrayList<InstitucionRequerida>();
        gadList = institucionRequeridaServicio.getGADList();
        registroMixtoList = new ArrayList<InstitucionRequerida>();
        registroMixtoList = institucionRequeridaServicio.getRegistroMixtoList();
    }

    public List<InstitucionRequerida> getInstitucionList() {
        return institucionList;
    }

    public void setInstitucionList(List<InstitucionRequerida> institucionList) {
        this.institucionList = institucionList;
    }

    public List<InstitucionRequerida> getDireccionRegionalList() {
        return direccionRegionalList;
    }

    public void setDireccionRegionalList(List<InstitucionRequerida> direccionRegionalList) {
        this.direccionRegionalList = direccionRegionalList;
    }

    public List<InstitucionRequerida> getGadList() {
        return gadList;
    }

    public void setGadList(List<InstitucionRequerida> gadList) {
        this.gadList = gadList;
    }

    public List<InstitucionRequerida> getRegistroMixtoList() {
        return registroMixtoList;
    }

    public void setRegistroMixtoList(List<InstitucionRequerida> registroMixtoList) {
        this.registroMixtoList = registroMixtoList;
    }

}
