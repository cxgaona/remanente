package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    private RemanenteMensual remanenteMensualSelected;
    private String mesRemanenteMensualSelected;

    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private List<Transaccion> transaccionRPropiedadList;
    private List<Transaccion> transaccionRMercantilList;
    private List<Transaccion> transaccionEgresosList;

    private Boolean enabledEdicionTransaccion;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @PostConstruct
    protected void init() {
        tituloPagina = "Gestión Remanente Mensual";
        mesRemanenteMensualSelected = "SinSelección";
        remanenteMensualSelected = new RemanenteMensual();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        enabledEdicionTransaccion = Boolean.FALSE;
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        nombreInstitucion = institucionRequeridaServicio.getInstitucionById(institucionId).getNombre();
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId);
    }

    public void rowEditInit(RowEditEvent event) {
        System.out.println("Inicio de edicion de la fila");        
        Transaccion t = (Transaccion) event.getObject();
        System.out.println("t: "+t.getCatalogoTransaccionId().getNombre());
        if (t.getCatalogoTransaccionId().getNombre().equals("Otros")) {
            enabledEdicionTransaccion = Boolean.TRUE;
        }
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

    public Boolean getEnabledEdicionTransaccion() {
        return enabledEdicionTransaccion;
    }

    public void setEnabledEdicionTransaccion(Boolean enabledEdicionTransaccion) {
        this.enabledEdicionTransaccion = enabledEdicionTransaccion;
    }

}
