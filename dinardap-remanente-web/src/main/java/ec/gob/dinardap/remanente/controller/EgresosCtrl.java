package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.FacturaPagadaServicio;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

@Named(value = "egresosCtrl")
@ViewScoped
public class EgresosCtrl extends BaseCtrl implements Serializable {

    @EJB
    private NominaServicio nominaServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @EJB
    private FacturaPagadaServicio facturaPagadaServicio;

    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    private String tituloEgreso;
    private String tituloN, tituloFP;
    private List<Nomina> nominaList;
    private Integer anio, mes;
    private Integer institucionId;
    private Date fecha;
    private Nomina nominaSelected;
    private List<FacturaPagada> facturaPagadaList;
    private FacturaPagada facturaPagadaSelected;
    private Integer idCatalogoTransaccion;
    private List<CatalogoTransaccion> catalogoList;
    private Boolean disableNuevoRegistro;
    private Boolean renderEdition;
    private List<RemanenteMensual> remanenteMensualList;
    private RemanenteMensual remanenteMensualSelected;

    @PostConstruct
    protected void init() {
        tituloEgreso = "Egresos";
        tituloN = "Nómina";
        tituloFP = "Factura Pagada";
        facturaPagadaList = new ArrayList<FacturaPagada>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fecha = new Date();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        nominaList = new ArrayList<Nomina>();
        nominaList = nominaServicio.getNominaByInstitucionFecha(institucionId, anio, mes);
        nominaSelected = new Nomina();
        facturaPagadaList = new ArrayList<FacturaPagada>();
        facturaPagadaList = facturaPagadaServicio.getFacturaPagadaByInstitucionFecha(institucionId, anio, mes);
        facturaPagadaSelected = new FacturaPagada();
        disableNuevoRegistro = Boolean.FALSE;
        renderEdition = Boolean.TRUE;
        obtenerRemanenteMensual();
    }

