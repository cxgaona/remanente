package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.remanente.servicio.ProrrogaRemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
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
    private Boolean disableCerrarProrrogas;
    private Boolean disableCerrarProrrogasTodas;

    private Boolean onAbrirProrroga;
    private Boolean onCerrarProrroga;

    //Variables de Negocio    
    private Institucion registroMixto;
    private Integer institucionId;

    private ProrrogaRemanenteMensual prorrogaApertura;

    private String comentarioCierre;

    private String rangoMes, rangoAño;

    //Listas
    private List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteMensualActivasList;
    private List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteSelectedList;

    private List<Institucion> registrosMixtosList;
    private Date fechaApertura;

    //EJB's
    @EJB
    private ProrrogaRemanenteMensualServicio prorrogaRemanenteMensualServicio;

    @EJB
    private InstitucionServicio institucionServicio;
    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @PostConstruct
    protected void init() {
        reloadProrrogasActivas();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        rangoAño = "2020:" + calendar.get(Calendar.YEAR);
        rangoMes = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);

        prorrogaRemanenteSelectedList = new ArrayList<ProrrogaRemanenteMensualDTO>();

        registrosMixtosList = new ArrayList<Institucion>();
        registrosMixtosList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.RMX_AUTONOMIA_FINANCIERA.getTipoInstitucion()));
        registrosMixtosList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.RMX_SIN_AUTONOMIA_FINANCIERA.getTipoInstitucion()));


        registroMixto = new Institucion();
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
        registroMixto = new Institucion();
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
        registroMixto = new Institucion();

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
        disableAbrirProrroga = Boolean.TRUE;
        fechaApertura = null;
    }

    public List<Institucion> completeNombreRegistroMixto(String query) {
        List<Institucion> filteredInstituciones = new ArrayList<Institucion>();
        for (Institucion ir : registrosMixtosList) {
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
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Ya existe una Prórroga Vigente para la FECHA e INSTITUCIÓN seleccionada"));
                break;
            }
        }

    }

    public void guardarProrroga() {
        prorrogaApertura.setEstado("A");
        prorrogaRemanenteMensualServicio.create(prorrogaApertura);
        prorrogaApertura = new ProrrogaRemanenteMensual();
        reloadProrrogasActivas();
        registroMixto = new Institucion();
        disableCerrarProrrogasBtn();

        disableAbrirProrroga = Boolean.TRUE;
        onAbrirProrroga = Boolean.FALSE;
        renderAbrirProrroga = Boolean.FALSE;
    }

    public void guardarCerrarProrroga() {
        List<ProrrogaRemanenteMensual> prorrogaRemanenteMensualList = new ArrayList<ProrrogaRemanenteMensual>();
        for (ProrrogaRemanenteMensualDTO prorroga : prorrogaRemanenteSelectedList) {
            prorroga.getProrrogaRemanenteMensual().setComentarioCierre(comentarioCierre);
            prorroga.getProrrogaRemanenteMensual().setEstado("I");
            prorrogaRemanenteMensualList.add(prorroga.getProrrogaRemanenteMensual());
        }
        prorrogaRemanenteMensualServicio.update(prorrogaRemanenteMensualList);

        reloadProrrogasActivas();
        registroMixto = new Institucion();
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

    public Institucion getRegistroMixto() {
        return registroMixto;
    }

    public void setRegistroMixto(Institucion registroMixto) {
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

    public String getRangoMes() {
        return rangoMes;
    }

    public void setRangoMes(String rangoMes) {
        this.rangoMes = rangoMes;
    }

    public String getRangoAño() {
        return rangoAño;
    }

    public void setRangoAño(String rangoAño) {
        this.rangoAño = rangoAño;
    }

}
