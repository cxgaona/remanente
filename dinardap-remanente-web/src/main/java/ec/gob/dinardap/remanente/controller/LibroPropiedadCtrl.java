package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.TipoLibroEnum;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;
import ec.gob.dinardap.remanente.modelo.Libro;
import ec.gob.dinardap.remanente.modelo.TipoLibro;
import ec.gob.dinardap.remanente.servicio.EstadoInventarioAnualServicio;
import ec.gob.dinardap.remanente.servicio.InventarioAnualServicio;
import ec.gob.dinardap.remanente.servicio.LibroServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;

@Named(value = "libroPropiedadCtrl")
@ViewScoped
public class LibroPropiedadCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventarioLibro;
    private String strBtnGuardar;

    private Boolean disableNuevoLibro;
    private Boolean disableDeleteLibro;
    private Boolean renderEdition;
    private Boolean onCreate;
    private Boolean onEdit;

    //Variables de negocio
    private Integer institucionId;
    private Integer año;
    private Libro libroSelected, libroAux;
    private Institucion institucion;
    private InventarioAnual inventarioAnual;

    //Listas
    private List<Libro> libroList;

    //
    @EJB
    private LibroServicio libroServicio;
    @EJB
    private InventarioAnualServicio inventarioAnualServicio;
    @EJB
    private EstadoInventarioAnualServicio estadoInventarioAnualServicio;

    @PostConstruct
    protected void init() {
        tituloInventarioLibro = "Inventario Libros Propiedad";

        disableNuevoLibro = Boolean.FALSE;
        disableDeleteLibro = Boolean.TRUE;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);

        institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        institucion = new Institucion();
        institucion.setInstitucionId(institucionId);
        obtenerLibro();
    }

    public void reloadLibro() {
        obtenerLibro();
        libroSelected = new Libro();
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        disableDeleteLibro = Boolean.TRUE;
        renderEdition = Boolean.FALSE;
    }

    public void obtenerLibro() {
        inventarioAnual = new InventarioAnual();
        inventarioAnual = inventarioAnualServicio.getInventarioPorInstitucionAño(institucionId, año);
        if (inventarioAnual.getInventarioAnualId() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Integer añoActual = calendar.get(Calendar.YEAR);
            Integer añoAnterior = calendar.get(Calendar.YEAR) - 1;
            if (añoActual.equals(año) || (añoAnterior).equals(año)) {
                inventarioAnual.setAnio(año);
                inventarioAnual.setInstitucion(institucion);
                inventarioAnualServicio.create(inventarioAnual);
                inventarioAnual = inventarioAnualServicio.getInventarioPorInstitucionAño(institucionId, año);
                EstadoInventarioAnual estadoInventarioAnual = new EstadoInventarioAnual();
                //agregar enum
                estadoInventarioAnual.setEstado((short) 1);
                estadoInventarioAnual.setFechaRegistro(new Date());
                estadoInventarioAnual.setInventarioAnual(inventarioAnual);
                estadoInventarioAnualServicio.create(estadoInventarioAnual);
            } else {
                año = calendar.get(Calendar.YEAR);
                inventarioAnual = inventarioAnualServicio.getInventarioPorInstitucionAño(institucionId, año);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "Año no permitido."));
            }

        }
        libroList = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.PROPIEDAD.getTipoLibro());
    }

    public void onRowSelectLibro() {
        strBtnGuardar = getBundleEtiquetas("btnActualizar", null);
        libroAux = new Libro();
        libroAux = (Libro) SerializationUtils.clone(libroSelected);
        onEdit = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        disableDeleteLibro = Boolean.FALSE;
        renderEdition = Boolean.TRUE;
    }

    public void nuevoRegistroLibro() {
        strBtnGuardar = getBundleEtiquetas("btnGuardar", null);;

        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        disableDeleteLibro = Boolean.TRUE;
        renderEdition = Boolean.TRUE;

        libroSelected = new Libro();
    }

    public void borrarLibroSeleccionado() {
        libroSelected.setEstado(EstadoEnum.INACTIVO.getEstado());
        libroSelected.setFechaModificacionRegistro(new Date());
        libroServicio.update(libroSelected);
        reloadLibro();
    }

    public void guardarLibro() {
        TipoLibro tipoLibro = new TipoLibro(TipoLibroEnum.PROPIEDAD.getTipoLibro());
        libroSelected.setTipoLibro(tipoLibro);
        if (libroSelected.getFechaApertura().after(libroSelected.getFechaCierre())) {
            libroSelected=libroAux;
            obtenerLibro();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La Fecha de Apertura no puede ser posterior a la Fecha de Cierre."));
        } else {
            if (onCreate) {
                libroSelected.setFechaRegistro(new Date());
                libroSelected.setInventarioAnual(inventarioAnual);
                libroSelected.setEstado(EstadoEnum.ACTIVO.getEstado());
                libroServicio.create(libroSelected);
            } else if (onEdit) {
                libroSelected.setFechaModificacionRegistro(new Date());
                libroServicio.update(libroSelected);
            }
            reloadLibro();
        }
    }

    public void cancelarLibro() {
        libroSelected = new Libro();
        reloadLibro();
    }

    //Getters & Setters
    public Libro getLibroSelected() {
        return libroSelected;
    }

    public void setLibroSelected(Libro libroSelected) {
        this.libroSelected = libroSelected;
    }

    public String getTituloInventarioLibro() {
        return tituloInventarioLibro;
    }

    public void setTituloInventarioLibro(String tituloInventarioLibro) {
        this.tituloInventarioLibro = tituloInventarioLibro;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public List<Libro> getLibroList() {
        return libroList;
    }

    public void setLibroList(List<Libro> libroList) {
        this.libroList = libroList;
    }

    public Boolean getRenderEdition() {
        return renderEdition;
    }

    public void setRenderEdition(Boolean renderEdition) {
        this.renderEdition = renderEdition;
    }

    public String getStrBtnGuardar() {
        return strBtnGuardar;
    }

    public void setStrBtnGuardar(String strBtnGuardar) {
        this.strBtnGuardar = strBtnGuardar;
    }

    public Boolean getDisableNuevoLibro() {
        return disableNuevoLibro;
    }

    public void setDisableNuevoLibro(Boolean disableNuevoLibro) {
        this.disableNuevoLibro = disableNuevoLibro;
    }

    public Boolean getDisableDeleteLibro() {
        return disableDeleteLibro;
    }

    public void setDisableDeleteLibro(Boolean disableDeleteLibro) {
        this.disableDeleteLibro = disableDeleteLibro;
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

    public InventarioAnual getInventarioAnual() {
        return inventarioAnual;
    }

    public void setInventarioAnual(InventarioAnual inventarioAnual) {
        this.inventarioAnual = inventarioAnual;
    }

}
