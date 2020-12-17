package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Nomina;

@Local
public interface NominaServicio extends GenericService<Nomina, Integer> {

    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes, Integer idRemanenteMensual);

    public List<Nomina> getNominaByTransaccion(Integer transaccionId);

    public void crearNominas(List<Nomina> nominas);

    public void borrarNominas(List<Nomina> nominas);

    public void actualizarTransaccionValor(Integer remanenteMensualId);
}
