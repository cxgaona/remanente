package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dto.RemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.EstadoRemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "validarRemanenteMensualCtrl")
@ViewScoped
public class ValidarRemanenteMensualCtrl extends BaseCtrl implements Serializable {

    //Variables
    private Integer usuarioId;
    private Integer direccionRegionalId;
    private String nombreDireccionRegional;

    private Integer institucionId;
    private String nombreInstitucion;

    private String tituloPagina;
    private Integer año;
    private List<RemanenteMensualDTO> remanenteMensualDTOList;
    private RemanenteMensualDTO remanenteMensualDTOSelected;
    private String mesSelected;
    private String tituloDetalleDlg;
    private InstitucionRequerida institucionNotificacion;
    private List<Usuario> usuarioListNotificacion;
    private String rutaArchivo;

    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private Transaccion transaccionSelected;
    private InstitucionRequerida institucionSelected;

    private List<Transaccion> transaccionRPropiedadList;
    private List<Transaccion> transaccionRMercantilList;
    private List<Transaccion> transaccionEgresosList;
    private List<Transaccion> transaccionList;
    private List<Tramite> tramiteRPropiedadMercantilList;
    private List<Nomina> egresoNominaList;
    private List<FacturaPagada> egresoFacturaList;
    private List<InstitucionRequerida> institucionList;

    private Boolean btnActivated;
    private Boolean displayComment;
    private Boolean disabledBtnReload;
    private String comentariosRechazo;

    private SftpDto sftpDto;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

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

    @PostConstruct
    protected void init() {
        //Session
        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        direccionRegionalId = Integer.parseInt(this.getSessionVariable("institucionId"));
        nombreDireccionRegional = institucionRequeridaServicio.getInstitucionById(direccionRegionalId).getNombre();

        //Inicialiación de Variables
        tituloPagina = "Gestión Remanente Mensual";
        mesSelected = "Sin selección";
        nombreInstitucion = "Sin selección";
        institucionId = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
        tituloDetalleDlg = "";
        comentariosRechazo = "";
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        disabledBtnReload = Boolean.TRUE;

        institucionSelected = new InstitucionRequerida();
        remanenteMensualDTOSelected = new RemanenteMensualDTO();
        transaccionSelected = new Transaccion();
        sftpDto = new SftpDto();

        institucionList = new ArrayList<InstitucionRequerida>();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        remanenteMensualDTOList = new ArrayList<RemanenteMensualDTO>();

        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);

