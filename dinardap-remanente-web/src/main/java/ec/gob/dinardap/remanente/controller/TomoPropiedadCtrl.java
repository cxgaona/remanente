package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.TipoLibroEnum;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;
import ec.gob.dinardap.remanente.modelo.Libro;
import ec.gob.dinardap.remanente.modelo.Tomo;
import ec.gob.dinardap.remanente.servicio.EstadoInventarioAnualServicio;
import ec.gob.dinardap.remanente.servicio.InventarioAnualServicio;
import ec.gob.dinardap.remanente.servicio.LibroServicio;
import ec.gob.dinardap.remanente.servicio.TomoServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.util.constante.EstadoEnum;
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

@Named(value = "tomoPropiedadCtrl")
@ViewScoped
public class TomoPropiedadCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventarioLibro;
    private String strBtnGuardar;

    private Boolean disableNuevoTomo;
    private Boolean disableDeleteTomo;
    private Boolean renderEdition;
    private Boolean onCreate;
    private Boolean onEdit;

    //Variables de negocio
    private Integer institucionId;
    private Integer año;
    private Libro libroSelected;    
    private Institucion institucion;
    private Tomo tomoSelected;
    private InventarioAnual inventarioAnual;
    
    //Listas
    private List<Libro> libroList;
    private List<Tomo> tomoList;

    //
    @EJB
    private LibroServicio libroServicio; 
    @EJB
    private TomoServicio tomoServicio;
    @EJB
    private InventarioAnualServicio inventarioAnualServicio;
    @EJB
    private EstadoInventarioAnualServicio estadoInventarioAnualServicio;
    
    @PostConstruct
    protected void init() {
        tituloInventarioLibro = "Inventario Libros y Tomos Propiedad";

        disableNuevoTomo = Boolean.TRUE;
        disableDeleteTomo = Boolean.TRUE;

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
        tomoSelected = new Tomo();
        tomoList = new ArrayList<Tomo>();
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        disableNuevoTomo = Boolean.TRUE;
        disableDeleteTomo = Boolean.TRUE;
        renderEdition = Boolean.FALSE;
    }
    
    public void obtenerLibro() {
        inventarioAnual = new InventarioAnual();
        inventarioAnual = inventarioAnualServicio.getInventarioPorInstitucionAño(institucionId, año);
        if (inventarioAnual.getInventarioAnualId() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Integer añoActual = calendar.get(Calendar.YEAR);
            Integer añoAnterior = calendar.get(Calendar.YEAR)-1;
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
            }else{
                año = calendar.get(Calendar.YEAR);
                inventarioAnual = inventarioAnualServicio.getInventarioPorInstitucionAño(institucionId, año);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "Año no permitido."));
            }

        }
        libroList = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.PROPIEDAD.getTipoLibro());
    }
    
    public void obtenerTomo() {        
        tomoList = new ArrayList<Tomo>();
        tomoList=tomoServicio.getTomosActivosPorLibro(libroSelected.getLibroId());        
    }
    
    public void onRowSelectLibro() {
        //strBtnGuardar = getBundleEtiquetas("btnActualizar", null);
        obtenerTomo();
        //onEdit = Boolean.TRUE;
        //onCreate = Boolean.FALSE;
        //disableDeleteTomo = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        disableNuevoTomo = Boolean.FALSE;
    }
    
    public void onRowSelectTomo() {
        strBtnGuardar = getBundleEtiquetas("btnActualizar", null);

        onEdit = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        disableDeleteTomo = Boolean.FALSE;        
        renderEdition = Boolean.TRUE;
    }
    
    public void nuevoRegistroTomo() {
        strBtnGuardar = getBundleEtiquetas("btnGuardar", null);;

        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        disableDeleteTomo = Boolean.TRUE;
        renderEdition = Boolean.TRUE;

        tomoSelected = new Tomo();
    }
    
    public void borrarTomoSeleccionado() {
        tomoSelected.setEstado(EstadoEnum.INACTIVO.getEstado());
        tomoSelected.setFechaModificacionRegistro(new Date());
        tomoServicio.update(tomoSelected);
        //reloadLibro();
        tomoSelected = new Tomo();
        obtenerTomo();
        disableDeleteTomo = Boolean.TRUE;
        renderEdition = Boolean.FALSE;
    }
    
    public void guardarTomo() {
        tomoSelected.setLibro(libroSelected);

        if (onCreate) {
            tomoSelected.setFechaRegistro(new Date());
            tomoSelected.setEstado(EstadoEnum.ACTIVO.getEstado());
            tomoServicio.create(tomoSelected);
        } else if (onEdit) {
            tomoSelected.setFechaModificacionRegistro(new Date());
            tomoServicio.update(tomoSelected);
        }

        //reloadLibro();
        tomoSelected = new Tomo();
        renderEdition = Boolean.FALSE;
        disableDeleteTomo = Boolean.TRUE;
        obtenerTomo();
    }

    public void cancelarTomo() {
        //libroSelected = new Libro();
        tomoSelected = new Tomo();
        renderEdition = Boolean.FALSE;
        disableDeleteTomo = Boolean.TRUE;
        obtenerTomo();
        //reloadLibro();        
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

    public Boolean getDisableNuevoTomo() {
        return disableNuevoTomo;
    }

    public void setDisableNuevoTomo(Boolean disableNuevoTomo) {
        this.disableNuevoTomo = disableNuevoTomo;
    }

    public Boolean getDisableDeleteTomo() {
        return disableDeleteTomo;
    }

    public void setDisableDeleteTomo(Boolean disableDeleteTomo) {
        this.disableDeleteTomo = disableDeleteTomo;
    }

    public Tomo getTomoSelected() {
        return tomoSelected;
    }

    public void setTomoSelected(Tomo tomoSelected) {
        this.tomoSelected = tomoSelected;
    }

    public List<Tomo> getTomoList() {
        return tomoList;
    }

    public void setTomoList(List<Tomo> tomoList) {
        this.tomoList = tomoList;
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
    
}
