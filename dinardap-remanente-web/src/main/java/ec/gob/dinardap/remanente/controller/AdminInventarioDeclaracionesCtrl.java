package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.EstadoInventarioDeclaracionEnum;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.constante.PerfilEnum;
import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import static ec.gob.dinardap.remanente.controller.BaseCtrl.getSessionVariable;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioDeclaracion;
import ec.gob.dinardap.remanente.modelo.InventarioDeclaracion;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.EstadoInventarioDeclaracionServicio;
import ec.gob.dinardap.remanente.servicio.InventarioDeclaracionServicio;
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

@Named(value = "adminInventarioDeclaracionesCtrl")
@ViewScoped
public class AdminInventarioDeclaracionesCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventario;
    private Boolean disableFormularioInventario;
    private Boolean renderResumen;
    private Boolean disableBtnDescargarArchivo;
    private Boolean disabledBtnReload;
    private Boolean btnActivated;
    private Boolean displayComment;

    //Variables de negocio
    private Integer institucionId;
    private Integer usuarioId;
    private Integer direccionRegionalId;
    private String nombreDireccionRegional;
    private String nombreInstitucion;
    private Integer año;
    private InventarioDeclaracion inventarioDeclaracion;
    private String comentariosRechazo;
    private Institucion institucionNotificacion, institucionSelected;
    private SftpDto sftpDto;

    //Listas
    private List<Institucion> institucionList;
    private List<Usuario> usuarioListNotificacion;

    //
    @EJB
    private InventarioDeclaracionServicio inventarioDeclaracionServicio;
    @EJB
    private EstadoInventarioDeclaracionServicio estadoInventarioDeclaracionServicio;
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
        
        institucionSelected = new Institucion();
        institucionList = new ArrayList<Institucion>();
        institucionList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.RMX_AUTONOMIA_FINANCIERA.getTipoInstitucion()));
        institucionList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.RMX_SIN_AUTONOMIA_FINANCIERA.getTipoInstitucion()));
        institucionList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.REGISTRO_MERCANTIL.getTipoInstitucion()));
        institucionList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.REGISTRO_PROPIEDAD.getTipoInstitucion()));

        sftpDto = new SftpDto();
        tituloInventario = "Administración Inventario de Declaraciones Responsables de Trámites en Línea";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
        usuarioId = Integer.parseInt(getSessionVariable("usuarioId"));
        displayComment = Boolean.FALSE;
        disabledBtnReload = Boolean.TRUE;
        disableFormularioInventario = Boolean.TRUE;
        comentariosRechazo = ""; 
        btnActivated = Boolean.TRUE;
    }

    public void onRowSelectInstitucion() {
        institucionId = institucionSelected.getInstitucionId();
        nombreInstitucion = institucionSelected.getNombre();
        reloadInventario();
        disabledBtnReload = Boolean.FALSE;

    }
    
    public void reloadInventario() {
        renderResumen = Boolean.FALSE;        
        inventarioDeclaracion = inventarioDeclaracionServicio.getInventarioPorInstitucionAño(institucionId, año);
        if (inventarioDeclaracion.getInventarioDeclaracionId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "No se ha ingresado un Inventario de Declaraciones para el año " + año + "."));
        } else {
            renderResumen = Boolean.TRUE;
            if (inventarioDeclaracion.getUrlArchivo() == null || inventarioDeclaracion.getUrlArchivo().isEmpty()) {
                disableBtnDescargarArchivo = Boolean.TRUE;
            } else {
                disableBtnDescargarArchivo = Boolean.FALSE;
            }
            Short ultimoEstadoInventario = inventarioDeclaracion.getEstadoInventarioDeclaracionList().get(inventarioDeclaracion.getEstadoInventarioDeclaracionList().size() - 1).getEstado();
            if (ultimoEstadoInventario.equals(EstadoInventarioDeclaracionEnum.COMPLETO.getEstadoInventarioDeclaracion())) {
                btnActivated = Boolean.FALSE;

            } else {
                btnActivated = Boolean.TRUE;
            }
            comentariosRechazo = inventarioDeclaracion.getComentarios();
        }
    } 

    public void visualizarArchivoRespaldo() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        String rutaArchivo = inventarioDeclaracion.getUrlArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_INVENTARIO_DECLARACION.name()).getValor().concat(rutaArchivo));
            byte[] contenido = inventarioDeclaracionServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error", "Archivo no disponible");
            }
        }
    }
    
    

    //Getters & Setters
    public String getTituloInventario() {
        return tituloInventario;
    }

    public void setTituloInventario(String tituloInventario) {
        this.tituloInventario = tituloInventario;
    }

    public Boolean getDisableBtnDescargarArchivo() {
        return disableBtnDescargarArchivo;
    }

    public void setDisableBtnDescargarArchivo(Boolean disableBtnDescargarArchivo) {
        this.disableBtnDescargarArchivo = disableBtnDescargarArchivo;
    }

    public Integer getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(Integer institucionId) {
        this.institucionId = institucionId;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public String getComentariosRechazo() {
        return comentariosRechazo;
    }

    public void setComentariosRechazo(String comentariosRechazo) {
        this.comentariosRechazo = comentariosRechazo;
    }

    public Boolean getRenderResumen() {
        return renderResumen;
    }

    public void setRenderResumen(Boolean renderResumen) {
        this.renderResumen = renderResumen;
    }

    public InventarioDeclaracion getInventarioDeclaracion() {
        return inventarioDeclaracion;
    }

    public void setInventarioDeclaracion(InventarioDeclaracion inventarioDeclaracion) {
        this.inventarioDeclaracion = inventarioDeclaracion;
    }

    public Boolean getDisableFormularioInventario() {
        return disableFormularioInventario;
    }

    public void setDisableFormularioInventario(Boolean disableFormularioInventario) {
        this.disableFormularioInventario = disableFormularioInventario;
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

    public Boolean getDisabledBtnReload() {
        return disabledBtnReload;
    }

    public void setDisabledBtnReload(Boolean disabledBtnReload) {
        this.disabledBtnReload = disabledBtnReload;
    }

    public List<Institucion> getInstitucionList() {
        return institucionList;
    }

    public void setInstitucionList(List<Institucion> institucionList) {
        this.institucionList = institucionList;
    }

    public Institucion getInstitucionSelected() {
        return institucionSelected;
    }

    public void setInstitucionSelected(Institucion institucionSelected) {
        this.institucionSelected = institucionSelected;
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

}
