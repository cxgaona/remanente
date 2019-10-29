/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.utils;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author enery
 */
public class FacesUtils {

    public static String getPath() {
        try {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                    .getExternalContext().getContext();
            return ctx.getRealPath("/");
        } catch (Exception e) {
            return null;
        }
    }
    
     public static String generarContraseña() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String claveGenerada = "";
        int numero;
        for (Integer i = 0; i < 8; i++) {
            numero = (int) (Math.random() * 36);
            claveGenerada = claveGenerada + str.substring(numero, numero + 1);
        }
        return claveGenerada;
    }
}
