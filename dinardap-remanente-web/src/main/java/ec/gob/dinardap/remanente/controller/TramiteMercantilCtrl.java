package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.remanente.servicio.TramiteServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "tramiteMercantilCtrl")
@ViewScoped
public class TramiteMercantilCtrl extends BaseCtrl implements Serializable {

    @EJB
    private TramiteServicio tramiteServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;

    private String tituloMercantil, tituloPropiedad, actividadRegistral;
    private List<Tramite> tramiteList;
    private Integer anio, mes;
    private Integer institucionId;
    private Date fecha;
    private Tramite tramiteSelected;
    private Integer idCatalogoTransaccion;
    private List<CatalogoTransaccion> catalogoList;
    private Boolean onEdit;
    private Boolean onCreate;
    private Boolean renderEdition;
    private String btnGuardar;

    @PostConstruct
    protected void init() {
        tituloPropiedad = "Trámite Propiedad";
        tituloMercantil = "Trámite Mercantil";
        actividadRegistral = "Mercantil";
        tramiteList = new ArrayList<Tramite>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fecha = new Date();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        tramiteList = tramiteServicio.getTramiteByInstitucionFechaActividad(institucionId, anio, mes, "Mercantil");
        tramiteSelected = new Tramite();
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        btnGuardar = "";
    }
  
    public void nuevoTramite() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        tramiteSelected = new Tramite();
        btnGuardar = "Guardar";
        //tipoInstitucion = "Dirección Regional";
        //institucionRequeridaList = institucionRequeridaServicio.getDireccionRegionalList();

    }

    public void onRowSelectTramite() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        onEdit = Boolean.TRUE;
        btnGuardar = "Actualizar";

    }

    public void cancelar() {
        tramiteList = new ArrayList<Tramite>();
        tramiteSelected = new Tramite();
        reloadTramite();
        onEdit = Boolean.FALSE;
        onCreate = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
    }

    public void guardar() {
        tramiteSelected.setActividadRegistral(actividadRegistral);
        if (onCreate) {
            tramiteSelected.setFechaRegistro(new Date()); 
            catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionListTipo(actividadRegistral);
            for (CatalogoTransaccion ct : catalogoList) {
                if (ct.getNombre().equals(tramiteSelected.getTipo())) {
                    idCatalogoTransaccion = ct.getCatalogoTransaccionId();
                }
            }
            Transaccion t = new Transaccion();
            t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);            
            tramiteSelected.setTransaccionId(t);
            tramiteServicio.crearTramite(tramiteSelected);
            tramiteList = new ArrayList<Tramite>();
            tramiteSelected = new Tramite();
            //reloadTramite();
            onEdit = Boolean.FALSE;
            onCreate = Boolean.FALSE;
            renderEdition = Boolean.FALSE;
        } else if (onEdit) {
            catalogoList = catalogoTransaccionServicio.getCatalogoTransaccionListTipo(actividadRegistral);
            for (CatalogoTransaccion ct : catalogoList) {
                if (ct.getNombre().equals(tramiteSelected.getTipo())) {
                    idCatalogoTransaccion = ct.getCatalogoTransaccionId();
                }
            }
            Transaccion t = new Transaccion();
            t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, idCatalogoTransaccion);            
            tramiteSelected.setTransaccionId(t);
            tramiteServicio.editTramite(tramiteSelected);
            tramiteList = new ArrayList<Tramite>();
            tramiteSelected = new Tramite();
            //reloadTramite();
            onEdit = Boolean.FALSE;
            onCreate = Boolean.FALSE;
            renderEdition = Boolean.FALSE;
        }
        //llamar a la funcion de actualizacion de valores
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = transaccionServicio.getTransaccionByInstitucionAñoMes(tramiteSelected.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                tramiteSelected.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                tramiteSelected.getTransaccionId().getRemanenteMensualId().getMes());
        for (Transaccion tl : transaccionList) {
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(1)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 1);
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(2)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 2);
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(5)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 5);
            }
            if (tl.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(6)) {
                tramiteServicio.actualizarTransaccionValor(tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                        tl.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                        tl.getRemanenteMensualId().getMes(), 6);
            }
        }
    }

    public void reloadTramite() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;

        tramiteList = tramiteServicio.getTramiteByInstitucionFechaActividad(institucionId, anio, mes, "Mercantil");
    }

    public void onRowDeleteFacturaPagada() {
        /*facturaPagadaServicio.borrarFacturaPagada(facturaPagadaSelected);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 10);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 11);
        facturaPagadaServicio.actualizarTransaccionValor(institucionId, anio, mes, 12);
        reloadFacturaPagada();*/
    }

    public String getTituloMercantil() {
        return tituloMercantil;
    }

    public void setTituloMercantil(String tituloMercantil) {
        this.tituloMercantil = tituloMercantil;
    }

    public String getTituloPropiedad() {
        return tituloPropiedad;
    }

    public void setTituloPropiedad(String tituloPropiedad) {
        this.tituloPropiedad = tituloPropiedad;
    }

    public List<Tramite> getTramiteList() {
        return tramiteList;
    }

    public void setTramiteList(List<Tramite> tramiteList) {
        this.tramiteList = tramiteList;
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

    public Integer getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(Integer institucionId) {
        this.institucionId = institucionId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Tramite getTramiteSelected() {
        return tramiteSelected;
    }

    public void setTramiteSelected(Tramite tramiteSelected) {
        this.tramiteSelected = tramiteSelected;
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

    public Boolean getOnEdit() {
        return onEdit;
    }

    public void setOnEdit(Boolean onEdit) {
        this.onEdit = onEdit;
    }

    public Boolean getOnCreate() {
        return onCreate;
    }

    public void setOnCreate(Boolean onCreate) {
        this.onCreate = onCreate;
    }

    public Boolean getRenderEdition() {
        return renderEdition;
    }

    public void setRenderEdition(Boolean renderEdition) {
        this.renderEdition = renderEdition;
    }

    public String getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(String btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    
}