        institucionList = institucionRequeridaServicio.getRegistroMixtoList(direccionRegionalId);
    }

    public void loadRemanenteMensualByAño() {
        if (año == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            año = calendar.get(calendar.YEAR);
        }
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        remanenteMensualDTOSelected = new RemanenteMensualDTO();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        mesSelected = "Sin Selección";
    }

    public void onRowSelectInstitucion() {
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        transaccionSelected = new Transaccion();

        institucionId = institucionSelected.getInstitucionId();
        nombreInstitucion = institucionSelected.getNombre();

        remanenteMensualDTOList = new ArrayList<RemanenteMensualDTO>();
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        disabledBtnReload = Boolean.FALSE;

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

        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                remanenteMensualDTOSelected.getRemanenteMensual().getMes(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId());
        for (Transaccion t : transaccionList) {
            if (t.getCatalogoTransaccionId().getTipo().equals("Ingreso-Propiedad")) {
                transaccionRPropiedadList.add(t);
                if (!t.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(4)) {
                    totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
                }
            } else if (t.getCatalogoTransaccionId().getTipo().equals("Ingreso-Mercantil")) {
                transaccionRMercantilList.add(t);
                if (!t.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(8)) {
                    totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
                }
            } else if (t.getCatalogoTransaccionId().getTipo().equals("Egreso")) {
                transaccionEgresosList.add(t);
                totalEgresos = totalEgresos.add(t.getValorTotal());
            }
        }
        Collections.sort(transaccionRPropiedadList, new Comparator<Transaccion>() {
            @Override
            public int compare(Transaccion t1, Transaccion t2) {
                return new Integer(t1.getCatalogoTransaccionId().getCatalogoTransaccionId()).compareTo(new Integer(t2.getCatalogoTransaccionId().getCatalogoTransaccionId()));
            }
        });
        Collections.sort(transaccionRMercantilList, new Comparator<Transaccion>() {
            @Override
            public int compare(Transaccion t1, Transaccion t2) {
                return new Integer(t1.getCatalogoTransaccionId().getCatalogoTransaccionId()).compareTo(new Integer(t2.getCatalogoTransaccionId().getCatalogoTransaccionId()));
            }
        });
        Collections.sort(transaccionEgresosList, new Comparator<Transaccion>() {
            @Override
            public int compare(Transaccion t1, Transaccion t2) {
                return new Integer(t1.getCatalogoTransaccionId().getCatalogoTransaccionId()).compareTo(new Integer(t2.getCatalogoTransaccionId().getCatalogoTransaccionId()));
            }
        });

    }

    public void aprobarRemanenteMensual() {
        List<EstadoRemanenteMensual> estadoRemanenteMensualList = new ArrayList<EstadoRemanenteMensual>();
        estadoRemanenteMensualList = remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList();
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensualId(remanenteMensualDTOSelected.getRemanenteMensual());
        erm.setUsuarioId(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("Validado-Aprobado");
        estadoRemanenteMensualServicio.create(erm);
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        comentariosRechazo = "Remanente Mensual Validado";
        remanenteMensualDTOSelected.getRemanenteMensual().setComentarios(comentariosRechazo);
        remanenteMensualServicio.editRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual());
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        //ENVIO DE NOTIFICACION//
        //institucionNotificacion = institucionRequeridaServicio.getInstitucionById(Integer.parseInt(this.getSessionVariable("institucionId")));
        institucionNotificacion = remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida();
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Registrador", "REM-Validador", 1, remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        String mensajeNotificacion = "El Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " ha sido APROBADO.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        /////        
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Verificador", "REM-Validador", 1, remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        mensajeNotificacion = "El Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " del " + institucionNotificacion.getNombre() + " ha sido APROBADO.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");
        //FIN ENVIO//
    }

    public void detalleRPropiedad() {
        tituloDetalleDlg = "Registro de la Propiedad";
        tramiteRPropiedadMercantilList = new ArrayList<Tramite>();
        for (Transaccion transaccion : transaccionRPropiedadList) {
            if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(1)) {
                for (Tramite tramite : transaccion.getTramiteList()) {
                    tramiteRPropiedadMercantilList.add(tramite);
                }
            } else if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(2)) {
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
            if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(5)) {
                for (Tramite tramite : transaccion.getTramiteList()) {
                    tramiteRPropiedadMercantilList.add(tramite);
                }
            } else if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(6)) {
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
            if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(9)) {
                for (Nomina nomina : transaccion.getNominaList()) {
                    egresoNominaList.add(nomina);
                }
            } else if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(10)
                    || transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(11)
                    || transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(12)) {
                for (FacturaPagada facturaPagada : transaccion.getFacturaPagadaList()) {
                    egresoFacturaList.add(facturaPagada);
                }
            }
        }
    }

    public void habilitarComentarioRechazo() {
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.TRUE;
    }

    public void cancelar() {
        btnActivated = Boolean.FALSE;
        displayComment = Boolean.FALSE;
    }

    public void rechazarRemanenteMensual() {
        List<EstadoRemanenteMensual> estadoRemanenteMensualList = new ArrayList<EstadoRemanenteMensual>();
        estadoRemanenteMensualList = remanenteMensualDTOSelected.getRemanenteMensual().getEstadoRemanenteMensualList();
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensualId(remanenteMensualDTOSelected.getRemanenteMensual());
        erm.setUsuarioId(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("Validado-Rechazado");
        estadoRemanenteMensualServicio.create(erm);
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        remanenteMensualDTOSelected.getRemanenteMensual().setComentarios(comentariosRechazo);
        remanenteMensualServicio.editRemanenteMensual(remanenteMensualDTOSelected.getRemanenteMensual());
        remanenteMensualDTOList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        //ENVIO DE NOTIFICACION//
//        institucionNotificacion = institucionRequeridaServicio.getInstitucionById(Integer.parseInt(this.getSessionVariable("institucionId")));
        institucionNotificacion = remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida();
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Registrador", "REM-Validador", 1, remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        String mensajeNotificacion = "El Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " ha sido RECHAZADO.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");

        /////
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Verificador", "REM-Validador", 1, remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral());
        mensajeNotificacion = "El Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " del " + institucionNotificacion.getNombre() + " ha sido RECHAZADO.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                remanenteMensualDTOSelected.getRemanenteMensual().getRemanenteMensualId(),
                mensajeNotificacion, "RM");

        //FIN ENVIO//
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

    //Getters & Setters
    public Boolean getDisabledBtnReload() {
        return disabledBtnReload;
    }

    public void setDisabledBtnReload(Boolean disabledBtnReload) {
        this.disabledBtnReload = disabledBtnReload;
    }

    public String getNombreDireccionRegional() {
        return nombreDireccionRegional;
    }

    public void setNombreDireccionRegional(String nombreDireccionRegional) {
        this.nombreDireccionRegional = nombreDireccionRegional;
    }

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

    public List<InstitucionRequerida> getInstitucionList() {
        return institucionList;
    }

    public void setInstitucionList(List<InstitucionRequerida> institucionList) {
        this.institucionList = institucionList;
    }

    public InstitucionRequerida getInstitucionSelected() {
        return institucionSelected;
    }

    public void setInstitucionSelected(InstitucionRequerida institucionSelected) {
        this.institucionSelected = institucionSelected;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

}
