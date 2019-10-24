/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller.converters;

import javax.annotation.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
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
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        return null;
    }
}
