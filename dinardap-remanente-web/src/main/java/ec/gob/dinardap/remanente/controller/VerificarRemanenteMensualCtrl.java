package ec.gob.dinardap.remanente.controller;

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
import ec.gob.dinardap.remanente.utils.FacesUtils;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "verificarRemanenteMensualCtrl")
@ViewScoped
public class VerificarRemanenteMensualCtrl extends BaseCtrl implements Serializable {

    private UploadedFile file;
    private String tituloPagina;
    private String nombreInstitucion;
    private Integer año;
    private List<RemanenteMensual> remanenteMensualList;
    private RemanenteMensual remanenteMensualSelected;
    private String mesSelected;
    private Integer institucionId;
    private Integer usuarioId;
    private String tituloDetalleDlg;
    private InstitucionRequerida institucionNotificacion;
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

    @PostConstruct
    protected void init() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        tituloPagina = "Gestión Remanente Mensual";
        año = 0;
        año = calendar.get(Calendar.YEAR);
        tituloDetalleDlg = "";
        mesSelected = "Sin Selección";
        remanenteMensualSelected = new RemanenteMensual();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        comentariosRechazo = "";
        transaccionSelected = new Transaccion();
        institucionId = this.getInstitucionID(this.getSessionVariable("perfil"));
        System.out.println("INstitucionID: " + institucionId);

        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        nombreInstitucion = institucionRequeridaServicio.getInstitucionById(institucionId).getNombre();
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);

