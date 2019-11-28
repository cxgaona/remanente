/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller.converters;

import ec.gob.dinardap.remanente.modelo.Transaccion;
import java.text.DecimalFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author enery
 */
@FacesConverter("valorConverter")
public class ValorConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        String simboloDolar = "$";
        String valor = "";
        Transaccion t = new Transaccion();
        t = (Transaccion) object;
        if (t.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(4)
                || t.getCatalogoTransaccionId().getCatalogoTransaccionId().equals(8)) {
            DecimalFormat decimalFormat = new DecimalFormat("#");
            String format = decimalFormat.format(t.getValorTotal());
            valor = format;
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String format = decimalFormat.format(t.getValorTotal());
            valor = simboloDolar + format;
        }
        return valor;
    }
}
