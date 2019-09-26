package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.EstadoRemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteCuatrimestralServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
import org.primefaces.model.UploadedFile;

@Named(value = "remanenteCuatrimestralCtrl")
@ViewScoped
public class RemanenteCuatrimestralCtrl extends BaseCtrl implements Serializable {

    //Variables
    private String tituloPagina;
    private String nombreInstitucion;
    private Integer año;
    private RemanenteCuatrimestral remanenteCuatrimestralSelected;
    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;

    private List<RemanenteCuatrimestral> remanenteCuatrimestralList;
    private List<Row> transaccionRegistrosList;
    private List<Row> transaccionEgresosList;

    private List<BigDecimal> totalIngRPropiedadList;
    private List<BigDecimal> totalIngRMercantilList;
    private List<BigDecimal> totalIngList;
    private List<BigDecimal> factorIncidenciaList;

    @EJB
    private RemanenteCuatrimestralServicio remanenteCuatrimestralServicio;
    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;

    //Anteriores
    private UploadedFile file;
    private UploadedFile fileSolicitud;

    private String mesSelected;
    private Integer institucionId;
    private Integer usuarioId;
    private String tituloDetalleDlg;

    private Transaccion transaccionSelected;

    private List<Transaccion> transaccionList;
    private List<Tramite> tramiteRPropiedadMercantilList;
    private List<Nomina> egresoNominaList;
    private List<FacturaPagada> egresoFacturaList;

    private Boolean btnActivated;
    private Boolean displayUploadEdit;
    private Boolean displaySolicitud;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @EJB
    private EstadoRemanenteMensualServicio estadoRemanenteMensualServicio;

    @PostConstruct
    protected void init() {
        //Session
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        nombreInstitucion = institucionRequeridaServicio.getInstitucionById(institucionId).getNombre();

        //Inicializacion de variables
        remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        remanenteCuatrimestralSelected = new RemanenteCuatrimestral();
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);

        totalIngRPropiedadList = new ArrayList<BigDecimal>();
        totalIngRMercantilList = new ArrayList<BigDecimal>();
        totalIngList = new ArrayList<BigDecimal>();

        //FechaACtual
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
        tituloPagina = "Gestión Remanente Cuatrimestral";

        //Proceso
        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);

        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        btnActivated = Boolean.TRUE;
        displayUploadEdit = Boolean.FALSE;
        displaySolicitud = Boolean.FALSE;
        transaccionSelected = new Transaccion();

//        remanenteCuatrimestralList = new ArrayList<RemanenteMensual>();
//        remanenteCuatrimestralList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
//        String a = SemillaEnum.SEMILLA_REMANENTE.getSemilla() + "D1N4Rd4p.2019";
//        EncriptarCadenas.encriptarCadenaSha1(a);
//        System.out.println("a = " + EncriptarCadenas.encriptarCadenaSha1(a));
    }

    public void loadRemanenteCuatrimestralByAño() {
        remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        remanenteCuatrimestralSelected = new RemanenteCuatrimestral();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);

        if (año == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            año = calendar.get(calendar.YEAR);
        }
        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);
