package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.EstadoInventarioAnualEnum;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.constante.PerfilEnum;
import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.remanente.constante.TipoLibroEnum;
import ec.gob.dinardap.remanente.dao.TomoDao;
import ec.gob.dinardap.remanente.dto.ResumenLibroDTO;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;
import ec.gob.dinardap.remanente.modelo.Tomo;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.EstadoInventarioAnualServicio;
import ec.gob.dinardap.remanente.servicio.InventarioAnualServicio;
import ec.gob.dinardap.remanente.servicio.TomoServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
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

@Named(value = "revisarResumenLibrosCtrl")
@ViewScoped
public class RevisarResumenLibrosCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventarioLibro;
    private String strBtnGuardar;
    private Boolean disableSolicitarRevision;
    private Boolean renderPropiedad;
    private Boolean renderMercantil;

    //Variables de negocio
    private Integer usuarioId;
    private Integer direccionRegionalId;
    private String nombreDireccionRegional;
    private String nombreInstitucion;
    private Integer institucionId;
    private Integer año;
    private InventarioAnual inventarioAnual;
    private ResumenLibroDTO resumenLibroDTOPropiedad, resumenLibroDTOMercantil;
    private ResumenLibroDTO resumenLibroDTORepertorioPropiedad, resumenLibroDTORepertorioMercantil;
    private ResumenLibroDTO resumenLibroDTOIndiceGeneralPropiedad, resumenLibroDTOIndiceGeneralMercantil;
    private String nombreRegistrador;
    private Boolean btnActivated;
    private Boolean displayComment;
    private Boolean disabledBtnReload;
    private Boolean disableBtnDescargarArchivo;
    private String comentariosRechazo;
    private Institucion institucionSelected;
    private Institucion institucionNotificacion;
    private SftpDto sftpDto;

    //Listas
    private List<Institucion> institucionList;
    private List<Tomo> tomoListPropiedad, tomoListMercantil;
    private List<Tomo> tomoListRepertorioPropiedad, tomoListRepertorioMercantil;
    private List<Tomo> tomoListIndiceGeneralPropiedad, tomoListIndiceGeneralMercantil;
    private List<ResumenLibroDTO> resumeLibroDTOListPropiedad, resumeLibroDTOListMercantil;
    private List<ResumenLibroDTO> resumeLibroDTOListRepertorioPropiedad, resumeLibroDTOListRepertorioMercantil;
    private List<ResumenLibroDTO> resumeLibroDTOListIndiceGeneralPropiedad, resumeLibroDTOListIndiceGeneralMercantil;
    private List<Usuario> usuarioListNotificacion;
    
    //
    @EJB
    private TomoServicio tomoServicio;
    @EJB
    private InventarioAnualServicio inventarioAnualServicio;
    @EJB
    private EstadoInventarioAnualServicio estadoInventarioAnualServicio;
    @EJB
    private TomoDao tomoDao;
    @EJB
    private InstitucionServicio institucionServicio;
    @EJB
    private ec.gob.dinardap.remanente.servicio.InstitucionServicio institucionRemanenteServicio;
    @EJB
    private UsuarioServicio usuarioServicio;
    @EJB
    private BandejaServicio bandejaServicio;
    @EJB
    private ParametroServicio parametroServicio;

    @PostConstruct
    protected void init() {
        //Session
        usuarioId = Integer.parseInt(getSessionVariable("usuarioId"));
        direccionRegionalId = Integer.parseInt(getSessionVariable("institucionId"));
        nombreDireccionRegional = institucionServicio.findByPk(direccionRegionalId).getNombre();

        //Inicialiación de Variables        
        nombreInstitucion = "Sin selección";
        institucionId = null;
        sftpDto = new SftpDto();

        comentariosRechazo = "";
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        disabledBtnReload = Boolean.TRUE;
        renderMercantil = Boolean.TRUE;
        renderPropiedad = Boolean.TRUE;
        disableBtnDescargarArchivo = Boolean.TRUE;

        institucionSelected = new Institucion();
        institucionList = new ArrayList<Institucion>();
        institucionList = institucionRemanenteServicio.getRegistroMixtoList(direccionRegionalId);
        institucionList.addAll(institucionRemanenteServicio.getRegistroMercantilList(direccionRegionalId));
        institucionList.addAll(institucionRemanenteServicio.getRegistroPropiedadList(direccionRegionalId));

        tituloInventarioLibro = "Revisar Resumen Libros";
        disableSolicitarRevision = Boolean.FALSE;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
    }

    public void onRowSelectInstitucion() {
        institucionId = institucionSelected.getInstitucionId();
        nombreInstitucion = institucionSelected.getNombre();
        renderMercantil = Boolean.TRUE;
        renderPropiedad = Boolean.TRUE;
        if (institucionSelected.getTipoInstitucion().getTipoInstitucionId().equals(TipoInstitucionEnum.REGISTRO_MERCANTIL.getTipoInstitucion())) {
            renderPropiedad = Boolean.FALSE;
        } else if (institucionSelected.getTipoInstitucion().getTipoInstitucionId().equals(TipoInstitucionEnum.REGISTRO_PROPIEDAD.getTipoInstitucion())) {
            renderMercantil = Boolean.FALSE;
        }
        reloadLibro();
        disabledBtnReload = Boolean.FALSE;

    }

    public void reloadLibro() {
        obtenerLibrosTomos();
        obtenerResumen();
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
                inventarioAnual.setInstitucion(institucionSelected);
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

        resumenLibroDTOMercantil = new ResumenLibroDTO();
        resumenLibroDTOMercantil = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.MERCANTIL.getTipoLibro());
        resumeLibroDTOListMercantil = new ArrayList<>();
        resumeLibroDTOListMercantil.add(resumenLibroDTOMercantil);

        resumenLibroDTORepertorioPropiedad = new ResumenLibroDTO();
        resumenLibroDTORepertorioPropiedad = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.REPERTORIO_PROPIEDAD.getTipoLibro());
        resumeLibroDTOListRepertorioPropiedad = new ArrayList<>();
        resumeLibroDTOListRepertorioPropiedad.add(resumenLibroDTORepertorioPropiedad);

        resumenLibroDTORepertorioMercantil = new ResumenLibroDTO();
        resumenLibroDTORepertorioMercantil = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.REPERTORIO_MERCANTIL.getTipoLibro());
        resumeLibroDTOListRepertorioMercantil = new ArrayList<>();
        resumeLibroDTOListRepertorioMercantil.add(resumenLibroDTORepertorioMercantil);

        resumenLibroDTOIndiceGeneralPropiedad = new ResumenLibroDTO();
        resumenLibroDTOIndiceGeneralPropiedad = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.INDICE_GENERAL_PROPIEDAD.getTipoLibro());
        resumeLibroDTOListIndiceGeneralPropiedad = new ArrayList<>();
        resumeLibroDTOListIndiceGeneralPropiedad.add(resumenLibroDTOIndiceGeneralPropiedad);

        resumenLibroDTOIndiceGeneralMercantil = new ResumenLibroDTO();
        resumenLibroDTOIndiceGeneralMercantil = tomoDao.getResumenPorInstitucionAñoTipo(institucionId, año, TipoLibroEnum.INDICE_GENERAL_MERCANTIL.getTipoLibro());
        resumeLibroDTOListIndiceGeneralMercantil = new ArrayList<>();
        resumeLibroDTOListIndiceGeneralMercantil.add(resumenLibroDTOIndiceGeneralMercantil);

        nombreRegistrador = resumenLibroDTOPropiedad.getNombreRegistrador();
        Short ultimoEstadoInventario = inventarioAnual.getEstadoInventarioAnualList().get(inventarioAnual.getEstadoInventarioAnualList().size() - 1).getEstado();
        if (ultimoEstadoInventario.equals(EstadoInventarioAnualEnum.COMPLETO.getEstadoInventarioAnual())) {
            btnActivated = Boolean.FALSE;
            //displayUploadEdit = Boolean.TRUE;

        } else {
            btnActivated = Boolean.TRUE;
            //displayUploadEdit = Boolean.FALSE;
        }
        comentariosRechazo=inventarioAnual.getComentarios();
        if(inventarioAnual.getUrlArchivo()==null || inventarioAnual.getUrlArchivo().isEmpty()){
            disableBtnDescargarArchivo = Boolean.TRUE;
        }else{
            disableBtnDescargarArchivo = Boolean.FALSE;
        }
    }

    public void aprobarInventarioAnual() {
        List<EstadoInventarioAnual> estadoInventarioAnualList = new ArrayList<EstadoInventarioAnual>();
        estadoInventarioAnualList = inventarioAnual.getEstadoInventarioAnualList();
        EstadoInventarioAnual estadoInventarioAnual = new EstadoInventarioAnual();
        estadoInventarioAnual.setInventarioAnual(inventarioAnual);
        estadoInventarioAnual.setFechaRegistro(new Date());
        estadoInventarioAnual.setEstado(EstadoInventarioAnualEnum.APROBADO.getEstadoInventarioAnual());
        estadoInventarioAnualServicio.create(estadoInventarioAnual);
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        comentariosRechazo = "Inventario Anual Aprobado";
        inventarioAnual.setComentarios(comentariosRechazo);
        inventarioAnualServicio.update(inventarioAnual);
        reloadLibro();
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = institucionServicio.findByPk(institucionId);
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRolInventario(institucionNotificacion,
                PerfilEnum.INV_REGISTRADOR_MERCANTIL.getPerfilId(), PerfilEnum.INV_VALIDADOR.getPerfilId(), inventarioAnual);
        usuarioListNotificacion.addAll(usuarioServicio.getUsuarioByIstitucionRolInventario(institucionNotificacion,
                PerfilEnum.INV_REGISTRADOR_PROPIEDAD.getPerfilId(), PerfilEnum.INV_VALIDADOR.getPerfilId(), inventarioAnual));
        String mensajeNotificacion = "El Inventario Anual correspondiente al año " + año + " ha sido APROBADO.";
        bandejaServicio.generarNotificacionInventario(usuarioListNotificacion, usuarioId, inventarioAnual.getInstitucion(), 
                inventarioAnual.getInventarioAnualId(), mensajeNotificacion, "IA");
        /////
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRolInventario(institucionNotificacion,
                PerfilEnum.INV_ADMINISTRADOR.getPerfilId(), PerfilEnum.INV_VALIDADOR.getPerfilId(), inventarioAnual);
        mensajeNotificacion = "El Inventario Anual correspondiente al año " + año + " del " + institucionNotificacion.getNombre() + " ha sido APROBADO.";
        bandejaServicio.generarNotificacionInventario(usuarioListNotificacion, usuarioId, inventarioAnual.getInstitucion(), 
                inventarioAnual.getInventarioAnualId(), mensajeNotificacion, "IA");
        //FIN ENVIO//
    }

    public void habilitarComentarioRechazo() {
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.TRUE;
    }

    public void cancelar() {
        btnActivated = Boolean.FALSE;
        displayComment = Boolean.FALSE;
    }

    public void rechazarInventarioAnual() {
        List<EstadoInventarioAnual> estadoInventarioAnualList = new ArrayList<EstadoInventarioAnual>();
        estadoInventarioAnualList = inventarioAnual.getEstadoInventarioAnualList();
        EstadoInventarioAnual estadoInventarioAnual = new EstadoInventarioAnual();
        estadoInventarioAnual.setInventarioAnual(inventarioAnual);
        estadoInventarioAnual.setFechaRegistro(new Date());
        estadoInventarioAnual.setEstado(EstadoInventarioAnualEnum.RECHAZADO.getEstadoInventarioAnual());
        estadoInventarioAnualServicio.create(estadoInventarioAnual);
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        inventarioAnual.setComentarios(comentariosRechazo);
        inventarioAnualServicio.update(inventarioAnual);
        reloadLibro();
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = institucionServicio.findByPk(institucionId);
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRolInventario(institucionNotificacion,
                PerfilEnum.INV_REGISTRADOR_MERCANTIL.getPerfilId(), PerfilEnum.INV_VALIDADOR.getPerfilId(), inventarioAnual);
        usuarioListNotificacion.addAll(usuarioServicio.getUsuarioByIstitucionRolInventario(institucionNotificacion,
                PerfilEnum.INV_REGISTRADOR_PROPIEDAD.getPerfilId(), PerfilEnum.INV_VALIDADOR.getPerfilId(), inventarioAnual));
        String mensajeNotificacion = "El Inventario Anual correspondiente al año " + año + " ha sido RECHAZADO.";
        bandejaServicio.generarNotificacionInventario(usuarioListNotificacion, usuarioId, inventarioAnual.getInstitucion(), 
                inventarioAnual.getInventarioAnualId(), mensajeNotificacion, "IA");
        /////
//        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRolInventario(institucionNotificacion,
//                PerfilEnum.INV_ADMINISTRADOR.getPerfilId(), PerfilEnum.INV_VALIDADOR.getPerfilId(), inventarioAnual);
//        mensajeNotificacion = "El Inventario Anual correspondiente al año " + año + " del " + institucionNotificacion.getNombre() + " ha sido RECHAZADO.";
//        bandejaServicio.generarNotificacionInventario(usuarioListNotificacion, usuarioId, inventarioAnual.getInstitucion(), 
//                inventarioAnual.getInventarioAnualId(), mensajeNotificacion, "IA");
        //FIN ENVIO//
    }  
    
    public void visualizarArchivoRespaldo() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        String rutaArchivo = inventarioAnual.getUrlArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_INVENTARIO.name()).getValor().concat(rutaArchivo));
            byte[] contenido = inventarioAnualServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error: Archivo no disponible", "");
            }
        }
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

    public Boolean getDisableSolicitarRevision() {
        return disableSolicitarRevision;
    }

    public void setDisableSolicitarRevision(Boolean disableSolicitarRevision) {
        this.disableSolicitarRevision = disableSolicitarRevision;
    }

    public Institucion getInstitucionSelected() {
        return institucionSelected;
    }

    public void setInstitucionSelected(Institucion institucionSelected) {
        this.institucionSelected = institucionSelected;
    }

    public List<Institucion> getInstitucionList() {
        return institucionList;
    }

    public void setInstitucionList(List<Institucion> institucionList) {
        this.institucionList = institucionList;
    }

    public String getNombreDireccionRegional() {
        return nombreDireccionRegional;
    }

    public void setNombreDireccionRegional(String nombreDireccionRegional) {
        this.nombreDireccionRegional = nombreDireccionRegional;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public Boolean getRenderPropiedad() {
        return renderPropiedad;
    }

    public void setRenderPropiedad(Boolean renderPropiedad) {
        this.renderPropiedad = renderPropiedad;
    }

    public Boolean getRenderMercantil() {
        return renderMercantil;
    }

    public void setRenderMercantil(Boolean renderMercantil) {
        this.renderMercantil = renderMercantil;
    }

    public Boolean getBtnActivated() {
        return btnActivated;
    }

    public void setBtnActivated(Boolean btnActivated) {
        this.btnActivated = btnActivated;
    }

    public Boolean getDisplayComment() {
        return displayComment;
    }

    public void setDisplayComment(Boolean displayComment) {
        this.displayComment = displayComment;
    }

    public String getComentariosRechazo() {
        return comentariosRechazo;
    }

    public void setComentariosRechazo(String comentariosRechazo) {
        this.comentariosRechazo = comentariosRechazo;
    }

    public Boolean getDisabledBtnReload() {
        return disabledBtnReload;
    }

    public void setDisabledBtnReload(Boolean disabledBtnReload) {
        this.disabledBtnReload = disabledBtnReload;
    }

    public InventarioAnual getInventarioAnual() {
        return inventarioAnual;
    }

    public void setInventarioAnual(InventarioAnual inventarioAnual) {
        this.inventarioAnual = inventarioAnual;
    }

    public Boolean getDisableBtnDescargarArchivo() {
        return disableBtnDescargarArchivo;
    }

    public void setDisableBtnDescargarArchivo(Boolean disableBtnDescargarArchivo) {
        this.disableBtnDescargarArchivo = disableBtnDescargarArchivo;
    }

}
