package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dto.RemanenteCuatrimestralDTO;
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
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.poi.util.IOUtils;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.model.UploadedFile;

@Named(value = "validarRemanenteCuatrimestralCtrl")
@ViewScoped
public class ValidarRemanenteCuatrimestralCtrl extends BaseCtrl implements Serializable {

    //Variables
    private Integer usuarioId;
    private Integer direccionRegionalId;
    private String nombreDireccionRegional;
    private Integer institucionId;
    private String nombreInstitucion;
    private String rutaArchivo;
    private String tituloPagina;
    private Integer año;
//    private RemanenteCuatrimestral remanenteCuatrimestralSelected;
    private RemanenteCuatrimestralDTO remanenteCuatrimestralDTOSelected;
    private InstitucionRequerida institucionSelected;

    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private InstitucionRequerida institucionNotificacion;
    private List<Usuario> usuarioListNotificacion;

//    private List<RemanenteCuatrimestral> remanenteCuatrimestralList;
    private List<RemanenteCuatrimestralDTO> remanenteCuatrimestralDTOList;
    private List<Row> transaccionRegistrosList;
    private List<Row> transaccionEgresosList;
    private List<InstitucionRequerida> institucionList;

    private Boolean displayUploadInformeCuatrimestral;
    private Boolean disabledBtnReload;
    private Boolean disabledBtnEnvCan;

    private SftpDto sftpDto;

    @EJB
    private RemanenteCuatrimestralServicio remanenteCuatrimestralServicio;
    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;
    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;
    @EJB
    private EstadoRemanenteCuatrimestralServicio estadoRemanenteCuatrimestralServicio;
    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;
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

        //Inicializacion de variables
        sftpDto = new SftpDto();
        remanenteCuatrimestralDTOList = new ArrayList<RemanenteCuatrimestralDTO>();
        remanenteCuatrimestralDTOSelected = new RemanenteCuatrimestralDTO();
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        institucionList = new ArrayList<InstitucionRequerida>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        displayUploadInformeCuatrimestral = Boolean.FALSE;
        disabledBtnReload = Boolean.TRUE;
        institucionId = null;
        nombreInstitucion = "Sin selección";

        //FechaACtual
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        año = calendar.get(Calendar.YEAR);
        tituloPagina = "Gestión Remanente Cuatrimestral";

