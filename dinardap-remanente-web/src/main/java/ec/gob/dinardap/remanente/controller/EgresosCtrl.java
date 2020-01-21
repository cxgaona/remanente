package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.DiasNoLaborablesServicio;
import ec.gob.dinardap.remanente.servicio.FacturaPagadaServicio;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
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
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "egresosCtrl")
@ViewScoped
public class EgresosCtrl extends BaseCtrl implements Serializable {

    @EJB
    private NominaServicio nominaServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @EJB
    private FacturaPagadaServicio facturaPagadaServicio;

    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private DiasNoLaborablesServicio diasNoLaborablesServicio;

    private String tituloEgreso;
    private String tituloN, tituloFP;
    private List<Nomina> nominaList;
    private Integer anio, mes;
    private Integer institucionId;
    private Date fecha;
    private Nomina nominaSelected;
    private List<FacturaPagada> facturaPagadaList;
    private FacturaPagada facturaPagadaSelected;
    private Integer idCatalogoTransaccion;
    private List<CatalogoTransaccion> catalogoList;
    private Boolean disableNuevoRegistro;
    private Boolean renderEdition;
    private List<RemanenteMensual> remanenteMensualList;
    private RemanenteMensual remanenteMensualSelected;
    private String fechaMin;
    private String fechaMax;

    @PostConstruct
    protected void init() {
        tituloEgreso = "Egresos";
        tituloN = "Nómina";
        tituloFP = "Factura Pagada";
        facturaPagadaList = new ArrayList<FacturaPagada>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fecha = new Date();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        fechaMin = fechasLimiteMin(anio, mes);
        fechaMax = fechasLimiteMax(anio, mes);
        institucionId = this.getInstitucionID(this.getSessionVariable("perfil"));
        nominaList = new ArrayList<Nomina>();
        nominaList = nominaServicio.getNominaByInstitucionFecha(institucionId, anio, mes);
        nominaSelected = new Nomina();
        facturaPagadaList = new ArrayList<FacturaPagada>();
        facturaPagadaList = facturaPagadaServicio.getFacturaPagadaByInstitucionFecha(institucionId, anio, mes);
        facturaPagadaSelected = new FacturaPagada();
        disableNuevoRegistro = Boolean.FALSE;
        renderEdition = Boolean.TRUE;
        obtenerRemanenteMensual();
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

    public void addRowNomina() {
        Nomina newNomina = new Nomina();
        newNomina.setAportePatronal(BigDecimal.ZERO);
        newNomina.setDecimoCuarto(BigDecimal.ZERO);
        newNomina.setDecimoTercero(BigDecimal.ZERO);
        newNomina.setFechaRegistro(new Date());
        newNomina.setFondosReserva(BigDecimal.ZERO);
        newNomina.setImpuestoRenta(BigDecimal.ZERO);
        newNomina.setLiquidoRecibir(BigDecimal.ZERO);
        newNomina.setRemuneracion(BigDecimal.ZERO);
        newNomina.setTotalDesc(BigDecimal.ZERO);
        newNomina.setNominaId(null);
        Transaccion t = new Transaccion();
        //9 CORRESPONDE A REMUNERACIONES(NOMINA) DEL CATALOGO DE TRANSACCIONES
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 9);
        newNomina.setTransaccionId(t);
        nominaServicio.crearNomina(newNomina);
        nominaList.add(newNomina);
    }

