/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller.converters;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
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
@Named(value = "institucionConverter")
public class InstitucionConverter implements Converter {

    @EJB
    private InstitucionServicio service;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        Institucion ir = new Institucion();
        if (value != null && value.trim().length() > 0) {
            try {
                ir = service.findByPk(Integer.parseInt(value));
                return ir;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Institución"));
            }
        } else {
            return ir;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Institucion) object).getInstitucionId());
        } else {
            return null;
        }
    }

}
