/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller.converters;

import ec.gob.dinardap.seguridad.modelo.Perfil;
import ec.gob.dinardap.seguridad.servicio.PerfilServicio;
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
@Named(value = "perfilConverter")
public class PerfilConverter implements Converter {

    @EJB
    private PerfilServicio service;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        Perfil p = new Perfil();
        if (value != null && value.trim().length() > 0) {
            try {
                p = service.findByPk(Integer.parseInt(value));
                return p;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Perfil"));
            }
        } else {
            return p;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Perfil) object).getPerfilId());
        } else {
            return null;
        }
    }
}