    public void onRowEditNomina(RowEditEvent event) {
        Nomina nomina = new Nomina();
        nomina = (Nomina) event.getObject();
        Transaccion t = new Transaccion();
        //9 CORRESPONDE A REMUNERACIONES(NOMINA) DEL CATALOGO DE TRANSACCIONES
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 9);
        nomina.setTransaccionId(t);
        nomina.setFechaRegistro(new Date());
        nominaServicio.editNomina(nomina);
    }

    public void reloadNomina() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        nominaList = nominaServicio.getNominaByInstitucionFecha(institucionId, anio, mes);
        reloadFacturaPagada();
        obtenerRemanenteMensual();
    }

    public void onRowDeleteNomina() {
        nominaServicio.borrarNomina(nominaSelected);
        nominaServicio.actualizarTransaccionValor(institucionId, anio, mes);
        reloadNomina();
    }

    public void obtenerRemanenteMensual() {
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucionAñoMes(institucionId, anio, mes);
        remanenteMensualSelected = new RemanenteMensual();
        remanenteMensualSelected = remanenteMensualList.get(remanenteMensualList.size() - 1);
        if (remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("GeneradoAutomaticamente")
                || remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("Verificado-Rechazado")
                || remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("GeneradoNuevaVersion")) {
            disableNuevoRegistro = Boolean.FALSE;
            renderEdition = Boolean.TRUE;
            if (diasNoLaborablesServicio.habilitarDiasAdicionales(remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(), remanenteMensualSelected.getMes())) {
                disableNuevoRegistro = Boolean.FALSE;
                renderEdition = Boolean.TRUE;
            } else {
                renderEdition = Boolean.FALSE;
                disableNuevoRegistro = Boolean.TRUE;
            }
        } else {
            renderEdition = Boolean.FALSE;
            disableNuevoRegistro = Boolean.TRUE;
        }
    }

    public void addRowFacturaPagada() {
        FacturaPagada newFacturaPagada = new FacturaPagada();
        String fechaStr = anio + "-" + mes + "-01";
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
            newFacturaPagada.setFecha(date);
        } catch (ParseException ex) {
            Logger.getLogger(EgresosCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }

        newFacturaPagada.setNumero(null);
        newFacturaPagada.setTipo("Otros");
        newFacturaPagada.setDetalle(null);
        newFacturaPagada.setValor(BigDecimal.ZERO);
        newFacturaPagada.setFechaRegistro(new Date());
        newFacturaPagada.setFacturaPagadaId(null);
        Transaccion t = new Transaccion();
        //11 CORRESPONDE A OTROS DEL CATALOGO DE TRANSACCIONES
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 11);
        newFacturaPagada.setTransaccionId(t);
        facturaPagadaServicio.crearFacturaPagada(newFacturaPagada);
        facturaPagadaList.add(newFacturaPagada);
    }

    public void onRowEditFacturaPagada(RowEditEvent event) {
        FacturaPagada facturaPagada = new FacturaPagada();
        facturaPagada = (FacturaPagada) event.getObject();
        catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionList();
        for (CatalogoTransaccion ct : catalogoList) {
            if (ct.getNombre().equals(facturaPagada.getTipo())) {
                idCatalogoTransaccion = ct.getCatalogoTransaccionId();
            }
        }
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
        facturaPagada.setTransaccionId(t);
        facturaPagada.setFechaRegistro(new Date());
        facturaPagadaServicio.editFacturaPagada(facturaPagada);
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getMes(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteMensualId());
        for (Transaccion tl : transaccionList) {
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(10)) {
                facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 10);
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(11)) {
                facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 11);
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(12)) {
                facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 12);
            }
        }
    }

    public void reloadFacturaPagada() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        fechaMin = fechasLimiteMin(anio, mes);
        fechaMax = fechasLimiteMax(anio, mes);
        facturaPagadaList = facturaPagadaServicio.getFacturaPagadaByInstitucionFecha(institucionId, anio, mes);
    }

    public void onRowDeleteFacturaPagada() {
        facturaPagadaServicio.borrarFacturaPagada(facturaPagadaSelected);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 10);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 11);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 12);
        reloadFacturaPagada();
    }

    public void crearNominaBloque(FileUploadEvent event) {
        InputStream in = null;
        try {
            UploadedFile uploadedFile = event.getFile();
            in = uploadedFile.getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(in);
            XSSFSheet sheet = worbook.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            List<Nomina> nominaNuevoList = new ArrayList<Nomina>();

            Boolean flagValidacionCampos = Boolean.TRUE;
            List<String> celdaError = new ArrayList<String>();
            for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
                row = sheet.getRow(r);
                if (row.getRowNum() != 0) {
                    Nomina nominaNuevo = new Nomina();
                    nominaNuevo.setFechaRegistro(new Date());
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
                                    datoVal = validarCampoVacio(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        Transaccion t = new Transaccion();
                                        //9 CORRESPONDE A REMUNERACIONES(NOMINA) DEL CATALOGO DE TRANSACCIONES
                                        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 9);
                                        nominaNuevo.setTransaccionId(t);
                                        nominaNuevo.setNombre(datoVal);
                                    }
                                    break;
                                case 1:
                                    datoVal = validarCampoVacio(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setCargo(datoVal);
                                    }
                                    break;
                                case 2:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setRemuneracion(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 3:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setAportePatronal(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 4:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setImpuestoRenta(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 5:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setFondosReserva(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 6:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setDecimoTercero(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 7:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setDecimoCuarto(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 8:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setTotalDesc(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 9:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        nominaNuevo.setLiquidoRecibir(new BigDecimal(datoVal));
                                    }
                                    break;
                            }
                        }
                    }
                    if (nominaNuevo.getNombre() == null || nominaNuevo.getNombre().isEmpty()) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("A" + (r + 1));
                    }
                    if (nominaNuevo.getCargo() == null || nominaNuevo.getCargo().isEmpty()) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("B" + (r + 1));
                    }
                    if (nominaNuevo.getRemuneracion() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("C" + (r + 1));
                    }
                    if (nominaNuevo.getAportePatronal() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("D" + (r + 1));
                    }
                    if (nominaNuevo.getImpuestoRenta() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("E" + (r + 1));
                    }
                    if (nominaNuevo.getFondosReserva() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("F" + (r + 1));
                    }
                    if (nominaNuevo.getDecimoTercero() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("G" + (r + 1));
                    }
                    if (nominaNuevo.getDecimoCuarto() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("H" + (r + 1));
                    }
                    if (nominaNuevo.getTotalDesc() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("I" + (r + 1));
                    }
                    if (nominaNuevo.getLiquidoRecibir() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("J" + (r + 1));
                    }
                    nominaNuevoList.add(nominaNuevo);
                }
            }

            if (flagValidacionCampos) {
                for (Nomina nomina : nominaNuevoList) {
                    nominaSelected = nomina;
                    nominaServicio.crearNomina(nomina);
                    nominaSelected = new Nomina();
                }
                reloadNomina();
                nominaServicio.actualizarTransaccionValor(institucionId, anio, mes);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha creado la Nómina en bloque satisfactoriamente"));
            } else {
                String celdas = "";
                for (String s : celdaError) {
                    celdas += s + ", ";
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Favor verificar su archivo de carga. \n Verificar las celdas: " + celdas + "de su archivo de carga"));
            }

        } catch (IOException ex) {
            System.out.println("Error carga de Archivo");
            Logger.getLogger(TramitePropiedadCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearFacturaBloque(FileUploadEvent event) {
        InputStream in = null;
        try {
            UploadedFile uploadedFile = event.getFile();
            in = uploadedFile.getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(in);
            XSSFSheet sheet = worbook.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            List<FacturaPagada> facturaPagadaNuevaList = new ArrayList<FacturaPagada>();
            catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionList();

            Boolean flagValidacionCampos = Boolean.TRUE;
            List<String> celdaError = new ArrayList<String>();
            for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
                row = sheet.getRow(r);
                if (row.getRowNum() != 0) {
                    FacturaPagada facturaPagadaNueva = new FacturaPagada();
                    facturaPagadaNueva.setFechaRegistro(new Date());
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
                                    datoVal = validarCampoNumero(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        facturaPagadaNueva.setNumero(datoVal);
                                    }
                                    break;
                                case 1:
                                    datoVal = validarCampoFecha(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        facturaPagadaNueva.setFecha(new SimpleDateFormat("yyyy-MM-dd").parse(datoVal));
                                    }
                                    break;
                                case 2:
                                    if (dato.equals("Otros")
                                            || dato.equals("Bienes y Servicios de Consumo (Arriendo, Servicios Básicos)")
                                            || dato.equals("Bienes de Larga Duración")) {
                                        facturaPagadaNueva.setTipo(dato);
                                        for (CatalogoTransaccion ct : catalogoList) {
                                            if (ct.getNombre().equals(facturaPagadaNueva.getTipo()) && ct.getTipo().equals("Egreso")) {
                                                idCatalogoTransaccion = ct.getCatalogoTransaccionId();
                                                break;
                                            }
                                        }
                                        Transaccion t = new Transaccion();
                                        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
                                        facturaPagadaNueva.setTransaccionId(t);
                                    }
                                    break;
                                case 3:
                                    datoVal = validarCampoValorNumerico(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        facturaPagadaNueva.setValor(new BigDecimal(datoVal));
                                    }
                                    break;
                                case 4:
                                    datoVal = validarCampoVacio(dato);
                                    if (!datoVal.equals("INVALIDO")) {
                                        facturaPagadaNueva.setDetalle(datoVal);
                                    }
                                    break;
                            }
                        }
                    }

                    if (facturaPagadaNueva.getNumero() == null || facturaPagadaNueva.getNumero().isEmpty()) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("A" + (r + 1));
                    }
                    if (facturaPagadaNueva.getFecha() == null || facturaPagadaNueva.getFecha().equals("")) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("B" + (r + 1));
                    }
                    if (facturaPagadaNueva.getTipo() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("C" + (r + 1));
                    }
                    if (facturaPagadaNueva.getValor() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("D" + (r + 1));
                    }
                    if (facturaPagadaNueva.getDetalle() == null) {
                        flagValidacionCampos = Boolean.FALSE;
                        celdaError.add("E" + (r + 1));
                    }
                    facturaPagadaNuevaList.add(facturaPagadaNueva);
                }
            }

            if (flagValidacionCampos) {
                for (FacturaPagada facturaPagada : facturaPagadaNuevaList) {
                    facturaPagadaSelected = facturaPagada;
                    facturaPagadaServicio.crearFacturaPagada(facturaPagada);
                    facturaPagadaSelected = new FacturaPagada();
                }
                reloadFacturaPagada();
                List<Transaccion> transaccionList = new ArrayList<Transaccion>();
                transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(facturaPagadaNuevaList.get(0).getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        facturaPagadaNuevaList.get(0).getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        facturaPagadaNuevaList.get(0).getTransaccionId().getRemanenteMensualId().getMes(),
                        facturaPagadaNuevaList.get(0).getTransaccionId().getRemanenteMensualId().getRemanenteMensualId());
                for (Transaccion tl : transaccionList) {
                    if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(10)) {
                        facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                                tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                                tl.getRemanenteMensualId().getMes(), 10);
                    }
                    if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(11)) {
                        facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                                tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                                tl.getRemanenteMensualId().getMes(), 11);
                    }
                    if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(12)) {
                        facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                                tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                                tl.getRemanenteMensualId().getMes(), 12);
                    }
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha creado el bloque de Facturas Pagadas satisfactoriamente"));
            } else {
                String celdas = "";
                for (String s : celdaError) {
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
    public String getTituloEgreso() {
        return tituloEgreso;
    }

    public void setTituloEgreso(String tituloEgreso) {
        this.tituloEgreso = tituloEgreso;
    }

    public String getTituloN() {
        return tituloN;
    }

    public void setTituloN(String tituloN) {
        this.tituloN = tituloN;
    }

    public String getTituloFP() {
        return tituloFP;
    }

    public void setTituloFP(String tituloFP) {
        this.tituloFP = tituloFP;
    }

    public List<Nomina> getNominaList() {
        return nominaList;
    }

    public void setNominaList(List<Nomina> nominaList) {
        this.nominaList = nominaList;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Nomina getNominaSelected() {
        return nominaSelected;
    }

    public void setNominaSelected(Nomina nominaSelected) {
        this.nominaSelected = nominaSelected;
    }

    public List<FacturaPagada> getFacturaPagadaList() {
        return facturaPagadaList;
    }

    public void setFacturaPagadaList(List<FacturaPagada> facturaPagadaList) {
        this.facturaPagadaList = facturaPagadaList;
    }

    public FacturaPagada getFacturaPagadaSelected() {
        return facturaPagadaSelected;
    }

    public void setFacturaPagadaSelected(FacturaPagada facturaPagadaSelected) {
        this.facturaPagadaSelected = facturaPagadaSelected;
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

    public Boolean getDisableNuevoRegistro() {
        return disableNuevoRegistro;
    }

    public void setDisableNuevoRegistro(Boolean disableNuevoRegistro) {
        this.disableNuevoRegistro = disableNuevoRegistro;
    }

    public Boolean getRenderEdition() {
        return renderEdition;
    }

    public void setRenderEdition(Boolean renderEdition) {
        this.renderEdition = renderEdition;
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

}
