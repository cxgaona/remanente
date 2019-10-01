package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.Bandeja;
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

    @EJB
    private BandejaServicio bandejaServicio;

        
    @PostConstruct
    protected void init() {
        titulo="Bandeja";
        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());        
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        bandejaList=bandejaServicio.getBandejaByUsuarioAñoMes(usuarioId, anio, mes);
        Integer mesMin, anioMin;
        if (mes == 1) {
            anioMin=anio-1;            
            bandejaListMesAnterior=bandejaServicio.getBandejaByUsuarioAñoMes(usuarioId, anioMin, 12);
        } else {
            mesMin=mes-1;
            bandejaListMesAnterior=bandejaServicio.getBandejaByUsuarioAñoMes(usuarioId, anio, mesMin);
        }
        bandejaList.addAll(bandejaListMesAnterior);
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
    
  

    
    
    
    
    

}
