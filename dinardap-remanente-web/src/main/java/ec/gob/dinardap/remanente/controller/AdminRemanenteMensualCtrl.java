package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.constante.PerfilEnum;
import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.remanente.dto.RemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.EstadoRemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.FacturaPagadaServicio;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.TramiteServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;

import org.primefaces.model.UploadedFile;

@Named(value = "adminRemanenteMensualCtrl")
@ViewScoped
public class AdminRemanenteMensualCtrl extends BaseCtrl implements Serializable {

    private List<Institucion> institucionList;
    private Institucion institucionSelected;
    private Boolean displaySolicitud;
    private Boolean disabledBtnAproRechCan;

    private String tituloPagina;
    private String nombreInstitucion;
    private String rutaArchivo;
    private Integer año;
    private List<RemanenteMensualDTO> remanenteMensualDTOList;
    private RemanenteMensualDTO remanenteMensualDTOSelected;
    private String mesSelected;
    private Integer institucionId;
    private Integer usuarioId;
    private String tituloDetalleDlg;
    private Institucion institucionNotificacion;
    private List<Usuario> usuarioListNotificacion;

    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private Transaccion transaccionSelected;

    private List<Transaccion> transaccionRPropiedadList;
    private List<Transaccion> transaccionRMercantilList;
    private List<Transaccion> transaccionEgresosList;
    private List<Transaccion> transaccionList;
    private List<Tramite> tramiteRPropiedadMercantilList;
    private List<Nomina> egresoNominaList;
    private List<FacturaPagada> egresoFacturaList;

    private Boolean btnActivated;
    private Boolean displayComment;
    private String comentariosRechazo;

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
    private TramiteServicio tramiteServicio;
    @EJB
    private NominaServicio nominaServicio;
    @EJB
    private FacturaPagadaServicio facturaPagadaServicio;
    @EJB
    private BandejaServicio bandejaServicio;
    @EJB
    private UsuarioServicio usuarioServicio;
    @EJB
    private ParametroServicio parametroServicio;

