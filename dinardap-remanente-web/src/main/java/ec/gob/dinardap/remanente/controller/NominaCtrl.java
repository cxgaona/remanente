package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteAnual;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteCuatrimestralServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

@Named(value = "nominaCtrl")
@ViewScoped
public class NominaCtrl extends BaseCtrl implements Serializable {

    @EJB
    private NominaServicio nominaServicio;

    private String titulo;   
    private List <Nomina> nominaList;

    @PostConstruct
    protected void init() {
        titulo = "Nómina";
        nominaList = new ArrayList<Nomina>();
        
    }
    
    public void onRowEditNomina(RowEditEvent event){
        Nomina nomina = new Nomina();
        nomina= (Nomina) event.getObject(); 
        Transaccion t = new Transaccion();
        t.setTransaccionId(2493);
        nomina.setTransaccionId(t);
        nomina.setFechaRegistro(new Date());
        nominaServicio.crearNomina(nomina);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Nomina> getNominaList() {
        return nominaList;
    }

    public void setNominaList(List<Nomina> nominaList) {
        this.nominaList = nominaList;
    }
    
    

}
