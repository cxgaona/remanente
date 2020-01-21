/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.utils;

import ec.gob.dinardap.remanente.controller.TramitePropiedadCtrl;
import ec.gob.dinardap.remanente.servicio.DiasNoLaborablesServicio;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author christian.gaona
 */
@ViewScoped
public class CargaBloque implements Serializable {

    @EJB
    private DiasNoLaborablesServicio diasNoLaborablesServicio;

    public String getDato(XSSFWorkbook worbook, XSSFCell cell) {
        String dato = "";
        switch (cell.getCellType()) {
            case NUMERIC:
                dato = cell.getNumericCellValue() + "";
                break;
            case FORMULA:
                FormulaEvaluator evaluator = worbook.getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case STRING:
                        dato = cellValue.getStringValue();
                        break;
                    case NUMERIC:
                        dato = (int) cellValue.getNumberValue() + "";
                        break;
                }
                break;
            case STRING:
                dato = cell.getStringCellValue();
                break;
            case BLANK:
                dato = "";
                break;
            default:
                dato = cell.getStringCellValue();
                break;
        }
        return dato;
    }

    public String validarCampoNumero(String data) { //Valida campos Número de tramites comprobantes numero repertorio
        String datoValidado = "INVALIDO";
        if (data.equals("") || data == null) {
            datoValidado = "INVALIDO";
        } else {
            if (isNumeric(data)) {
                if (data.contains(".") || data.contains(",")) {
                    Double datoAux = Double.parseDouble(data);
                    data = datoAux.intValue() + "";
                }
                datoValidado = data;
            }
        }
        return datoValidado;
    }

    public String validarCampoValorNumerico(String data) {
        String datoValidado = "INVALIDO";
        if (data.equals("") || data == null) {
            datoValidado = "INVALIDO";
        } else {
            if (isNumeric(data)) {
                datoValidado = Double.parseDouble(data) + "";
            } else {
                datoValidado = "INVALIDO";
            }
        }
        return datoValidado + "";
    }

    public String validarCampoFecha(String data) {
        String datoValidado = "INVALIDO";
        if (data == null || data.equals("")) {
            datoValidado = "INVALIDO";
        } else {
            if (isDate(data)) {
                try {
                    Calendar fecha = Calendar.getInstance();
                    fecha.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(data));
                    Calendar fechaActual = Calendar.getInstance();
                    if (diasNoLaborablesServicio.habilitarDiasAdicionales(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH) + 1)) {
                        datoValidado = data;
                    } else {
                        datoValidado = "INVALIDO";
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(TramitePropiedadCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return datoValidado;
    }

    public String validarCampoVacio(String data) {
        String datoValidado = "INVALIDO";
        if (data.equals("") || data == null) {
            datoValidado = "INVALIDO";
        } else {
            datoValidado = data;
        }
        return datoValidado;
    }

    public boolean isNumeric(String cadena) {
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException excepcion) {
            return false;
        }
    }

    public boolean isDate(String cadena) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(cadena);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

}
