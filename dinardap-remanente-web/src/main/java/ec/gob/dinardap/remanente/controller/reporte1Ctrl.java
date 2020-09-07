package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Integer anio;
    private Integer mes;
    private Boolean disabledBtnReloadMes;

    //Variables de negocio
    private Institucion direccionRegionalSelected;
    private Date mesSelecionado;

    //Listas
    private List<Institucion> direccionRegionalList;

    @EJB
    private InstitucionServicio institucionServicio;
//
//    private List<InstitucionRequerida> direccionRegionalList;
//    private List<InstitucionRequerida> gadList;
//    private List<InstitucionRequerida> registroMixtoList;

    @PostConstruct
    protected void init() {
        tituloPagina = "Reporte: Cumplimiento e incumplimiento de Registro de Información";
        direccionRegionalList = new ArrayList<Institucion>();
        direccionRegionalList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.REGIONAL.getTipoInstitucion()));
        disabledBtnReloadMes = Boolean.TRUE;
        mesSelecionado = new Date();
//        direccionRegionalList = new ArrayList<InstitucionRequerida>();
//        direccionRegionalList = institucionRequeridaServicio.getDireccionRegionalList();
//        gadList = new ArrayList<InstitucionRequerida>();
//        gadList = institucionRequeridaServicio.getGADList();
//        registroMixtoList = new ArrayList<InstitucionRequerida>();
//        registroMixtoList = institucionRequeridaServicio.getRegistroMixtoList();
    }

    public void seleccionarDireccionRegional() {
        System.out.println("Dirección Regional Seleccionada");
        System.out.println("Dirección seleccionada: " + direccionRegionalSelected.getNombre());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        mesSelecionado = new Date();
        disabledBtnReloadMes = Boolean.FALSE;
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

    public void reloadMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mesSelecionado);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;

//        fechaMin = fechasLimiteMin(anio, mes);
//        fechaMax = fechasLimiteMax(anio, mes);
//        tramiteList = new ArrayList<Tramite>();
//        obtenerRemanenteMensual();
//        tramiteList = tramiteServicio.getTramiteByInstitucionFechaActividad(institucionId, anio, mes, "Propiedad", remanenteMensualSelected.getRemanenteMensualId());
//        disableDelete = Boolean.TRUE;
//        renderEdition = Boolean.FALSE;
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

    public List<Institucion> getDireccionRegionalList() {
        return direccionRegionalList;
    }

    public void setDireccionRegionalList(List<Institucion> direccionRegionalList) {
        this.direccionRegionalList = direccionRegionalList;
    }

    public Institucion getDireccionRegionalSelected() {
        return direccionRegionalSelected;
    }

    public void setDireccionRegionalSelected(Institucion direccionRegionalSelected) {
        this.direccionRegionalSelected = direccionRegionalSelected;
    }

    public Date getMesSelecionado() {
        return mesSelecionado;
    }

    public void setMesSelecionado(Date mesSelecionado) {
        this.mesSelecionado = mesSelecionado;
    }

    public Boolean getDisabledBtnReloadMes() {
        return disabledBtnReloadMes;
    }

    public void setDisabledBtnReloadMes(Boolean disabledBtnReloadMes) {
        this.disabledBtnReloadMes = disabledBtnReloadMes;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

}
