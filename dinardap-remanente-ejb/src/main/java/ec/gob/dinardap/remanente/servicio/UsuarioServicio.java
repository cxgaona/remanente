package ec.gob.dinardap.remanente.servicio;


import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import java.util.List;

@Local
public interface UsuarioServicio extends GenericService<Usuario, Integer> {
   public List<Usuario> getUsuarioByIstitucionRol(Institucion institucion, Integer rolAsignado, Integer rolSolicitante, RemanenteCuatrimestral remanenteCuatrimestral);   
   public List<Usuario> getUsuarioByIstitucionRolInventario(Institucion institucion, Integer rolAsignado, Integer rolSolicitante, InventarioAnual inventarioAnual);   
}
