package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "remanenteMensualCtrl")
@ViewScoped
public class RemanenteMensualCtrl extends BaseCtrl implements Serializable {

    private String tituloPagina;
    private List<RemanenteMensual> remanenteMensualList;
    private Integer institucionId;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @PostConstruct
    protected void init() {
        tituloPagina = "Gestión Remanente Mensual";
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId);
        System.out.println("Front");
        for (RemanenteMensual rm : remanenteMensualList) {
            System.out.println("RM: " + rm.getMes());
            System.out.println("RM: " + rm.getSolicitudCambioUrl());
            System.out.println("RM: " + rm.getInformeAprobacionUrl());
            System.out.println("RM: " + rm.getComentarios());
            System.out.println("RM: " + rm.getTotal());
            System.out.println("RM: " + rm.getFechaRegistro());
            System.out.println("RM INS: " + rm.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId());
            System.out.println("RM INS: " + rm.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getNombre());
        }
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

}
