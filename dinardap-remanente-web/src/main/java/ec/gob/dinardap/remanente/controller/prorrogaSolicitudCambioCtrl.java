package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SolicitudCambioDTO;
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

@Named(value = "prorrogaSolicitudCambioCtrl")
@ViewScoped
public class prorrogaSolicitudCambioCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private Boolean renderAbrirProrroga;
    private Boolean renderCerrarProrroga;

    private Boolean disableAbrirProrroga;
    private Boolean disableCerrarProrrogas;
    private Boolean disableCerrarProrrogasTodas;

    private Boolean onAbrirProrroga;
    private Boolean onCerrarProrroga;

    //Variables de Negocio    
    private InstitucionRequerida registroMixto;
    private Integer institucionId;

    private SolicitudCambioDTO solicitudCambioSelectedDTO;

    private ProrrogaRemanenteMensual prorrogaApertura;

    private String comentarioCierre;

    //Listas
    private List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteMensualActivasList;
    private List<SolicitudCambioDTO> solicitudCambioDTOList;
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

    @PostConstruct
    protected void init() {
        reloadProrrogasActivas();
        solicitudCambioSelectedDTO = new SolicitudCambioDTO();

        prorrogaRemanenteSelectedList = new ArrayList<ProrrogaRemanenteMensualDTO>();

        registrosMixtosList = new ArrayList<InstitucionRequerida>();
        registrosMixtosList = institucionRequeridaServicio.getRegistroMixtoList();

        solicitudCambioDTOList = new ArrayList<SolicitudCambioDTO>();

        registroMixto = new InstitucionRequerida();
        prorrogaApertura = new ProrrogaRemanenteMensual();

        renderAbrirProrroga = Boolean.FALSE;
        renderCerrarProrroga = Boolean.FALSE;

        disableAbrirProrroga = Boolean.TRUE;

        disableCerrarProrrogas = Boolean.TRUE;
        disableCerrarProrrogasTodas = Boolean.TRUE;

        onAbrirProrroga = Boolean.FALSE;
        onCerrarProrroga = Boolean.FALSE;

        disableCerrarProrrogasBtn();

    }

    private void disableCerrarProrrogasBtn() {
        disableCerrarProrrogasTodas = prorrogaRemanenteMensualActivasList.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
    }

    public void onRowSelectProrrogaCheckbox() {
        disableCerrarProrrogas = prorrogaRemanenteSelectedList.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
//        renderCerrarProrroga = prorrogaRemanenteSelectedList.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }

    public void onRowSelectSolicitudCambio() {
        prorrogaApertura.setRemanenteMensualId(solicitudCambioSelectedDTO.getRemanenteMensual());
        disableCrearProrrogaBtn();
        for (ProrrogaRemanenteMensualDTO prmdto : prorrogaRemanenteMensualActivasList) {
            if (prmdto.getProrrogaRemanenteMensual().getRemanenteMensualId().getRemanenteMensualId()
                    .equals(prorrogaApertura.getRemanenteMensualId().getRemanenteMensualId())) {
                disableAbrirProrroga = Boolean.TRUE;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Ya existe una Prórroga Vigente para la Solicitud de Cambio seleccionada"));
                break;
            }
        }
    }

    private void reloadProrrogasActivas() {
        prorrogaRemanenteMensualActivasList = new ArrayList<ProrrogaRemanenteMensualDTO>();
        prorrogaRemanenteMensualActivasList = prorrogaRemanenteMensualServicio.getListProrrogaRemanenteMensualEstado("AC");

    }

    public void abrirProrroga() {
        renderAbrirProrroga = Boolean.TRUE;
        renderCerrarProrroga = Boolean.FALSE;

        onAbrirProrroga = Boolean.TRUE;
        onCerrarProrroga = Boolean.FALSE;

        prorrogaApertura = new ProrrogaRemanenteMensual();
        registroMixto = new InstitucionRequerida();
        solicitudCambioSelectedDTO = new SolicitudCambioDTO();
        solicitudCambioDTOList = new ArrayList<SolicitudCambioDTO>();
        fechaApertura = null;
    }

    public void cerrarProrroga() {
        renderAbrirProrroga = Boolean.FALSE;
        renderCerrarProrroga = Boolean.TRUE;

        onAbrirProrroga = Boolean.FALSE;
        onCerrarProrroga = Boolean.TRUE;
    }

    public void cancelarAbrirProrroga() {
        prorrogaApertura = new ProrrogaRemanenteMensual();
        registroMixto = new InstitucionRequerida();

        renderAbrirProrroga = Boolean.FALSE;
        renderCerrarProrroga = Boolean.FALSE;

        disableAbrirProrroga = Boolean.TRUE;

        onAbrirProrroga = Boolean.FALSE;
        onCerrarProrroga = Boolean.FALSE;
    }

    public void cancelarCerrarProrroga() {
        prorrogaRemanenteSelectedList = new ArrayList<ProrrogaRemanenteMensualDTO>();

        renderAbrirProrroga = Boolean.FALSE;
        renderCerrarProrroga = Boolean.FALSE;

        disableCerrarProrrogas = Boolean.TRUE;

        onAbrirProrroga = Boolean.FALSE;
        onCerrarProrroga = Boolean.FALSE;
    }

    public void cerrarProrrogaTodas() {
        prorrogaRemanenteSelectedList = new ArrayList<ProrrogaRemanenteMensualDTO>();
        prorrogaRemanenteSelectedList = prorrogaRemanenteMensualActivasList;

        onRowSelectProrrogaCheckbox();

        renderAbrirProrroga = Boolean.FALSE;
        renderCerrarProrroga = Boolean.TRUE;

        onAbrirProrroga = Boolean.FALSE;
        onCerrarProrroga = Boolean.TRUE;
    }

    public void onInstitucionSelect() {
        institucionId = registroMixto.getInstitucionId();
        solicitudCambioDTOList = prorrogaRemanenteMensualServicio.getRemanenteMensualSolicitudCambioAprobada(institucionId);
        solicitudCambioSelectedDTO = new SolicitudCambioDTO();
        if (solicitudCambioDTOList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "La Institución Seleccionada no tiene Solicitudes de Cambio Pendientes"));
        }
        disableCrearProrrogaBtn();
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

    public void disableCrearProrrogaBtn() {
        disableAbrirProrroga = solicitudCambioSelectedDTO.getRemanenteMensual() == null
                ? Boolean.TRUE : Boolean.FALSE;
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
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Ya existe una Prórroga Vigente para la FECHA e INSTITUCIÓN seleccionada"));
                break;
            }
        }

    }

    public void guardarProrroga() {
        prorrogaApertura.setEstado("AC");
        prorrogaRemanenteMensualServicio.create(prorrogaApertura);
        prorrogaApertura = new ProrrogaRemanenteMensual();
        reloadProrrogasActivas();
        registroMixto = new InstitucionRequerida();
        disableCerrarProrrogasBtn();

        disableAbrirProrroga = Boolean.TRUE;
        onAbrirProrroga = Boolean.FALSE;
        renderAbrirProrroga = Boolean.FALSE;
    }

    public void guardarCerrarProrroga() {
        List<ProrrogaRemanenteMensual> prorrogaRemanenteMensualList = new ArrayList<ProrrogaRemanenteMensual>();
        for (ProrrogaRemanenteMensualDTO prorroga : prorrogaRemanenteSelectedList) {
            prorroga.getProrrogaRemanenteMensual().setComentarioCierre(comentarioCierre);
            prorroga.getProrrogaRemanenteMensual().setEstado("IC");
            prorrogaRemanenteMensualList.add(prorroga.getProrrogaRemanenteMensual());
        }
        prorrogaRemanenteMensualServicio.update(prorrogaRemanenteMensualList);

        reloadProrrogasActivas();
        registroMixto = new InstitucionRequerida();
        comentarioCierre = null;

        disableCerrarProrrogas = Boolean.TRUE;

        onCerrarProrroga = Boolean.FALSE;

        renderCerrarProrroga = Boolean.FALSE;

        disableCerrarProrrogasBtn();
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

    public Boolean getDisableCerrarProrrogas() {
        return disableCerrarProrrogas;
    }

    public void setDisableCerrarProrrogas(Boolean disableCerrarProrrogas) {
        this.disableCerrarProrrogas = disableCerrarProrrogas;
    }

    public Boolean getDisableCerrarProrrogasTodas() {
        return disableCerrarProrrogasTodas;
    }

    public void setDisableCerrarProrrogasTodas(Boolean disableCerrarProrrogasTodas) {
        this.disableCerrarProrrogasTodas = disableCerrarProrrogasTodas;
    }

    public String getComentarioCierre() {
        return comentarioCierre;
    }

    public void setComentarioCierre(String comentarioCierre) {
        this.comentarioCierre = comentarioCierre;
    }

    public List<SolicitudCambioDTO> getSolicitudCambioDTOList() {
        return solicitudCambioDTOList;
    }

    public void setSolicitudCambioDTOList(List<SolicitudCambioDTO> solicitudCambioDTOList) {
        this.solicitudCambioDTOList = solicitudCambioDTOList;
    }

    public SolicitudCambioDTO getSolicitudCambioSelectedDTO() {
        return solicitudCambioSelectedDTO;
    }

    public void setSolicitudCambioSelectedDTO(SolicitudCambioDTO solicitudCambioSelectedDTO) {
        this.solicitudCambioSelectedDTO = solicitudCambioSelectedDTO;
    }

}
