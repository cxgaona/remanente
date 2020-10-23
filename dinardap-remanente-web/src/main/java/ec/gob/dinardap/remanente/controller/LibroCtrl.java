package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.EstadoInventarioAnualEnum;
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

@Named(value = "libroCtrl")
@ViewScoped
public class LibroCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventarioLibroPropiedad, tituloInventarioLibroMercantil;
    private String tituloInventarioLibroRepertorioPropiedad, tituloInventarioLibroRepertorioMercantil;
    private String tituloInventarioLibroIndiceGeneralPropiedad, tituloInventarioLibroIndiceGeneralMercantil;
    private String strBtnGuardar;

    private Boolean disableNuevoLibro;
    private Boolean disableDeleteLibro;
    private Boolean disableActualizarLibro;
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
    private List<Libro> libroListPropiedad, libroListMercantil;
    private List<Libro> libroListRepertorioPropiedad, libroListRepertorioMercantil;
    private List<Libro> libroListIndiceGeneralPropiedad, libroListIndiceGeneralMercantil;

    //
    @EJB
    private LibroServicio libroServicio;
    @EJB
    private InventarioAnualServicio inventarioAnualServicio;
    @EJB
    private EstadoInventarioAnualServicio estadoInventarioAnualServicio;

    @PostConstruct
    protected void init() {
        tituloInventarioLibroPropiedad = "Inventario Libros Propiedad";
        tituloInventarioLibroMercantil = "Inventario Libros Mercantil";
        tituloInventarioLibroRepertorioPropiedad = "Inventario Libros Repertorio Propiedad";
        tituloInventarioLibroRepertorioMercantil = "Inventario Libros Repertorio Mercantil";
        tituloInventarioLibroIndiceGeneralPropiedad = "Inventario Libros Índice General Propiedad";
        tituloInventarioLibroIndiceGeneralMercantil = "Inventario Libros Índice General Mercantil";

        disableNuevoLibro = Boolean.FALSE;
        disableDeleteLibro = Boolean.TRUE;
        disableActualizarLibro = Boolean.FALSE;

        libroAux = new Libro();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);

        institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        institucion = new Institucion();
        institucion.setInstitucionId(institucionId);
        obtenerLibro();
        validarEstadoInventario();
    }

    public void reloadLibro() {
        obtenerLibro();
        libroSelected = new Libro();
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        disableNuevoLibro = Boolean.FALSE;
        disableDeleteLibro = Boolean.TRUE;
        disableActualizarLibro = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        validarEstadoInventario();
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
                EstadoInventarioAnual estadoInventarioAnual = new EstadoInventarioAnual();
                estadoInventarioAnual.setEstado(EstadoInventarioAnualEnum.GENERADO.getEstadoInventarioAnual());
                estadoInventarioAnual.setFechaRegistro(new Date());
                estadoInventarioAnual.setInventarioAnual(inventarioAnual);
                estadoInventarioAnualServicio.create(estadoInventarioAnual);
                inventarioAnual = inventarioAnualServicio.getInventarioPorInstitucionAño(institucionId, año);
            } else {
                año = calendar.get(Calendar.YEAR);
                inventarioAnual = inventarioAnualServicio.getInventarioPorInstitucionAño(institucionId, año);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "Año no permitido."));
            }

        }
        libroListPropiedad = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.PROPIEDAD.getTipoLibro());
        libroListMercantil = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.MERCANTIL.getTipoLibro());
        libroListRepertorioPropiedad = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.REPERTORIO_PROPIEDAD.getTipoLibro());
        libroListRepertorioMercantil = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.REPERTORIO_MERCANTIL.getTipoLibro());
        libroListIndiceGeneralPropiedad = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.INDICE_GENERAL_PROPIEDAD.getTipoLibro());
        libroListIndiceGeneralMercantil = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.INDICE_GENERAL_MERCANTIL.getTipoLibro());
    }

    public void validarEstadoInventario() {
        Short ultimoEstadoInventario = inventarioAnual.getEstadoInventarioAnualList().get(inventarioAnual.getEstadoInventarioAnualList().size() - 1).getEstado();
        if (ultimoEstadoInventario.equals(EstadoInventarioAnualEnum.APROBADO.getEstadoInventarioAnual())
                || ultimoEstadoInventario.equals(EstadoInventarioAnualEnum.COMPLETO.getEstadoInventarioAnual())) {
            disableNuevoLibro = Boolean.TRUE;
            disableDeleteLibro = Boolean.TRUE;
            disableActualizarLibro = Boolean.TRUE;
        }
    }

    public void onRowSelectLibro() {
        strBtnGuardar = getBundleEtiquetas("btnActualizar", null);
        libroAux = new Libro();
        libroAux = (Libro) SerializationUtils.clone(libroSelected);
        onEdit = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        disableDeleteLibro = Boolean.FALSE;
        renderEdition = Boolean.TRUE;
        validarEstadoInventario();
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

    public void guardarLibroPropiedad() {
        TipoLibro tipoLibro = new TipoLibro(TipoLibroEnum.PROPIEDAD.getTipoLibro());
        libroSelected.setTipoLibro(tipoLibro);
        guardarLibro();
    }

    public void guardarLibroMercantil() {
        TipoLibro tipoLibro = new TipoLibro(TipoLibroEnum.MERCANTIL.getTipoLibro());
        libroSelected.setTipoLibro(tipoLibro);
        guardarLibro();
    }

    public void guardarLibroRepertorioPropiedad() {
        TipoLibro tipoLibro = new TipoLibro(TipoLibroEnum.REPERTORIO_PROPIEDAD.getTipoLibro());
        libroSelected.setTipoLibro(tipoLibro);
        guardarLibro();
    }

    public void guardarLibroRepertorioMercantil() {
        TipoLibro tipoLibro = new TipoLibro(TipoLibroEnum.REPERTORIO_MERCANTIL.getTipoLibro());
        libroSelected.setTipoLibro(tipoLibro);
        guardarLibro();
    }

    public void guardarLibroIndiceGeneralPropiedad() {
        TipoLibro tipoLibro = new TipoLibro(TipoLibroEnum.INDICE_GENERAL_PROPIEDAD.getTipoLibro());
        libroSelected.setTipoLibro(tipoLibro);
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

    public void guardarLibroIndiceGeneralMercantil() {
        TipoLibro tipoLibro = new TipoLibro(TipoLibroEnum.INDICE_GENERAL_MERCANTIL.getTipoLibro());
        libroSelected.setTipoLibro(tipoLibro);
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

    public void guardarLibro() {
        if (libroSelected.getFechaApertura().after(libroSelected.getFechaCierre())) {
            libroSelected = libroAux;
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

    public String getTituloInventarioLibroPropiedad() {
        return tituloInventarioLibroPropiedad;
    }

    public void setTituloInventarioLibroPropiedad(String tituloInventarioLibroPropiedad) {
        this.tituloInventarioLibroPropiedad = tituloInventarioLibroPropiedad;
    }

    public String getTituloInventarioLibroMercantil() {
        return tituloInventarioLibroMercantil;
    }

    public void setTituloInventarioLibroMercantil(String tituloInventarioLibroMercantil) {
        this.tituloInventarioLibroMercantil = tituloInventarioLibroMercantil;
    }

    public String getTituloInventarioLibroRepertorioPropiedad() {
        return tituloInventarioLibroRepertorioPropiedad;
    }

    public void setTituloInventarioLibroRepertorioPropiedad(String tituloInventarioLibroRepertorioPropiedad) {
        this.tituloInventarioLibroRepertorioPropiedad = tituloInventarioLibroRepertorioPropiedad;
    }

    public String getTituloInventarioLibroRepertorioMercantil() {
        return tituloInventarioLibroRepertorioMercantil;
    }

    public void setTituloInventarioLibroRepertorioMercantil(String tituloInventarioLibroRepertorioMercantil) {
        this.tituloInventarioLibroRepertorioMercantil = tituloInventarioLibroRepertorioMercantil;
    }

    public String getTituloInventarioLibroIndiceGeneralPropiedad() {
        return tituloInventarioLibroIndiceGeneralPropiedad;
    }

    public void setTituloInventarioLibroIndiceGeneralPropiedad(String tituloInventarioLibroIndiceGeneralPropiedad) {
        this.tituloInventarioLibroIndiceGeneralPropiedad = tituloInventarioLibroIndiceGeneralPropiedad;
    }

    public String getTituloInventarioLibroIndiceGeneralMercantil() {
        return tituloInventarioLibroIndiceGeneralMercantil;
    }

    public void setTituloInventarioLibroIndiceGeneralMercantil(String tituloInventarioLibroIndiceGeneralMercantil) {
        this.tituloInventarioLibroIndiceGeneralMercantil = tituloInventarioLibroIndiceGeneralMercantil;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public List<Libro> getLibroListPropiedad() {
        return libroListPropiedad;
    }

    public void setLibroListPropiedad(List<Libro> libroListPropiedad) {
        this.libroListPropiedad = libroListPropiedad;
    }

    public List<Libro> getLibroListMercantil() {
        return libroListMercantil;
    }

    public void setLibroListMercantil(List<Libro> libroListMercantil) {
        this.libroListMercantil = libroListMercantil;
    }

    public List<Libro> getLibroListRepertorioPropiedad() {
        return libroListRepertorioPropiedad;
    }

    public void setLibroListRepertorioPropiedad(List<Libro> libroListRepertorioPropiedad) {
        this.libroListRepertorioPropiedad = libroListRepertorioPropiedad;
    }

    public List<Libro> getLibroListRepertorioMercantil() {
        return libroListRepertorioMercantil;
    }

    public void setLibroListRepertorioMercantil(List<Libro> libroListRepertorioMercantil) {
        this.libroListRepertorioMercantil = libroListRepertorioMercantil;
    }

    public List<Libro> getLibroListIndiceGeneralPropiedad() {
        return libroListIndiceGeneralPropiedad;
    }

    public void setLibroListIndiceGeneralPropiedad(List<Libro> libroListIndiceGeneralPropiedad) {
        this.libroListIndiceGeneralPropiedad = libroListIndiceGeneralPropiedad;
    }

    public List<Libro> getLibroListIndiceGeneralMercantil() {
        return libroListIndiceGeneralMercantil;
    }

    public void setLibroListIndiceGeneralMercantil(List<Libro> libroListIndiceGeneralMercantil) {
        this.libroListIndiceGeneralMercantil = libroListIndiceGeneralMercantil;
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

    public Boolean getDisableActualizarLibro() {
        return disableActualizarLibro;
    }

    public void setDisableActualizarLibro(Boolean disableActualizarLibro) {
        this.disableActualizarLibro = disableActualizarLibro;
    }

}
