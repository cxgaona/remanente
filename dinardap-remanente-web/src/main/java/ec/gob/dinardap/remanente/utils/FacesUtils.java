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
}
