package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.ProrrogaRemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "prorrogaMensualCtrl")
@ViewScoped
public class prorrogaMensualCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private Boolean renderAbrirProrroga;
    private Boolean renderCerrarProrroga;

    private Boolean disableAbrirProrroga;

    private Boolean onAbrirProrroga;
    private Boolean onCerrarProrroga;

    //Variables de Negocio    
    private InstitucionRequerida registroMixto;
    private Integer institucionId;

    private ProrrogaRemanenteMensual prorrogaApertura;

    //Listas
    private List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteMensualActivasList;
    private List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteSelectedList;

    private List<InstitucionRequerida> registrosMixtosList;
    private Date fechaApertura;

    //EJB's
    @EJB
    private ProrrogaRemanenteMensualServicio prorrogaRemanenteMensualServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;
    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

//Desde Aqui
    //Declaración de variables
    //Variables de control visual
//
//    private Boolean disableNuevoTramite;
//    private Boolean disableDeleteTramite;
//    private Boolean disableDeleteTramiteTodos;
//
//
//    private Boolean renderedNumeroRepertorio;
    //Variables de negocio
//    private Integer institucionId;
//    private Date fechaSeleccionada;
//    private Integer año;
//    private Integer mes;
//
//    private Tramite tramiteSelected;
//
//    private String actividadRegistral;
//    private String ultimoEstado;
//    private Integer idCatalogoTransaccion;
//
//    private String fechaMin;
//    private String fechaMax;
    //Listas
//    private List<Tramite> tramiteList;
//    private List<Tramite> tramiteSelectedList;
//
//    private List<CatalogoTransaccion> catalogoList;
//    private List<RemanenteMensual> remanenteMensualList;
    //
