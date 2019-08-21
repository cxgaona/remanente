package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;

@Local
public interface InstitucionRequeridaServicio extends GenericService<InstitucionRequerida, Integer> {

    public List<InstitucionRequerida> getInstitucion();

    public List<InstitucionRequerida> getRegistroMixtoList();

    public List<InstitucionRequerida> getGADList();

    public List<InstitucionRequerida> getDireccionRegionalList();

    public InstitucionRequerida getInstitucionById(Integer institucionId);

//    public List<InstitucionRequerida> getDireccionRegionalList(InstitucionRequerida registroMixto);
}