//        String a = SemillaEnum.SEMILLA_REMANENTE.getSemilla() + "D1N4Rd4p.2019";
//        EncriptarCadenas.encriptarCadenaSha1(a);
//        System.out.println("a = " + EncriptarCadenas.encriptarCadenaSha1(a));
    }

    public void loadRemanenteMensualByAño() {
        if (año == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            año = calendar.get(calendar.YEAR);
        }
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        remanenteMensualSelected = new RemanenteMensual();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        mesSelected = "Sin Selección";
    }

    public void onRowSelectRemanenteMensual() {
        switch (remanenteMensualSelected.getMes()) {
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
        Collections.sort(remanenteMensualSelected.getEstadoRemanenteMensualList(), new Comparator<EstadoRemanenteMensual>() {
            @Override
            public int compare(EstadoRemanenteMensual erm1, EstadoRemanenteMensual erm2) {
                return new Integer(erm1.getEstadoRemanenteMensualId()).compareTo(new Integer(erm2.getEstadoRemanenteMensualId()));
            }
        });
        if (remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("Completo")) {
            btnActivated = Boolean.FALSE;
        } else {
            btnActivated = Boolean.TRUE;
        }

        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                remanenteMensualSelected.getMes(),
                remanenteMensualSelected.getRemanenteMensualId());
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

    public void rowTransaccionEdit(RowEditEvent event) {
        Transaccion transaccion = new Transaccion();
        transaccion = (Transaccion) event.getObject();
        transaccionServicio.editTransaccion(transaccion);
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                remanenteMensualSelected.getMes(),
                remanenteMensualSelected.getRemanenteMensualId());
        for (Transaccion t : transaccionRPropiedadList) {
            if (!t.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(4)) {
                totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
            }
        }
        for (Transaccion t : transaccionRMercantilList) {
            if (!t.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(8)) {
                totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
            }
        }
        for (Transaccion t : transaccionEgresosList) {
            totalEgresos = totalEgresos.add(t.getValorTotal());
        }
    }

    public void rowTransaccionEditCancel() {
        System.out.println("Cancelado");
    }

    public void aprobarRemanenteMensual() {
        System.out.println("===Guardar Remanente Mensual===");
        System.out.println("Remanente mensual ID: " + remanenteMensualSelected.getRemanenteMensualId());
        System.out.println("Remanente mensual Mes: " + remanenteMensualSelected.getMes());
        List<EstadoRemanenteMensual> estadoRemanenteMensualList = new ArrayList<EstadoRemanenteMensual>();
        estadoRemanenteMensualList = remanenteMensualSelected.getEstadoRemanenteMensualList();
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensualId(remanenteMensualSelected);
        erm.setUsuarioId(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("Verificado-Aprobado");
        estadoRemanenteMensualServicio.create(erm);
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        comentariosRechazo = "Remanente Mensual Verificado";
        remanenteMensualSelected.setComentarios(comentariosRechazo);
        remanenteMensualServicio.editRemanenteMensual(remanenteMensualSelected);
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = institucionRequeridaServicio.getInstitucionById(Integer.parseInt(this.getSessionVariable("institucionId")));
        System.out.println("institucionNotificacion id = " + institucionNotificacion.getInstitucionId());
        System.out.println("remanenteMensualSelected = " + remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId());
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Validador", "REM-Verificador", 391, remanenteMensualSelected.getRemanenteCuatrimestral());
        String mensajeNotificacion = "Se le asigno la Validación del Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " del " + institucionNotificacion.getNombre();
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                remanenteMensualSelected.getRemanenteMensualId(),
                mensajeNotificacion, "");
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

        for (Nomina n : egresoNominaList) {
            System.out.println("Nomina: " + n.getNominaId());
            System.out.println("Nomina: " + n.getNomNombres());
        }

        for (FacturaPagada fp : egresoFacturaList) {
            System.out.println("Factura: " + fp.getFacturaPagadaId());
            System.out.println("Factura: " + fp.getDetalle());
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
        System.out.println("===Guardar Remanente Mensual===");
        System.out.println("Remanente mensual ID: " + remanenteMensualSelected.getRemanenteMensualId());
        System.out.println("Remanente mensual Mes: " + remanenteMensualSelected.getMes());
        List<EstadoRemanenteMensual> estadoRemanenteMensualList = new ArrayList<EstadoRemanenteMensual>();
        estadoRemanenteMensualList = remanenteMensualSelected.getEstadoRemanenteMensualList();
        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erm.setRemanenteMensualId(remanenteMensualSelected);
        erm.setUsuarioId(u);
        erm.setFechaRegistro(new Date());
        erm.setDescripcion("Verificado-Rechazado");
        estadoRemanenteMensualServicio.create(erm);
        btnActivated = Boolean.TRUE;
        displayComment = Boolean.FALSE;
        remanenteMensualSelected.setComentarios(comentariosRechazo);
        remanenteMensualServicio.editRemanenteMensual(remanenteMensualSelected);
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
        //ENVIO DE NOTIFICACION//
        institucionNotificacion = institucionRequeridaServicio.getInstitucionById(Integer.parseInt(this.getSessionVariable("institucionId")));
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Registrador", "REM-Verificador", 391, remanenteMensualSelected.getRemanenteCuatrimestral());
        String mensajeNotificacion = "Se le ha reasignado el Remanente Mensual correspondiente al mes de " + mesSelected + " del año " + año + " con estado RECHAZADO.";
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteMensualSelected.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                remanenteMensualSelected.getRemanenteMensualId(),
                mensajeNotificacion, "");
        //FIN ENVIO//
    }

    public void handleFileUploadRPropiedad(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        try {
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());
            String path = FacesUtils.getPath() + "/archivos/transacciones/";
            String realPath = path + transaccionSelected.getTransaccionId() + ".pdf";
            FileOutputStream fos = new FileOutputStream(realPath);
            fos.write(fileByte);
            fos.close();
            transaccionSelected.setRespaldoUrl("/archivos/transacciones/" + transaccionSelected.getTransaccionId() + ".pdf");
            transaccionServicio.editTransaccion(transaccionSelected);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VerificarRemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VerificarRemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verEstadoRemanenteMensualSeleccionado() {
        System.out.println("Remanente: " + remanenteMensualSelected.getMes());
        for (EstadoRemanenteMensual erm : remanenteMensualSelected.getEstadoRemanenteMensualList()) {
            System.out.println("Estado: " + erm.getUsuarioId().getNombre());
            System.out.println("Estado: " + erm.getFechaRegistro());
            System.out.println("Estado: " + erm.getDescripcion());
        }
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public RemanenteMensual getRemanenteMensualSelected() {
        return remanenteMensualSelected;
    }

    public void setRemanenteMensualSelected(RemanenteMensual remanenteMensualSelected) {
        this.remanenteMensualSelected = remanenteMensualSelected;
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
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

}
