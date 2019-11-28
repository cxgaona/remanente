package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
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
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
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

@Named(value = "adminRemanenteCuatrimestralCtrl")
@ViewScoped
public class AdminRemanenteCuatrimestralCtrl extends BaseCtrl implements Serializable {

    //Variables
    private Integer usuarioId;
    private Integer direccionRegionalId;
    private String nombreDireccionRegional;
    private Integer institucionId;
    private String nombreInstitucion;
    private String tituloPagina;
    private Integer año;
    private RemanenteCuatrimestral remanenteCuatrimestralSelected;
    private InstitucionRequerida institucionSelected;
    private String rutaArchivo;

    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private InstitucionRequerida institucionNotificacion;
    private List<Usuario> usuarioListNotificacion;

    private List<RemanenteCuatrimestral> remanenteCuatrimestralList;
    private List<Row> transaccionRegistrosList;
    private List<Row> transaccionEgresosList;
    private List<InstitucionRequerida> institucionList;

    private Boolean displayUploadInformeCuatrimestral;
    private Boolean disabledBtnReload;
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
        remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        remanenteCuatrimestralSelected = new RemanenteCuatrimestral();
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        institucionList = new ArrayList<InstitucionRequerida>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        sftpDto = new SftpDto();
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
        institucionList = institucionRequeridaServicio.getRegistroMixtoList();
//        remanenteCuatrimestralList = remanenteCuatrimestralServicio.getRemanenteCuatrimestralListByInstitucion(institucionId, año);
    }

    public void onRowSelectInstitucion() {
        remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        remanenteCuatrimestralSelected = new RemanenteCuatrimestral();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        displayUploadInformeCuatrimestral = Boolean.FALSE;
        disabledBtnReload = Boolean.FALSE;

        institucionId = institucionSelected.getInstitucionId();
        nombreInstitucion = institucionSelected.getNombre();
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
        List<RemanenteMensual> rms = new ArrayList<RemanenteMensual>();
        transaccionRegistrosList = new ArrayList<Row>();
        transaccionEgresosList = new ArrayList<Row>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        if (remanenteCuatrimestralSelected.getEstadoRemanenteCuatrimestralList().get(remanenteCuatrimestralSelected.getEstadoRemanenteCuatrimestralList().size() - 1).getDescripcion().equals("InformeSubido")) {
            displayUploadInformeCuatrimestral = Boolean.TRUE;
        } else {
            displayUploadInformeCuatrimestral = Boolean.FALSE;
        }
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