//    @EJB
//    private TransaccionServicio transaccionServicio;
//
//    @EJB
//    private CatalogoTransaccionServicio catalogoTransaccionServicio;
//
//    @EJB
//    private RemanenteMensualServicio remanenteMensualServicio;
//
//    @EJB
//    private DiasNoLaborablesServicio diasNoLaborablesServicio;
    @PostConstruct
    protected void init() {
        reloadProrrogasActivas();

        prorrogaRemanenteSelectedList = new ArrayList<ProrrogaRemanenteMensualDTO>();

        registrosMixtosList = new ArrayList<InstitucionRequerida>();
        registrosMixtosList = institucionRequeridaServicio.getRegistroMixtoList();

        registroMixto = new InstitucionRequerida();
        prorrogaApertura = new ProrrogaRemanenteMensual();

        renderAbrirProrroga = Boolean.FALSE;
        renderCerrarProrroga = Boolean.FALSE;

        disableAbrirProrroga = Boolean.TRUE;

        onAbrirProrroga = Boolean.FALSE;
        onCerrarProrroga = Boolean.FALSE;
    }

    private void reloadProrrogasActivas() {
        prorrogaRemanenteMensualActivasList = new ArrayList<ProrrogaRemanenteMensualDTO>();
        prorrogaRemanenteMensualActivasList = prorrogaRemanenteMensualServicio.getListProrrogaRemanenteMensualEstado("A");
    }

    public void abrirProrroga() {
        renderAbrirProrroga = Boolean.TRUE;
        renderCerrarProrroga = Boolean.FALSE;

        onAbrirProrroga = Boolean.TRUE;
        onCerrarProrroga = Boolean.FALSE;

        prorrogaApertura = new ProrrogaRemanenteMensual();
        registroMixto = new InstitucionRequerida();
        fechaApertura = null;
    }

    public void onInstitucionSelect() {
        institucionId = registroMixto.getInstitucionId();
        disableAbrirProrroga = Boolean.TRUE;
        fechaApertura = null;
    }

    public List<InstitucionRequerida> completeNombreRegistroMixto(String query) {
        List<InstitucionRequerida> filteredInstituciones = new ArrayList<InstitucionRequerida>();
        for (InstitucionRequerida ir : registrosMixtosList) {
            if (ir.getNombre().toLowerCase().contains(query)
                    || ir.getNombre().toUpperCase().contains(query)) {
                filteredInstituciones.add(ir);
            }
        }
        return filteredInstituciones;
    }

    public void onDateSelect() {
        RemanenteMensual remanenteMensual = new RemanenteMensual();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaApertura);
        remanenteMensual = remanenteMensualServicio.getUltimoRemanenteMensual(
                institucionId,
                calendar.get(Calendar.YEAR),
                (calendar.get(Calendar.MONTH) + 1));
        prorrogaApertura.setRemanenteMensualId(remanenteMensual);
        disableAbrirProrroga = Boolean.FALSE;
        for (ProrrogaRemanenteMensualDTO prmdto : prorrogaRemanenteMensualActivasList) {
            if (prmdto.getProrrogaRemanenteMensual().getRemanenteMensualId().getRemanenteMensualId()
                    .equals(prorrogaApertura.getRemanenteMensualId().getRemanenteMensualId())) {
                disableAbrirProrroga = Boolean.TRUE;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No es posible aperturar la prorroga, ya que existe una prorroga Vigente"));
                break;
            }
        }

    }

    public void guardarProrroga() {
        prorrogaApertura.setEstado("A");
        prorrogaRemanenteMensualServicio.create(prorrogaApertura);
        prorrogaApertura = new ProrrogaRemanenteMensual();
        reloadProrrogasActivas();
        registroMixto = new InstitucionRequerida();

        disableAbrirProrroga = Boolean.TRUE;
        onAbrirProrroga = Boolean.FALSE;
        renderAbrirProrroga = Boolean.FALSE;
    }

    //Getters & Setters
    public List<ProrrogaRemanenteMensualDTO> getProrrogaRemanenteSelectedList() {
        return prorrogaRemanenteSelectedList;
    }

    public void setProrrogaRemanenteSelectedList(List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteSelectedList) {
        this.prorrogaRemanenteSelectedList = prorrogaRemanenteSelectedList;
    }

    public List<ProrrogaRemanenteMensualDTO> getProrrogaRemanenteMensualActivasList() {
        return prorrogaRemanenteMensualActivasList;
    }

    public void setProrrogaRemanenteMensualActivasList(List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteMensualActivasList) {
        this.prorrogaRemanenteMensualActivasList = prorrogaRemanenteMensualActivasList;
    }

    public InstitucionRequerida getRegistroMixto() {
        return registroMixto;
    }

    public void setRegistroMixto(InstitucionRequerida registroMixto) {
        this.registroMixto = registroMixto;
    }

    public ProrrogaRemanenteMensual getProrrogaApertura() {
        return prorrogaApertura;
    }

    public void setProrrogaApertura(ProrrogaRemanenteMensual prorrogaApertura) {
        this.prorrogaApertura = prorrogaApertura;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Boolean getRenderAbrirProrroga() {
        return renderAbrirProrroga;
    }

    public void setRenderAbrirProrroga(Boolean renderAbrirProrroga) {
        this.renderAbrirProrroga = renderAbrirProrroga;
    }

    public Boolean getRenderCerrarProrroga() {
        return renderCerrarProrroga;
    }

    public void setRenderCerrarProrroga(Boolean renderCerrarProrroga) {
        this.renderCerrarProrroga = renderCerrarProrroga;
    }

    public Boolean getOnAbrirProrroga() {
        return onAbrirProrroga;
    }

    public void setOnAbrirProrroga(Boolean onAbrirProrroga) {
        this.onAbrirProrroga = onAbrirProrroga;
    }

    public Boolean getOnCerrarProrroga() {
        return onCerrarProrroga;
    }

    public void setOnCerrarProrroga(Boolean onCerrarProrroga) {
        this.onCerrarProrroga = onCerrarProrroga;
    }

    public Boolean getDisableAbrirProrroga() {
        return disableAbrirProrroga;
    }

    public void setDisableAbrirProrroga(Boolean disableAbrirProrroga) {
        this.disableAbrirProrroga = disableAbrirProrroga;
    }

}
