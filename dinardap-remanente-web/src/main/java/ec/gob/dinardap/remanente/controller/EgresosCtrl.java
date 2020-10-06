package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
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
import org.primefaces.model.UploadedFile;

@Named(value = "egresosCtrl")
@ViewScoped
public class EgresosCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloPagina;
    private String strBtnGuardar;

    private String tituloNomina;
    private String tituloFacturaPagada;

    private Boolean disableNuevoEgreso;

    private Boolean disabledDeleteNomina;
    private Boolean disabledDeleteNominaTodos;

    private Boolean disabledDeleteFacturaPagada;
    private Boolean disabledDeleteFacturaPagadaTodos;

    private Boolean renderEditionNomina;
    private Boolean renderEditionFacturaPagada;

    private Boolean onCreateNomina;
    private Boolean onEditNomina;

    private Boolean onCreateFacturaPagada;
    private Boolean onEditFacturaPagada;

    //Variables de negocio
    private Integer institucionId;
    private Date fechaSeleccionada;
    private Integer año;
    private Integer mes;
    private RemanenteMensual remanenteMensualSelected;

    private Nomina nominaSelected;
    private FacturaPagada facturaPagadaSelected;

    private String ultimoEstado;

    private String fechaMin;
    private String fechaMax;

    //Listas    
    private List<Nomina> nominaList;
    private List<Nomina> nominaSelectedList;

    private List<FacturaPagada> facturaPagadaList;
    private List<FacturaPagada> facturaPagadaSelectedList;

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

    @PostConstruct
    protected void init() {
        tituloPagina = "Egresos";
        tituloNomina = "Nómina";
        tituloFacturaPagada = "Factura Pagada";

        onCreateNomina = Boolean.FALSE;
        onEditNomina = Boolean.FALSE;

        onCreateFacturaPagada = Boolean.FALSE;
        onEditFacturaPagada = Boolean.FALSE;

        disableNuevoEgreso = Boolean.FALSE;

        disabledDeleteNomina = Boolean.TRUE;
        disabledDeleteFacturaPagada = Boolean.TRUE;

        renderEditionNomina = Boolean.FALSE;
        renderEditionFacturaPagada = Boolean.FALSE;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fechaSeleccionada = new Date();
        año = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        fechaMin = fechasLimiteMin(año, mes);
        fechaMax = fechasLimiteMax(año, mes);

        institucionId = this.getInstitucionID(this.getSessionVariable("perfil"));

        nominaList = new ArrayList<Nomina>();
        facturaPagadaList = new ArrayList<FacturaPagada>();

        obtenerRemanenteMensual();
    }

    public void obtenerRemanenteMensual() {
        remanenteMensualSelected = new RemanenteMensual();
        remanenteMensualSelected = remanenteMensualServicio.getUltimoRemanenteMensual(institucionId, año, mes);

        nominaList = nominaServicio.getNominaByInstitucionFecha(institucionId, año, mes, remanenteMensualSelected.getRemanenteMensualId());
        facturaPagadaList = facturaPagadaServicio.getFacturaPagadaByInstitucionFecha(institucionId, año, mes, remanenteMensualSelected.getRemanenteMensualId());

        ultimoEstado = remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion();

        if (ultimoEstado.equals("GeneradoAutomaticamente")
                || ultimoEstado.equals("Verificado-Rechazado")) {
            if (diasNoLaborablesServicio.habilitarDiasAdicionales(remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(), remanenteMensualSelected.getMes(), remanenteMensualSelected.getRemanenteMensualId())) {
                disableNuevoEgreso = Boolean.FALSE;
                disableDeleteNomina();
                disableDeleteFacturaPagada();
            } else {
                renderEditionNomina = Boolean.FALSE;
                renderEditionFacturaPagada = Boolean.FALSE;
                disabledDeleteNomina = Boolean.TRUE;
                disabledDeleteNominaTodos = Boolean.TRUE;
                disabledDeleteFacturaPagada = Boolean.TRUE;
                disabledDeleteFacturaPagadaTodos = Boolean.TRUE;
                disableNuevoEgreso = Boolean.TRUE;
            }
        } else {
            if (ultimoEstado.equals("GeneradoNuevaVersion")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(remanenteMensualSelected.getFechaRegistro());
                Integer añoSC = calendar.get(Calendar.YEAR);
                Integer mesSC = calendar.get(Calendar.MONTH) + 1;
                Integer diaSC = calendar.get(Calendar.DAY_OF_MONTH);
                if (diasNoLaborablesServicio.habilitarDiasAdicionalesCS(añoSC, mesSC, diaSC, remanenteMensualSelected.getRemanenteMensualId())) {
                    disableNuevoEgreso = Boolean.FALSE;
                    disableDeleteNomina();
                    disableDeleteFacturaPagada();
                } else {
                    renderEditionNomina = Boolean.FALSE;
                    renderEditionFacturaPagada = Boolean.FALSE;
                    disabledDeleteNomina = Boolean.TRUE;
                    disabledDeleteNominaTodos = Boolean.TRUE;
                    disabledDeleteFacturaPagada = Boolean.TRUE;
                    disabledDeleteFacturaPagadaTodos = Boolean.TRUE;
                    disableNuevoEgreso = Boolean.TRUE;
                }
            } else {
                renderEditionNomina = Boolean.FALSE;
                renderEditionFacturaPagada = Boolean.FALSE;
                disabledDeleteNomina = Boolean.TRUE;
                disabledDeleteNominaTodos = Boolean.TRUE;
                disabledDeleteFacturaPagada = Boolean.TRUE;
                disabledDeleteFacturaPagadaTodos = Boolean.TRUE;
                disableNuevoEgreso = Boolean.TRUE;
            }
        }
        
    }

    public void reloadEgresos() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaSeleccionada);
        año = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        fechaMin = fechasLimiteMin(año, mes);
        fechaMax = fechasLimiteMax(año, mes);

        nominaList = new ArrayList<Nomina>();
        facturaPagadaList = new ArrayList<FacturaPagada>();
        obtenerRemanenteMensual();

        nominaSelectedList = new ArrayList<Nomina>();
        nominaSelected = new Nomina();

        facturaPagadaSelectedList = new ArrayList<FacturaPagada>();
        facturaPagadaSelected = new FacturaPagada();

        renderEditionNomina = Boolean.FALSE;
        renderEditionFacturaPagada = Boolean.FALSE;
    }

    public void onRowSelectNomina() {
        strBtnGuardar = "Actualizar";

        onEditNomina = Boolean.TRUE;
        onCreateNomina = Boolean.FALSE;
        disabledDeleteNomina = Boolean.FALSE;
        renderEditionNomina = Boolean.TRUE;

        nominaSelected = nominaSelectedList.get(0);
        obtenerRemanenteMensual();
    }

    public void onRowSelectFacturaPagada() {
        strBtnGuardar = "Actualizar";

        onEditFacturaPagada = Boolean.TRUE;
        onCreateFacturaPagada = Boolean.FALSE;
        disabledDeleteFacturaPagada = Boolean.FALSE;
        renderEditionFacturaPagada = Boolean.TRUE;

        facturaPagadaSelected = facturaPagadaSelectedList.get(0);
        obtenerRemanenteMensual();
    }

    public void onRowSelectNominaCheckbox() {
        disabledDeleteNomina = ultimoEstado.equals("GeneradoAutomaticamente")
                || ultimoEstado.equals("Verificado-Rechazado")
                || ultimoEstado.equals("GeneradoNuevaVersion") ? nominaSelectedList.isEmpty() ? Boolean.TRUE : Boolean.FALSE : Boolean.FALSE;
    }

    public void onRowSelectFacturaPagadaCheckbox() {
        disabledDeleteFacturaPagada = ultimoEstado.equals("GeneradoAutomaticamente")
                || ultimoEstado.equals("Verificado-Rechazado")
                || ultimoEstado.equals("GeneradoNuevaVersion") ? facturaPagadaSelectedList.isEmpty() ? Boolean.TRUE : Boolean.FALSE : Boolean.FALSE;
    }

    public void nuevoRegistroNomina() {
        strBtnGuardar = "Guardar";

        onCreateNomina = Boolean.TRUE;
        onEditNomina = Boolean.FALSE;
        disabledDeleteNomina = Boolean.TRUE;
        renderEditionNomina = Boolean.TRUE;

        nominaSelected = new Nomina();
    }

    public void nuevoRegistroFacturaPagada() {
        strBtnGuardar = "Guardar";

        onCreateFacturaPagada = Boolean.TRUE;
        onEditFacturaPagada = Boolean.FALSE;
        disabledDeleteFacturaPagada = Boolean.TRUE;
        renderEditionFacturaPagada = Boolean.TRUE;

        facturaPagadaSelected = new FacturaPagada();
    }

    public void borrarRegistroNominaSeleccionado() {
        nominaServicio.borrarNominas(nominaSelectedList);
        nominaServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId());

        obtenerRemanenteMensual();

        nominaSelectedList = new ArrayList<Nomina>();
        nominaSelected = new Nomina();

        onCreateNomina = Boolean.FALSE;
        onEditNomina = Boolean.FALSE;
        disabledDeleteNomina = Boolean.TRUE;
