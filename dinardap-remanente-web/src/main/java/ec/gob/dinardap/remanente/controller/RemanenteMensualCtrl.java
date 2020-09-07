package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dto.RemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.DiasNoLaborablesServicio;
import ec.gob.dinardap.remanente.servicio.EstadoRemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import ec.gob.dinardap.util.TipoArchivo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "remanenteMensualCtrl")
@ViewScoped
public class RemanenteMensualCtrl extends BaseCtrl implements Serializable {

    private String tituloPagina;
    private String nombreInstitucion;
    private Integer año;

    private RemanenteMensualDTO remanenteMensualDTOSelected;
    private String mesSelected;
    private Integer institucionId;
    private Integer usuarioId;
    private String tituloDetalleDlg;
    private String rutaArchivo;
    private Institucion institucionNotificacion;
    private List<Usuario> usuarioListNotificacion;

    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private Transaccion transaccionSelected;

    private List<RemanenteMensualDTO> remanenteMensualDTOList;
    private List<Transaccion> transaccionRPropiedadList;
    private List<Transaccion> transaccionRMercantilList;
    private List<Transaccion> transaccionEgresosList;
    private List<Transaccion> transaccionList;
    private List<Tramite> tramiteRPropiedadMercantilList;
    private List<Nomina> egresoNominaList;
    private List<FacturaPagada> egresoFacturaList;

    private Boolean btnActivated;
    private Boolean displayUploadEdit;
    private Boolean displaySolicitud;

    private Boolean disabledBtnEnvCan;

    private SftpDto sftpDto;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private InstitucionServicio institucionServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @EJB
    private EstadoRemanenteMensualServicio estadoRemanenteMensualServicio;

    @EJB
    private BandejaServicio bandejaServicio;

    @EJB
    private UsuarioServicio usuarioServicio;

    @EJB
    private ParametroServicio parametroServicio;

    @EJB
    private DiasNoLaborablesServicio diasNoLaborablesServicio;

    @PostConstruct
    protected void init() {

        sftpDto = new SftpDto();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        tituloPagina = "Gestión Remanente Mensual";
        año = 0;
        año = calendar.get(Calendar.YEAR);
        mesSelected = "Sin Selección";
        remanenteMensualDTOSelected = new RemanenteMensualDTO();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        btnActivated = Boolean.TRUE;
        displayUploadEdit = Boolean.FALSE;
        displaySolicitud = Boolean.FALSE;
        disabledBtnEnvCan = Boolean.TRUE;
        transaccionSelected = new Transaccion();
        institucionId = this.getInstitucionID(this.getSessionVariable("perfil"));
        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        nombreInstitucion = institucionServicio.findByPk(institucionId).getNombre();
        remanenteMensualDTOList = new ArrayList<RemanenteMensualDTO>();
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
    }

    public void loadRemanenteMensualByAño() {
        if (año == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            año = calendar.get(calendar.YEAR);
        }
        btnActivated = Boolean.TRUE;
        displayUploadEdit = Boolean.FALSE;
        displaySolicitud = Boolean.FALSE;
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        remanenteMensualDTOSelected = new RemanenteMensualDTO();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        mesSelected = "Sin Selección";
    }

    public void onRowSelectRemanenteMensual() {
        switch (remanenteMensualDTOSelected.getRemanenteMensual().getMes()) {
            case 1:
                mesSelected = "Enero";
                break;
            case 2:
                mesSelected = "Febrero";
                break;
            case 3:
                mesSelected = "Marzo";
                break;
            case 4:
                mesSelected = "Abril";
                break;
            case 5:
                mesSelected = "Mayo";
                break;
            case 6:
                mesSelected = "Junio";
                break;
            case 7:
                mesSelected = "Julio";
                break;
            case 8:
                mesSelected = "Agosto";
                break;
            case 9:
                mesSelected = "Septiembre";
                break;
            case 10:
                mesSelected = "Octubre";
                break;
            case 11:
                mesSelected = "Noviembre";
                break;
            case 12:
                mesSelected = "Diciembre";
                break;
        }
        btnActivated = Boolean.FALSE;
        displayUploadEdit = Boolean.TRUE;
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);

