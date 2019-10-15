package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.Bandeja;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "bandejaCtrl")
@ViewScoped
public class BandejaCtrl extends BaseCtrl implements Serializable {

    private String titulo, linkRedireccion;
    private Integer usuarioId, anio, mes;
    private List<Bandeja> bandejaList;
    private List<Bandeja> bandejaListMesAnterior;
    private Bandeja bandejaSelected;

    @EJB
    private BandejaServicio bandejaServicio;

    @PostConstruct
    protected void init() {
        titulo = "Bandeja";
        linkRedireccion="#";
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
        switch(this.getSessionVariable("perfil")){
            case "REM-Registrador, ": linkRedireccion="paginas/gestionRemanenteMensual.jsf";
                break;
            case "REM-Verificador, ": linkRedireccion="paginas/verificarRemanenteMensual.jsf";
                break;
            case "REM-Validador, ": linkRedireccion="paginas/validarRemanenteMensual.jsf";
                break;
            case "REM-Administrador, ": linkRedireccion="paginas/administracion/adminRemanenteMensual.jsf";
                break;
        }
    }

    public void onRowSelectBandeja() {
        if (bandejaSelected.getLeido().equals(Boolean.FALSE)) {
            bandejaSelected.setLeido(Boolean.TRUE);
            bandejaSelected.setFechaLeido(new Date());
            bandejaServicio.editBandeja(bandejaSelected);
        }  
        FacesContext.getCurrentInstance().getExternalContext().redirect(linkRedireccion);
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

    public String getLinkRedireccion() {
        return linkRedireccion;
    }

    public void setLinkRedireccion(String linkRedireccion) {
        this.linkRedireccion = linkRedireccion;
    }

}
