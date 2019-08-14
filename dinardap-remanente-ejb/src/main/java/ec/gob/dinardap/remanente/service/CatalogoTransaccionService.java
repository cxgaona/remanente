/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.service;

import ec.gob.dinardap.remanente.facade.CatalogoTransaccionFacade;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author christian.gaona
 */
@Stateless
@LocalBean
public class CatalogoTransaccionService {

    @EJB
    CatalogoTransaccionFacade catalogoTransaccionFacade;

    public List<CatalogoTransaccion> getCatalogoTransaccion() {
        System.out.println("En Servicio: ");
        List<CatalogoTransaccion> cts = new CatalogoTransaccionFacade().findAll();
        System.out.println(cts);
        System.out.println("Respuesta: " + cts.toString());

        return null;
    }

}