        String ultimoEstadoMensual = remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList().get(remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList().size() - 1).getDescripcion();
        String ultimoEstadoCuatrimestral = remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getEstadoRemanenteCuatrimestralList().get(remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getEstadoRemanenteCuatrimestralList().size() - 1).getDescripcion();

        if (ultimoEstadoMensual.equals("GeneradoAutomaticamente")
                || ultimoEstadoMensual.equals("Verificado-Rechazado")
                || ultimoEstadoMensual.equals("GeneradoNuevaVersion")) {
            btnActivated = Boolean.FALSE;
            displayUploadEdit = Boolean.TRUE;
            if (diasNoLaborablesServicio.habilitarDiasAdicionales(remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(), remanenteMensualDTOSelected.getRemanenteMensual().getMes())) {
                btnActivated = Boolean.FALSE;
                displayUploadEdit = Boolean.TRUE;
            } else {
                btnActivated = Boolean.TRUE;
                displayUploadEdit = Boolean.FALSE;
            }
        } else {
            btnActivated = Boolean.TRUE;
            displayUploadEdit = Boolean.FALSE;
        }

        if ((ultimoEstadoMensual.equals("Verificado-Aprobado")
                || ultimoEstadoMensual.equals("Validado-Aprobado")
                || ultimoEstadoMensual.equals("Validado-Rechazado"))
                && (!ultimoEstadoCuatrimestral.equals("InformeTecnicoSubido")
                && !ultimoEstadoCuatrimestral.equals("InformeSubido"))) {
            displaySolicitud = Boolean.TRUE;
            if (remanenteMensualDTOSelected.getRemanenteMensual().getSolicitudCambioUrl() == null) {
                disabledBtnEnvCan = Boolean.TRUE;
            } else {
                disabledBtnEnvCan = Boolean.FALSE;
            }
        } else {
            displaySolicitud = Boolean.FALSE;
        }
        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion().getInstitucionId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                remanenteMensualDTOSelected.getRemanenteMensual().getMes(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId());
        for (Transaccion t : transaccionList) {
            if (t.getCatalogoTransaccion().getTipo().equals("Ingreso-Propiedad")) {
                transaccionRPropiedadList.add(t);
                if (!t.getCatalogoTransaccion().getCatalogoTransaccionId().equals(4)) {
                    totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
                }
            } else if (t.getCatalogoTransaccion().getTipo().equals("Ingreso-Mercantil")) {
                transaccionRMercantilList.add(t);
                if (!t.getCatalogoTransaccion().getCatalogoTransaccionId().equals(8)) {
                    totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
                }
            } else if (t.getCatalogoTransaccion().getTipo().equals("Egreso")) {
                transaccionEgresosList.add(t);
                totalEgresos = totalEgresos.add(t.getValorTotal());
            }
        }
        Collections.sort(transaccionRPropiedadList, new Comparator<Transaccion>() {
            @Override
            public int compare(Transaccion t1, Transaccion t2) {
                return new Integer(t1.getCatalogoTransaccion().getCatalogoTransaccionId()).compareTo(new Integer(t2.getCatalogoTransaccion().getCatalogoTransaccionId()));
            }
        });
        Collections.sort(transaccionRMercantilList, new Comparator<Transaccion>() {
            @Override
            public int compare(Transaccion t1, Transaccion t2) {
                return new Integer(t1.getCatalogoTransaccion().getCatalogoTransaccionId()).compareTo(new Integer(t2.getCatalogoTransaccion().getCatalogoTransaccionId()));
            }
        });
        Collections.sort(transaccionEgresosList, new Comparator<Transaccion>() {
            @Override
            public int compare(Transaccion t1, Transaccion t2) {
                return new Integer(t1.getCatalogoTransaccion().getCatalogoTransaccionId()).compareTo(new Integer(t2.getCatalogoTransaccion().getCatalogoTransaccionId()));
            }
        });

    }

    public void rowTransaccionEdit(RowEditEvent event) {
        Transaccion transaccion = new Transaccion();
        transaccion = (Transaccion) event.getObject();
        if (transaccion.getValorTotal() == null) {
            transaccion.setValorTotal(BigDecimal.ZERO);
        }
        transaccionServicio.editTransaccion(transaccion);
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion().getInstitucionId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                remanenteMensualDTOSelected.getRemanenteMensual().getMes(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId());
        for (Transaccion t : transaccionRPropiedadList) {
            if (!t.getCatalogoTransaccion().getCatalogoTransaccionId().equals(4)) {
                totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
            }
        }
        for (Transaccion t : transaccionRMercantilList) {
            if (!t.getCatalogoTransaccion().getCatalogoTransaccionId().equals(8)) {
                totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
            }
        }
        for (Transaccion t : transaccionEgresosList) {
            totalEgresos = totalEgresos.add(t.getValorTotal());
        }
    }

    public void visualizarArchivoTransaccion() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.REMANENTE_TRANSACCION.name()).getValor().concat(rutaArchivo));
            byte[] contenido = transaccionServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error: Archivo no disponible", "");
            }
        }
    }

    public void eliminarArchivo() {
        transaccionSelected.setRespaldoUrl(null);
        transaccionServicio.update(transaccionSelected);
    }

    public void visualizarArchivoSolicitudCambio() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.REMANENTE_SOLICITUD_CAMBIO.name()).getValor().concat(rutaArchivo));
            byte[] contenido = remanenteMensualServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error: Archivo no disponible", "");
            }
        }
    }

    public void visualizarArchivoInformeSolicitudCambio() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.REMANENTE_INFORME_SOLICITUD_CAMBIO.name()).getValor().concat(rutaArchivo));
            byte[] contenido = remanenteMensualServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error: Archivo no disponible", "");
            }
        }
    }

    public void completarRemanenteMensual() {
        List<EstadoRemanenteMensual> estadoRemanenteMensualList = new ArrayList<EstadoRemanenteMensual>();
        estadoRemanenteMensualList = remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList();
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual());
        erm.setUsuario(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("Completo");
        estadoRemanenteMensualServicio.create(erm);
        btnActivated = Boolean.TRUE;
        displayUploadEdit = Boolean.FALSE;
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = institucionServicio.findByPk(Integer.parseInt(this.getSessionVariable("institucionId")));
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Verificador", "REM-Registrador", 1, remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        String mensajeNotificacion = "Se le asignó la Verificación del Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " del " + institucionNotificacion.getNombre();
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        //FIN ENVIO//
    }

    public void uploadTransaccion(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());

            String realPath = (Calendar.getInstance().get(Calendar.YEAR) + "/").concat(transaccionSelected.getTransaccionId().toString()).concat(".pdf");
            sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.REMANENTE_TRANSACCION.name()).getValor().concat(realPath));
            sftpDto.setArchivo(fileByte);
            transaccionSelected.setRespaldoUrl(realPath);
            transaccionServicio.editTransaccion(transaccionSelected, sftpDto);
            fileByte = null;
            PrimeFaces.current().executeScript("PF('transaccionUploadDlg').hide()");
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void uploadSolicitud(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());
            String realPath = (Calendar.getInstance().get(Calendar.YEAR) + "/").concat("sc_" + remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId().toString()).concat(".pdf");
            sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.REMANENTE_SOLICITUD_CAMBIO.name()).getValor().concat(realPath));
            sftpDto.setArchivo(fileByte);
            remanenteMensualDTOSelected.getRemanenteMensual().setSolicitudCambioUrl(realPath);
            remanenteMensualServicio.editRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual(), sftpDto);
            fileByte = null;
            disabledBtnEnvCan = Boolean.FALSE;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarSolicitudCambio() {
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual());
        erm.setUsuario(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("CambioSolicitado");
        estadoRemanenteMensualServicio.create(erm);
        displaySolicitud = Boolean.FALSE;
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = institucionServicio.findByPk(Integer.parseInt(this.getSessionVariable("institucionId")));
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Verificador", "REM-Registrador", 1, remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        String mensajeNotificacion = "Se ha realizado una solicitud de cambio para el Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " del " + institucionNotificacion.getNombre();
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        /////
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Validador", "REM-Registrador", 1, remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        /////
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Administrador", "REM-Registrador", 1, remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        //FIN ENVIO//
    }

    public void cancelarSolicitudCambio() {
        remanenteMensualDTOSelected.getRemanenteMensual().setSolicitudCambioUrl(null);
        remanenteMensualServicio.editRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual());
        disabledBtnEnvCan = Boolean.TRUE;
    }

    public void detalleRPropiedad() {
        tituloDetalleDlg = "Registro de la Propiedad";
        tramiteRPropiedadMercantilList = new ArrayList<Tramite>();
        for (Transaccion transaccion : transaccionRPropiedadList) {
            if (transaccion.getCatalogoTransaccion().getCatalogoTransaccionId().equals(1)) {
                for (Tramite tramite : transaccion.getTramiteList()) {
                    tramiteRPropiedadMercantilList.add(tramite);
                }
            } else if (transaccion.getCatalogoTransaccion().getCatalogoTransaccionId().equals(2)) {
                for (Tramite tramite : transaccion.getTramiteList()) {
                    tramiteRPropiedadMercantilList.add(tramite);
                }
            }
        }
    }

    public void detalleRMercantil() {
        tituloDetalleDlg = "Registro Mercantil";
        tramiteRPropiedadMercantilList = new ArrayList<Tramite>();
        for (Transaccion transaccion : transaccionRMercantilList) {
            if (transaccion.getCatalogoTransaccion().getCatalogoTransaccionId().equals(5)) {
                for (Tramite tramite : transaccion.getTramiteList()) {
                    tramiteRPropiedadMercantilList.add(tramite);
                }
            } else if (transaccion.getCatalogoTransaccion().getCatalogoTransaccionId().equals(6)) {
                for (Tramite tramite : transaccion.getTramiteList()) {
                    tramiteRPropiedadMercantilList.add(tramite);
                }
            }
        }
    }

    public void detalleEgresos() {
        egresoNominaList = new ArrayList<Nomina>();
        egresoFacturaList = new ArrayList<FacturaPagada>();
        for (Transaccion transaccion : transaccionEgresosList) {
            if (transaccion.getCatalogoTransaccion().getCatalogoTransaccionId().equals(9)) {
                for (Nomina nomina : transaccion.getNominaList()) {
                    egresoNominaList.add(nomina);
                }
            } else if (transaccion.getCatalogoTransaccion().getCatalogoTransaccionId().equals(10)
                    || transaccion.getCatalogoTransaccion().getCatalogoTransaccionId().equals(11)
                    || transaccion.getCatalogoTransaccion().getCatalogoTransaccionId().equals(12)) {
                for (FacturaPagada facturaPagada : transaccion.getFacturaPagadaList()) {
                    egresoFacturaList.add(facturaPagada);
                }
            }
        }
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public String getMesSelected() {
        return mesSelected;
    }

    public void setMesSelected(String mesSelected) {
        this.mesSelected = mesSelected;
    }

    public List<Transaccion> getTransaccionRPropiedadList() {
        return transaccionRPropiedadList;
    }

    public void setTransaccionRPropiedadList(List<Transaccion> transaccionRPropiedadList) {
        this.transaccionRPropiedadList = transaccionRPropiedadList;
    }

    public List<Transaccion> getTransaccionRMercantilList() {
        return transaccionRMercantilList;
    }

    public void setTransaccionRMercantilList(List<Transaccion> transaccionRMercantilList) {
        this.transaccionRMercantilList = transaccionRMercantilList;
    }

    public List<Transaccion> getTransaccionEgresosList() {
        return transaccionEgresosList;
    }

    public void setTransaccionEgresosList(List<Transaccion> transaccionEgresosList) {
        this.transaccionEgresosList = transaccionEgresosList;
    }

    public BigDecimal getTotalIngRPropiedad() {
        return totalIngRPropiedad;
    }

    public void setTotalIngRPropiedad(BigDecimal totalIngRPropiedad) {
        this.totalIngRPropiedad = totalIngRPropiedad;
    }

    public BigDecimal getTotalIngRMercantil() {
        return totalIngRMercantil;
    }

    public void setTotalIngRMercantil(BigDecimal totalIngRMercantil) {
        this.totalIngRMercantil = totalIngRMercantil;
    }

    public BigDecimal getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(BigDecimal totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    public Transaccion getTransaccionSelected() {
        return transaccionSelected;
    }

    public void setTransaccionSelected(Transaccion transaccionSelected) {
        this.transaccionSelected = transaccionSelected;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public List<Transaccion> getTransaccionList() {
        return transaccionList;
    }

    public void setTransaccionList(List<Transaccion> transaccionList) {
        this.transaccionList = transaccionList;
    }

    public Boolean getBtnActivated() {
        return btnActivated;
    }

    public void setBtnActivated(Boolean btnActivated) {
        this.btnActivated = btnActivated;
    }

    public Boolean getDisplayUploadEdit() {
        return displayUploadEdit;
    }

    public void setDisplayUploadEdit(Boolean displayUploadEdit) {
        this.displayUploadEdit = displayUploadEdit;
    }

    public Boolean getDisplaySolicitud() {
        return displaySolicitud;
    }

    public void setDisplaySolicitud(Boolean displaySolicitud) {
        this.displaySolicitud = displaySolicitud;
    }

    public String getTituloDetalleDlg() {
        return tituloDetalleDlg;
    }

    public void setTituloDetalleDlg(String tituloDetalleDlg) {
        this.tituloDetalleDlg = tituloDetalleDlg;
    }

    public List<Tramite> getTramiteRPropiedadMercantilList() {
        return tramiteRPropiedadMercantilList;
    }

    public void setTramiteRPropiedadMercantilList(List<Tramite> tramiteRPropiedadMercantilList) {
        this.tramiteRPropiedadMercantilList = tramiteRPropiedadMercantilList;
    }

    public List<Nomina> getEgresoNominaList() {
        return egresoNominaList;
    }

    public void setEgresoNominaList(List<Nomina> egresoNominaList) {
        this.egresoNominaList = egresoNominaList;
    }

    public List<FacturaPagada> getEgresoFacturaList() {
        return egresoFacturaList;
    }

    public void setEgresoFacturaList(List<FacturaPagada> egresoFacturaList) {
        this.egresoFacturaList = egresoFacturaList;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Boolean getDisabledBtnEnvCan() {
        return disabledBtnEnvCan;
    }

    public void setDisabledBtnEnvCan(Boolean disabledBtnEnvCan) {
        this.disabledBtnEnvCan = disabledBtnEnvCan;
    }

    public List<RemanenteMensualDTO> getRemanenteMensualDTOList() {
        return remanenteMensualDTOList;
    }

    public void setRemanenteMensualDTOList(List<RemanenteMensualDTO> remanenteMensualDTOList) {
        this.remanenteMensualDTOList = remanenteMensualDTOList;
    }

    public RemanenteMensualDTO getRemanenteMensualDTOSelected() {
        return remanenteMensualDTOSelected;
    }

    public void setRemanenteMensualDTOSelected(RemanenteMensualDTO remanenteMensualDTOSelected) {
        this.remanenteMensualDTOSelected = remanenteMensualDTOSelected;
    }

}
