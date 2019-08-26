package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private List<Nomina> nominaList;

    @PostConstruct
    protected void init() {
        titulo = "Nómina";
        nominaList = new ArrayList<Nomina>();
        nominaList = nominaServicio.getNominaByInstitucionFecha(186, 2019, 2);

    }

    public void addRowNomina() {
        System.out.println("Agregar nueva fila");
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
        t.setTransaccionId(2493);
        newNomina.setTransaccionId(t);
        nominaServicio.crearNomina(newNomina);
        nominaList.add(newNomina);
    }

    public void onRowEditNomina(RowEditEvent event) {
        Nomina nomina = new Nomina();
        nomina = (Nomina) event.getObject();
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
