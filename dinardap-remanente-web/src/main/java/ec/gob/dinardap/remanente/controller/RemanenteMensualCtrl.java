package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
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

@Named(value = "remanenteMensualCtrl")
@ViewScoped
public class RemanenteMensualCtrl extends BaseCtrl implements Serializable {

    private String tituloPagina;
    private List<RemanenteMensual> remanenteMensualList;
    private Integer institucionId;
    private String nombreInstitucion;
    private Integer año;

    private RemanenteMensual remanenteMensualSelected;
    private String mesRemanenteMensualSelected;

    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private Transaccion transaccionSelected;

    private List<Transaccion> transaccionRPropiedadList;
    private List<Transaccion> transaccionRMercantilList;
    private List<Transaccion> transaccionEgresosList;

    private Boolean renderedEdicionTransaccion;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @PostConstruct
    protected void init() {
        tituloPagina = "Gestión Remanente Mensual";
        año = 0;
        mesRemanenteMensualSelected = "SinSelección";
        remanenteMensualSelected = new RemanenteMensual();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        renderedEdicionTransaccion = Boolean.FALSE;
        transaccionSelected = new Transaccion();
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        nombreInstitucion = institucionRequeridaServicio.getInstitucionById(institucionId).getNombre();
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, calendar.get(Calendar.YEAR));
    }

    public void reloadRemanentes() {
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
    }

    public void rowEdit() {
    }

    public void rowEditCancel() {
    }

    public void onRowSelect() {
        switch (remanenteMensualSelected.getMes()) {
            case 1:
                mesRemanenteMensualSelected = "Enero";
                break;
            case 2:
                mesRemanenteMensualSelected = "Febrero";
                break;
            case 3:
                mesRemanenteMensualSelected = "Marzo";
                break;
            case 4:
                mesRemanenteMensualSelected = "Abril";
                break;
            case 5:
                mesRemanenteMensualSelected = "Mayo";
                break;
            case 6:
                mesRemanenteMensualSelected = "Junio";
                break;
            case 7:
                mesRemanenteMensualSelected = "Julio";
                break;
            case 8:
                mesRemanenteMensualSelected = "Agosto";
                break;
            case 9:
                mesRemanenteMensualSelected = "Septiembre";
                break;
            case 10:
                mesRemanenteMensualSelected = "Octubre";
                break;
            case 11:
                mesRemanenteMensualSelected = "Noviembre";
                break;
            case 12:
                mesRemanenteMensualSelected = "Diciembre";
                break;
        }
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        for (Transaccion t : remanenteMensualSelected.getTransaccionList()) {
            if (t.getCatalogoTransaccionId().getTipo().equals("Ingreso-Propiedad")) {
                transaccionRPropiedadList.add(t);
                totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
            } else if (t.getCatalogoTransaccionId().getTipo().equals("Ingreso-Mercantil")) {
                transaccionRMercantilList.add(t);
                totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
            } else if (t.getCatalogoTransaccionId().getTipo().equals("Egreso")) {
                transaccionEgresosList.add(t);
                totalEgresos = totalEgresos.add(t.getValorTotal());
            }
        }
    }

    public void rowTransaccionEdit(RowEditEvent event) {
        Transaccion transaccion = new Transaccion();
        transaccion = (Transaccion) event.getObject();
        transaccionServicio.editTransaccion(transaccion);
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        System.out.println("Transaccion Seleccionada: " + remanenteMensualSelected.getFechaRegistro());
        for (Transaccion t : transaccionRPropiedadList) {
            totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
        }
        for (Transaccion t : transaccionRMercantilList) {
            totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
        }
        for (Transaccion t : transaccionEgresosList) {
            totalEgresos = totalEgresos.add(t.getValorTotal());
        }
    }

    public void rowTransaccionEditCancel() {
        System.out.println("Cancelado");
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

    public String getMesRemanenteMensualSelected() {
        return mesRemanenteMensualSelected;
    }

    public void setMesRemanenteMensualSelected(String mesRemanenteMensualSelected) {
        this.mesRemanenteMensualSelected = mesRemanenteMensualSelected;
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

    public Boolean getRenderedEdicionTransaccion() {
        return renderedEdicionTransaccion;
    }

    public void setRenderedEdicionTransaccion(Boolean renderedEdicionTransaccion) {
        this.renderedEdicionTransaccion = renderedEdicionTransaccion;
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

}
