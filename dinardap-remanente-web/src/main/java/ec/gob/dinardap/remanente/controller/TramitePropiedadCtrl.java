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

@Named(value = "tramitePropiedadCtrl")
@ViewScoped
public class TramitePropiedadCtrl extends BaseCtrl implements Serializable {

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

    private String tituloMercantil, tituloPropiedad, actividadRegistral;
    private List<Tramite> tramiteList;
    private Integer anio, mes;
    private Integer institucionId;
    private Date fecha;
    private Tramite tramiteSelected;
    private Integer idCatalogoTransaccion;
    private List<CatalogoTransaccion> catalogoList;
    private List<RemanenteMensual> remanenteMensualList;
    private Boolean onEdit;
    private Boolean onCreate;
    private String btnGuardar;
    private RemanenteMensual remanenteMensualSelected;
    private String fechaMin;
    private String fechaMax;

    private Boolean disableNuevoT;
    private Boolean disableDelete;
    private Boolean renderEdition;

    private Boolean renderedNumeroRepertorio;

    @PostConstruct
    protected void init() {
        renderedNumeroRepertorio = Boolean.FALSE;
        tituloPropiedad = "Trámite Propiedad";
        tituloMercantil = "Trámite Mercantil";
        actividadRegistral = "Propiedad";
        tramiteList = new ArrayList<Tramite>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fecha = new Date();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        fechaMin = fechasLimiteMin(anio, mes);
        fechaMax = fechasLimiteMax(anio, mes);
        institucionId = this.getInstitucionID(this.getSessionVariable("perfil"));
        obtenerRemanenteMensual();
        tramiteList = tramiteServicio.getTramiteByInstitucionFechaActividad(institucionId, anio, mes, "Propiedad", remanenteMensualSelected.getRemanenteMensualId());
        tramiteSelected = new Tramite();
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        disableDelete = Boolean.TRUE;
        btnGuardar = "";
        diasNoLaborablesServicio.diasFestivosAtivos();

//        for (Date d : diasNoLaborablesServicio.diasFestivosAtivos()) {
//            System.out.println("Dia d: " + d);
//        }
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

    public Boolean getDisableDelete() {
        return disableDelete;
    }

    public void setDisableDelete(Boolean disableDelete) {
        this.disableDelete = disableDelete;
    }

    public void nuevoTramite() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        disableDelete = Boolean.TRUE;
        tramiteSelected = new Tramite();
        btnGuardar = "Guardar";
        //tipoInstitucion = "Dirección Regional";
        //institucionRequeridaList = institucionRequeridaServicio.getDireccionRegionalList();

    }