    @PostConstruct
    protected void init() {
        sftpDto = new SftpDto();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        tituloPagina = "Administración Remanente Mensual";
        institucionList = new ArrayList<Institucion>();
        institucionList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.RMX_AUTONOMIA_FINANCIERA.getTipoInstitucion()));
        institucionList.addAll(institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.RMX_SIN_AUTONOMIA_FINANCIERA.getTipoInstitucion()));
        institucionSelected = new Institucion();
        año = 0;
        año = calendar.get(Calendar.YEAR);
        tituloDetalleDlg = "";
        mesSelected = "Sin Selección";
        remanenteMensualDTOSelected = new RemanenteMensualDTO();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        displaySolicitud = Boolean.FALSE;
        remanenteMensualDTOList = new ArrayList<RemanenteMensualDTO>();
        transaccionSelected = new Transaccion();
        disabledBtnAproRechCan = Boolean.TRUE;
    }

    public void onRowSelectInstitucion() {
        institucionId = institucionSelected.getInstitucionId();
        nombreInstitucion = institucionSelected.getNombre();
        remanenteMensualDTOList = new ArrayList<RemanenteMensualDTO>();
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);

        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();

        transaccionSelected = new Transaccion();
        displaySolicitud = Boolean.FALSE;
    }

    public void loadRemanenteMensualByAño() {
        if (año == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            año = calendar.get(calendar.YEAR);
        }
        btnActivated = Boolean.TRUE;
        displaySolicitud = Boolean.FALSE;
        displayComment = Boolean.FALSE;
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
        remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId();
        btnActivated = Boolean.FALSE;
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        Collections.sort(remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList(), new Comparator<EstadoRemanenteMensual>() {
            @Override
            public int compare(EstadoRemanenteMensual erm1, EstadoRemanenteMensual erm2) {
                return new Integer(erm1.getEstadoRemanenteMensualId()).compareTo(new Integer(erm2.getEstadoRemanenteMensualId()));
            }
        });
        if (remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList().get(remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("Verificado-Aprobado")) {
            btnActivated = Boolean.FALSE;
        } else {
            btnActivated = Boolean.TRUE;
        }

        if (remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList().get(remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("CambioSolicitado")) {
            displaySolicitud = Boolean.TRUE;
            if (remanenteMensualDTOSelected.getRemanenteMensual().getInformeAprobacionUrl() == null) {
                disabledBtnAproRechCan = Boolean.TRUE;
            } else {
                disabledBtnAproRechCan = Boolean.FALSE;
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

    public void uploadInformeSolicitudCambio(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());
            String realPath = (año + "/").concat("isc_" + remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId().toString()).concat(".pdf");
            sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.REMANENTE_INFORME_SOLICITUD_CAMBIO.name()).getValor().concat(realPath));
            sftpDto.setArchivo(fileByte);
            remanenteMensualDTOSelected.getRemanenteMensual().setInformeAprobacionUrl(realPath);
            remanenteMensualServicio.editRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual(), sftpDto);
            fileByte = null;
            disabledBtnAproRechCan = Boolean.FALSE;
        } catch (IOException ex) {
            Logger.getLogger(AdminRemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void aprobarSolicitudCambio() {
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual());
        erm.setUsuario(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("CambioAprobado");
        estadoRemanenteMensualServicio.create(erm);
        displaySolicitud = Boolean.FALSE;
        crearVersionRemanente(remanenteMensualDTOSelected.getRemanenteMensual());

        //ENVIO DE NOTIFICACION//
        institucionNotificacion = remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion();
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                PerfilEnum.REGISTRADOR.getPerfilId(), PerfilEnum.ADMINISTRADOR.getPerfilId(), remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        String mensajeNotificacion = "Su solicitud de cambio para el Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " ha sido APROBADA.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        /////
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                PerfilEnum.VERIFICADOR.getPerfilId(), PerfilEnum.ADMINISTRADOR.getPerfilId(), remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        mensajeNotificacion = "La solicitud de cambio para el Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " del " + institucionNotificacion.getNombre() + " ha sido APROBADA.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        /////
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                PerfilEnum.VALIDADOR.getPerfilId(), PerfilEnum.ADMINISTRADOR.getPerfilId(), remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        //FIN ENVIO//

        remanenteMensualDTOSelected = new RemanenteMensualDTO();
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
    }

    public void rechazarSolicitudCambio() {
        remanenteMensualServicio.editRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual(), sftpDto);
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual());
        erm.setUsuario(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("CambioRechazado");
        estadoRemanenteMensualServicio.create(erm);
        displaySolicitud = Boolean.FALSE;
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion();
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                PerfilEnum.REGISTRADOR.getPerfilId(), PerfilEnum.ADMINISTRADOR.getPerfilId(), remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        String mensajeNotificacion = "Su solicitud de cambio para el Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " ha sido RECHAZADA.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        /////
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                PerfilEnum.VERIFICADOR.getPerfilId(), PerfilEnum.ADMINISTRADOR.getPerfilId(), remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        mensajeNotificacion = "La solicitud de cambio para el Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " del " + institucionNotificacion.getNombre() + " ha sido RECHAZADA.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        /////
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                PerfilEnum.VALIDADOR.getPerfilId(), PerfilEnum.ADMINISTRADOR.getPerfilId(), remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        //FIN ENVIO//
    }

    public void cancelarInformeSolicitudCambio() {
        remanenteMensualDTOSelected.getRemanenteMensual().setInformeAprobacionUrl(null);
        remanenteMensualServicio.editRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual());
        disabledBtnAproRechCan = Boolean.TRUE;
    }

    public void crearVersionRemanente(RemanenteMensual remanenteMensual) {
        RemanenteMensual rmn = new RemanenteMensual();
        Integer remanenteMensualOrigenID = remanenteMensual.getRemanenteMensualId();
        remanenteMensualServicio.crearVersionRemanenteMensual(remanenteMensual);
        rmn = remanenteMensualServicio.obtenerVersionRemanenteMensual(remanenteMensualOrigenID);
        //Creación de nuevo estado
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensual(rmn);
        erm.setUsuario(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("GeneradoNuevaVersion");
        estadoRemanenteMensualServicio.create(erm);
        //Creación de transacciones
        for (Transaccion transaccion : remanenteMensual.getTransaccionList()) {
            transaccion.setTransaccionId(null);
            transaccion.setFechaRegistro(new Date());
            transaccion.setRemanenteMensual(rmn);
            transaccionServicio.create(transaccion);
        }
        List<Transaccion> transaccionListNuevoRemanente = new ArrayList<Transaccion>();
        transaccionListNuevoRemanente = transaccionServicio.getTransaccionByInstitucionAñoMes(rmn.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion().getInstitucionId(),
                rmn.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                rmn.getMes(),
                rmn.getRemanenteMensualId());
        for (Transaccion transaccionOrigen : remanenteMensual.getTransaccionList()) {
            //Creación de trámites              
            for (Tramite tramiteOrigen : transaccionOrigen.getTramiteList()) {
                for (Transaccion transaccionNueva : transaccionListNuevoRemanente) {
                    if (tramiteOrigen.getTransaccion().getCatalogoTransaccion().getCatalogoTransaccionId()
                            .equals(transaccionNueva.getCatalogoTransaccion().getCatalogoTransaccionId())) {
                        tramiteOrigen.setTramiteId(null);
                        tramiteOrigen.setTransaccion(transaccionNueva);
                        tramiteOrigen.setFechaRegistro(new Date());
                        tramiteServicio.create(tramiteOrigen);
                        break;
                    }
                }
            }
            //Creacion de nómina
            for (Nomina nominaOrigen : transaccionOrigen.getNominaList()) {
                for (Transaccion transaccionNueva : transaccionListNuevoRemanente) {
                    if (nominaOrigen.getTransaccion().getCatalogoTransaccion().getCatalogoTransaccionId()
                            .equals(transaccionNueva.getCatalogoTransaccion().getCatalogoTransaccionId())) {
                        nominaOrigen.setNominaId(null);
                        nominaOrigen.setTransaccion(transaccionNueva);
                        nominaOrigen.setFechaRegistro(new Date());
                        nominaServicio.create(nominaOrigen);
                        break;
                    }
                }
            }
            //Creación de facturas pagadas
            for (FacturaPagada facturaPagadaOrigen : transaccionOrigen.getFacturaPagadaList()) {
                for (Transaccion transaccionNueva : transaccionListNuevoRemanente) {
                    if (facturaPagadaOrigen.getTransaccion().getCatalogoTransaccion().getCatalogoTransaccionId()
                            .equals(transaccionNueva.getCatalogoTransaccion().getCatalogoTransaccionId())) {
                        facturaPagadaOrigen.setFacturaPagadaId(null);
                        facturaPagadaOrigen.setTransaccion(transaccionNueva);
                        facturaPagadaOrigen.setFechaRegistro(new Date());
                        facturaPagadaServicio.create(facturaPagadaOrigen);
                        break;
                    }
                }
            }
        }

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

    public void visualizarArchivoSolicitudCambio() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.REMANENTE_SOLICITUD_CAMBIO.name()).getValor().concat(rutaArchivo));
            byte[] contenido = remanenteMensualServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error", "Archivo no disponible");
            }
        }
    }

    public void visualizarArchivoInformeSolicitudCambio() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.REMANENTE_INFORME_SOLICITUD_CAMBIO.name()).getValor().concat(rutaArchivo));
            byte[] contenido = remanenteMensualServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error", "Archivo no disponible");
            }
        }
    }

    public void visualizarArchivoTransaccion() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.SFTP_RUTA_REMANENTE.name()).getValor() + parametroServicio.findByPk(ParametroEnum.REMANENTE_TRANSACCION.name()).getValor().concat(rutaArchivo));
            byte[] contenido = transaccionServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error", "Archivo no disponible");
            }
        }
    }

    /*===Getters & Setters===*/
    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<RemanenteMensualDTO> getRemanenteMensualDTOList() {
        return remanenteMensualDTOList;
    }

    public void setRemanenteMensualDTOList(List<RemanenteMensualDTO> remanenteMensualDTOList) {
        this.remanenteMensualDTOList = remanenteMensualDTOList;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public RemanenteMensualDTO getRemanenteMensualDTOSelected() {
        return remanenteMensualDTOSelected;
    }

    public void setRemanenteMensualDTOSelected(RemanenteMensualDTO remanenteMensualDTOSelected) {
        this.remanenteMensualDTOSelected = remanenteMensualDTOSelected;
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

    public Boolean getDisplaySolicitud() {
        return displaySolicitud;
    }

    public void setDisplaySolicitud(Boolean displaySolicitud) {
        this.displaySolicitud = displaySolicitud;
    }

    public Boolean getDisabledBtnAproRechCan() {
        return disabledBtnAproRechCan;
    }

    public void setDisabledBtnAproRechCan(Boolean disabledBtnAproRechCan) {
        this.disabledBtnAproRechCan = disabledBtnAproRechCan;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
}
