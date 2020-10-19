package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.InventarioAnualDao;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;
import ec.gob.dinardap.remanente.servicio.InventarioAnualServicio;

@Stateless(name = "InventarioAnualServicio")
public class InventarioAnualServicioImpl extends GenericServiceImpl<InventarioAnual, Integer> implements InventarioAnualServicio {

    @EJB
    private InventarioAnualDao inventarioAnualDao;

    @Override
    public GenericDao<InventarioAnual, Integer> getDao() {
        return inventarioAnualDao;
    }

    @Override
    public InventarioAnual getInventarioPorInstitucionAño(Integer institucionId, Integer año) {
        InventarioAnual inventarioAnual = new InventarioAnual();
        String[] criteriaNombres = {"anio", "institucion.institucionId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {año, institucionId};
        String[] orderBy = {"inventarioAnualId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        if (!findByCriterias(criteria).isEmpty()) {
            inventarioAnual = findByCriterias(criteria).get(findByCriterias(criteria).size() - 1);
            for (EstadoInventarioAnual eia : inventarioAnual.getEstadoInventarioAnualList()) {
                eia.getEstadoInventarioAnualId();
            }
        }

        return inventarioAnual;
    }

}
