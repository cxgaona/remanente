package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.EstadoInventarioDeclaracionEnum;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.constante.PerfilEnum;
import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioDeclaracion;
import ec.gob.dinardap.remanente.modelo.InventarioDeclaracion;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.EstadoInventarioDeclaracionServicio;
import ec.gob.dinardap.remanente.servicio.InventarioDeclaracionServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.remanente.utils.FacesUtils;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.poi.util.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "inventarioDeclaracionesCtrl")
@ViewScoped
public class InventarioDeclaracionesCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventario;
    private String strBtnGuardar;
    private Boolean disableFormularioInventario;
    private Boolean disableNuevoInventario;
    private Boolean disableActualizarInventario;
    private Boolean renderResumen;
    private Boolean disableSolicitarRevision;
    private Boolean onCreate;
    private Boolean onEdit;
    private Boolean disableBtnCargarArchivo;
    private Boolean disableBtnDescargarArchivo;

    //Variables de negocio
    private Integer institucionId;
    private Integer usuarioId;
    private Integer año;
    private Institucion institucion;
    private InventarioDeclaracion inventarioDeclaracion;
    private String comentariosRechazo;
    private Institucion institucionNotificacion;
    private SftpDto sftpDto;

    //Listas
    private List<Usuario> usuarioListNotificacion;

    //
    @EJB
    private InventarioDeclaracionServicio inventarioDeclaracionServicio;
    @EJB
    private EstadoInventarioDeclaracionServicio estadoInventarioDeclaracionServicio;
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
        tituloInventario = "Inventario de Declaraciones Responsables de Trámites en Línea";
        disableSolicitarRevision = Boolean.FALSE;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
        usuarioId = Integer.parseInt(getSessionVariable("usuarioId"));
        institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        institucion = new Institucion();
        institucion.setInstitucionId(institucionId);
        disableBtnCargarArchivo = Boolean.FALSE;
        comentariosRechazo = ""; 
        reloadInventario();
    }

    public void reloadInventario() {
        strBtnGuardar = getBundleEtiquetas("btnGuardar", null);
        renderResumen = Boolean.FALSE;
        disableNuevoInventario = Boolean.FALSE;
        disableActualizarInventario = Boolean.TRUE;
        disableFormularioInventario = Boolean.TRUE;
        inventarioDeclaracion = inventarioDeclaracionServicio.getInventarioPorInstitucionAño(institucionId, año);
        if (inventarioDeclaracion.getInventarioDeclaracionId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "No se ha ingresado un Inventario de Declaraciones para el año " + año + "."));
        } else {
            renderResumen = Boolean.TRUE;
            disableNuevoInventario = Boolean.TRUE;
            onCreate = Boolean.FALSE;
            onEdit = Boolean.TRUE;
            disableActualizarInventario = Boolean.FALSE;
            strBtnGuardar = getBundleEtiquetas("btnActualizar", null);
            if (inventarioDeclaracion.getUrlArchivo() == null || inventarioDeclaracion.getUrlArchivo().isEmpty()) {
                disableBtnDescargarArchivo = Boolean.TRUE;
            } else {
                disableBtnDescargarArchivo = Boolean.FALSE;
            }
            Short ultimoEstadoInventario = inventarioDeclaracion.getEstadoInventarioDeclaracionList().get(inventarioDeclaracion.getEstadoInventarioDeclaracionList().size() - 1).getEstado();
            if (ultimoEstadoInventario.equals(EstadoInventarioDeclaracionEnum.GENERADO.getEstadoInventarioDeclaracion())
                    || ultimoEstadoInventario.equals(EstadoInventarioDeclaracionEnum.RECHAZADO.getEstadoInventarioDeclaracion())) {
                disableSolicitarRevision = Boolean.FALSE;
                disableBtnCargarArchivo = Boolean.FALSE;
                disableActualizarInventario = Boolean.FALSE;

            } else {
                disableSolicitarRevision = Boolean.TRUE;
                disableBtnCargarArchivo = Boolean.TRUE;
                disableActualizarInventario = Boolean.TRUE;
            }
            comentariosRechazo = inventarioDeclaracion.getComentarios();
        }
    }

    public void nuevoRegistroInventarioDeclaracion() {
        disableFormularioInventario = Boolean.FALSE;
        disableNuevoInventario = Boolean.TRUE;
        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
    }

    public void actualizarRegistroInventarioDeclaracion() {
        disableFormularioInventario = Boolean.FALSE;
        disableActualizarInventario = Boolean.TRUE;
        renderResumen = Boolean.FALSE;
        onCreate = Boolean.FALSE;
        onEdit = Boolean.TRUE;
    }

    public void guardarInventario() {
        if (inventarioDeclaracion.getFechaApertura().after(inventarioDeclaracion.getFechaCierre())) {
            //inventarioDeclaracion = inventarioAux;
            //reloadInventario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La Fecha de Apertura no puede ser posterior a la Fecha de Cierre."));
        } else {
            if (onCreate) {
                inventarioDeclaracion.setFechaRegistro(new Date());
                inventarioDeclaracion.setAnio(año);
                inventarioDeclaracion.setInstitucion(institucion);
                inventarioDeclaracionServicio.create(inventarioDeclaracion);
                //insertar estado inventario
                EstadoInventarioDeclaracion estadoInventarioDeclaracion = new EstadoInventarioDeclaracion();
                estadoInventarioDeclaracion.setEstado(EstadoInventarioDeclaracionEnum.GENERADO.getEstadoInventarioDeclaracion());
                estadoInventarioDeclaracion.setFechaRegistro(new Date());
                estadoInventarioDeclaracion.setInventarioDeclaracion(inventarioDeclaracion);
                estadoInventarioDeclaracionServicio.create(estadoInventarioDeclaracion);
            } else if (onEdit) {
                //inventarioDeclaracion.setFechaModificacionRegistro(new Date());
                inventarioDeclaracionServicio.update(inventarioDeclaracion);
            }
            reloadInventario();
        }
    }

    public void solicitarRevisionInventario() {
        List<EstadoInventarioDeclaracion> estadoInventarioDeclaracionList = new ArrayList<EstadoInventarioDeclaracion>();
        estadoInventarioDeclaracionList = inventarioDeclaracion.getEstadoInventarioDeclaracionList();
        EstadoInventarioDeclaracion estadoInventarioDeclaracion = new EstadoInventarioDeclaracion();
        estadoInventarioDeclaracion.setInventarioDeclaracion(inventarioDeclaracion);
        estadoInventarioDeclaracion.setFechaRegistro(new Date());
        estadoInventarioDeclaracion.setEstado(EstadoInventarioDeclaracionEnum.COMPLETO.getEstadoInventarioDeclaracion());
        estadoInventarioDeclaracionServicio.create(estadoInventarioDeclaracion);
        inventarioDeclaracion.setComentarios(comentariosRechazo);
        inventarioDeclaracionServicio.update(inventarioDeclaracion);
        disableSolicitarRevision = Boolean.TRUE;
        reloadInventario();
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = institucionServicio.findByPk(institucionId);
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRolInventarioDeclaracion(institucionNotificacion,
                PerfilEnum.INV_VALIDADOR.getPerfilId(), PerfilEnum.INV_REGISTRADOR_PROPIEDAD.getPerfilId(), inventarioDeclaracion);
        String mensajeNotificacion = "Se le asignó la Revisión del Inventario de Declaraciones Responsables de Trámites en Línea correspondiente al año " + año + " del " + institucionNotificacion.getNombre();
        bandejaServicio.generarNotificacionInventario(usuarioListNotificacion, usuarioId, inventarioDeclaracion.getInstitucion(),
                inventarioDeclaracion.getInventarioDeclaracionId(), mensajeNotificacion, "ID");
        //FIN ENVIO//
    }
    
    public void uploadSolicitud(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());

            String realPath = (año + "/").concat(inventarioDeclaracion.getInventarioDeclaracionId().toString()).concat(".pdf");
            sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_INVENTARIO_DECLARACION.name()).getValor().concat(realPath));
            sftpDto.setArchivo(fileByte);
            inventarioDeclaracion.setUrlArchivo(realPath);
            inventarioDeclaracionServicio.editInventarioDeclaracion(inventarioDeclaracion, sftpDto);
            fileByte = null;
            //PrimeFaces.current().executeScript("PF('transaccionUploadDlg').hide()");
            disableBtnDescargarArchivo = Boolean.FALSE;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Archivo de respaldo cargado exitosamente."));
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
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

    public void cancelarInventario() {
        inventarioDeclaracion = new InventarioDeclaracion();
        reloadInventario();
    }
    
    public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        String path = FacesUtils.getPath() + "/resource/images/";
      
        parametros.put("realPath", path);
        parametros.put("nombreRegistro", inventarioDeclaracion.getInstitucion().getNombre());
        parametros.put("nombreRegional", inventarioDeclaracion.getInstitucion().getTipoInstitucion().equals(TipoInstitucionEnum.RMX_SIN_AUTONOMIA_FINANCIERA.getTipoInstitucion()) ? inventarioDeclaracion.getInstitucion().getInstitucion().getInstitucion().getNombre() : inventarioDeclaracion.getInstitucion().getInstitucion().getNombre());
        parametros.put("anioG", año);
        parametros.put("institucionIdG", inventarioDeclaracion.getInstitucion().getInstitucionId());
                
        //Inventario Declaracion
        parametros.put("nombreRegistradorInventarioDeclaracionG", inventarioDeclaracion.getNombreRegistrador());
        parametros.put("tipoSoporteInventarioDeclaracionG", inventarioDeclaracion.getTipoSoporte());
        parametros.put("denominacionInventarioDeclaracionG", inventarioDeclaracion.getDenominacion());
        parametros.put("anioInventarioDeclaracionG", inventarioDeclaracion.getAnio());
        parametros.put("fechaAperturaInventarioDeclaracionG", inventarioDeclaracion.getFechaApertura());
        parametros.put("fechaCierreInventarioDeclaracionG", inventarioDeclaracion.getFechaCierre());
        parametros.put("totalAnotacionesInventarioDeclaracionG", inventarioDeclaracion.getTotalAnotaciones());
        
        JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resource/templatesReports/reportInventarioDeclaracion.jasper"));//
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                report,
                parametros,
                new JREmptyDataSource());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=InventarioDeclaraciones.pdf");
        ServletOutputStream stream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
        stream.flush();
        stream.close();

        FacesContext.getCurrentInstance().responseComplete();
    }

    //Getters & Setters
    public String getTituloInventario() {
        return tituloInventario;
    }

    public void setTituloInventario(String tituloInventario) {
        this.tituloInventario = tituloInventario;
    }

    public String getStrBtnGuardar() {
        return strBtnGuardar;
    }

    public void setStrBtnGuardar(String strBtnGuardar) {
        this.strBtnGuardar = strBtnGuardar;
    }

    public Boolean getDisableSolicitarRevision() {
        return disableSolicitarRevision;
    }

    public void setDisableSolicitarRevision(Boolean disableSolicitarRevision) {
        this.disableSolicitarRevision = disableSolicitarRevision;
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

    public Boolean getDisableNuevoInventario() {
        return disableNuevoInventario;
    }

    public void setDisableNuevoInventario(Boolean disableNuevoInventario) {
        this.disableNuevoInventario = disableNuevoInventario;
    }

    public Boolean getDisableActualizarInventario() {
        return disableActualizarInventario;
    }

    public void setDisableActualizarInventario(Boolean disableActualizarInventario) {
        this.disableActualizarInventario = disableActualizarInventario;
    }

    public Boolean getDisableBtnCargarArchivo() {
        return disableBtnCargarArchivo;
    }

    public void setDisableBtnCargarArchivo(Boolean disableBtnCargarArchivo) {
        this.disableBtnCargarArchivo = disableBtnCargarArchivo;
    }

}
