package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.RemanenteAnual;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.servicio.RemanenteCuatrimestralServicio;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "pruebaSesionCtrl")
@ViewScoped
public class PruebaSesionCtrl extends BaseCtrl implements Serializable {

    private String titulo;

    @PostConstruct
    protected void init() {
        titulo = "Contenido";
        this.setSessionVariable("usuarioId", "1");
        this.setSessionVariable("usuario", "christian.gaona");
        this.setSessionVariable("institucionId", "186");
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
