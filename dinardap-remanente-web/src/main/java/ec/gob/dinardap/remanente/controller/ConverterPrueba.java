/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author christian.gaona
 */
@FacesConverter(value = "converterPrueba")
public class ConverterPrueba implements Converter<Object> {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        InstitucionRequerida ir = null;
        if (value.isEmpty()) {
            ir = new InstitucionRequerida();
        } else {
            System.out.println("Tiene Institucion asignada");
        }
        return ir;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object t) {
        System.out.println("oBTENER VALOR" + t);
        return t.toString();
    }

}
