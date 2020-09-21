package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.DiasNoLaborablesServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.TramiteServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.io.Serializable;
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
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "libroPropiedadCtrl")
@ViewScoped
public class LibroPropiedadCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventarioLibro;
    private String strBtnGuardar;

    private Boolean disableNuevoTramite;
    private Boolean disableDeleteTramite;
    private Boolean disableDeleteTramiteTodos;

    private Boolean renderEdition;
    private Boolean onCreate;
    private Boolean onEdit;

    private Boolean renderedNumeroRepertorio;

    //Variables de negocio
    private Integer institucionId;
    private Date fechaSeleccionada;
    private Integer año;
    private Integer mes;
    private RemanenteMensual remanenteMensualSelected;

    private Tramite tramiteSelected;

    private String actividadRegistral;
    private String ultimoEstado;
    private Integer idCatalogoTransaccion;

    private String fechaMin;
    private String fechaMax;

    //Listas
    private List<Tramite> tramiteList;
    private List<Tramite> tramiteSelectedList;

    private List<CatalogoTransaccion> catalogoList;
    private List<RemanenteMensual> remanenteMensualList;

    //
    @EJB
    private TramiteServicio tramiteServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private DiasNoLaborablesServicio diasNoLaborablesServicio;

    @PostConstruct
    protected void init() {
        tituloInventarioLibro = "Inventario Libros Propiedad";
        actividadRegistral = "Propiedad";
        ultimoEstado = "";

        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;

        disableNuevoTramite = Boolean.FALSE;
        disableDeleteTramite = Boolean.TRUE;
        disableDeleteTramiteTodos = Boolean.TRUE;

        renderEdition = Boolean.FALSE;
        renderedNumeroRepertorio = Boolean.FALSE;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fechaSeleccionada = new Date();
        año = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        fechaMin = fechasLimiteMin(año, mes);
        fechaMax = fechasLimiteMax(año, mes);

        institucionId = this.getInstitucionID(getSessionVariable("perfil"));

        tramiteList = new ArrayList<Tramite>();
        obtenerRemanenteMensual();
    }

    public void obtenerRemanenteMensual() {
        remanenteMensualSelected = new RemanenteMensual();
        remanenteMensualSelected = remanenteMensualServicio.getUltimoRemanenteMensual(institucionId, año, mes);
        tramiteList = tramiteServicio.getTramiteByInstitucionFechaActividad(institucionId, año, mes, actividadRegistral, remanenteMensualSelected.getRemanenteMensualId());
        ultimoEstado = remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion();

        if (ultimoEstado.equals("GeneradoAutomaticamente")
                || ultimoEstado.equals("Verificado-Rechazado")
                || ultimoEstado.equals("GeneradoNuevaVersion")) {
            disableNuevoTramite = Boolean.FALSE;
            disableDeleteTramite();
            if (diasNoLaborablesServicio.habilitarDiasAdicionales(remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(), remanenteMensualSelected.getMes())) {
                disableNuevoTramite = Boolean.FALSE;
                disableDeleteTramite();
            } else {
                renderEdition = Boolean.FALSE;
                disableDeleteTramite = Boolean.TRUE;
                disableDeleteTramiteTodos = Boolean.TRUE;
                disableNuevoTramite = Boolean.TRUE;
            }
        } else {
            renderEdition = Boolean.FALSE;
            disableDeleteTramite = Boolean.TRUE;
            disableDeleteTramiteTodos = Boolean.TRUE;
            disableNuevoTramite = Boolean.TRUE;
        }
    }

    public void reloadTramite() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaSeleccionada);
        año = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        fechaMin = fechasLimiteMin(año, mes);
        fechaMax = fechasLimiteMax(año, mes);

        tramiteList = new ArrayList<Tramite>();
        obtenerRemanenteMensual();

        tramiteSelectedList = new ArrayList<Tramite>();
        tramiteSelected = new Tramite();

        renderEdition = Boolean.FALSE;
    }

    public void onRowSelectTramite() {
        strBtnGuardar = "Actualizar";

        onEdit = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        disableDeleteTramite = Boolean.FALSE;
        renderEdition = Boolean.TRUE;

        tramiteSelected = tramiteSelectedList.get(0);
        obtenerRemanenteMensual();
    }

    public void onRowSelectTramiteCheckbox() {
        disableDeleteTramite = ultimoEstado.equals("GeneradoAutomaticamente")
                || ultimoEstado.equals("Verificado-Rechazado")
                || ultimoEstado.equals("GeneradoNuevaVersion") ? tramiteSelectedList.isEmpty() ? Boolean.TRUE : Boolean.FALSE : Boolean.TRUE;
    }

    public void nuevoRegistroTramite() {
        strBtnGuardar = "Guardar";

        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        disableDeleteTramite = Boolean.TRUE;
        renderEdition = Boolean.TRUE;

        tramiteSelected = new Tramite();
    }

    public void borrarTramiteSeleccionado() {
        tramiteServicio.borrarTramites(tramiteSelectedList);
        tramiteServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId(), actividadRegistral);

        reloadTramite();

        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        disableDeleteTramite = Boolean.TRUE;
        //disableDeleteTramite();
        renderEdition = Boolean.FALSE;
    }

    private void disableDeleteTramite() {
        disableDeleteTramiteTodos = tramiteList.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
    }    

    public void guardarTramite() {
        tramiteSelected.setActividadRegistral(actividadRegistral);

        CatalogoTransaccion catalogoTransaccion = new CatalogoTransaccion();
        catalogoTransaccion = catalogoTransaccionServicio.getCatalogoTransaccionIngresoTipoNombre(tramiteSelected.getActividadRegistral(), tramiteSelected.getTipo());

        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, año, mes, catalogoTransaccion.getCatalogoTransaccionId());

        tramiteSelected.setTransaccion(t);
        tramiteSelected.setFechaRegistro(new Date());

        if (onCreate) {
            tramiteServicio.create(tramiteSelected);
        } else if (onEdit) {
            if (tramiteSelected.getTipo().equals("Certificaciones")) {
                tramiteSelected.setNumeroRepertorio(null);
            }
            tramiteServicio.update(tramiteSelected);
        }
        tramiteServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId(), actividadRegistral);

        tramiteSelected = new Tramite();
        reloadTramite();

        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
    }

    public void cancelarTramite() {
        tramiteSelected = new Tramite();

        reloadTramite();

        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
    }

    private String fechasLimiteMin(Integer anio, Integer mes) {
        return anio + "-" + mes + "-01";
    }

    private String fechasLimiteMax(Integer anio, Integer mes) {
        try {
            String stringDate = anio + "-" + mes + "-01";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return anio + "-" + mes + "-" + calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException ex) {
            Logger.getLogger(TramiteMercantilCtrl.class.getName()).log(Level.SEVERE, null, ex);
            return "0000-00-00";
        }
    }    
   
    private String validarCampoFecha(String data) {
        String datoValidado = "INVALIDO";
        if (data == null || data.equals("")) {
            datoValidado = "INVALIDO";
        } else {
            if (isDate(data)) {
                try {
                    Calendar fecha = Calendar.getInstance();
                    fecha.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(data));
                    Calendar fechaActual = Calendar.getInstance();
                    if (diasNoLaborablesServicio.habilitarDiasAdicionales(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH) + 1)) {
                        datoValidado = data;
                    } else {
                        datoValidado = "INVALIDO";
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(LibroPropiedadCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return datoValidado;
    }

    private static String validarCampoVacio(String data) {
        String datoValidado = "INVALIDO";
        if (data.equals("") || data == null) {
            datoValidado = "INVALIDO";
        } else {
            datoValidado = data;
        }
        return datoValidado;
    }

    private static boolean isDate(String cadena) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(cadena);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    //Getters & Setters
    public String getTituloInventarioLibro() {
        return tituloInventarioLibro;
    }

    public void setTituloInventarioLibro(String tituloInventarioLibro) {
        this.tituloInventarioLibro = tituloInventarioLibro;
    }

    public String getStrBtnGuardar() {
        return strBtnGuardar;
    }

    public void setStrBtnGuardar(String strBtnGuardar) {
        this.strBtnGuardar = strBtnGuardar;
    }

    public Boolean getDisableNuevoTramite() {
        return disableNuevoTramite;
    }

    public void setDisableNuevoTramite(Boolean disableNuevoTramite) {
        this.disableNuevoTramite = disableNuevoTramite;
    }

    public Boolean getDisableDeleteTramite() {
        return disableDeleteTramite;
    }

    public void setDisableDeleteTramite(Boolean disableDeleteTramite) {
        this.disableDeleteTramite = disableDeleteTramite;
    }

    public Boolean getDisableDeleteTramiteTodos() {
        return disableDeleteTramiteTodos;
    }

    public void setDisableDeleteTramiteTodos(Boolean disableDeleteTramiteTodos) {
        this.disableDeleteTramiteTodos = disableDeleteTramiteTodos;
    }

    public Boolean getRenderEdition() {
        return renderEdition;
    }

    public void setRenderEdition(Boolean renderEdition) {
        this.renderEdition = renderEdition;
    }

    public Boolean getOnCreate() {
        return onCreate;
    }

    public void setOnCreate(Boolean onCreate) {
        this.onCreate = onCreate;
    }

    public Boolean getOnEdit() {
        return onEdit;
    }

    public void setOnEdit(Boolean onEdit) {
        this.onEdit = onEdit;
    }

    public Boolean getRenderedNumeroRepertorio() {
        return renderedNumeroRepertorio;
    }

    public void setRenderedNumeroRepertorio(Boolean renderedNumeroRepertorio) {
        this.renderedNumeroRepertorio = renderedNumeroRepertorio;
    }

    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public RemanenteMensual getRemanenteMensualSelected() {
        return remanenteMensualSelected;
    }

    public void setRemanenteMensualSelected(RemanenteMensual remanenteMensualSelected) {
        this.remanenteMensualSelected = remanenteMensualSelected;
    }

    public Tramite getTramiteSelected() {
        return tramiteSelected;
    }

    public void setTramiteSelected(Tramite tramiteSelected) {
        this.tramiteSelected = tramiteSelected;
    }

    public String getFechaMin() {
        return fechaMin;
    }

    public void setFechaMin(String fechaMin) {
        this.fechaMin = fechaMin;
    }

    public String getFechaMax() {
        return fechaMax;
    }

    public void setFechaMax(String fechaMax) {
        this.fechaMax = fechaMax;
    }

    public List<Tramite> getTramiteSelectedList() {
        return tramiteSelectedList;
    }

    public void setTramiteSelectedList(List<Tramite> tramiteSelectedList) {
        this.tramiteSelectedList = tramiteSelectedList;
    }

    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

    public List<Tramite> getTramiteList() {
        return tramiteList;
    }

    public void setTramiteList(List<Tramite> tramiteList) {
        this.tramiteList = tramiteList;
    }

    public String getUltimoEstado() {
        return ultimoEstado;
    }

    public void setUltimoEstado(String ultimoEstado) {
        this.ultimoEstado = ultimoEstado;
    }

}
