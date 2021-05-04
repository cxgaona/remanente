package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.InformeCumplimiento;
import ec.gob.dinardap.remanente.servicio.InformeCumplimientoServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "informeCumplimientoInventariosRegionalCtrl")
@ViewScoped
public class InformeCumplimientoInventariosRegionalCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventario;
    private Boolean disableBtnCargarArchivo;
    private Boolean renderArchivoTemporal;

    //Variables de negocio
    private Integer institucionId;
    private Integer usuarioId;
    private Integer año;
    private Integer cantidadInformes;
    private Usuario usuario;
    private Institucion institucion;
    private InformeCumplimiento informeCumplimiento;
    private String rutaArchivo;
    private SftpDto sftpDto;
    private byte[] fileByte;
    private String nombreArchivoTemporal;

    //Listas
    private List<InformeCumplimiento> informeCumplimientoList;

    //
    @EJB
    private InformeCumplimientoServicio informeCumplimientoServicio;
    @EJB
    private ParametroServicio parametroServicio;

    @PostConstruct
    protected void init() {
        sftpDto = new SftpDto();
        tituloInventario = "Informe de Cumplimiento de Inventarios";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
        usuarioId = Integer.parseInt(getSessionVariable("usuarioId"));
        usuario = new Usuario();
        usuario.setUsuarioId(usuarioId);
        institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        institucion = new Institucion();
        institucion.setInstitucionId(institucionId);
        informeCumplimientoList = new ArrayList<InformeCumplimiento>();
        cantidadInformes = Integer.parseInt(parametroServicio.findByPk(ParametroEnum.CANTIDAD_INFORMES_CUMPLIMIENTO_INVENTARIOS.name()).getValor());
        reloadInformes();
    }

    public void reloadInformes() {
        renderArchivoTemporal=Boolean.FALSE;
        disableBtnCargarArchivo = Boolean.FALSE;
        informeCumplimiento = new InformeCumplimiento();
        informeCumplimientoList = informeCumplimientoServicio.getInformesCumplimientoPorInstitucionAñoTipo(institucionId, año, "DR");
        if (informeCumplimientoList.size() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "No se han cargado Informes de Cumplimiento para el año " + año + "."));
        } else if (informeCumplimientoList.size() >= cantidadInformes) {
            disableBtnCargarArchivo = Boolean.TRUE;
        }
    }

    public void guardarInformeCumplimiento() {
        informeCumplimiento.setFechaRegistro(new Date());
        informeCumplimiento.setAnio(año);
        informeCumplimiento.setInstitucion(institucion);
        informeCumplimiento.setUsuario(usuario);
        informeCumplimiento.setEstado((short) 1);
        informeCumplimiento.setTipo("DR");
        informeCumplimientoServicio.create(informeCumplimiento);
    }

    public void uploadInformeRegional(FileUploadEvent event) {
        try {            
            UploadedFile file = event.getFile();
            setFileByte(IOUtils.toByteArray(file.getInputstream()));
            setNombreArchivoTemporal(event.getFile().getFileName());
            renderArchivoTemporal=Boolean.TRUE;
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
            renderArchivoTemporal=Boolean.FALSE;
        }
    }

    public void confirmarUpload() {
        guardarInformeCumplimiento();
        String realPath = (año + "/icdr").concat(informeCumplimiento.getInformeCumplimientoId().toString()).concat(".pdf");
        sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_INFORME_CUMPLIMIENTO.name()).getValor().concat(realPath));
        sftpDto.setArchivo(getFileByte());
        informeCumplimiento.setUrlArchivo(realPath);
        informeCumplimientoServicio.editInformeCumplimiento(informeCumplimiento, sftpDto);
        setFileByte(null);
        //PrimeFaces.current().executeScript("PF('transaccionUploadDlg').hide()");
        renderArchivoTemporal=Boolean.FALSE;
        reloadInformes();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Informe de Cumplimiento cargado exitosamente."));
    }

    public void vistaPrevia() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (getFileByte() != null) {
            downloadFile(getFileByte(), tipoArchivo.obtenerTipoArchivo(getNombreArchivoTemporal()), getNombreArchivoTemporal());
        } else {
            this.addErrorMessage("1", "Error", "Archivo no disponible");
        }
    }

    public void visualizarArchivoRespaldo() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        //String rutaArchivo = inventarioDeclaracion.getUrlArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_INFORME_CUMPLIMIENTO.name()).getValor().concat(rutaArchivo));
            byte[] contenido = informeCumplimientoServicio.descargarArchivo(sftpDto);
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

    public List<InformeCumplimiento> getInformeCumplimientoList() {
        return informeCumplimientoList;
    }

    public void setInformeCumplimientoList(List<InformeCumplimiento> informeCumplimientoList) {
        this.informeCumplimientoList = informeCumplimientoList;
    }

    public Boolean getDisableBtnCargarArchivo() {
        return disableBtnCargarArchivo;
    }

    public void setDisableBtnCargarArchivo(Boolean disableBtnCargarArchivo) {
        this.disableBtnCargarArchivo = disableBtnCargarArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Integer getCantidadInformes() {
        return cantidadInformes;
    }

    public void setCantidadInformes(Integer cantidadInformes) {
        this.cantidadInformes = cantidadInformes;
    }

    public byte[] getFileByte() {
        return fileByte;
    }

    public void setFileByte(byte[] fileByte) {
        this.fileByte = fileByte;
    }

    public String getNombreArchivoTemporal() {
        return nombreArchivoTemporal;
    }

    public void setNombreArchivoTemporal(String nombreArchivoTemporal) {
        this.nombreArchivoTemporal = nombreArchivoTemporal;
    }

    public Boolean getRenderArchivoTemporal() {
        return renderArchivoTemporal;
    }

    public void setRenderArchivoTemporal(Boolean renderArchivoTemporal) {
        this.renderArchivoTemporal = renderArchivoTemporal;
    }

}
