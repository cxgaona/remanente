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

@Named(value = "pruebaCtrl")
@ViewScoped
public class PruebaCtrl extends BaseCtrl implements Serializable {

    @EJB
    private RemanenteCuatrimestralServicio remanenteCuatrimestralServicio;

    private String titulo;
    private List<RemanenteCuatrimestral> remanenteCuatrimestralList;

    @PostConstruct
    protected void init() {
        titulo = "Prueba";
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.YEAR, 2019);
        fecha.set(Calendar.MONTH, 10);
        fecha.set(Calendar.DAY_OF_MONTH, 28);
        Date date = fecha.getTime();
        fecha.set(Calendar.YEAR, 2000);
        fecha.set(Calendar.MONTH, 2);
        fecha.set(Calendar.DAY_OF_MONTH, 15);
        date = fecha.getTime();
        RemanenteCuatrimestral rc = new RemanenteCuatrimestral();
        remanenteCuatrimestralServicio.createRemanenteCuatrimestral(date, 220);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
