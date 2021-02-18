package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.EstadoInventarioAnualEnum;
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
import java.time.Clock;
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

@Named(value = "tomoCtrl")
@ViewScoped
public class TomoCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventarioTomoPropiedad, tituloInventarioTomoMercantil;
    private String tituloInventarioTomoRepertorioPropiedad, tituloInventarioTomoRepertorioMercantil;
    private String tituloInventarioTomoIndiceGeneralPropiedad, tituloInventarioTomoIndiceGeneralMercantil;
    private String strBtnGuardar;

    private Boolean disableNuevoTomo;
    private Boolean disableDeleteTomo;
    private Boolean disableActualizarTomo;
    private Boolean renderEdition;
    private Boolean onCreate;
    private Boolean onEdit;

    //Variables de negocio
    private Integer institucionId;
    private Integer año;
    private Libro libroSelected;    
    private Institucion institucion;
    private Tomo tomoSelected, ultimoTomo;
    private InventarioAnual inventarioAnual;
    
    //Listas
    private List<Libro> libroListPropiedad, libroListMercantil;
    private List<Libro> libroListRepertorioPropiedad, libroListRepertorioMercantil;
    private List<Libro> libroListIndiceGeneralPropiedad, libroListIndiceGeneralMercantil;
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
        tituloInventarioTomoPropiedad = "Inventario Libros y Tomos Propiedad";
        tituloInventarioTomoMercantil = "Inventario Libros y Tomos Mercantil";
        tituloInventarioTomoRepertorioPropiedad = "Inventario Libros y Tomos Repertorio Propiedad";
        tituloInventarioTomoRepertorioMercantil = "Inventario Libros y Tomos Repertorio Mercantil";
        tituloInventarioTomoIndiceGeneralPropiedad = "Inventario Libros y Tomos Índice General Propiedad";
        tituloInventarioTomoIndiceGeneralMercantil = "Inventario Libros y Tomos Índice General Mercantil";

        disableNuevoTomo = Boolean.TRUE;
        disableDeleteTomo = Boolean.TRUE;
        disableActualizarTomo = Boolean.FALSE;

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
        disableActualizarTomo = Boolean.FALSE;
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
        libroListPropiedad = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.PROPIEDAD.getTipoLibro());
        libroListMercantil = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.MERCANTIL.getTipoLibro());
        libroListRepertorioPropiedad = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.REPERTORIO_PROPIEDAD.getTipoLibro());
        libroListRepertorioMercantil = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.REPERTORIO_MERCANTIL.getTipoLibro());
        libroListIndiceGeneralPropiedad = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.INDICE_GENERAL_PROPIEDAD.getTipoLibro());
        libroListIndiceGeneralMercantil = libroServicio.getLibrosActivosPorInventarioTipo(inventarioAnual.getInventarioAnualId(), TipoLibroEnum.INDICE_GENERAL_MERCANTIL.getTipoLibro());
    }
    
    public void obtenerTomo() {        
        tomoList = new ArrayList<Tomo>();
        tomoList=tomoServicio.getTomosActivosPorLibro(libroSelected.getLibroId());        
    }
    
    public void validarEstadoInventario() {
        Short ultimoEstadoInventario = inventarioAnual.getEstadoInventarioAnualList().get(inventarioAnual.getEstadoInventarioAnualList().size() - 1).getEstado();
        if (ultimoEstadoInventario.equals(EstadoInventarioAnualEnum.APROBADO.getEstadoInventarioAnual()) ||
            ultimoEstadoInventario.equals(EstadoInventarioAnualEnum.COMPLETO.getEstadoInventarioAnual())) {
            disableNuevoTomo = Boolean.TRUE;
            disableDeleteTomo = Boolean.TRUE;
            disableActualizarTomo = Boolean.TRUE;
        }
    }
    
    public void onRowSelectLibro() {
        //strBtnGuardar = getBundleEtiquetas("btnActualizar", null);
        obtenerTomo();
        //onEdit = Boolean.TRUE;
        //onCreate = Boolean.FALSE;
        //disableDeleteTomo = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        disableNuevoTomo = Boolean.FALSE;
        validarEstadoInventario();
    }
    
    public void onRowSelectTomo() {
        strBtnGuardar = getBundleEtiquetas("btnActualizar", null);

        onEdit = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        disableDeleteTomo = Boolean.FALSE;        
        renderEdition = Boolean.TRUE;
        validarEstadoInventario();
    }
    
    public void nuevoRegistroTomo() {
        strBtnGuardar = getBundleEtiquetas("btnGuardar", null);;

        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        disableDeleteTomo = Boolean.TRUE;
        renderEdition = Boolean.TRUE;
        
        tomoSelected = new Tomo();
        ultimoTomo=tomoServicio.getUltimoTomoPorLibro(libroSelected.getLibroId());
        if(ultimoTomo.getTomoId()!=null){
            tomoSelected.setNumero(ultimoTomo.getNumero()+1);
            tomoSelected.setFojaInicio(ultimoTomo.getFojaFin()+1);
        }else{
            tomoSelected.setNumero(1);
            tomoSelected.setFojaInicio(1);
        }
        if(ultimoTomo.getNumeroInscripcionInicio()!=null){
            tomoSelected.setNumeroInscripcionInicio(ultimoTomo.getNumeroInscripcionFin()+1);
        }
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
    
    public void calcularTotalInscripciones(){
        if(tomoSelected.getNumeroInscripcionInicio()!=null && tomoSelected.getNumeroInscripcionFin()!=null){
            Integer total = tomoSelected.getNumeroInscripcionFin()-tomoSelected.getNumeroInscripcionInicio()+1;
            tomoSelected.setTotalInscripcionesContenidas(total);
            System.out.println("total inscripciones: "+total);
        }
    }
    
    public void calcularTotalHojas(){
        if(tomoSelected.getFojaInicio()!=null && tomoSelected.getFojaFin()!=null){
            Integer total = (tomoSelected.getFojaFin()-tomoSelected.getFojaInicio()+1)*2;
            tomoSelected.setNumeroTotalHojas(total);
            System.out.println("total hojas: "+total);
        }
    }

    //Getters & Setters

    public Libro getLibroSelected() {
        return libroSelected;
    }

    public void setLibroSelected(Libro libroSelected) {
        this.libroSelected = libroSelected;
    }

    public String getTituloInventarioTomoPropiedad() {
        return tituloInventarioTomoPropiedad;
    }

    public void setTituloInventarioTomoPropiedad(String tituloInventarioTomoPropiedad) {
        this.tituloInventarioTomoPropiedad = tituloInventarioTomoPropiedad;
    }

    public String getTituloInventarioTomoMercantil() {
        return tituloInventarioTomoMercantil;
    }

    public void setTituloInventarioTomoMercantil(String tituloInventarioTomoMercantil) {
        this.tituloInventarioTomoMercantil = tituloInventarioTomoMercantil;
    }

    public String getTituloInventarioTomoRepertorioPropiedad() {
        return tituloInventarioTomoRepertorioPropiedad;
    }

    public void setTituloInventarioTomoRepertorioPropiedad(String tituloInventarioTomoRepertorioPropiedad) {
        this.tituloInventarioTomoRepertorioPropiedad = tituloInventarioTomoRepertorioPropiedad;
    }

    public String getTituloInventarioTomoRepertorioMercantil() {
        return tituloInventarioTomoRepertorioMercantil;
    }

    public void setTituloInventarioTomoRepertorioMercantil(String tituloInventarioTomoRepertorioMercantil) {
        this.tituloInventarioTomoRepertorioMercantil = tituloInventarioTomoRepertorioMercantil;
    }

    public String getTituloInventarioTomoIndiceGeneralPropiedad() {
        return tituloInventarioTomoIndiceGeneralPropiedad;
    }

    public void setTituloInventarioTomoIndiceGeneralPropiedad(String tituloInventarioTomoIndiceGeneralPropiedad) {
        this.tituloInventarioTomoIndiceGeneralPropiedad = tituloInventarioTomoIndiceGeneralPropiedad;
    }

    public String getTituloInventarioTomoIndiceGeneralMercantil() {
        return tituloInventarioTomoIndiceGeneralMercantil;
    }

    public void setTituloInventarioTomoIndiceGeneralMercantil(String tituloInventarioTomoIndiceGeneralMercantil) {
        this.tituloInventarioTomoIndiceGeneralMercantil = tituloInventarioTomoIndiceGeneralMercantil;
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

    public Boolean getDisableActualizarTomo() {
        return disableActualizarTomo;
    }

    public void setDisableActualizarTomo(Boolean disableActualizarTomo) {
        this.disableActualizarTomo = disableActualizarTomo;
    }
    
}
