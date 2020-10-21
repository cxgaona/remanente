package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.RemanenteHistorico;
import ec.gob.dinardap.remanente.servicio.RemanenteHistoricoServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "adminRemanenteHistoricoCtrl")
@ViewScoped
public class AdminRemanenteHistoricoCtrl extends BaseCtrl implements Serializable {

    //Declaración de variables
    //Variables de control visual
    private String tituloPagina;

    //Listas
    private List<RemanenteHistorico> remanenteHistoricoList;
    private List<RemanenteHistorico> remanenteHistoricoListFiltrada;

    //
    @EJB
    private RemanenteHistoricoServicio remanenteHistoricoServicio ;
    

    @PostConstruct
    protected void init() {
        tituloPagina = "Remanentes Históricos";
        remanenteHistoricoList = new ArrayList<RemanenteHistorico>();
        remanenteHistoricoList = remanenteHistoricoServicio.findAll();
    }

    

    //Getters & Setters

    public List<RemanenteHistorico> getRemanenteHistoricoList() {
        return remanenteHistoricoList;
    }

    public void setRemanenteHistoricoList(List<RemanenteHistorico> remanenteHistoricoList) {
        this.remanenteHistoricoList = remanenteHistoricoList;
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<RemanenteHistorico> getRemanenteHistoricoListFiltrada() {
        return remanenteHistoricoListFiltrada;
    }

    public void setRemanenteHistoricoListFiltrada(List<RemanenteHistorico> remanenteHistoricoListFiltrada) {
        this.remanenteHistoricoListFiltrada = remanenteHistoricoListFiltrada;
    }
   

}
