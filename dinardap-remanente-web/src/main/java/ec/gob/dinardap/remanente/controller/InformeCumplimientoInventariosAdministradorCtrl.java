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

@Named(value = "informeCumplimientoInventariosAdministradorCtrl")
@ViewScoped
public class InformeCumplimientoInventariosAdministradorCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloInventario;
    private String tituloInventarioDPI;
    private String tituloInventarioDCE;
    private Boolean disableBtnCargarArchivoDPI;
    private Boolean disableBtnCargarArchivoDCE;
    private Boolean renderArchivoTemporalDPI;
    private Boolean renderArchivoTemporalDCE;

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
    private List<InformeCumplimiento> informeCumplimientoDPIList;
    private List<InformeCumplimiento> informeCumplimientoDCEList;

    //
    @EJB
    private InformeCumplimientoServicio informeCumplimientoServicio;
    @EJB
    private ParametroServicio parametroServicio;

    @PostConstruct
    protected void init() {
        sftpDto = new SftpDto();
        tituloInventario = "Informe de Cumplimiento de Inventarios";
        tituloInventarioDPI = "Informe de Cumplimiento de Inventarios DPI";
        tituloInventarioDCE = "Informe de Cumplimiento de Inventarios DCE";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
        usuarioId = Integer.parseInt(getSessionVariable("usuarioId"));
        usuario = new Usuario();
        usuario.setUsuarioId(usuarioId);
        institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        institucion = new Institucion();
        institucion.setInstitucionId(institucionId);
        informeCumplimientoDPIList = new ArrayList<InformeCumplimiento>();
        informeCumplimientoDCEList = new ArrayList<InformeCumplimiento>();
        cantidadInformes = Integer.parseInt(parametroServicio.findByPk(ParametroEnum.CANTIDAD_INFORMES_CUMPLIMIENTO_INVENTARIOS.name()).getValor());
        reloadInformes();
    }

    public void reloadInformes() {
        renderArchivoTemporalDPI=Boolean.FALSE;
        renderArchivoTemporalDCE=Boolean.FALSE;
        disableBtnCargarArchivoDPI = Boolean.FALSE;
        disableBtnCargarArchivoDCE = Boolean.FALSE;
        informeCumplimiento = new InformeCumplimiento();
        informeCumplimientoDPIList = informeCumplimientoServicio.getInformesCumplimientoPorInstitucionAñoTipo(institucionId, año, "DPI");
        informeCumplimientoDCEList = informeCumplimientoServicio.getInformesCumplimientoPorInstitucionAñoTipo(institucionId, año, "DCE");
        if (informeCumplimientoDPIList.size() == 0 && informeCumplimientoDCEList.size() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "No se han cargado Informes de Cumplimiento para el año " + año + "."));
        } else {
            if (informeCumplimientoDPIList.size() >= cantidadInformes){
                disableBtnCargarArchivoDPI = Boolean.TRUE;
            }
            if (informeCumplimientoDCEList.size() >= cantidadInformes){
                disableBtnCargarArchivoDCE = Boolean.TRUE;
            }            
        }
    }

    public void guardarInformeCumplimiento(String tipo) {
        informeCumplimiento.setFechaRegistro(new Date());
        informeCumplimiento.setAnio(año);
        informeCumplimiento.setInstitucion(institucion);
        informeCumplimiento.setUsuario(usuario);
        informeCumplimiento.setEstado((short) 1);
        informeCumplimiento.setTipo(tipo);
        informeCumplimientoServicio.create(informeCumplimiento);
    }    

    public void uploadInformeDPI(FileUploadEvent event) {
        try {            
            UploadedFile file = event.getFile();
            setFileByte(IOUtils.toByteArray(file.getInputstream()));
            setNombreArchivoTemporal(event.getFile().getFileName());
            renderArchivoTemporalDPI=Boolean.TRUE;
            disableBtnCargarArchivoDCE = Boolean.TRUE;
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
            renderArchivoTemporalDPI=Boolean.FALSE;
        }
    }

    public void uploadInformeDCE(FileUploadEvent event) {
        try {            
            UploadedFile file = event.getFile();
            setFileByte(IOUtils.toByteArray(file.getInputstream()));
            setNombreArchivoTemporal(event.getFile().getFileName());
            renderArchivoTemporalDCE=Boolean.TRUE;
            disableBtnCargarArchivoDPI = Boolean.TRUE;
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
            renderArchivoTemporalDCE=Boolean.FALSE;
        }
    }

    public void confirmarUploadDPI() {
        guardarInformeCumplimiento("DPI");
        String realPath = (año + "/icdpi").concat(informeCumplimiento.getInformeCumplimientoId().toString()).concat(".pdf");
        sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_INFORME_CUMPLIMIENTO.name()).getValor().concat(realPath));
        sftpDto.setArchivo(getFileByte());
        informeCumplimiento.setUrlArchivo(realPath);
        informeCumplimientoServicio.editInformeCumplimiento(informeCumplimiento, sftpDto);
        setFileByte(null);
        //PrimeFaces.current().executeScript("PF('transaccionUploadDlg').hide()");
        renderArchivoTemporalDPI=Boolean.FALSE;
        reloadInformes();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Informe de Cumplimiento cargado exitosamente."));
    }

    public void confirmarUploadDCE() {
        guardarInformeCumplimiento("DCE");
        String realPath = (año + "/icdce").concat(informeCumplimiento.getInformeCumplimientoId().toString()).concat(".pdf");
        sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_INFORME_CUMPLIMIENTO.name()).getValor().concat(realPath));
        sftpDto.setArchivo(getFileByte());
        informeCumplimiento.setUrlArchivo(realPath);
        informeCumplimientoServicio.editInformeCumplimiento(informeCumplimiento, sftpDto);
        setFileByte(null);
        //PrimeFaces.current().executeScript("PF('transaccionUploadDlg').hide()");
        renderArchivoTemporalDCE=Boolean.FALSE;
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

    public String getTituloInventarioDPI() {
        return tituloInventarioDPI;
    }

    public void setTituloInventarioDPI(String tituloInventarioDPI) {
        this.tituloInventarioDPI = tituloInventarioDPI;
    }

    public String getTituloInventarioDCE() {
        return tituloInventarioDCE;
    }

    public void setTituloInventarioDCE(String tituloInventarioDCE) {
        this.tituloInventarioDCE = tituloInventarioDCE;
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

    public Boolean getDisableBtnCargarArchivoDPI() {
        return disableBtnCargarArchivoDPI;
    }

    public void setDisableBtnCargarArchivoDPI(Boolean disableBtnCargarArchivoDPI) {
        this.disableBtnCargarArchivoDPI = disableBtnCargarArchivoDPI;
    }

    public Boolean getDisableBtnCargarArchivoDCE() {
        return disableBtnCargarArchivoDCE;
    }

    public void setDisableBtnCargarArchivoDCE(Boolean disableBtnCargarArchivoDCE) {
        this.disableBtnCargarArchivoDCE = disableBtnCargarArchivoDCE;
    }

    public Boolean getRenderArchivoTemporalDPI() {
        return renderArchivoTemporalDPI;
    }

    public void setRenderArchivoTemporalDPI(Boolean renderArchivoTemporalDPI) {
        this.renderArchivoTemporalDPI = renderArchivoTemporalDPI;
    }

    public Boolean getRenderArchivoTemporalDCE() {
        return renderArchivoTemporalDCE;
    }

    public void setRenderArchivoTemporalDCE(Boolean renderArchivoTemporalDCE) {
        this.renderArchivoTemporalDCE = renderArchivoTemporalDCE;
    }

    public List<InformeCumplimiento> getInformeCumplimientoDPIList() {
        return informeCumplimientoDPIList;
    }

    public void setInformeCumplimientoDPIList(List<InformeCumplimiento> informeCumplimientoDPIList) {
        this.informeCumplimientoDPIList = informeCumplimientoDPIList;
    }

    public List<InformeCumplimiento> getInformeCumplimientoDCEList() {
        return informeCumplimientoDCEList;
    }

    public void setInformeCumplimientoDCEList(List<InformeCumplimiento> informeCumplimientoDCEList) {
        this.informeCumplimientoDCEList = informeCumplimientoDCEList;
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

}
