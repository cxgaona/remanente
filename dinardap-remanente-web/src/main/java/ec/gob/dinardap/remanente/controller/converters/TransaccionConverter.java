/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller.converters;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *
 * @author enery
 */
@ManagedBean
@Named(value = "transaccionConverter")
public class TransaccionConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        System.out.println("Valor en getAsObject: " + value);
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
//        if (object != null) {
//            if(((Transaccion) object).get)
//            return String.valueOf(((Transaccion) object).getValorTotal());
//        } else {
//            return null;
//        }
        System.out.println("Valor en getAsString: " + ((Transaccion) object).getTransaccionId());
        return null;
    }
}
