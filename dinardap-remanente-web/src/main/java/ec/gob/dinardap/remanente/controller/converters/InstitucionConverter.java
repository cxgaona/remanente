/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller.converters;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author enery
 */
@FacesConverter("institucionConverter")
public class InstitucionConverter implements Converter {

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                InstitucionRequerida ir = new InstitucionRequerida();
                ir = institucionRequeridaServicio.getInstitucionById(Integer.parseInt(value));
                System.out.println("ir: "+ir.getProvinciaCanton());
                return ir;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Institución."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((InstitucionRequerida) object).getInstitucionId());
        } else {
            return null;
        }
    }

}