//        disabledDeleteNomina();
        renderEditionNomina = Boolean.FALSE;
    }

    public void borrarRegistroFacturaPagadaSeleccionada() {
        facturaPagadaServicio.borrarFacturasPagadas(facturaPagadaSelectedList);
        facturaPagadaServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId());

        obtenerRemanenteMensual();

        facturaPagadaSelectedList = new ArrayList<FacturaPagada>();
        facturaPagadaSelected = new FacturaPagada();

        onCreateFacturaPagada = Boolean.FALSE;
        onEditFacturaPagada = Boolean.FALSE;
        disabledDeleteFacturaPagada = Boolean.TRUE;
//        disabledDeleteNomina();
        renderEditionFacturaPagada = Boolean.FALSE;
    }

    private void disableDeleteNomina() {
        disabledDeleteNominaTodos = nominaList.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
    }

    private void disableDeleteFacturaPagada() {
        disabledDeleteFacturaPagadaTodos = facturaPagadaList.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
    }

    public void borrarTodosRegistrosNomina() {
        nominaServicio.borrarNominas(nominaList);
        nominaServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId());

        obtenerRemanenteMensual();

        nominaSelectedList = new ArrayList<Nomina>();
        nominaSelected = new Nomina();

        onCreateNomina = Boolean.FALSE;
        onEditNomina = Boolean.FALSE;
        disabledDeleteNomina = Boolean.TRUE;

        renderEditionNomina = Boolean.FALSE;
    }

    public void borrarTodosRegistrosFacturaPagada() {
        facturaPagadaServicio.borrarFacturasPagadas(facturaPagadaList);
        facturaPagadaServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId());

        obtenerRemanenteMensual();

        facturaPagadaSelectedList = new ArrayList<FacturaPagada>();
        facturaPagadaSelected = new FacturaPagada();

        onCreateFacturaPagada = Boolean.FALSE;
        onEditFacturaPagada = Boolean.FALSE;
        disabledDeleteFacturaPagada = Boolean.TRUE;

        renderEditionFacturaPagada = Boolean.FALSE;
    }

    public void guardarNomina() {
        //9 CORRESPONDE A REMUNERACIONES(NOMINA) DEL CATALOGO DE TRANSACCIONES
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, año, mes, 9);

        nominaSelected.setTransaccionId(t);
        nominaSelected.setFechaRegistro(new Date());

        if (onCreateNomina) {
            nominaServicio.create(nominaSelected);
        } else if (onEditNomina) {
            nominaServicio.update(nominaSelected);
        }
        nominaServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId());

        obtenerRemanenteMensual();

        nominaSelectedList = new ArrayList<Nomina>();
        nominaSelected = new Nomina();

        onCreateNomina = Boolean.FALSE;
        onEditNomina = Boolean.FALSE;
        disabledDeleteNomina = Boolean.TRUE;
        renderEditionNomina = Boolean.FALSE;
    }

    public void cancelarNomina() {
        nominaSelected = new Nomina();

        obtenerRemanenteMensual();

        nominaSelectedList = new ArrayList<Nomina>();
        nominaSelected = new Nomina();

        onCreateNomina = Boolean.FALSE;
        onEditNomina = Boolean.FALSE;
        disabledDeleteNomina = Boolean.TRUE;
        renderEditionNomina = Boolean.FALSE;
    }

    public void guardarFacturaPagada() {
        CatalogoTransaccion catalogoTransaccion = new CatalogoTransaccion();
        catalogoTransaccion = catalogoTransaccionServicio.getCatalogoTransaccionEgresoNombre(facturaPagadaSelected.getTipo());

        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, año, mes, catalogoTransaccion.getCatalogoTransaccionId());

        facturaPagadaSelected.setTransaccionId(t);
        facturaPagadaSelected.setFechaRegistro(new Date());

        if (onCreateFacturaPagada) {
            facturaPagadaServicio.create(facturaPagadaSelected);
        } else if (onEditFacturaPagada) {
            facturaPagadaServicio.update(facturaPagadaSelected);
        }

        facturaPagadaServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId());

        obtenerRemanenteMensual();

        facturaPagadaSelectedList = new ArrayList<FacturaPagada>();
        facturaPagadaSelected = new FacturaPagada();

        onCreateFacturaPagada = Boolean.FALSE;
        onEditFacturaPagada = Boolean.FALSE;
        disabledDeleteFacturaPagada = Boolean.TRUE;
        renderEditionFacturaPagada = Boolean.FALSE;
    }

    public void cancelarFacturaPagada() {
        facturaPagadaSelected = new FacturaPagada();

        obtenerRemanenteMensual();

        facturaPagadaSelectedList = new ArrayList<FacturaPagada>();
        facturaPagadaSelected = new FacturaPagada();

        onCreateFacturaPagada = Boolean.FALSE;
        onEditFacturaPagada = Boolean.FALSE;
        disabledDeleteFacturaPagada = Boolean.TRUE;
        renderEditionFacturaPagada = Boolean.FALSE;
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
                                        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, año, mes, 9);
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
                nominaServicio.crearNominas(nominaNuevoList);
                nominaServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId());
                obtenerRemanenteMensual();
                nominaSelectedList = new ArrayList<Nomina>();
                nominaNuevoList = new ArrayList<Nomina>();
                nominaSelected = new Nomina();
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
//            List<CatalogoTransaccion> catalogoList = new ArrayList<CatalogoTransaccion>();
//            catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionList();
//            Integer idCatalogoTransaccion = 0;

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
                                        if (datoVal.length() <= 15) {
                                            facturaPagadaNueva.setNumero(datoVal);
                                        }
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
                                        CatalogoTransaccion catalogoTransaccion = new CatalogoTransaccion();
                                        catalogoTransaccion = catalogoTransaccionServicio.getCatalogoTransaccionEgresoNombre(facturaPagadaNueva.getTipo());
                                        Transaccion t = new Transaccion();
                                        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, año, mes, catalogoTransaccion.getCatalogoTransaccionId());
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
//                for (FacturaPagada facturaPagada : facturaPagadaNuevaList) {
//                    facturaPagadaServicio.crearFacturaPagada(facturaPagada);
//                }
//                reloadFacturaPagada();
                facturaPagadaServicio.crearFacturaPagadas(facturaPagadaNuevaList);
                facturaPagadaServicio.actualizarTransaccionValor(remanenteMensualSelected.getRemanenteMensualId());
                obtenerRemanenteMensual();
                facturaPagadaSelectedList = new ArrayList<FacturaPagada>();
                facturaPagadaNuevaList = new ArrayList<FacturaPagada>();
                facturaPagadaSelected = new FacturaPagada();
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
                    if (diasNoLaborablesServicio.habilitarDiasAdicionales(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH) + 1, remanenteMensualSelected.getRemanenteMensualId())) {
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
    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public String getTituloNomina() {
        return tituloNomina;
    }

    public void setTituloNomina(String tituloNomina) {
        this.tituloNomina = tituloNomina;
    }

    public String getTituloFacturaPagada() {
        return tituloFacturaPagada;
    }

    public void setTituloFacturaPagada(String tituloFacturaPagada) {
        this.tituloFacturaPagada = tituloFacturaPagada;
    }

    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public List<Nomina> getNominaList() {
        return nominaList;
    }

    public void setNominaList(List<Nomina> nominaList) {
        this.nominaList = nominaList;
    }

    public List<FacturaPagada> getFacturaPagadaList() {
        return facturaPagadaList;
    }

    public void setFacturaPagadaList(List<FacturaPagada> facturaPagadaList) {
        this.facturaPagadaList = facturaPagadaList;
    }

    public List<Nomina> getNominaSelectedList() {
        return nominaSelectedList;
    }

    public void setNominaSelectedList(List<Nomina> nominaSelectedList) {
        this.nominaSelectedList = nominaSelectedList;
    }

    public List<FacturaPagada> getFacturaPagadaSelectedList() {
        return facturaPagadaSelectedList;
    }

    public void setFacturaPagadaSelectedList(List<FacturaPagada> facturaPagadaSelectedList) {
        this.facturaPagadaSelectedList = facturaPagadaSelectedList;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Boolean getDisableNuevoEgreso() {
        return disableNuevoEgreso;
    }

    public void setDisableNuevoEgreso(Boolean disableNuevoEgreso) {
        this.disableNuevoEgreso = disableNuevoEgreso;
    }

    public Boolean getRenderEditionNomina() {
        return renderEditionNomina;
    }

    public void setRenderEditionNomina(Boolean renderEditionNomina) {
        this.renderEditionNomina = renderEditionNomina;
    }

    public Boolean getRenderEditionFacturaPagada() {
        return renderEditionFacturaPagada;
    }

    public void setRenderEditionFacturaPagada(Boolean renderEditionFacturaPagada) {
        this.renderEditionFacturaPagada = renderEditionFacturaPagada;
    }

    public Boolean getDisabledDeleteNomina() {
        return disabledDeleteNomina;
    }

    public void setDisabledDeleteNomina(Boolean disabledDeleteNomina) {
        this.disabledDeleteNomina = disabledDeleteNomina;
    }

    public Boolean getDisabledDeleteNominaTodos() {
        return disabledDeleteNominaTodos;
    }

    public void setDisabledDeleteNominaTodos(Boolean disabledDeleteNominaTodos) {
        this.disabledDeleteNominaTodos = disabledDeleteNominaTodos;
    }

    public Boolean getDisabledDeleteFacturaPagada() {
        return disabledDeleteFacturaPagada;
    }

    public void setDisabledDeleteFacturaPagada(Boolean disabledDeleteFacturaPagada) {
        this.disabledDeleteFacturaPagada = disabledDeleteFacturaPagada;
    }

    public Boolean getDisabledDeleteFacturaPagadaTodos() {
        return disabledDeleteFacturaPagadaTodos;
    }

    public void setDisabledDeleteFacturaPagadaTodos(Boolean disabledDeleteFacturaPagadaTodos) {
        this.disabledDeleteFacturaPagadaTodos = disabledDeleteFacturaPagadaTodos;
    }

    public Nomina getNominaSelected() {
        return nominaSelected;
    }

    public void setNominaSelected(Nomina nominaSelected) {
        this.nominaSelected = nominaSelected;
    }

    public String getStrBtnGuardar() {
        return strBtnGuardar;
    }

    public void setStrBtnGuardar(String strBtnGuardar) {
        this.strBtnGuardar = strBtnGuardar;
    }

    public Boolean getOnCreateNomina() {
        return onCreateNomina;
    }

    public void setOnCreateNomina(Boolean onCreateNomina) {
        this.onCreateNomina = onCreateNomina;
    }

    public Boolean getOnEditNomina() {
        return onEditNomina;
    }

    public void setOnEditNomina(Boolean onEditNomina) {
        this.onEditNomina = onEditNomina;
    }

    public Boolean getOnCreateFacturaPagada() {
        return onCreateFacturaPagada;
    }

    public void setOnCreateFacturaPagada(Boolean onCreateFacturaPagada) {
        this.onCreateFacturaPagada = onCreateFacturaPagada;
    }

    public Boolean getOnEditFacturaPagada() {
        return onEditFacturaPagada;
    }

    public void setOnEditFacturaPagada(Boolean onEditFacturaPagada) {
        this.onEditFacturaPagada = onEditFacturaPagada;
    }

    public FacturaPagada getFacturaPagadaSelected() {
        return facturaPagadaSelected;
    }

    public void setFacturaPagadaSelected(FacturaPagada facturaPagadaSelected) {
        this.facturaPagadaSelected = facturaPagadaSelected;
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

    public String getUltimoEstado() {
        return ultimoEstado;
    }

    public void setUltimoEstado(String ultimoEstado) {
        this.ultimoEstado = ultimoEstado;
    }

}