//        btnActivated = Boolean.TRUE;
//        displayUploadEdit = Boolean.FALSE;
//        displaySolicitud = Boolean.FALSE;        
//        mesSelected = "Sin Selección";
    }

    public void onRowSelectRemanenteCuatrimestral() {
        List<RemanenteMensual> rms = new ArrayList<RemanenteMensual>();
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);

        rms = getRemanentesActivos(remanenteCuatrimestralSelected.getRemanenteMensualList());
        List<Row> rows = new ArrayList<Row>();
        for (RemanenteMensual remanenteMensual : rms) {
            Collections.sort(remanenteMensual.getTransaccionList(), new Comparator<Transaccion>() {
                @Override
                public int compare(Transaccion o1, Transaccion o2) {
                    return new Integer(o1.getCatalogoTransaccionId().getCatalogoTransaccionId()).compareTo(new Integer(o2.getCatalogoTransaccionId().getCatalogoTransaccionId()));
                }
            });
        }
        for (CatalogoTransaccion catalogoTransaccion : catalogoTransaccionServicio.getCatalogoTransaccionList()) {
            Row row = new Row();
            row.setNombre(catalogoTransaccion.getNombre());
            rows.add(row);
        }
        int j = 0;
        for (RemanenteMensual remanenteMensual : rms) {
            int i = 0;
            for (Transaccion transaccion : remanenteMensual.getTransaccionList()) {
                switch (j) {
                    case 0:
                        rows.get(i).setValorMes1(transaccion.getValorTotal());
                        rows.get(i).setTipo(transaccion.getCatalogoTransaccionId().getTipo());
                        break;
                    case 1:
                        rows.get(i).setValorMes2(transaccion.getValorTotal());
                        rows.get(i).setTipo(transaccion.getCatalogoTransaccionId().getTipo());
                        break;
                    case 2:
                        rows.get(i).setValorMes3(transaccion.getValorTotal());
                        rows.get(i).setTipo(transaccion.getCatalogoTransaccionId().getTipo());
                        break;
                    case 3:
                        rows.get(i).setValorMes4(transaccion.getValorTotal());
                        rows.get(i).setTipo(transaccion.getCatalogoTransaccionId().getTipo());
                        break;
                }
                i++;
            }
            j++;
        }
        for (Row r : rows) {
            if (r.getTipo().equals("Ingreso-Propiedad") || r.getTipo().equals("Ingreso-Mercantil")) {
                r.setTipoIE("Ingreso");
                transaccionRegistrosList.add(r);
            } else if (r.getTipo().equals("Egreso")) {
                r.setTipoIE("Egreso");
                transaccionEgresosList.add(r);
            }
        }
    }

    public List<RemanenteMensual> getRemanentesActivos(List<RemanenteMensual> remanenteMensualList) {
        List<RemanenteMensual> rms = new ArrayList<RemanenteMensual>();
        for (RemanenteMensual remanenteMensual : remanenteMensualList) {
            if (!remanenteMensual.getEstadoRemanenteMensualList().get(remanenteMensual.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("CambioAprobado")) {
                rms.add(remanenteMensual);
            }
        }
        Collections.sort(rms, new Comparator<RemanenteMensual>() {
            @Override
            public int compare(RemanenteMensual rm1, RemanenteMensual rm2) {
                return new Integer(rm1.getMes()).compareTo(new Integer(rm2.getMes()));
            }
        });
        return rms;
    }

    public BigDecimal getValorTotalIngresos(int mes, String tipo) {
        BigDecimal valor = new BigDecimal(BigInteger.ZERO);
        if (tipo.equals("Ingreso-Propiedad")) {
            for (Row r : transaccionRegistrosList) {
                if (r.getTipo().equals("Ingreso-Propiedad")) {
                    switch (mes) {
                        case 1:
                            if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")) {
                                valor = valor.add(r.getValorMes1());
                            }
                            break;
                        case 2:
                            if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")) {
                                valor = valor.add(r.getValorMes2());
                            }
                            break;
                        case 3:
                            if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")) {
                                valor = valor.add(r.getValorMes3());
                            }
                            break;
                        case 4:
                            if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")) {
                                valor = valor.add(r.getValorMes4());
                            }
                            break;
                        case 5:
                            if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")) {
                                valor = valor.add(r.getValorMes1()).add(r.getValorMes2()).add(r.getValorMes3()).add(r.getValorMes4());
                            }
                            break;
                    }
                }
            }

        } else if (tipo.equals("Ingreso-Mercantil")) {
            for (Row r : transaccionRegistrosList) {
                if (r.getTipo().equals("Ingreso-Mercantil")) {
                    switch (mes) {
                        case 1:
                            if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                                valor = valor.add(r.getValorMes1());
                            }
                            break;
                        case 2:
                            if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                                valor = valor.add(r.getValorMes2());
                            }
                            break;
                        case 3:
                            if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                                valor = valor.add(r.getValorMes3());
                            }
                            break;
                        case 4:
                            if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                                valor = valor.add(r.getValorMes4());
                            }
                            break;
                        case 5:
                            if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                                valor = valor.add(r.getValorMes1()).add(r.getValorMes2()).add(r.getValorMes3()).add(r.getValorMes4());
                            }
                            break;
                    }
                }
            }

        }
        return valor;
    }

    public BigDecimal getValorIngresoTotal(int mes) {
        BigDecimal valor = new BigDecimal(BigInteger.ZERO);
        for (Row r : transaccionRegistrosList) {
            switch (mes) {
                case 1:
                    if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || !r.getNombre().equals("Número de trámites Registro Mercantil")) {
                        valor = valor.add(r.getValorMes1());
                    }
                    break;
                case 2:
                    if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || !r.getNombre().equals("Número de trámites Registro Mercantil")) {
                        valor = valor.add(r.getValorMes2());
                    }
                    break;
                case 3:
                    if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || !r.getNombre().equals("Número de trámites Registro Mercantil")) {
                        valor = valor.add(r.getValorMes3());
                    }
                    break;
                case 4:
                    if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || !r.getNombre().equals("Número de trámites Registro Mercantil")) {
                        valor = valor.add(r.getValorMes4());
                    }
                    break;
                case 5:
                    if (!r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || !r.getNombre().equals("Número de trámites Registro Mercantil")) {
                        valor = valor.add(r.getValorMes1()).add(r.getValorMes2()).add(r.getValorMes3()).add(r.getValorMes4());
                    }
                    break;
            }

        }

        return valor;
    }

    public BigDecimal getValorFactorIncidencia(int mes) {
        BigDecimal totalIngRMercantil = new BigDecimal(BigInteger.ZERO);
        BigDecimal ingresosTotales = new BigDecimal(BigInteger.ZERO);
        ingresosTotales = getValorIngresoTotal(mes);
        BigDecimal valor = new BigDecimal(BigInteger.ZERO);
        for (Row r : transaccionRegistrosList) {
            if (r.getTipo().equals("Ingreso-Mercantil")) {
                switch (mes) {
                    case 1:
                        if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                            totalIngRMercantil = totalIngRMercantil.add(r.getValorMes1());
                        }
                        break;
                    case 2:
                        if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                            totalIngRMercantil = totalIngRMercantil.add(r.getValorMes2());
                        }
                        break;
                    case 3:
                        if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                            totalIngRMercantil = totalIngRMercantil.add(r.getValorMes3());
                        }
                        break;
                    case 4:
                        if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                            totalIngRMercantil = totalIngRMercantil.add(r.getValorMes4());
                        }
                        break;
                    case 5:
                        if (!r.getNombre().equals("Número de trámites Registro Mercantil")) {
                            totalIngRMercantil = totalIngRMercantil.add(r.getValorMes1()).add(r.getValorMes2()).add(r.getValorMes3()).add(r.getValorMes4());
                        }
                        break;
                }
            }
        }
        System.out.println("Ingresos Totales: " + ingresosTotales);
        System.out.println("Ingresos Totales Mercantil: " + totalIngRMercantil);
        if (ingresosTotales.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("En el if");
            valor = BigDecimal.ZERO;
        } else {
            System.out.println("En el else");
            valor = totalIngRMercantil.divide(ingresosTotales, 2, RoundingMode.HALF_UP);
            System.out.println("ValorFinalELSE: " + valor);
        }
        System.out.println("ValorFinal: " + valor);
        return valor;
    }

