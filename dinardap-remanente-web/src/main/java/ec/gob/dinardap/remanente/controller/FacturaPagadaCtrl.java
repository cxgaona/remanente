package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.FacturaPagadaServicio;
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

@Named(value = "facturaPagadaCtrl")
@ViewScoped
public class FacturaPagadaCtrl extends BaseCtrl implements Serializable {

    @EJB
    private FacturaPagadaServicio facturaPagadaServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;

    private String titulo;
    private List<FacturaPagada> facturaPagadaList;
    private Integer anio, mes;
    private Integer institucionId;
    private Date fecha;
    private FacturaPagada facturaPagadaSelected;
    private Integer idCatalogoTransaccion;
    private List<CatalogoTransaccion> catalogoList;

    @PostConstruct
    protected void init() {
        titulo = "Factura Pagada";
        facturaPagadaList = new ArrayList<FacturaPagada>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fecha = new Date();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        facturaPagadaList = facturaPagadaServicio.getFacturaPagadaByInstitucionFecha(institucionId, anio, mes);
        facturaPagadaSelected = new FacturaPagada();
        idCatalogoTransaccion = 11;
    }

    public void addRowFacturaPagada() {
        FacturaPagada newFacturaPagada = new FacturaPagada();
        newFacturaPagada.setFecha(new Date());
        newFacturaPagada.setNumero(null);
        newFacturaPagada.setTipo(null);
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
        for (CatalogoTransaccion ct:catalogoList ) {
            if (ct.getNombre().equals(facturaPagada.getTipo())) {
                idCatalogoTransaccion=ct.getCatalogoTransaccionId();
            }
        }
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
        facturaPagada.setTransaccionId(t);
        facturaPagada.setFechaRegistro(new Date());
        facturaPagadaServicio.editFacturaPagada(facturaPagada);
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public List<FacturaPagada> getFacturaPagadaList() {
        return facturaPagadaList;
    }

    public void setFacturaPagadaList(List<FacturaPagada> facturaPagadaList) {
        this.facturaPagadaList = facturaPagadaList;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public FacturaPagada getFacturaPagadaSelected() {
        return facturaPagadaSelected;
    }

    public void setFacturaPagadaSelected(FacturaPagada facturaPagadaSelected) {
        this.facturaPagadaSelected = facturaPagadaSelected;
    }

}