        //Proceso
        institucionList = institucionRequeridaServicio.getRegistroMixtoList(direccionRegionalId);
//        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);
    }

    public void onRowSelectInstitucion() {
        remanenteCuatrimestralDTOList = new ArrayList<RemanenteCuatrimestralDTO>();
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        remanenteCuatrimestralDTOSelected = new RemanenteCuatrimestralDTO();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        displayUploadInformeCuatrimestral = Boolean.FALSE;
        disabledBtnReload = Boolean.FALSE;

        institucionId = institucionSelected.getInstitucionId();
        nombreInstitucion = institucionSelected.getNombre();
        List<RemanenteCuatrimestral> remanenteCuatrimestralList = new ArrayList<>();
        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);
        if (!remanenteCuatrimestralList.isEmpty()) {
            for (RemanenteCuatrimestral rc : remanenteCuatrimestralList) {
                remanenteCuatrimestralDTOList.add(new RemanenteCuatrimestralDTO(rc));
            }
        }
    }

    public void loadRemanenteCuatrimestralByAño() {
        remanenteCuatrimestralDTOList = new ArrayList<RemanenteCuatrimestralDTO>();
        remanenteCuatrimestralDTOSelected = new RemanenteCuatrimestralDTO();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        displayUploadInformeCuatrimestral = Boolean.FALSE;

        if (año == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            año = calendar.get(calendar.YEAR);
        }
        List<RemanenteCuatrimestral> remanenteCuatrimestralList = new ArrayList<>();
        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);
        if (!remanenteCuatrimestralList.isEmpty()) {
            for (RemanenteCuatrimestral rc : remanenteCuatrimestralList) {
                remanenteCuatrimestralDTOList.add(new RemanenteCuatrimestralDTO(rc));
            }
        }
    }

    public void onRowSelectRemanenteCuatrimestral() {
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);

        List<RemanenteMensual> rms = new ArrayList<RemanenteMensual>();
        List<Integer> mesesCuatrimestreList = new ArrayList<Integer>();
        switch (remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getCuatrimestre()) {
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
        if (remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getEstadoRemanenteCuatrimestralList().get(remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getEstadoRemanenteCuatrimestralList().size() - 1).getDescripcion().equals("InformeSubido")) {
            displayUploadInformeCuatrimestral = Boolean.TRUE;
            if (remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getInformeTecnicoUrl() == null) {
                disabledBtnEnvCan = Boolean.TRUE;
            } else {
                disabledBtnEnvCan = Boolean.FALSE;
            }
        } else {
            displayUploadInformeCuatrimestral = Boolean.FALSE;
        }

        for (Integer mes : mesesCuatrimestreList) {
            RemanenteMensual remanenteMensual = new RemanenteMensual();
            remanenteMensual = remanenteMensualServicio.getUltimoRemanenteMensual(
                    remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                    remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                    mes);
            rms.add(remanenteMensual);
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

    public void uploadInformeTecnicoCuatrimestral(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        try {
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());
            String realPath = (Calendar.getInstance().get(Calendar.YEAR) + "/").concat("itrc_" + remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId()).concat(".pdf");
            sftpDto.getCredencialesSFTP().setDirDestino(parametroServicio.findByPk(ParametroEnum.REMANENTE_INFORME_TECNICO_REMANENTE_CUATRIMESTRAL.name()).getValor().concat(realPath));
            sftpDto.setArchivo(fileByte);
            remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().setInformeTecnicoUrl(realPath);
            remanenteCuatrimestralServicio.editRemanenteCuatrimestral(remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral(), sftpDto);
            fileByte = null;
            disabledBtnEnvCan = Boolean.FALSE;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarInformeTecnicoCuatrimestral() {
        EstadoRemanenteCuatrimestral erc = new EstadoRemanenteCuatrimestral();
        Usuario u = new Usuario();
        u.setUsuarioId(usuarioId);
        erc.setRemanenteCuatrimestral(remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral());
        erc.setUsuarioId(u);
        erc.setFechaRegistro(new Date());
        erc.setDescripcion("InformeTecnicoSubido");
        estadoRemanenteCuatrimestralServicio.create(erc);
        List<RemanenteCuatrimestral> remanenteCuatrimestralList = new ArrayList<>();
        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);
        if (!remanenteCuatrimestralList.isEmpty()) {
            for (RemanenteCuatrimestral rc : remanenteCuatrimestralList) {
                remanenteCuatrimestralDTOList.add(new RemanenteCuatrimestralDTO(rc));
            }
        }
        displayUploadInformeCuatrimestral = Boolean.FALSE;

        //ENVIO DE NOTIFICACION//
        String meses = "";
        switch (remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getCuatrimestre()) {
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
        Integer numMensuales = remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getRemanenteMensualList().size();
        institucionNotificacion = institucionRequeridaServicio.getInstitucionById(Integer.parseInt(this.getSessionVariable("institucionId")));
        usuarioListNotificacion = usuarioServicio.getUsuarioByIstitucionRol(institucionNotificacion,
                "REM-Verificador", "REM-Validador", 1, remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral());
        String mensajeNotificacion = "Se ha subido el informe técnico del Remanente Cuatrimestral correspondiente a los meses " + meses + " del año " + año + " del " + institucionNotificacion.getNombre();
        bandejaServicio.generarNotificacion(usuarioListNotificacion, usuarioId,
                remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getRemanenteCuatrimestralPK().getRemanenteCuatrimestralId(),
                remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getRemanenteAnual().getRemanenteAnualPK().getRemanenteAnualId(),
                remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().getRemanenteMensualList().get(numMensuales - 1).getRemanenteMensualId(),
                mensajeNotificacion, "RC");
        //FIN ENVIO//
    }

    public void cancelarInformeTecnicoCuatrimestral() {
        remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral().setInformeTecnicoUrl(null);
        remanenteCuatrimestralServicio.update(remanenteCuatrimestralDTOSelected.getRemanenteCuatrimestral());
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

    public Boolean getDisabledBtnReload() {
        return disabledBtnReload;
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
    public void setDisabledBtnReload(Boolean disabledBtnReload) {
        this.disabledBtnReload = disabledBtnReload;
    }

    public String getNombreDireccionRegional() {
        return nombreDireccionRegional;
    }

    public void setNombreDireccionRegional(String nombreDireccionRegional) {
        this.nombreDireccionRegional = nombreDireccionRegional;
    }

    public List<InstitucionRequerida> getInstitucionList() {
        return institucionList;
    }

    public void setInstitucionList(List<InstitucionRequerida> institucionList) {
        this.institucionList = institucionList;
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

//    public RemanenteCuatrimestral getRemanenteCuatrimestralSelected() {
//        return remanenteCuatrimestralSelected;
//    }
//
//    public void setRemanenteCuatrimestralSelected(RemanenteCuatrimestral remanenteCuatrimestralSelected) {
//        this.remanenteCuatrimestralSelected = remanenteCuatrimestralSelected;
//    }
    public RemanenteCuatrimestralDTO getRemanenteCuatrimestralDTOSelected() {
        return remanenteCuatrimestralDTOSelected;
    }

    public void setRemanenteCuatrimestralDTOSelected(RemanenteCuatrimestralDTO remanenteCuatrimestralDTOSelected) {
        this.remanenteCuatrimestralDTOSelected = remanenteCuatrimestralDTOSelected;
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

//    public List<RemanenteCuatrimestral> getRemanenteCuatrimestralList() {
//        return remanenteCuatrimestralList;
//    }
//
//    public void setRemanenteCuatrimestralList(List<RemanenteCuatrimestral> remanenteCuatrimestralList) {
//        this.remanenteCuatrimestralList = remanenteCuatrimestralList;
//    }
    public List<RemanenteCuatrimestralDTO> getRemanenteCuatrimestralDTOList() {
        return remanenteCuatrimestralDTOList;
    }

    public void setRemanenteCuatrimestralDTOList(List<RemanenteCuatrimestralDTO> remanenteCuatrimestralDTOList) {
        this.remanenteCuatrimestralDTOList = remanenteCuatrimestralDTOList;
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

    public Boolean getDisabledBtnEnvCan() {
        return disabledBtnEnvCan;
    }

    public void setDisabledBtnEnvCan(Boolean disabledBtnEnvCan) {
        this.disabledBtnEnvCan = disabledBtnEnvCan;
    }

}
