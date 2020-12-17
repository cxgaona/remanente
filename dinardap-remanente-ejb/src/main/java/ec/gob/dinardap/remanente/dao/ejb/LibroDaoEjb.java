package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.LibroDao;
import ec.gob.dinardap.remanente.modelo.Libro;

import javax.ejb.Stateless;

@Stateless(name = "LibroDao")
public class LibroDaoEjb extends RemanenteGenericDao<Libro, Integer> implements LibroDao {

    public LibroDaoEjb() {
        super(Libro.class);
    }

}