//
//    
//
//    public void rowTransaccionEdit(RowEditEvent event) {
//        Transaccion transaccion = new Transaccion();
//        transaccion = (Transaccion) event.getObject();
//        transaccionServicio.editTransaccion(transaccion);
//        totalIngRPropiedad = new BigDecimal(0);
//        totalIngRMercantil = new BigDecimal(0);
//        totalEgresos = new BigDecimal(0);
//        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(
//                remanenteCuatrimestralSelected.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
//                remanenteCuatrimestralSelected.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
//                remanenteCuatrimestralSelected.getMes(),
//                remanenteCuatrimestralSelected.getRemanenteMensualId());
//        for (Transaccion t : transaccionRPropiedadList) {
//            if (!t.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(4)) {
//                totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
//            }
//        }
//        for (Transaccion t : transaccionRMercantilList) {
//            if (!t.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(8)) {
//                totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
//            }
//        }
//        for (Transaccion t : transaccionEgresosList) {
//            totalEgresos = totalEgresos.add(t.getValorTotal());
//        }
//    }
//
//    public void rowTransaccionEditCancel() {
//        System.out.println("Cancelado");
//    }
//
//    public void completarRemanenteMensual() {
//        System.out.println("===Guardar Remanente Mensual===");
//        System.out.println("Remanente mensual ID: " + remanenteCuatrimestralSelected.getRemanenteMensualId());
//        System.out.println("Remanente mensual Mes: " + remanenteCuatrimestralSelected.getMes());
//        List<EstadoRemanenteMensual> estadoRemanenteMensualList = new ArrayList<EstadoRemanenteMensual>();
//        estadoRemanenteMensualList = remanenteCuatrimestralSelected.getEstadoRemanenteMensualList();
//        EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
//        Usuario u = new Usuario();
//        u.setUsuarioId(usuarioId);
//        erm.setRemanenteMensualId(remanenteCuatrimestralSelected);
//        erm.setUsuarioId(u);
//        erm.setFechaRegistro(new Date());
//        erm.setDescripcion("Completo");
//        estadoRemanenteMensualServicio.create(erm);
//        btnActivated = Boolean.TRUE;
//        displayUploadEdit = Boolean.FALSE;
////        remanenteCuatrimestralList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
//    }
//
//    public void handleFileUploadRPropiedad(FileUploadEvent event) {
//        UploadedFile file = event.getFile();
//        try {
//            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());
//            String path = FacesUtils.getPath() + "/archivos/transacciones/";
//            String realPath = path + transaccionSelected.getTransaccionId() + ".pdf";
//            FileOutputStream fos = new FileOutputStream(realPath);
//            fos.write(fileByte);
//            fos.close();
//            transaccionSelected.setRespaldoUrl("/archivos/transacciones/" + transaccionSelected.getTransaccionId() + ".pdf");
//            transaccionServicio.editTransaccion(transaccionSelected);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(RemanenteCuatrimestralCtrl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(RemanenteCuatrimestralCtrl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void handleFileUploadSolicitud(FileUploadEvent event) {
//        UploadedFile file = event.getFile();
//        try {
//            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());
//            String path = FacesUtils.getPath() + "/archivos/solicitudCambio/";
//            String realPath = path + "sc_" + remanenteCuatrimestralSelected.getRemanenteMensualId() + ".pdf";
//            FileOutputStream fos = new FileOutputStream(realPath);
//            fos.write(fileByte);
//            fos.close();
//            remanenteCuatrimestralSelected.setSolicitudCambioUrl("/archivos/solicitudCambio/" + "sc_" + remanenteCuatrimestralSelected.getRemanenteMensualId() + ".pdf");
//            remanenteMensualServicio.editRemanenteMensual(remanenteCuatrimestralSelected);
//
//            EstadoRemanenteMensual erm = new EstadoRemanenteMensual();
//            Usuario u = new Usuario();
//            u.setUsuarioId(usuarioId);
//            erm.setRemanenteMensualId(remanenteCuatrimestralSelected);
//            erm.setUsuarioId(u);
//            erm.setFechaRegistro(new Date());
//            erm.setDescripcion("CambioSolicitado");
//            estadoRemanenteMensualServicio.create(erm);
//            displaySolicitud = Boolean.FALSE;
////            remanenteCuatrimestralList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(RemanenteCuatrimestralCtrl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(RemanenteCuatrimestralCtrl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void verEstadoRemanenteMensualSeleccionado() {
//        System.out.println("Remanente: " + remanenteCuatrimestralSelected.getMes());
//        for (EstadoRemanenteMensual erm : remanenteCuatrimestralSelected.getEstadoRemanenteMensualList()) {
//            System.out.println("Estado: " + erm.getUsuarioId().getNombre());
//            System.out.println("Estado: " + erm.getFechaRegistro());
//            System.out.println("Estado: " + erm.getDescripcion());
//        }
//    }
//
//    public void detalleRPropiedad() {
//        tituloDetalleDlg = "Registro de la Propiedad";
//        tramiteRPropiedadMercantilList = new ArrayList<Tramite>();
//        for (Transaccion transaccion : transaccionRPropiedadList) {
//            if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(1)) {
//                for (Tramite tramite : transaccion.getTramiteList()) {
//                    tramiteRPropiedadMercantilList.add(tramite);
//                }
//            } else if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(2)) {
//                for (Tramite tramite : transaccion.getTramiteList()) {
//                    tramiteRPropiedadMercantilList.add(tramite);
//                }
//            }
//        }
//    }
//
//    public void detalleRMercantil() {
//        tituloDetalleDlg = "Registro Mercantil";
//        tramiteRPropiedadMercantilList = new ArrayList<Tramite>();
//        for (Transaccion transaccion : transaccionRMercantilList) {
//            if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(5)) {
//                for (Tramite tramite : transaccion.getTramiteList()) {
//                    tramiteRPropiedadMercantilList.add(tramite);
//                }
//            } else if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(6)) {
//                for (Tramite tramite : transaccion.getTramiteList()) {
//                    tramiteRPropiedadMercantilList.add(tramite);
//                }
//            }
//        }
//    }
//
//    public void detalleEgresos() {
//        egresoNominaList = new ArrayList<Nomina>();
//        egresoFacturaList = new ArrayList<FacturaPagada>();
//        for (Transaccion transaccion : transaccionEgresosList) {
//            if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(9)) {
//                for (Nomina nomina : transaccion.getNominaList()) {
//                    egresoNominaList.add(nomina);
//                }
//            } else if (transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(10)
//                    || transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(11)
//                    || transaccion.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(12)) {
//                for (FacturaPagada facturaPagada : transaccion.getFacturaPagadaList()) {
//                    egresoFacturaList.add(facturaPagada);
//                }
//            }
//        }
//    }
    //Getters & Setters
    public List<Row> getTransaccionRegistrosList() {
        return transaccionRegistrosList;
    }

    public void setTransaccionRegistrosList(List<Row> transaccionRegistrosList) {
        this.transaccionRegistrosList = transaccionRegistrosList;
    }

    public List<Row> getTransaccionEgresosList() {
        return transaccionEgresosList;
    }

    public void setTransaccionEgresosList(List<Row> transaccionEgresosList) {
        this.transaccionEgresosList = transaccionEgresosList;
    }

    public RemanenteCuatrimestral getRemanenteCuatrimestralSelected() {
        return remanenteCuatrimestralSelected;
    }

    public void setRemanenteCuatrimestralSelected(RemanenteCuatrimestral remanenteCuatrimestralSelected) {
        this.remanenteCuatrimestralSelected = remanenteCuatrimestralSelected;
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<RemanenteCuatrimestral> getRemanenteCuatrimestralList() {
        return remanenteCuatrimestralList;
    }

    public void setRemanenteCuatrimestralList(List<RemanenteCuatrimestral> remanenteCuatrimestralList) {
        this.remanenteCuatrimestralList = remanenteCuatrimestralList;
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

}
