package ec.gob.dinardap.remanente.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "pruebaCtrl")
@ViewScoped
public class PruebaCtrl extends BaseCtrl implements Serializable {

    private String titulo;

    @PostConstruct
    protected void init() {
        titulo = "Prueba";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
