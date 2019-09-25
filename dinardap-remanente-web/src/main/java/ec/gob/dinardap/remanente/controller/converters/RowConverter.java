/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller.converters;

import ec.gob.dinardap.remanente.controller.Row;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author enery
 */
@FacesConverter("rowConverter")
public class RowConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        System.out.println("Valor en getAsObject: " + value);
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        String id = uic.getId();
        String simboloDolar = "$";
        String valor = "";
        Row r = new Row();
        r = (Row) object;
        String format = "";
        BigDecimal valorUIC = new BigDecimal(BigInteger.ZERO);
        if (id.contains("mes1")) {
            valorUIC = r.getValorMes1();
        } else if (id.contains("mes2")) {
            valorUIC = r.getValorMes2();
        } else if (id.contains("mes3")) {
            valorUIC = r.getValorMes3();
        } else if (id.contains("mes4")) {
            valorUIC = r.getValorMes4();
        } else if (id.contains("total")) {
            valorUIC = r.getValorMes1()
                    .add(r.getValorMes2())
                    .add(r.getValorMes3())
                    .add(r.getValorMes4());
        }
        if (r.getNombre().equals("Número de trámites Registro de la Propiedad")
                || r.getNombre().equals("Número de trámites Registro Mercantil")) {
            DecimalFormat decimalFormat = new DecimalFormat("#");
            format = decimalFormat.format(valorUIC);
            valor = format;
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            format = decimalFormat.format(valorUIC);
            valor = simboloDolar + format;
        }
        return valor;
    }
}
