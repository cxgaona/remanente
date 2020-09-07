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

@Named(value = "tramiteMercantilCtrl")
@ViewScoped
public class TramiteMercantilCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloMercantil;
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
        tituloMercantil = "Trámite Mercantil";
        actividadRegistral = "Mercantil";
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

        institucionId = this.getInstitucionID(this.getSessionVariable("perfil"));

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
//        disableDeleteTramite();
        renderEdition = Boolean.FALSE;
    }

    private void disableDeleteTramite() {
        disableDeleteTramiteTodos = tramiteList.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
    }

    public void borrarTodosTramites() {
        tramiteServicio.borrarTramites(tramiteList);
        tramiteServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId(), actividadRegistral);

        reloadTramite();

        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;

        disableDeleteTramite = Boolean.TRUE;
//        disableDeleteTramite();

        renderEdition = Boolean.FALSE;
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

    public void changeTipoTramite() {
        renderedNumeroRepertorio = tramiteSelected.getTipo().equals("Inscripciones") ? Boolean.TRUE : Boolean.FALSE;
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

    public void crearTramitesBloque(FileUploadEvent event) {
        try {
            InputStream in = null;
            UploadedFile uploadedFile = event.getFile();
            in = uploadedFile.getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(in);
            XSSFSheet sheet = worbook.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            List<Tramite> tramiteNuevoList = new ArrayList<Tramite>();
            catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionListTipo(actividadRegistral);

            Boolean flagValidacionCampos = Boolean.TRUE;
            List<String> celdaErrorVacio = new ArrayList<String>();
            for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
                row = sheet.getRow(r);
                if (row.getRowNum() != 0) {
                    Tramite tramiteNuevo = new Tramite();
                    tramiteNuevo.setActividadRegistral(actividadRegistral);
                    tramiteNuevo.setFechaRegistro(new Date());
                    for (int c = 0; c < (int) row.getLastCellNum(); c++) {
                        String dato = null;
                        cell = row.getCell(c);
                        if (cell == null) {
                            dato = null;
                        } else {
                            dato = getDato(worbook, cell);
                            String datoVal;
                            switch (cell.getColumnIndex()) {
                                case 0:
                                    if (dato.equals("Certificaciones") || dato.equals("Inscripciones")) {
                                        tramiteNuevo.setTipo(dato);
                                        for (CatalogoTransaccion ct : catalogoList) {
                                            if (ct.getNombre().equals(tramiteNuevo.getTipo()) && ct.getTipo().equals("Ingreso-Mercantil")) {
                                                idCatalogoTransaccion = ct.getCatalogoTransaccionId();
                                                break;
                                            }
                                        }
                                        Transaccion t = new Transaccion();
                                        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, año, mes, idCatalogoTransaccion);
                                        tramiteNuevo.setTransaccion(t);
                                    }
                                    break;
                                case 1:
                                    datoVal = validarCampoNumero(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        tramiteNuevo.setNumero(datoVal);
                                    }
                                    break;
                                case 2:
                                    if (tramiteNuevo.getTipo() == null) {
                                        tramiteNuevo.setNumeroRepertorio(null);
                                    } else {
                                        if (tramiteNuevo.getTipo().equals("Certificaciones")) {
                                            tramiteNuevo.setNumeroRepertorio("");
                                        } else {
                                            datoVal = validarCampoNumero(dato);
                                            if (!datoVal.equals("INVALIDO")) {
                                                tramiteNuevo.setNumeroRepertorio(datoVal);
                                            }
                                        }
                                    }
                                    break;
                                case 3:
                                    datoVal = validarCampoFecha(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        tramiteNuevo.setFecha(new SimpleDateFormat("yyyy-MM-dd").parse(datoVal));
                                    }
                                    break;
                                case 4:
                                    datoVal = validarCampoNumero(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        if (datoVal.length() <= 15) {
                                            tramiteNuevo.setNumeroComprobantePago(datoVal);
                                        }
                                    }
                                    break;
                                case 5:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        tramiteNuevo.setValor(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 6:
                                    datoVal = validarCampoVacio(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        tramiteNuevo.setActo(datoVal);
                                    }
                                    break;
                            }
                        }
                    }
                    if (tramiteNuevo.getTipo() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaErrorVacio.add("A" + (r + 1));
                    } else {
                        if (tramiteNuevo.getTipo().equals("Inscripciones")) {
                            if (tramiteNuevo.getNumeroRepertorio() == null || tramiteNuevo.getNumeroRepertorio().isEmpty()) {
                                flagValidacionCampos = Boolean.FALSE;
                                celdaErrorVacio.add("C" + (r + 1));
                            }
                        }
                    }

                    if (tramiteNuevo.getNumero() == null || tramiteNuevo.getNumero().isEmpty()) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaErrorVacio.add("B" + (r + 1));
                    }

                    if (tramiteNuevo.getFecha() == null || tramiteNuevo.getFecha().equals("")) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaErrorVacio.add("D" + (r + 1));
                    }
                    if (tramiteNuevo.getNumeroComprobantePago() == null || tramiteNuevo.getNumeroComprobantePago().isEmpty()) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaErrorVacio.add("E" + (r + 1));
                    }
                    if (tramiteNuevo.getValor() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaErrorVacio.add("F" + (r + 1));
                    }
                    if (tramiteNuevo.getActo() == null || tramiteNuevo.getActo().isEmpty()) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaErrorVacio.add("G" + (r + 1));
                    }
                    tramiteNuevoList.add(tramiteNuevo);
                }
            }
            if (flagValidacionCampos) {
                tramiteServicio.crearTramites(tramiteNuevoList);
                tramiteServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId(), actividadRegistral);
                reloadTramite();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha creado el bloque de trámites satisfactoriamente"));
            } else {
                String celdas = "";
                for (String s : celdaErrorVacio) {
                    celdas += s + ", ";
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Favor verificar su archivo de carga. \n Verificar las celdas: " + celdas + "de su archivo de carga"));
            }

        } catch (IOException ex) {
            System.out.println("Error carga de Archivo");
            Logger.getLogger(TramiteMercantilCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            System.out.println("Error Parseo de fecha");
            Logger.getLogger(TramiteMercantilCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getDato(XSSFWorkbook worbook, XSSFCell cell) {
        String dato = "";
        switch (cell.getCellType()) {
            case NUMERIC:
                dato = cell.getNumericCellValue() + "";
                break;
            case FORMULA:
                FormulaEvaluator evaluator = worbook.getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case STRING:
                        dato = cellValue.getStringValue();
                        break;
                    case NUMERIC:
                        dato = (int) cellValue.getNumberValue() + "";
                        break;
                }
                break;
            case STRING:
                dato = cell.getStringCellValue();
                break;
            case BLANK:
                dato = "";
                break;
            default:
                dato = cell.getStringCellValue();
                break;
        }
        return dato;
    }

    private static String validarCampoNumero(String data) { //Valida campos Número de tramites comprobantes numero repertorio
        String datoValidado = "INVALIDO";
        if (data.equals("") || data == null) {
            datoValidado = "INVALIDO";
        } else {
            if (isNumeric(data)) {
                if (data.contains(".") || data.contains(",")) {
                    Double datoAux = Double.parseDouble(data);
                    data = datoAux.intValue() + "";
                }
                datoValidado = data;
            }
        }
        return datoValidado;
    }

    private static String validarCampoValorNumerico(String data) {
        String datoValidado = "INVALIDO";
        if (data.equals("") || data == null) {
            datoValidado = "INVALIDO";
        } else {
            if (isNumeric(data)) {
                datoValidado = Double.parseDouble(data) + "";
            } else {
                datoValidado = "INVALIDO";
            }
        }
        return datoValidado + "";
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
                    Logger.getLogger(TramiteMercantilCtrl.class.getName()).log(Level.SEVERE, null, ex);
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

    private static boolean isNumeric(String cadena) {
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException excepcion) {
            return false;
        }
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
    public String getTituloMercantil() {
        return tituloMercantil;
    }

    public void setTituloMercantil(String tituloMercantil) {
        this.tituloMercantil = tituloMercantil;
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