    public void addRowNomina() {
        Nomina newNomina = new Nomina();
        newNomina.setAportePatronal(BigDecimal.ZERO);
        newNomina.setDecimoCuarto(BigDecimal.ZERO);
        newNomina.setDecimoTercero(BigDecimal.ZERO);
        newNomina.setFechaRegistro(new Date());
        newNomina.setFondosReserva(BigDecimal.ZERO);
        newNomina.setImpuestoRenta(BigDecimal.ZERO);
        newNomina.setLiquidoRecibir(BigDecimal.ZERO);
        newNomina.setRemuneracion(BigDecimal.ZERO);
        newNomina.setTotalDesc(BigDecimal.ZERO);
        newNomina.setNominaId(null);
        Transaccion t = new Transaccion();
        //9 CORRESPONDE A REMUNERACIONES(NOMINA) DEL CATALOGO DE TRANSACCIONES
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 9);
        newNomina.setTransaccionId(t);
        nominaServicio.crearNomina(newNomina);
        nominaList.add(newNomina);
    }

    public void onRowEditNomina(RowEditEvent event) {
        Nomina nomina = new Nomina();
        nomina = (Nomina) event.getObject();
        Transaccion t = new Transaccion();
        //9 CORRESPONDE A REMUNERACIONES(NOMINA) DEL CATALOGO DE TRANSACCIONES
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 9);
        nomina.setTransaccionId(t);
        nomina.setFechaRegistro(new Date());
        nominaServicio.editNomina(nomina);
    }

    public void reloadNomina() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;             
        nominaList = nominaServicio.getNominaByInstitucionFecha(institucionId, anio, mes);
        reloadFacturaPagada();
        obtenerRemanenteMensual();
    }

    public void onRowDeleteNomina() {
        nominaServicio.borrarNomina(nominaSelected);
        nominaServicio.actualizarTransaccionValor(institucionId, anio, mes);
        reloadNomina();
    }

    public void obtenerRemanenteMensual() {
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucionAñoMes(institucionId, anio, mes);
        remanenteMensualSelected = new RemanenteMensual();
        for (RemanenteMensual rm : remanenteMensualList) {
            System.out.println("rm" + rm.getMes());
            for (EstadoRemanenteMensual erm : rm.getEstadoRemanenteMensualList()) {
                System.out.println("erm:" + erm.getEstadoRemanenteMensualId());
            }
        }
        remanenteMensualSelected = remanenteMensualList.get(0);
        if (remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("GeneradoAutomaticamente")
                || remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("Verificado-Rechazado")
                || remanenteMensualSelected.getEstadoRemanenteMensualList().get(remanenteMensualSelected.getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("GeneradoNuevaVersion")) {
            disableNuevoRegistro = Boolean.FALSE;
            renderEdition = Boolean.TRUE;
        } else {
            renderEdition = Boolean.FALSE;
            disableNuevoRegistro = Boolean.TRUE;
        }
    }

    public void addRowFacturaPagada() {
        FacturaPagada newFacturaPagada = new FacturaPagada();
        newFacturaPagada.setFecha(new Date());
        newFacturaPagada.setNumero(null);
        newFacturaPagada.setTipo("Otros");
        newFacturaPagada.setDetalle(null);
        newFacturaPagada.setValor(BigDecimal.ZERO);
        newFacturaPagada.setFechaRegistro(new Date());
        newFacturaPagada.setFacturaPagadaId(null);
        Transaccion t = new Transaccion();
        //11 CORRESPONDE A OTROS DEL CATALOGO DE TRANSACCIONES
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 11);
        newFacturaPagada.setTransaccionId(t);
        facturaPagadaServicio.crearFacturaPagada(newFacturaPagada);
        facturaPagadaList.add(newFacturaPagada);
    }

    public void onRowEditFacturaPagada(RowEditEvent event) {
        FacturaPagada facturaPagada = new FacturaPagada();
        facturaPagada = (FacturaPagada) event.getObject();
        catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionList();
        for (CatalogoTransaccion ct : catalogoList) {
            if (ct.getNombre().equals(facturaPagada.getTipo())) {
                idCatalogoTransaccion = ct.getCatalogoTransaccionId();
            }
        }
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
        facturaPagada.setTransaccionId(t);
        facturaPagada.setFechaRegistro(new Date());
        facturaPagadaServicio.editFacturaPagada(facturaPagada);
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getMes(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteMensualId());
        for (Transaccion tl : transaccionList) {
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(10)) {
                facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 10);
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(11)) {
                facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 11);
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(12)) {
                facturaPagadaServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 12);
            }
        }
    }

    public void reloadFacturaPagada() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;

        facturaPagadaList = facturaPagadaServicio.getFacturaPagadaByInstitucionFecha(institucionId, anio, mes);
    }

    public void onRowDeleteFacturaPagada() {
        facturaPagadaServicio.borrarFacturaPagada(facturaPagadaSelected);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 10);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 11);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 12);
        reloadFacturaPagada();
    }

    public String getTituloEgreso() {
        return tituloEgreso;
    }

    public void setTituloEgreso(String tituloEgreso) {
        this.tituloEgreso = tituloEgreso;
    }

    public String getTituloN() {
        return tituloN;
    }

    public void setTituloN(String tituloN) {
        this.tituloN = tituloN;
    }

    public String getTituloFP() {
        return tituloFP;
    }

    public void setTituloFP(String tituloFP) {
        this.tituloFP = tituloFP;
    }

    public List<Nomina> getNominaList() {
        return nominaList;
    }

    public void setNominaList(List<Nomina> nominaList) {
        this.nominaList = nominaList;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Nomina getNominaSelected() {
        return nominaSelected;
    }

    public void setNominaSelected(Nomina nominaSelected) {
        this.nominaSelected = nominaSelected;
    }

    public List<FacturaPagada> getFacturaPagadaList() {
        return facturaPagadaList;
    }

    public void setFacturaPagadaList(List<FacturaPagada> facturaPagadaList) {
        this.facturaPagadaList = facturaPagadaList;
    }

    public FacturaPagada getFacturaPagadaSelected() {
        return facturaPagadaSelected;
    }

    public void setFacturaPagadaSelected(FacturaPagada facturaPagadaSelected) {
        this.facturaPagadaSelected = facturaPagadaSelected;
    }

    public Integer getIdCatalogoTransaccion() {
        return idCatalogoTransaccion;
    }

    public void setIdCatalogoTransaccion(Integer idCatalogoTransaccion) {
        this.idCatalogoTransaccion = idCatalogoTransaccion;
    }

    public List<CatalogoTransaccion> getCatalogoList() {
        return catalogoList;
    }

    public void setCatalogoList(List<CatalogoTransaccion> catalogoList) {
        this.catalogoList = catalogoList;
    }

    public Boolean getDisableNuevoRegistro() {
        return disableNuevoRegistro;
    }

    public void setDisableNuevoRegistro(Boolean disableNuevoRegistro) {
        this.disableNuevoRegistro = disableNuevoRegistro;
    }

    public Boolean getRenderEdition() {
        return renderEdition;
    }

    public void setRenderEdition(Boolean renderEdition) {
        this.renderEdition = renderEdition;
    }

}
