package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.EstadoRemanenteCuatrimestralServicio;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteCuatrimestralServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.remanente.utils.FacesUtils;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.apache.poi.util.IOUtils;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.model.UploadedFile;

@Named(value = "remanenteCuatrimestralCtrl")
@ViewScoped
public class RemanenteCuatrimestralCtrl extends BaseCtrl implements Serializable {

    //Variables
    private Integer institucionId;
    private Integer usuarioId;
    private String tituloPagina;
    private String nombreInstitucion;
    private String rutaArchivo;
    private Integer año;
    private RemanenteCuatrimestral remanenteCuatrimestralSelected;
    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private Boolean btnInfDisabled;
    private InstitucionRequerida institucionNotificacion;
    private List<Usuario> usuarioListNotificacion;

    private List<RemanenteCuatrimestral> remanenteCuatrimestralList;
    private List<Row> transaccionRegistrosList;
    private List<Row> transaccionEgresosList;

    private Boolean displayUploadInformeCuatrimestral;
    private Boolean disabledBtnEnvCan;

    private SftpDto sftpDto;

    @EJB
    private RemanenteCuatrimestralServicio remanenteCuatrimestralServicio;
    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;
    @EJB
    private EstadoRemanenteCuatrimestralServicio estadoRemanenteCuatrimestralServicio;
    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;
    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private BandejaServicio bandejaServicio;
    @EJB
    private UsuarioServicio usuarioServicio;
    @EJB
    private ParametroServicio parametroServicio;