    public void onRowSelectTramite() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        onEdit = Boolean.TRUE;
        disableDelete = Boolean.FALSE;
        btnGuardar = "Actualizar";
        obtenerRemanenteMensual();
    }

    public void obtenerRemanenteMensual() {
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucionAñoMes(institucionId, anio, mes);
        remanenteMensualSelected = new RemanenteMensual();
        remanenteMensualSelected = remanenteMensualList.get(remanenteMensualList.size() - 1);
        if (remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("GeneradoAutomaticamente")
                || remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("Verificado-Rechazado")
                || remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("GeneradoNuevaVersion")) {
            disableNuevoT = Boolean.FALSE;
            if (diasNoLaborablesServicio.habilitarDiasAdicionales(remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(), remanenteMensualSelected.getMes())) {
                disableNuevoT = Boolean.FALSE;
            } else {
                renderEdition = Boolean.FALSE;
                disableDelete = Boolean.TRUE;
                disableNuevoT = Boolean.TRUE;
            }
        } else {
            renderEdition = Boolean.FALSE;
            disableDelete = Boolean.TRUE;
            disableNuevoT = Boolean.TRUE;

        }
    }

    public void cancelar() {
        tramiteList = new ArrayList<Tramite>();
        tramiteSelected = new Tramite();
        reloadTramite();
        onEdit = Boolean.FALSE;
        onCreate = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
    }

    public void guardar() {
        tramiteSelected.setActividadRegistral(actividadRegistral);
        if (onCreate) {
            tramiteSelected.setFechaRegistro(new Date());
            catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionListTipo(actividadRegistral);
            for (CatalogoTransaccion ct : catalogoList) {
                if (ct.getNombre().equals(tramiteSelected.getTipo())) {
                    idCatalogoTransaccion = ct.getCatalogoTransaccionId();
                }
            }
            Transaccion t = new Transaccion();
            t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
            tramiteSelected.setTransaccionId(t);
            tramiteSelected.setFechaRegistro(new Date());
            tramiteServicio.crearTramite(tramiteSelected);
        } else if (onEdit) {
            catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionListTipo(actividadRegistral);
            for (CatalogoTransaccion ct : catalogoList) {
                if (ct.getNombre().equals(tramiteSelected.getTipo())) {
                    idCatalogoTransaccion = ct.getCatalogoTransaccionId();
                }
            }
            Transaccion t = new Transaccion();
            t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
            tramiteSelected.setTransaccionId(t);
            tramiteSelected.setFechaRegistro(new Date());
            tramiteServicio.editTramite(tramiteSelected);
        }
        actualizarTransaccionValores();
        tramiteSelected = new Tramite();
        reloadTramite();
        actualizarTransaccionConteo();
        onEdit = Boolean.FALSE;
        onCreate = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        disableDelete = Boolean.TRUE;
    }

    public void actualizarTransaccionValores() {
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(tramiteSelected.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                tramiteSelected.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                tramiteSelected.getTransaccionId().getRemanenteMensualId().getMes(),
                tramiteSelected.getTransaccionId().getRemanenteMensualId().getRemanenteMensualId());
        for (Transaccion tl : transaccionList) {
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(1)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 1);
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(2)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 2);
            }
        }
    }

    public void actualizarTransaccionConteo() {
        Integer cantidadTramites = 0;
        for (Tramite trl : tramiteList) {
            if (trl.getTransaccionId().getCatalogoTransaccionId().getCatalogoTransaccionId().equals(1)) {
                cantidadTramites++;
            }
            if (trl.getTransaccionId().getCatalogoTransaccionId().getCatalogoTransaccionId().equals(2)) {
                cantidadTramites++;
            }
        }
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 4);
        t.setValorTotal(new BigDecimal(cantidadTramites));
        transaccionServicio.editTransaccion(t);
    }

    public void reloadTramite() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        fechaMin = fechasLimiteMin(anio, mes);
        fechaMax = fechasLimiteMax(anio, mes);
        tramiteList = new ArrayList<Tramite>();
        obtenerRemanenteMensual();
        tramiteList = tramiteServicio.getTramiteByInstitucionFechaActividad(institucionId, anio, mes, "Propiedad", remanenteMensualSelected.getRemanenteMensualId());
        disableDelete = Boolean.TRUE;
        renderEdition = Boolean.FALSE;
    }

    public void borrarTramite() {
        tramiteServicio.borrarTramite(tramiteSelected);
        tramiteServicio.actualizarTransaccionValor(institucionId, anio, mes, 1);
        tramiteServicio.actualizarTransaccionValor(institucionId, anio, mes, 2);
        reloadTramite();
        actualizarTransaccionConteo();
        disableDelete = Boolean.TRUE;
    }

    public void changeTipoTramite() {
        if (tramiteSelected.getTipo().equals("Inscripciones")) {
            renderedNumeroRepertorio = Boolean.TRUE;
        } else {
            renderedNumeroRepertorio = Boolean.FALSE;
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
                                            if (ct.getNombre().equals(tramiteNuevo.getTipo())&& ct.getTipo().equals("Ingreso-Propiedad")) {
                                                idCatalogoTransaccion = ct.getCatalogoTransaccionId();
                                                break;
                                            }
                                        }
                                        Transaccion t = new Transaccion();
                                        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
                                        tramiteNuevo.setTransaccionId(t);
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
                                        tramiteNuevo.setNumeroComprobantePago(datoVal);
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
                for (Tramite tramite : tramiteNuevoList) {
                    tramiteSelected = tramite;
                    tramiteServicio.crearTramite(tramite);
                    actualizarTransaccionValores();
                    tramiteSelected = new Tramite();
                }
                reloadTramite();
                actualizarTransaccionConteo();
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
            Logger.getLogger(TramitePropiedadCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            System.out.println("Error Parseo de fecha");
            Logger.getLogger(TramitePropiedadCtrl.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(TramitePropiedadCtrl.class.getName()).log(Level.SEVERE, null, ex);
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

    public String getTituloPropiedad() {
        return tituloPropiedad;
    }

    public void setTituloPropiedad(String tituloPropiedad) {
        this.tituloPropiedad = tituloPropiedad;
    }

    public List<Tramite> getTramiteList() {
        return tramiteList;
    }

    public void setTramiteList(List<Tramite> tramiteList) {
        this.tramiteList = tramiteList;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(Integer institucionId) {
        this.institucionId = institucionId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Tramite getTramiteSelected() {
        return tramiteSelected;
    }

    public void setTramiteSelected(Tramite tramiteSelected) {
        this.tramiteSelected = tramiteSelected;
    }

    public Integer getIdCatalogoTransaccion() {
        return idCatalogoTransaccion;
    }

    public void setIdCatalogoTransaccion(Integer idCatalogoTransaccion) {
        this.idCatalogoTransaccion = idCatalogoTransaccion;
    }

    public List<CatalogoTransaccion> getCatalogoList() {
        return catalogoList;
    }

    public void setCatalogoList(List<CatalogoTransaccion> catalogoList) {
        this.catalogoList = catalogoList;
    }

    public Boolean getOnEdit() {
        return onEdit;
    }

    public void setOnEdit(Boolean onEdit) {
        this.onEdit = onEdit;
    }

    public Boolean getOnCreate() {
        return onCreate;
    }

    public void setOnCreate(Boolean onCreate) {
        this.onCreate = onCreate;
    }

    public Boolean getRenderEdition() {
        return renderEdition;
    }

    public void setRenderEdition(Boolean renderEdition) {
        this.renderEdition = renderEdition;
    }

    public String getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(String btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

    public Boolean getDisableNuevoT() {
        return disableNuevoT;
    }

    public void setDisableNuevoT(Boolean disableNuevoT) {
        this.disableNuevoT = disableNuevoT;
    }

    public RemanenteMensual getRemanenteMensualSelected() {
        return remanenteMensualSelected;
    }

    public void setRemanenteMensualSelected(RemanenteMensual remanenteMensualSelected) {
        this.remanenteMensualSelected = remanenteMensualSelected;
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

    public Boolean getRenderedNumeroRepertorio() {
        return renderedNumeroRepertorio;
    }

    public void setRenderedNumeroRepertorio(Boolean renderedNumeroRepertorio) {
        this.renderedNumeroRepertorio = renderedNumeroRepertorio;
    }

}
