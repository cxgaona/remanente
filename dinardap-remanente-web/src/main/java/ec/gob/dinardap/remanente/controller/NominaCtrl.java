package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
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

@Named(value = "nominaCtrl")
@ViewScoped
public class NominaCtrl extends BaseCtrl implements Serializable {

    @EJB
    private NominaServicio nominaServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    private String titulo;
    private List<Nomina> nominaList;
    private Integer anio, mes;
    private Integer institucionId;
    private Date fecha;
    private Nomina nominaSelected;

    @PostConstruct
    protected void init() {
        titulo = "Nómina";
        nominaList = new ArrayList<Nomina>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        fecha = new Date();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        nominaList = nominaServicio.getNominaByInstitucionFecha(institucionId, anio, mes);
        nominaSelected = new Nomina();
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
        //9 CORRESPONDE A REMUNERACIONES(NOMINA) DEL CATALOGO DE TRANSACCIONES
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 9);
        newNomina.setTransaccionId(t);
        nominaServicio.crearNomina(newNomina);
        nominaList.add(newNomina);
    }

    public void onRowEditNomina(RowEditEvent event) {
        Nomina nomina = new Nomina();
        nomina = (Nomina) event.getObject();
        Transaccion t = new Transaccion();
        //9 CORRESPONDE A REMUNERACIONES(NOMINA) DEL CATALOGO DE TRANSACCIONES
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(institucionId, anio, mes, 9);
        nomina.setTransaccionId(t);
        nomina.setFechaRegistro(new Date());
        nominaServicio.editNomina(nomina);
    }

    public void reloadNomina() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;

        nominaList = nominaServicio.getNominaByInstitucionFecha(institucionId, anio, mes);
    }

    public void onRowDeleteNomina() {
        nominaServicio.borrarNomina(nominaSelected);
        nominaServicio.actualizarTransaccionValor(institucionId, anio, mes);
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

    public List<Nomina> getNominaList() {
        return nominaList;
    }

    public void setNominaList(List<Nomina> nominaList) {
        this.nominaList = nominaList;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Nomina getNominaSelected() {
        return nominaSelected;
    }

    public void setNominaSelected(Nomina nominaSelected) {
        this.nominaSelected = nominaSelected;
    }

}
