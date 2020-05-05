package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.DiasNoLaborablesServicio;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.ProrrogaRemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.TramiteServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "prorrogaMensualCtrl")
@ViewScoped
public class prorrogaMensualCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    //Variables de Negocio
    private ProrrogaRemanenteMensualDTO prorrogaRemanenteMensualSelected;
    private InstitucionRequerida registroMixto;

    //Listas
    private List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteMensualActivasList;
    private List<InstitucionRequerida> registrosMixtosList;
    private List<Date> invalidaMonths;

    //EJB's
    @EJB
    private ProrrogaRemanenteMensualServicio prorrogaRemanenteMensualServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

//Desde Aqui
    //Declaración de variables
    //Variables de control visual
//    private String tituloPropiedad;
//    private String strBtnGuardar;
//
//    private Boolean disableNuevoTramite;
//    private Boolean disableDeleteTramite;
//    private Boolean disableDeleteTramiteTodos;
//
//    private Boolean renderEdition;
//    private Boolean onCreate;
//    private Boolean onEdit;
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
        prorrogaRemanenteMensualActivasList = new ArrayList<ProrrogaRemanenteMensualDTO>();
        prorrogaRemanenteMensualActivasList = prorrogaRemanenteMensualServicio.getListProrrogaRemanenteMensualEstado("A");

        invalidaMonths = new ArrayList<Date>();
        invalidaMonths.add(new Date());

        registrosMixtosList = new ArrayList<InstitucionRequerida>();
        registrosMixtosList = institucionRequeridaServicio.getRegistroMixtoList();

        registroMixto = new InstitucionRequerida();
//        tituloPropiedad = "Trámite Propiedad";
//        actividadRegistral = "Propiedad";
//        ultimoEstado = "";
//
//        onCreate = Boolean.FALSE;
//        onEdit = Boolean.FALSE;
//
//        disableNuevoTramite = Boolean.FALSE;
//        disableDeleteTramite = Boolean.TRUE;
//        disableDeleteTramiteTodos = Boolean.TRUE;
//
//        renderEdition = Boolean.FALSE;
//        renderedNumeroRepertorio = Boolean.FALSE;
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        fechaSeleccionada = new Date();
//        año = calendar.get(Calendar.YEAR);
//        mes = calendar.get(Calendar.MONTH) + 1;
//        fechaMin = fechasLimiteMin(año, mes);
//        fechaMax = fechasLimiteMax(año, mes);
//
//        institucionId = this.getInstitucionID(this.getSessionVariable("perfil"));
//
//        tramiteList = new ArrayList<Tramite>();
//        obtenerRemanenteMensual();
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

    public void prueba() {
        System.out.println("aQUi");
    }

    //Getters & Setters
    public ProrrogaRemanenteMensualDTO getProrrogaRemanenteMensualSelected() {
        return prorrogaRemanenteMensualSelected;
    }

    public void setProrrogaRemanenteMensualSelected(ProrrogaRemanenteMensualDTO prorrogaRemanenteMensualSelected) {
        this.prorrogaRemanenteMensualSelected = prorrogaRemanenteMensualSelected;
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

    public List<Date> getInvalidaMonths() {
        return invalidaMonths;
    }

    public void setInvalidaMonths(List<Date> invalidaMonths) {
        this.invalidaMonths = invalidaMonths;
    }

}
