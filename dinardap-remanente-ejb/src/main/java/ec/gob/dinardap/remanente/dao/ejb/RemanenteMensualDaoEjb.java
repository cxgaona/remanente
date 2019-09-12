package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.RemanenteMensualDao;
import static ec.gob.dinardap.remanente.modelo.CatalogoTransaccion_.tipo;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "RemanenteMensualDao")
public class RemanenteMensualDaoEjb extends RemanenteGenericDao<RemanenteMensual, Integer> implements RemanenteMensualDao {

    public RemanenteMensualDaoEjb() {
        super(RemanenteMensual.class);
    }

}
