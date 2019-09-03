/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author christian.gaona
 */
@FacesConverter("converterInstitucionTipo")
public class GestionUsuariosConverter1 implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        System.out.println("Valor: " + value);
        InstitucionRequerida ir = new InstitucionRequerida();
        if (value.equals("GAD")) {
            ir.setTipo("GAD");
        } else if (value.equals("REGIONAL")) {
            ir.setTipo("REGIONAL");
        }
        return ir;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object t) {
//        System.out.println("Obtener valor: " + t);
        return t.toString();
    }

}
