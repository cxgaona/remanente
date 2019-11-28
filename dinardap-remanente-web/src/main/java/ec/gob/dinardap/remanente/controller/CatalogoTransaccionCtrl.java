package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "catalogoTransaccionCtrl")
@ViewScoped
public class CatalogoTransaccionCtrl extends BaseCtrl implements Serializable {

    private static final long serialVersionUID = 3066578283525294119L;
    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;

    private List<CatalogoTransaccion> catalogoTransacciones;

    @PostConstruct
    protected void init() {
        catalogoTransacciones = new ArrayList<CatalogoTransaccion>();
        catalogoTransacciones = catalogoTransaccionServicio.getCatalogoTransaccionList();
    }

    public List<CatalogoTransaccion> getCatalogoTransacciones() {
        return catalogoTransacciones;
    }

    public void setCatalogoTransacciones(List<CatalogoTransaccion> catalogoTransacciones) {
        this.catalogoTransacciones = catalogoTransacciones;
    }
}
