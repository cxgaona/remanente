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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
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
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "resumenLibrosCtrl")
@ViewScoped
public class ResumenLibrosCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventarioLibro;
    private String strBtnGuardar;
    private Boolean disableSolicitarRevision;
    private Boolean renderPropiedad;
    private Boolean renderMercantil;
    private Boolean disableEditRegistrador;

    //Variables de negocio
    private Integer institucionId;
    private Integer usuarioId;
    private Integer año;
    private Institucion institucion;
    private InventarioAnual inventarioAnual;
    private ResumenLibroDTO resumenLibroDTOPropiedad, resumenLibroDTOMercantil;
    private ResumenLibroDTO resumenLibroDTORepertorioPropiedad, resumenLibroDTORepertorioMercantil;
    private ResumenLibroDTO resumenLibroDTOIndiceGeneralPropiedad, resumenLibroDTOIndiceGeneralMercantil;
    private String nombreRegistrador, comentariosRechazo;
    private Institucion institucionNotificacion;
    private SftpDto sftpDto;

    //Listas
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
    private UsuarioServicio usuarioServicio;
    @EJB
    private BandejaServicio bandejaServicio;
    @EJB
    private ParametroServicio parametroServicio;

    @PostConstruct
    protected void init() {
        sftpDto = new SftpDto();
        tituloInventarioLibro = "Resumen Libros";
        disableSolicitarRevision = Boolean.FALSE;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);

        usuarioId = Integer.parseInt(getSessionVariable("usuarioId"));
        institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        institucion = new Institucion();
        institucion.setInstitucionId(institucionId);
        disableEditRegistrador = Boolean.FALSE;
        renderMercantil = Boolean.TRUE;
        renderPropiedad = Boolean.TRUE;
        comentariosRechazo = "";
        if (BaseCtrl.getSessionVariable("institucionTipo").equals(TipoInstitucionEnum.REGISTRO_MERCANTIL.getTipoInstitucion().toString())) {
            renderPropiedad = Boolean.FALSE;
        } else if (BaseCtrl.getSessionVariable("institucionTipo").equals(TipoInstitucionEnum.REGISTRO_PROPIEDAD.getTipoInstitucion().toString())) {
            renderMercantil = Boolean.FALSE;
        }
        reloadLibro();
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
        if (nombreRegistrador.isEmpty()) {
            disableSolicitarRevision = Boolean.TRUE;
            disableEditRegistrador = Boolean.FALSE;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "Recuerde actualizar el Nombre del Registrador para Solicitar Revisión."));

        } else {
            Short ultimoEstadoInventario = inventarioAnual.getEstadoInventarioAnualList().get(inventarioAnual.getEstadoInventarioAnualList().size() - 1).getEstado();
            if (ultimoEstadoInventario.equals(EstadoInventarioAnualEnum.GENERADO.getEstadoInventarioAnual())
                    || ultimoEstadoInventario.equals(EstadoInventarioAnualEnum.RECHAZADO.getEstadoInventarioAnual())) {
                disableSolicitarRevision = Boolean.FALSE;
                disableEditRegistrador = Boolean.FALSE;
                //displayUploadEdit = Boolean.TRUE;

            } else {
                disableSolicitarRevision = Boolean.TRUE;
                disableEditRegistrador = Boolean.TRUE;
                //displayUploadEdit = Boolean.FALSE;
            }
        }
        comentariosRechazo = inventarioAnual.getComentarios();
    }

    public void solicitarRevisionInventario() {
        List<EstadoInventarioAnual> estadoInventarioAnualList = new ArrayList<EstadoInventarioAnual>();
        estadoInventarioAnualList = inventarioAnual.getEstadoInventarioAnualList();
        EstadoInventarioAnual estadoInventarioAnual = new EstadoInventarioAnual();
        estadoInventarioAnual.setInventarioAnual(inventarioAnual);
        estadoInventarioAnual.setFechaRegistro(new Date());
        estadoInventarioAnual.setEstado(EstadoInventarioAnualEnum.COMPLETO.getEstadoInventarioAnual());
        estadoInventarioAnualServicio.create(estadoInventarioAnual);
        inventarioAnual.setComentarios(comentariosRechazo);
        inventarioAnualServicio.update(inventarioAnual);
        disableSolicitarRevision = Boolean.TRUE;
        //displayUploadEdit = Boolean.FALSE;
        reloadLibro();
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = institucionServicio.findByPk(institucionId);
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRolInventario(institucionNotificacion,
                PerfilEnum.INV_VALIDADOR.getPerfilId(), PerfilEnum.INV_REGISTRADOR_PROPIEDAD.getPerfilId(), inventarioAnual);
        String mensajeNotificacion = "Se le asignó la Revisión del Inventario Anual correspondiente al año " + año + " del " + institucionNotificacion.getNombre();
        bandejaServicio.generarNotificacionInventario(usuarioListNotificacion, usuarioId, inventarioAnual.getInstitucion(),
                inventarioAnual.getInventarioAnualId(), mensajeNotificacion, "IA");
        //FIN ENVIO//
    }
    
    public void uploadSolicitud(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());

            String realPath = (Calendar.getInstance().get(Calendar.YEAR) + "/").concat(inventarioAnual.getInventarioAnualId().toString()).concat(".pdf");
            sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_INVENTARIO.name()).getValor().concat(realPath));
            sftpDto.setArchivo(fileByte);
            inventarioAnual.setUrlArchivo(realPath);
//            inventarioAnualServicio.editTransaccion(transaccionSelected, sftpDto);
            fileByte = null;
            PrimeFaces.current().executeScript("PF('transaccionUploadDlg').hide()");
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
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

    public Boolean getDisableEditRegistrador() {
        return disableEditRegistrador;
    }

    public void setDisableEditRegistrador(Boolean disableEditRegistrador) {
        this.disableEditRegistrador = disableEditRegistrador;
    }

    public String getComentariosRechazo() {
        return comentariosRechazo;
    }

    public void setComentariosRechazo(String comentariosRechazo) {
        this.comentariosRechazo = comentariosRechazo;
    }

    public InventarioAnual getInventarioAnual() {
        return inventarioAnual;
    }

    public void setInventarioAnual(InventarioAnual inventarioAnual) {
        this.inventarioAnual = inventarioAnual;
    }

}
