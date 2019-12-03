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
            System.out.println("===Prueba dias no laborables===");
            System.out.println(diasNoLaborablesServicio.habilitarDiasAdicionales(remanenteMensualSelected.getMes()));
//            if (diasNoLaborablesServicio.habilitarDiasAdicionales(remanenteMensualSelected.getMes())) {
//                disableNuevoT = Boolean.FALSE;
//            } else {
//                renderEdition = Boolean.FALSE;
//                disableDelete = Boolean.TRUE;
//                disableNuevoT = Boolean.TRUE;
//            }

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

    public void crearTramitesBloque(FileUploadEvent event) {
        InputStream in = null;
        try {
            System.out.println("==Tramites en bloque==");
            UploadedFile uploadedFile = event.getFile();
            in = uploadedFile.getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(in);
            XSSFSheet sheet = worbook.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            List<Tramite> tramiteNuevoList = new ArrayList<Tramite>();

            catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionListTipo(actividadRegistral);

            for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
                row = sheet.getRow(r);
                Tramite tramiteNuevo = new Tramite();
                tramiteNuevo.setActividadRegistral(actividadRegistral);
                tramiteNuevo.setFechaRegistro(new Date());

                if (row.getRowNum() != 0) {
                    for (int c = 0; c < (int) row.getLastCellNum(); c++) {
                        String dato = "";
                        cell = row.getCell(c);
                        if (cell == null) {
                            dato = null;
                        } else {
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    dato = cell.getNumericCellValue() + "";
                                    break;
                                case FORMULA:
                                    FormulaEvaluator evaluator = worbook.getCreationHelper().createFormulaEvaluator();
                                    CellValue cellValue = evaluator.evaluate(cell);
                                    switch (cellValue.getCellType()) {
                                        case STRING:
                                            System.out.print(cellValue.getStringValue());
                                            dato = cellValue.getStringValue();
                                            break;
                                        case NUMERIC:
                                            System.out.print(cellValue.getNumberValue());
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
                        }
                        switch (cell.getColumnIndex()) {
                            case 0:
                                tramiteNuevo.setTipo(dato);
                                break;
                            case 1:
                                tramiteNuevo.setNumero(dato);
                                break;
                            case 2:
                                if (tramiteNuevo.getTipo().equals("Certificaciones")) {
                                    if (dato.equals("NA")) {
                                        tramiteNuevo.setNumeroRepertorio("");
                                    }
                                } else {
                                    tramiteNuevo.setNumeroRepertorio(dato);
                                }
                                break;
                            case 3:
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dato);
                                tramiteNuevo.setFecha(date);
                                break;
                            case 4:
                                tramiteNuevo.setNumeroComprobantePago(dato);
                                break;
                            case 5:
                                tramiteNuevo.setValor(new BigDecimal(dato));
                                break;
                            case 6:
                                tramiteNuevo.setActo(dato);
                                break;
                        }
                    }
                    for (CatalogoTransaccion ct : catalogoList) {
                        if (ct.getNombre().equals(tramiteNuevo.getTipo())) {
                            idCatalogoTransaccion = ct.getCatalogoTransaccionId();
                        }
                    }
                    Transaccion t = new Transaccion();
                    t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
                    tramiteNuevo.setTransaccionId(t);
                    tramiteNuevoList.add(tramiteNuevo);
//                    tramiteServicio.crearTramite(tramiteSelected);
                }
            }
            for (Tramite tramite : tramiteNuevoList) {
                tramiteSelected = tramite;
                tramiteServicio.crearTramite(tramite);
                actualizarTransaccionValores();
                tramiteSelected = new Tramite();
            }
            reloadTramite();
            actualizarTransaccionConteo();
            this.addInfoMessage("Se ha creado el bloque de trámites satisfactoriamente", "Info");
        } catch (IOException ex) {
            Logger.getLogger(TramitePropiedadCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TramitePropiedadCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            this.addErrorMessage("0", "Error: El Excel que se pretende subir tiene errores, favor verificar su archivo de carga", "No funcionó");
        }
    }
    
    public void changeTipoTramite() {
        if (tramiteSelected.getTipo().equals("Inscripciones")) {
            renderedNumeroRepertorio = Boolean.TRUE;
        } else {
            renderedNumeroRepertorio = Boolean.FALSE;
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
