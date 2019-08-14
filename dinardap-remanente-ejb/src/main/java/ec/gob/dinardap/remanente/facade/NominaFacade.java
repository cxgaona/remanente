/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.facade;

import ec.gob.dinardap.remanente.modelo.Nomina;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author christian.gaona
 */
@Stateless
public class NominaFacade extends AbstractFacade<Nomina> {

    @PersistenceContext(unitName = "remanentePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NominaFacade() {
        super(Nomina.class);
    }
    
}
