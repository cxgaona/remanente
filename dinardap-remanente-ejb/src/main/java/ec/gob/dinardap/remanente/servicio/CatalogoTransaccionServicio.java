package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;

@Local
public interface CatalogoTransaccionServicio extends GenericService<CatalogoTransaccion, Integer> {

    public List<CatalogoTransaccion> getCatalogoTransaccionList();

    public List<CatalogoTransaccion> getCatalogoTransaccionListTipo(String tipo);

    public CatalogoTransaccion getCatalogoTransaccionEgresoNombre(String nombre);

    public CatalogoTransaccion getCatalogoTransaccionIngresoTipoNombre(String tipo, String nombre);
}
