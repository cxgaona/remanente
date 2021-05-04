package ec.gob.dinardap.remanente.servicio;


import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.InventarioDeclaracion;


@Local
public interface InventarioDeclaracionServicio extends GenericService<InventarioDeclaracion, Integer> { 
    public InventarioDeclaracion getInventarioPorInstitucionAño(Integer institucionId, Integer año);    
    public void editInventarioDeclaracion(InventarioDeclaracion inventarioDeclaracion, SftpDto sftpDto);
    public byte[] descargarArchivo(SftpDto sftpDto);

}
