package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.dto.BandejaDTO;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
        String[] perfilesArray=getSessionVariable("perfil").split(",");
        List<String> perfilesList = Arrays.asList(perfilesArray);
        switch(bandejaSelected.getTipo()){
            case "RM":
                for(String perfil:perfilesList ){                   
                    if(perfil.equals("2")){
                        linkRedireccion = "administracion/adminRemanenteMensual.jsf";
                    }else if(perfil.equals("3")){
                        linkRedireccion = "gestionRemanenteMensual.jsf";
                    }else if(perfil.equals("4")){
                        linkRedireccion = "verificarRemanenteMensual.jsf";
                    }else if(perfil.equals("5")){
                        linkRedireccion = "validarRemanenteMensual.jsf";
                    }
                }
                break;
            case "RC":
                for(String perfil:perfilesList ){                   
                    if(perfil.equals("4")){
                        linkRedireccion = "gestionRemanenteCuatrimestral.jsf";
                    }else if(perfil.equals("5")){
                        linkRedireccion = "gestionValidacionRemanenteCuatrimestral.jsf";
                    }
                }
                break;
            case "IA":
                for(String perfil:perfilesList ){                   
                    if(perfil.equals("6") || perfil.equals("7")){
                        linkRedireccion = "inventarios/inventarioResumenLibros.jsf";
                    }else if(perfil.equals("8")){
                        linkRedireccion = "inventarios/inventarioRevisarResumenLibros.jsf";
                    }else if(perfil.equals("9")){
                        linkRedireccion = "inventarios/inventarioAdministradorInventarios.jsf";
                    }
                }
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
