package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.Bandeja;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "bandejaCtrl")
@ViewScoped
public class BandejaCtrl extends BaseCtrl implements Serializable {

    private String titulo;
    private Integer usuarioId, anio, mes;
    private List<Bandeja> bandejaList;
    private List<Bandeja> bandejaListMesAnterior;
    private Bandeja bandejaSelected;

    @EJB
    private BandejaServicio bandejaServicio;

    @PostConstruct
    protected void init() {
        titulo = "Bandeja";
        bandejaSelected = new Bandeja();
        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        bandejaList = bandejaServicio.getBandejaByUsuarioAñoMes(usuarioId, anio, mes);
        Integer mesMin, anioMin;
        if (mes == 1) {
            anioMin = anio - 1;
            bandejaListMesAnterior = bandejaServicio.getBandejaByUsuarioAñoMes(usuarioId, anioMin, 12);
        } else {
            mesMin = mes - 1;
            bandejaListMesAnterior = bandejaServicio.getBandejaByUsuarioAñoMes(usuarioId, anio, mesMin);
        }
        bandejaList.addAll(bandejaListMesAnterior);
    }

    public void onRowSelectBandeja() {
        if (bandejaSelected.getLeido().equals(Boolean.FALSE)) {
            bandejaSelected.setLeido(Boolean.TRUE);
            bandejaSelected.setFechaLeido(new Date());
            bandejaServicio.editBandeja(bandejaSelected);
        }
    }

    public void generarNotificacion(Integer usuarioAsignadoId, Integer usuarioSolicitanteId,
            Integer remanenteCuatrimestralId, Integer remanenteAnualId, Integer institucionId,
            Integer remanenteMensualId, String descripcion, String estado) {
        Bandeja bandeja = new Bandeja();
        Usuario ua = new Usuario();
        ua.setUsuarioId(usuarioAsignadoId);
        Usuario us = new Usuario();
        us.setUsuarioId(usuarioSolicitanteId);
        RemanenteCuatrimestral rc = new RemanenteCuatrimestral();
        rc.setRemanenteCuatrimestralPK(new RemanenteCuatrimestralPK(remanenteCuatrimestralId, remanenteAnualId, institucionId));
        RemanenteMensual rm = new RemanenteMensual();
        rm.setRemanenteMensualId(remanenteMensualId);

        bandeja.setUsuarioAsignadoId(ua);
        bandeja.setUsuarioSolicitanteId(us);
        bandeja.setRemanenteCuatrimestral(rc);
        bandeja.setRemanenteMensualId(rm);
        bandeja.setDescripcion(descripcion);
        bandeja.setEstado(estado);
        bandeja.setLeido(Boolean.FALSE);
        bandeja.setFechaRegistro(new Date());
        bandejaServicio.crearBandeja(bandeja);
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

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<Bandeja> getBandejaList() {
        return bandejaList;
    }

    public void setBandejaList(List<Bandeja> bandejaList) {
        this.bandejaList = bandejaList;
    }

    public List<Bandeja> getBandejaListMesAnterior() {
        return bandejaListMesAnterior;
    }

    public void setBandejaListMesAnterior(List<Bandeja> bandejaListMesAnterior) {
        this.bandejaListMesAnterior = bandejaListMesAnterior;
    }

    public Bandeja getBandejaSelected() {
        return bandejaSelected;
    }

    public void setBandejaSelected(Bandeja bandejaSelected) {
        this.bandejaSelected = bandejaSelected;
    }

}