    @PostConstruct
    protected void init() {
        //Session
        institucionId = this.getInstitucionID(this.getSessionVariable("perfil"));
        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        nombreInstitucion = institucionRequeridaServicio.getInstitucionById(institucionId).getNombre();

        //Inicializacion de variables
        remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        remanenteCuatrimestralSelected = new RemanenteCuatrimestral();
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        sftpDto = new SftpDto();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        displayUploadInformeCuatrimestral = Boolean.FALSE;
        btnInfDisabled = Boolean.TRUE;
        disabledBtnEnvCan = Boolean.TRUE;

        //FechaACtual
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
        tituloPagina = "Gestión Remanente Cuatrimestral";

        //Proceso
        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);
    }

    public void loadRemanenteCuatrimestralByAño() {
        remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        remanenteCuatrimestralSelected = new RemanenteCuatrimestral();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        displayUploadInformeCuatrimestral = Boolean.FALSE;

        if (año == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            año = calendar.get(calendar.YEAR);
        }
        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);
    }

    public void onRowSelectRemanenteCuatrimestral() {

        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        btnInfDisabled = Boolean.FALSE;
        Boolean flagDisplay = Boolean.TRUE;

        List<RemanenteMensual> rms = new ArrayList<RemanenteMensual>();
        List<Integer> mesesCuatrimestreList = new ArrayList<Integer>();
        switch (remanenteCuatrimestralSelected.getCuatrimestre()) {
            case 1:
                mesesCuatrimestreList.add(1);
                mesesCuatrimestreList.add(2);
                mesesCuatrimestreList.add(3);
                mesesCuatrimestreList.add(4);
                break;
            case 2:
                mesesCuatrimestreList.add(5);
                mesesCuatrimestreList.add(6);
                mesesCuatrimestreList.add(7);
                mesesCuatrimestreList.add(8);
                break;
            case 3:
                mesesCuatrimestreList.add(9);
                mesesCuatrimestreList.add(10);
                mesesCuatrimestreList.add(11);
                mesesCuatrimestreList.add(12);
                break;
            default:
                break;
        }
        for (Integer mes : mesesCuatrimestreList) {
            RemanenteMensual remanenteMensual = new RemanenteMensual();
            remanenteMensual = remanenteMensualServicio.getUltimoRemanenteMensual(
                    remanenteCuatrimestralSelected.getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                    remanenteCuatrimestralSelected.getRemanenteAnual().getAnio(),
                    mes);
            rms.add(remanenteMensual);
        }

        for (RemanenteMensual remanenteMensual : rms) {
            String estadoRemanenteMensual = remanenteMensual.getEstadoRemanenteMensualList().get(remanenteMensual.getEstadoRemanenteMensualList().size() - 1).getDescripcion();            
            if (!estadoRemanenteMensual.equals("Validado-Aprobado")) {
                flagDisplay = Boolean.FALSE;
                break;
            }
        }
        String estadoRemanenteCuatrimestral = remanenteCuatrimestralSelected.getEstadoRemanenteCuatrimestralList().get(remanenteCuatrimestralSelected.getEstadoRemanenteCuatrimestralList().size() - 1).getDescripcion();
        if (flagDisplay
                && estadoRemanenteCuatrimestral.equals("GeneradoAutomaticamente")) {
            displayUploadInformeCuatrimestral = Boolean.TRUE;
            if (remanenteCuatrimestralSelected.getInformeRemanenteUrl() == null) {
                disabledBtnEnvCan = Boolean.TRUE;
            } else {
                disabledBtnEnvCan = Boolean.FALSE;
            }
        } else {
            displayUploadInformeCuatrimestral = Boolean.FALSE;
        }

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

    public void uploadInformeCuatrimestral(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        try {
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());
            String realPath = (Calendar.getInstance().get(Calendar.YEAR) + "/").concat("irc_" + remanenteCuatrimestralSelected.getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId()).concat(".pdf");
            sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.REMANENTE_INFORME_REMANENTE_CUATRIMESTRAL.name()).getValor().concat(realPath));
            sftpDto.setArchivo(fileByte);
            remanenteCuatrimestralSelected.setInformeRemanenteUrl(realPath);
            remanenteCuatrimestralServicio.editRemanenteCuatrimestral(remanenteCuatrimestralSelected, sftpDto);
            fileByte = null;
            disabledBtnEnvCan = Boolean.FALSE;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarInformeCuatrimestral() {
        EstadoRemanenteCuatrimestral erc = new EstadoRemanenteCuatrimestral();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erc.setRemanenteCuatrimestral(remanenteCuatrimestralSelected);
        erc.setUsuarioId(u);
        erc.setFechaRegistro(new Date());
        erc.setDescripcion("InformeSubido");
        estadoRemanenteCuatrimestralServicio.create(erc);
        displayUploadInformeCuatrimestral = Boolean.FALSE;
        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);

        //ENVIO DE NOTIFICACION//
        String meses = "";
        switch (remanenteCuatrimestralSelected.getCuatrimestre()) {
            case 1:
                meses = "Enero - Abril";
                break;
            case 2:
                meses = "Mayo - Agosto";
                break;
            case 3:
                meses = "Septiembre - Diciembre";
                break;
        }
        Integer numMensuales = remanenteCuatrimestralSelected.getRemanenteMensualList().size();
        institucionNotificacion = institucionRequeridaServicio.getInstitucionById(Integer.parseInt(this.getSessionVariable("institucionId")));
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Validador", "REM-Verificador", 1, remanenteCuatrimestralSelected);
        String mensajeNotificacion = "Se ha subido el informe del Remanente Cuatrimestral suscrito correspondiente a los meses " + meses + " del año " + año + " del " + institucionNotificacion.getNombre();
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteCuatrimestralSelected.getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteCuatrimestralSelected.getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteCuatrimestralSelected.getRemanenteAnual().getInstitucionRequerida(),
                remanenteCuatrimestralSelected.getRemanenteMensualList().get(numMensuales - 1).getRemanenteMensualId(),
                mensajeNotificacion, "RC");
        //FIN ENVIO//
    }

    public void cancelarInformeCuatrimestral() {
        remanenteCuatrimestralSelected.setInformeRemanenteUrl(null);
        remanenteCuatrimestralServicio.update(remanenteCuatrimestralSelected);
        disabledBtnEnvCan = Boolean.TRUE;
    }

    //Ingresos
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
                    if (!(r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || r.getNombre().equals("Número de trámites Registro Mercantil"))) {
                        valor = valor.add(r.getValorMes1());
                    }
                    break;
                case 2:
                    if (!(r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || r.getNombre().equals("Número de trámites Registro Mercantil"))) {
                        valor = valor.add(r.getValorMes2());
                    }
                    break;
                case 3:
                    if (!(r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || r.getNombre().equals("Número de trámites Registro Mercantil"))) {
                        valor = valor.add(r.getValorMes3());
                    }
                    break;
                case 4:
                    if (!(r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || r.getNombre().equals("Número de trámites Registro Mercantil"))) {
                        valor = valor.add(r.getValorMes4());
                    }
                    break;
                case 5:
                    if (!(r.getNombre().equals("Número de trámites Registro de la Propiedad")
                            || r.getNombre().equals("Número de trámites Registro Mercantil"))) {
                        valor = valor.add(r.getValorMes1()).add(r.getValorMes2()).add(r.getValorMes3()).add(r.getValorMes4());
                    }
                    break;
            }

        }

        return valor;
    }

    public BigDecimal getValorFactorIncidencia(int mes) {
        BigDecimal totalIngRMercantil = BigDecimal.ZERO;
        BigDecimal ingresosTotales = BigDecimal.ZERO;
        ingresosTotales = getValorIngresoTotal(mes);
        BigDecimal valor = BigDecimal.ZERO;
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
        if (ingresosTotales.compareTo(BigDecimal.ZERO) == 0) {
            valor = BigDecimal.ZERO;
        } else {
            valor = totalIngRMercantil.divide(ingresosTotales, 8, RoundingMode.HALF_UP);
        }
        return valor;
    }

    //Egresos
    public BigDecimal getValorTotalGastos(int mes) {
        BigDecimal valor = new BigDecimal(BigInteger.ZERO);
        for (Row r : transaccionEgresosList) {
            switch (mes) {
                case 1:
                    valor = valor.add(r.getValorMes1());
                    break;
                case 2:
                    valor = valor.add(r.getValorMes2());
                    break;
                case 3:
                    valor = valor.add(r.getValorMes3());
                    break;
                case 4:
                    valor = valor.add(r.getValorMes4());
                    break;
                case 5:
                    valor = valor.add(r.getValorMes1()).add(r.getValorMes2()).add(r.getValorMes3()).add(r.getValorMes4());
                    break;
            }
        }
        return valor;
    }

    public BigDecimal getValorGastosRMercantil(int mes) {
        BigDecimal totalGastosRPropiedad = BigDecimal.ZERO;
        BigDecimal factorIncidencia = BigDecimal.ZERO;
        BigDecimal valor = BigDecimal.ZERO;
        totalGastosRPropiedad = getValorTotalGastos(mes);
        factorIncidencia = getValorFactorIncidencia(mes);
        valor = totalGastosRPropiedad.multiply(factorIncidencia).setScale(8, BigDecimal.ROUND_HALF_EVEN);

        return valor;
    }

    public BigDecimal getValorRemanenteMercantil(int mes) {
        BigDecimal totalIngRMercatil = BigDecimal.ZERO;
        BigDecimal gastosRMercantilEst = BigDecimal.ZERO;
        BigDecimal valor = BigDecimal.ZERO;
        totalIngRMercatil = getValorTotalIngresos(mes, "Ingreso-Mercantil");
        gastosRMercantilEst = getValorGastosRMercantil(mes);
        valor = totalIngRMercatil.subtract(gastosRMercantilEst).setScale(8, BigDecimal.ROUND_HALF_EVEN);
        return valor;
    }

    public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        BigDecimal totalMes = BigDecimal.ZERO;
        parametros.put("nombreInstitucion", nombreInstitucion);
        String cuatrimestre = remanenteCuatrimestralSelected.getCuatrimestre().equals(1) ? "I"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(2) ? "II"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(3) ? "III" : "No definido";
        String mes1 = remanenteCuatrimestralSelected.getCuatrimestre().equals(1) ? "Enero"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(2) ? "Mayo"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(3) ? "Septiembre" : "Sin mes";

        String mes2 = remanenteCuatrimestralSelected.getCuatrimestre().equals(1) ? "Febrero"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(2) ? "Junio"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(3) ? "Octubre" : "Sin mes";

        String mes3 = remanenteCuatrimestralSelected.getCuatrimestre().equals(1) ? "Marzo"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(2) ? "Julio"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(3) ? "Noviembre" : "Sin mes";

        String mes4 = remanenteCuatrimestralSelected.getCuatrimestre().equals(1) ? "Abril"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(2) ? "Agosto"
                : remanenteCuatrimestralSelected.getCuatrimestre().equals(3) ? "Diciembre" : "Sin mes";
        String path = FacesUtils.getPath() + "/resource/images/";
        parametros.put("realPath", path);
        parametros.put("cuatrimestreAnio", "CUATRIMESTRE " + cuatrimestre + " AÑO: " + remanenteCuatrimestralSelected.getRemanenteAnual().getAnio());
        parametros.put("mes1", mes1);
        parametros.put("mes2", mes2);
        parametros.put("mes3", mes3);
        parametros.put("mes4", mes4);

        for (Row r : transaccionRegistrosList) {
            if (r.getTipo().equals("Ingreso-Propiedad")) {
                switch (r.getNombre()) {
                    case "Certificaciones":
                        parametros.put("rpcrt1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpcrt2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpcrt3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpcrt4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        r.setValorTotal(r.getValorMes1().
                                add(r.getValorMes2()).
                                add(r.getValorMes3()).
                                add(r.getValorMes4()));
                        parametros.put("rpcrttotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        break;
                    case "Inscripciones":
                        parametros.put("rpins1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpins2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpins3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpins4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        r.setValorTotal(r.getValorMes1().
                                add(r.getValorMes2()).
                                add(r.getValorMes3()).
                                add(r.getValorMes4()));
                        parametros.put("rpinstotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        break;
                    case "Otros":
                        parametros.put("rpotr1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpotr2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpotr3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rpotr4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        r.setValorTotal(r.getValorMes1().
                                add(r.getValorMes2()).
                                add(r.getValorMes3()).
                                add(r.getValorMes4()));
                        parametros.put("rpotrtotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        break;
                    case "Número de trámites Registro de la Propiedad":
                        parametros.put("rpntra1", r.getValorMes1().intValue() + " ");
                        parametros.put("rpntra2", r.getValorMes2().intValue() + " ");
                        parametros.put("rpntra3", r.getValorMes3().intValue() + " ");
                        parametros.put("rpntra4", r.getValorMes4().intValue() + " ");
                        r.setValorTotal(r.getValorMes1().
                                add(r.getValorMes2()).
                                add(r.getValorMes3()).
                                add(r.getValorMes4()));
                        parametros.put("rpntratotal", r.getValorTotal().intValue() + " ");
                        break;
                }
            } else if (r.getTipo().equals("Ingreso-Mercantil")) {
                switch (r.getNombre()) {
                    case "Certificaciones":
                        parametros.put("rmcrt1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmcrt2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmcrt3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmcrt4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        r.setValorTotal(r.getValorMes1().
                                add(r.getValorMes2()).
                                add(r.getValorMes3()).
                                add(r.getValorMes4()));
                        parametros.put("rmcrttotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        break;
                    case "Inscripciones":
                        parametros.put("rmins1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmins2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmins3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmins4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        r.setValorTotal(r.getValorMes1().
                                add(r.getValorMes2()).
                                add(r.getValorMes3()).
                                add(r.getValorMes4()));
                        parametros.put("rminstotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        break;
                    case "Otros":
                        parametros.put("rmotr1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmotr2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmotr3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        parametros.put("rmotr4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        r.setValorTotal(r.getValorMes1().
                                add(r.getValorMes2()).
                                add(r.getValorMes3()).
                                add(r.getValorMes4()));
                        parametros.put("rmotrtotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                        break;
                    case "Número de trámites Registro Mercantil":
                        parametros.put("rmntra1", r.getValorMes1().intValue() + " ");
                        parametros.put("rmntra2", r.getValorMes2().intValue() + " ");
                        parametros.put("rmntra3", r.getValorMes3().intValue() + " ");
                        parametros.put("rmntra4", r.getValorMes4().intValue() + " ");
                        r.setValorTotal(r.getValorMes1().
                                add(r.getValorMes2()).
                                add(r.getValorMes3()).
                                add(r.getValorMes4()));
                        parametros.put("rmntratotal", r.getValorTotal().intValue() + " ");
                        break;
                }
            }
        }

        for (Row r : transaccionEgresosList) {
            switch (r.getNombre()) {
                case "Personal (Remuneraciones)":
                    parametros.put("grem1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("grem2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("grem3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("grem4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    r.setValorTotal(r.getValorMes1().
                            add(r.getValorMes2()).
                            add(r.getValorMes3()).
                            add(r.getValorMes4()));
                    parametros.put("gremtotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    break;
                case "Bienes y Servicios de Consumo (Arriendo, Servicios Básicos)":
                    parametros.put("gbienes1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gbienes2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gbienes3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gbienes4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    r.setValorTotal(r.getValorMes1().
                            add(r.getValorMes2()).
                            add(r.getValorMes3()).
                            add(r.getValorMes4()));
                    parametros.put("gbienestotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    break;
                case "Otros":
                    parametros.put("gotr1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gotr2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gotr3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gotr4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    r.setValorTotal(r.getValorMes1().
                            add(r.getValorMes2()).
                            add(r.getValorMes3()).
                            add(r.getValorMes4()));
                    parametros.put("gotrtotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    break;
                case "Bienes de Larga Duración":
                    parametros.put("gblarga1", r.getValorMes1().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gblarga2", r.getValorMes2().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gblarga3", r.getValorMes3().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    parametros.put("gblarga4", r.getValorMes4().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    r.setValorTotal(r.getValorMes1().
                            add(r.getValorMes2()).
                            add(r.getValorMes3()).
                            add(r.getValorMes4()));
                    parametros.put("gblargatotal", r.getValorTotal().setScale(2, RoundingMode.HALF_UP).toString() + " ");
                    break;
            }
        }

        parametros.put("rptin1", getValorTotalIngresos(1, "Ingreso-Propiedad").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rptin2", getValorTotalIngresos(2, "Ingreso-Propiedad").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rptin3", getValorTotalIngresos(3, "Ingreso-Propiedad").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rptin4", getValorTotalIngresos(4, "Ingreso-Propiedad").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rptintotal", getValorTotalIngresos(5, "Ingreso-Propiedad").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rmtin1", getValorTotalIngresos(1, "Ingreso-Mercantil").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rmtin2", getValorTotalIngresos(2, "Ingreso-Mercantil").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rmtin3", getValorTotalIngresos(3, "Ingreso-Mercantil").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rmtin4", getValorTotalIngresos(4, "Ingreso-Mercantil").setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("rmtintotal", getValorTotalIngresos(5, "Ingreso-Mercantil").setScale(2, RoundingMode.HALF_UP).toString());

        parametros.put("gtotrp1", getValorTotalGastos(1).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("gtotrp2", getValorTotalGastos(2).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("gtotrp3", getValorTotalGastos(3).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("gtotrp4", getValorTotalGastos(4).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("gtotrptotal", getValorTotalGastos(5).setScale(2, RoundingMode.HALF_UP).toString());

        parametros.put("ingTotal1", getValorIngresoTotal(1).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("ingTotal2", getValorIngresoTotal(2).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("ingTotal3", getValorIngresoTotal(3).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("ingTotal4", getValorIngresoTotal(4).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("ingTotaltotal", getValorIngresoTotal(5).setScale(2, RoundingMode.HALF_UP).toString());

        parametros.put("finc1", getValorFactorIncidencia(1).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("finc2", getValorFactorIncidencia(2).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("finc3", getValorFactorIncidencia(3).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("finc4", getValorFactorIncidencia(4).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("finctotal", getValorFactorIncidencia(5).setScale(2, RoundingMode.HALF_UP).toString());

        parametros.put("grmest1", getValorGastosRMercantil(1).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("grmest2", getValorGastosRMercantil(2).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("grmest3", getValorGastosRMercantil(3).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("grmest4", getValorGastosRMercantil(4).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("grmesttotal", getValorGastosRMercantil(5).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("remrm1", getValorRemanenteMercantil(1).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("remrm2", getValorRemanenteMercantil(2).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("remrm3", getValorRemanenteMercantil(3).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("remrm4", getValorRemanenteMercantil(4).setScale(2, RoundingMode.HALF_UP).toString());
        parametros.put("remrmtotal", getValorRemanenteMercantil(5).setScale(2, RoundingMode.HALF_UP).toString());

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resource/templatesReports/reportInformeCuatrimestral.jasper"));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JREmptyDataSource());

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=InformeCuatrimestral.pdf");
        ServletOutputStream stream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void visualizarArchivoInfRemanenteCuatrimestral() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.REMANENTE_INFORME_REMANENTE_CUATRIMESTRAL.name()).getValor().concat(rutaArchivo));
            byte[] contenido = remanenteCuatrimestralServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error: Archivo no disponible", "");
            }
        }
    }

    public void visualizarArchivoInfTecRemanenteCuatrimestral() {
        TipoArchivo tipoArchivo = new TipoArchivo();
        if (rutaArchivo != null || rutaArchivo != "") {
            sftpDto.getCredencialesSFTP().setDirOrigen(parametroServicio.findByPk(ParametroEnum.REMANENTE_INFORME_TECNICO_REMANENTE_CUATRIMESTRAL.name()).getValor().concat(rutaArchivo));
            byte[] contenido = remanenteCuatrimestralServicio.descargarArchivo(sftpDto);
            if (contenido != null) {
                downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(rutaArchivo), rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1));
            } else {
                this.addErrorMessage("1", "Error: Archivo no disponible", "");
            }
        }
    }

    //Getters & Setters
    public Boolean getBtnInfDisabled() {
        return btnInfDisabled;
    }

    public void setBtnInfDisabled(Boolean btnInfDisabled) {
        this.btnInfDisabled = btnInfDisabled;
    }

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

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public Boolean getDisplayUploadInformeCuatrimestral() {
        return displayUploadInformeCuatrimestral;
    }

    public void setDisplayUploadInformeCuatrimestral(Boolean displayUploadInformeCuatrimestral) {
        this.displayUploadInformeCuatrimestral = displayUploadInformeCuatrimestral;
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

}
