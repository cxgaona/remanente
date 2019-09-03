package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.TramiteServicio;
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

@Named(value = "tramiteCtrl")
@ViewScoped
public class TramiteCtrl extends BaseCtrl implements Serializable {

    @EJB
    private TramiteServicio tramiteServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;

    private String tituloMercantil, tituloPropiedad;
    private List<Tramite> tramiteList;
    private Integer anio, mes;
    private Integer institucionId;
    private Date fecha;
    private Tramite tramiteSelected;
    private Integer idCatalogoTransaccion;
    private List<CatalogoTransaccion> catalogoList;

    @PostConstruct
    protected void init() {
        tituloPropiedad = "Trámite Propiedad";
        tituloMercantil = "Trámite Mercantil";
        tramiteList = new ArrayList<Tramite>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fecha = new Date();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        tramiteList = tramiteServicio.getTramiteByInstitucionFecha(institucionId, anio, mes);
        tramiteSelected = new Tramite();
    }

    public void addRowTramitePropiedad() {
        Tramite newTramite = new Tramite();
        newTramite.setFecha(new Date());
        newTramite.setNumero(null);
        newTramite.setTipo(null);
        newTramite.setNumeroComprobantePago(null);
        newTramite.setValor(BigDecimal.ZERO);
        newTramite.setActividadRegistral("Propiedad");
        newTramite.setActo(null);
        newTramite.setFechaRegistro(new Date());
        newTramite.setTramiteId(null);
        Transaccion t = new Transaccion();
        //3 CORRESPONDE A OTROS DEL CATALOGO DE TRANSACCIONES PARA PROPIEDAD
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 3);
        newTramite.setTransaccionId(t);
        tramiteServicio.crearTramite(newTramite);
        tramiteList.add(newTramite);
    }
    public void addRowTramiteMercantil() {
        Tramite newTramite = new Tramite();
        newTramite.setFecha(new Date());
        newTramite.setNumero(null);
        newTramite.setTipo(null);
        newTramite.setNumeroComprobantePago(null);
        newTramite.setValor(BigDecimal.ZERO);
        newTramite.setActividadRegistral("Mercantil");
        newTramite.setActo(null);
        newTramite.setFechaRegistro(new Date());
        newTramite.setTramiteId(null);
        Transaccion t = new Transaccion();
        //7 CORRESPONDE A OTROS DEL CATALOGO DE TRANSACCIONES PARA MERCANTIL
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 7);
        newTramite.setTransaccionId(t);
        tramiteServicio.crearTramite(newTramite);
        tramiteList.add(newTramite);
    }

    public void onRowEditTramite(RowEditEvent event) {
        Tramite tramite = new Tramite();
        tramite = (Tramite) event.getObject();
        catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionList();
        for (CatalogoTransaccion ct:catalogoList ) {
            if (ct.getNombre().equals(tramite.getTipo())) {
                idCatalogoTransaccion=ct.getCatalogoTransaccionId();
            }
        }
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);
        tramite.setTransaccionId(t);
        tramite.setFechaRegistro(new Date());
        tramiteServicio.editTramite(tramite);
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(tramite.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                tramite.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                tramite.getTransaccionId().getRemanenteMensualId().getMes());        
        for (Transaccion tl:transaccionList ) {            
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(10)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                tl.getRemanenteMensualId().getMes(),10);                
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(11)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                tl.getRemanenteMensualId().getMes(),11);                
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(12)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                tl.getRemanenteMensualId().getMes(),12);
            }
        }
    }

    public void reloadTramite() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;

        tramiteList = tramiteServicio.getTramiteByInstitucionFecha(institucionId, anio, mes);
    }

    public void onRowDeleteFacturaPagada() {
        /*facturaPagadaServicio.borrarFacturaPagada(facturaPagadaSelected);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 10);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 11);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 12);
        reloadFacturaPagada();*/
    }

    public String getTituloPropiedad() {
        return tituloPropiedad;
    }

    public void setTituloPropiedad(String tituloPropiedad) {
        this.tituloPropiedad = tituloPropiedad;
    }
    
    public String getTituloMercantil() {
        return tituloMercantil;
    }

    public void setTituloMercantil(String tituloMercantil) {
        this.tituloMercantil = tituloMercantil;
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

    public List<Tramite> getTramiteList() {
        return tramiteList;
    }

    public void setTramiteList(List<Tramite> tramiteList) {
        this.tramiteList = tramiteList;
    }

    public Tramite getTramiteSelected() {
        return tramiteSelected;
    }

    public void setTramiteSelected(Tramite tramiteSelected) {
        this.tramiteSelected = tramiteSelected;
    }

    public List<CatalogoTransaccion> getCatalogoList() {
        return catalogoList;
    }

    public void setCatalogoList(List<CatalogoTransaccion> catalogoList) {
        this.catalogoList = catalogoList;
    }
    
    

  

}
