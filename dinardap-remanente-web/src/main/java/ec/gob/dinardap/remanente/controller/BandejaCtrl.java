package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.dto.BandejaDTO;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import java.io.IOException;
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
    private List<BandejaDTO> bandejaList;
    private BandejaDTO bandejaSelected;

    @EJB
    private BandejaServicio bandejaServicio;

    @PostConstruct
    protected void init() {
        titulo = "Bandeja";
        linkRedireccion = "#";
        bandejaSelected = new BandejaDTO();
        usuarioId = Integer.parseInt(getSessionVariable("usuarioId"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        bandejaList = bandejaServicio.getBandejaByUsuarioAñoMes(usuarioId, anio, mes);
    }

    public void onRowSelectBandeja() throws IOException {
        //TO-DO: split en variable perfil
        switch (getSessionVariable("perfil")) {
            case "3":
                linkRedireccion = "gestionRemanenteMensual.jsf";
                break;
            case "4":
                linkRedireccion = "verificarRemanenteMensual.jsf";
                if (bandejaSelected.getTipo().equals("RC")) {
                    linkRedireccion = "gestionRemanenteCuatrimestral.jsf";
                }
                break;
            case "5":
                linkRedireccion = "validarRemanenteMensual.jsf";
                if (bandejaSelected.getTipo().equals("RC")) {
                    linkRedireccion = "gestionValidacionRemanenteCuatrimestral.jsf";
                }
                break;
            case "2":
                linkRedireccion = "administracion/adminRemanenteMensual.jsf";
                break;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(linkRedireccion);

        if (bandejaSelected.getLeido().equals(Boolean.FALSE)) {
            bandejaSelected.setLeido(Boolean.TRUE);
            bandejaSelected.setFechaLeido(new Date());
            bandejaServicio.editBandeja(bandejaSelected);
        }
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

    public List<BandejaDTO> getBandejaList() {
        return bandejaList;
    }

    public void setBandejaList(List<BandejaDTO> bandejaList) {
        this.bandejaList = bandejaList;
    }

    public String getLinkRedireccion() {
        return linkRedireccion;
    }

    public void setLinkRedireccion(String linkRedireccion) {
        this.linkRedireccion = linkRedireccion;
    }

    public BandejaDTO getBandejaSelected() {
        return bandejaSelected;
    }

    public void setBandejaSelected(BandejaDTO bandejaSelected) {
        this.bandejaSelected = bandejaSelected;
    }

}
