package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.EstadoInventarioAnualEnum;
import ec.gob.dinardap.remanente.constante.TipoLibroEnum;
import ec.gob.dinardap.remanente.dao.TomoDao;
import ec.gob.dinardap.remanente.dto.ResumenLibroDTO;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;
import ec.gob.dinardap.remanente.modelo.Tomo;
import ec.gob.dinardap.remanente.servicio.EstadoInventarioAnualServicio;
import ec.gob.dinardap.remanente.servicio.InventarioAnualServicio;
import ec.gob.dinardap.remanente.servicio.TomoServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
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

@Named(value = "resumenLibrosCtrl")
@ViewScoped
public class ResumenLibrosCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventarioLibro;
    private String strBtnGuardar;

    //Variables de negocio
    private Integer institucionId;
    private Integer año;
    private Institucion institucion;
    private InventarioAnual inventarioAnual;
    private ResumenLibroDTO resumenLibroDTOPropiedad, resumenLibroDTOMercantil;
    private ResumenLibroDTO resumenLibroDTORepertorioPropiedad, resumenLibroDTORepertorioMercantil;
    private ResumenLibroDTO resumenLibroDTOIndiceGeneralPropiedad, resumenLibroDTOIndiceGeneralMercantil;
    private String nombreRegistrador;

    //Listas
    private List<Tomo> tomoListPropiedad, tomoListMercantil;
    private List<Tomo> tomoListRepertorioPropiedad, tomoListRepertorioMercantil;
    private List<Tomo> tomoListIndiceGeneralPropiedad, tomoListIndiceGeneralMercantil;
    private List<ResumenLibroDTO> resumeLibroDTOListPropiedad, resumeLibroDTOListMercantil;
    private List<ResumenLibroDTO> resumeLibroDTOListRepertorioPropiedad, resumeLibroDTOListRepertorioMercantil;
    private List<ResumenLibroDTO> resumeLibroDTOListIndiceGeneralPropiedad, resumeLibroDTOListIndiceGeneralMercantil;

    //
    @EJB
    private TomoServicio tomoServicio;
    @EJB
    private InventarioAnualServicio inventarioAnualServicio;
    @EJB
    private EstadoInventarioAnualServicio estadoInventarioAnualServicio;
    @EJB
    private TomoDao tomoDao;

    @PostConstruct
    protected void init() {
        tituloInventarioLibro = "Resumen Libros";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);

        institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        institucion = new Institucion();
        institucion.setInstitucionId(institucionId);
        obtenerLibrosTomos();
        obtenerResumen();

    }

    public void reloadLibro() {
        obtenerLibrosTomos();
        obtenerResumen();
    }

    public void actualizarRegistrador() {
        inventarioAnual.setNombreRegistrador(nombreRegistrador);
        inventarioAnualServicio.update(inventarioAnual);
        reloadLibro();
    }

    public void obtenerLibrosTomos() {
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
                estadoInventarioAnual.setEstado(EstadoInventarioAnualEnum.GENERADO.getEstadoInventarioAnual());
                estadoInventarioAnual.setFechaRegistro(new Date());
                estadoInventarioAnual.setInventarioAnual(inventarioAnual);
                estadoInventarioAnualServicio.create(estadoInventarioAnual);
            } else {
                año = calendar.get(Calendar.YEAR);
                inventarioAnual = inventarioAnualServicio.getInventarioPorInstitucionAño(institucionId, año);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "Año no permitido."));
            }

        }
        tomoListPropiedad = new ArrayList<>();
        tomoListPropiedad = tomoServicio.getTomosPorInstitucionAñoTipo(institucionId, inventarioAnual.getAnio(), TipoLibroEnum.PROPIEDAD.getTipoLibro());
        tomoListMercantil = new ArrayList<>();
        tomoListMercantil = tomoServicio.getTomosPorInstitucionAñoTipo(institucionId, inventarioAnual.getAnio(), TipoLibroEnum.MERCANTIL.getTipoLibro());
        tomoListRepertorioPropiedad = new ArrayList<>();
        tomoListRepertorioPropiedad = tomoServicio.getTomosPorInstitucionAñoTipo(institucionId, inventarioAnual.getAnio(), TipoLibroEnum.REPERTORIO_PROPIEDAD.getTipoLibro());
        tomoListRepertorioMercantil = new ArrayList<>();
        tomoListRepertorioMercantil = tomoServicio.getTomosPorInstitucionAñoTipo(institucionId, inventarioAnual.getAnio(), TipoLibroEnum.REPERTORIO_MERCANTIL.getTipoLibro());
        tomoListIndiceGeneralPropiedad = new ArrayList<>();
        tomoListIndiceGeneralPropiedad = tomoServicio.getTomosPorInstitucionAñoTipo(institucionId, inventarioAnual.getAnio(), TipoLibroEnum.INDICE_GENERAL_PROPIEDAD.getTipoLibro());
        tomoListIndiceGeneralMercantil = new ArrayList<>();
        tomoListIndiceGeneralMercantil = tomoServicio.getTomosPorInstitucionAñoTipo(institucionId, inventarioAnual.getAnio(), TipoLibroEnum.INDICE_GENERAL_MERCANTIL.getTipoLibro());
    }

    public void obtenerResumen() {
        resumenLibroDTOPropiedad = new ResumenLibroDTO();
        resumenLibroDTOPropiedad = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.PROPIEDAD.getTipoLibro());
        resumeLibroDTOListPropiedad = new ArrayList<>();
        resumeLibroDTOListPropiedad.add(resumenLibroDTOPropiedad);
        
        resumenLibroDTOMercantil= new ResumenLibroDTO();
        resumenLibroDTOMercantil = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.MERCANTIL.getTipoLibro());
        resumeLibroDTOListMercantil = new ArrayList<>();
        resumeLibroDTOListMercantil.add(resumenLibroDTOMercantil);

        resumenLibroDTORepertorioPropiedad= new ResumenLibroDTO();
        resumenLibroDTORepertorioPropiedad = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.REPERTORIO_PROPIEDAD.getTipoLibro());
        resumeLibroDTOListRepertorioPropiedad = new ArrayList<>();
        resumeLibroDTOListRepertorioPropiedad.add(resumenLibroDTORepertorioPropiedad);

        resumenLibroDTORepertorioMercantil= new ResumenLibroDTO();
        resumenLibroDTORepertorioMercantil = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.REPERTORIO_MERCANTIL.getTipoLibro());
        resumeLibroDTOListRepertorioMercantil = new ArrayList<>();
        resumeLibroDTOListRepertorioMercantil.add(resumenLibroDTORepertorioMercantil);

        resumenLibroDTOIndiceGeneralPropiedad= new ResumenLibroDTO();
        resumenLibroDTOIndiceGeneralPropiedad = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.INDICE_GENERAL_PROPIEDAD.getTipoLibro());
        resumeLibroDTOListIndiceGeneralPropiedad = new ArrayList<>();
        resumeLibroDTOListIndiceGeneralPropiedad.add(resumenLibroDTOIndiceGeneralPropiedad);

        resumenLibroDTOIndiceGeneralMercantil= new ResumenLibroDTO();
        resumenLibroDTOIndiceGeneralMercantil = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.INDICE_GENERAL_MERCANTIL.getTipoLibro());
        resumeLibroDTOListIndiceGeneralMercantil = new ArrayList<>();
        resumeLibroDTOListIndiceGeneralMercantil.add(resumenLibroDTOIndiceGeneralMercantil);

        nombreRegistrador = resumenLibroDTOPropiedad.getNombreRegistrador();
    }

    //Getters & Setters
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

    public String getStrBtnGuardar() {
        return strBtnGuardar;
    }

    public void setStrBtnGuardar(String strBtnGuardar) {
        this.strBtnGuardar = strBtnGuardar;
    }

    public ResumenLibroDTO getResumenLibroDTOPropiedad() {
        return resumenLibroDTOPropiedad;
    }

    public void setResumenLibroDTOPropiedad(ResumenLibroDTO resumenLibroDTOPropiedad) {
        this.resumenLibroDTOPropiedad = resumenLibroDTOPropiedad;
    }

    public ResumenLibroDTO getResumenLibroDTOMercantil() {
        return resumenLibroDTOMercantil;
    }

    public void setResumenLibroDTOMercantil(ResumenLibroDTO resumenLibroDTOMercantil) {
        this.resumenLibroDTOMercantil = resumenLibroDTOMercantil;
    }

    public ResumenLibroDTO getResumenLibroDTORepertorioPropiedad() {
        return resumenLibroDTORepertorioPropiedad;
    }

    public void setResumenLibroDTORepertorioPropiedad(ResumenLibroDTO resumenLibroDTORepertorioPropiedad) {
        this.resumenLibroDTORepertorioPropiedad = resumenLibroDTORepertorioPropiedad;
    }

    public ResumenLibroDTO getResumenLibroDTORepertorioMercantil() {
        return resumenLibroDTORepertorioMercantil;
    }

    public void setResumenLibroDTORepertorioMercantil(ResumenLibroDTO resumenLibroDTORepertorioMercantil) {
        this.resumenLibroDTORepertorioMercantil = resumenLibroDTORepertorioMercantil;
    }

    public ResumenLibroDTO getResumenLibroDTOIndiceGeneralPropiedad() {
        return resumenLibroDTOIndiceGeneralPropiedad;
    }

    public void setResumenLibroDTOIndiceGeneralPropiedad(ResumenLibroDTO resumenLibroDTOIndiceGeneralPropiedad) {
        this.resumenLibroDTOIndiceGeneralPropiedad = resumenLibroDTOIndiceGeneralPropiedad;
    }

    public ResumenLibroDTO getResumenLibroDTOIndiceGeneralMercantil() {
        return resumenLibroDTOIndiceGeneralMercantil;
    }

    public void setResumenLibroDTOIndiceGeneralMercantil(ResumenLibroDTO resumenLibroDTOIndiceGeneralMercantil) {
        this.resumenLibroDTOIndiceGeneralMercantil = resumenLibroDTOIndiceGeneralMercantil;
    }

    public List<Tomo> getTomoListPropiedad() {
        return tomoListPropiedad;
    }

    public void setTomoListPropiedad(List<Tomo> tomoListPropiedad) {
        this.tomoListPropiedad = tomoListPropiedad;
    }

    public List<Tomo> getTomoListMercantil() {
        return tomoListMercantil;
    }

    public void setTomoListMercantil(List<Tomo> tomoListMercantil) {
        this.tomoListMercantil = tomoListMercantil;
    }

    public List<Tomo> getTomoListRepertorioPropiedad() {
        return tomoListRepertorioPropiedad;
    }

    public void setTomoListRepertorioPropiedad(List<Tomo> tomoListRepertorioPropiedad) {
        this.tomoListRepertorioPropiedad = tomoListRepertorioPropiedad;
    }

    public List<Tomo> getTomoListRepertorioMercantil() {
        return tomoListRepertorioMercantil;
    }

    public void setTomoListRepertorioMercantil(List<Tomo> tomoListRepertorioMercantil) {
        this.tomoListRepertorioMercantil = tomoListRepertorioMercantil;
    }

    public List<Tomo> getTomoListIndiceGeneralPropiedad() {
        return tomoListIndiceGeneralPropiedad;
    }

    public void setTomoListIndiceGeneralPropiedad(List<Tomo> tomoListIndiceGeneralPropiedad) {
        this.tomoListIndiceGeneralPropiedad = tomoListIndiceGeneralPropiedad;
    }

    public List<Tomo> getTomoListIndiceGeneralMercantil() {
        return tomoListIndiceGeneralMercantil;
    }

    public void setTomoListIndiceGeneralMercantil(List<Tomo> tomoListIndiceGeneralMercantil) {
        this.tomoListIndiceGeneralMercantil = tomoListIndiceGeneralMercantil;
    }

    public List<ResumenLibroDTO> getResumeLibroDTOListPropiedad() {
        return resumeLibroDTOListPropiedad;
    }

    public void setResumeLibroDTOListPropiedad(List<ResumenLibroDTO> resumeLibroDTOListPropiedad) {
        this.resumeLibroDTOListPropiedad = resumeLibroDTOListPropiedad;
    }

    public List<ResumenLibroDTO> getResumeLibroDTOListMercantil() {
        return resumeLibroDTOListMercantil;
    }

    public void setResumeLibroDTOListMercantil(List<ResumenLibroDTO> resumeLibroDTOListMercantil) {
        this.resumeLibroDTOListMercantil = resumeLibroDTOListMercantil;
    }

    public List<ResumenLibroDTO> getResumeLibroDTOListRepertorioPropiedad() {
        return resumeLibroDTOListRepertorioPropiedad;
    }

    public void setResumeLibroDTOListRepertorioPropiedad(List<ResumenLibroDTO> resumeLibroDTOListRepertorioPropiedad) {
        this.resumeLibroDTOListRepertorioPropiedad = resumeLibroDTOListRepertorioPropiedad;
    }

    public List<ResumenLibroDTO> getResumeLibroDTOListRepertorioMercantil() {
        return resumeLibroDTOListRepertorioMercantil;
    }

    public void setResumeLibroDTOListRepertorioMercantil(List<ResumenLibroDTO> resumeLibroDTOListRepertorioMercantil) {
        this.resumeLibroDTOListRepertorioMercantil = resumeLibroDTOListRepertorioMercantil;
    }

    public List<ResumenLibroDTO> getResumeLibroDTOListIndiceGeneralPropiedad() {
        return resumeLibroDTOListIndiceGeneralPropiedad;
    }

    public void setResumeLibroDTOListIndiceGeneralPropiedad(List<ResumenLibroDTO> resumeLibroDTOListIndiceGeneralPropiedad) {
        this.resumeLibroDTOListIndiceGeneralPropiedad = resumeLibroDTOListIndiceGeneralPropiedad;
    }

    public List<ResumenLibroDTO> getResumeLibroDTOListIndiceGeneralMercantil() {
        return resumeLibroDTOListIndiceGeneralMercantil;
    }

    public void setResumeLibroDTOListIndiceGeneralMercantil(List<ResumenLibroDTO> resumeLibroDTOListIndiceGeneralMercantil) {
        this.resumeLibroDTOListIndiceGeneralMercantil = resumeLibroDTOListIndiceGeneralMercantil;
    }

    public String getNombreRegistrador() {
        return nombreRegistrador;
    }

    public void setNombreRegistrador(String nombreRegistrador) {
        this.nombreRegistrador = nombreRegistrador;
    }

}
