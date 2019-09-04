package ec.gob.dinardap.remanente.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "pruebaSesionCtrl")
@ViewScoped
public class PruebaSesionCtrl extends BaseCtrl implements Serializable {

    private String titulo;

    @PostConstruct
    protected void init() {
        titulo = "Contenido";
        //        this.setSessionVariable("usuarioId", "1");
        //        this.setSessionVariable("usuario", "christian.gaona");
        //        this.setSessionVariable("institucionId", "186");
        

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
