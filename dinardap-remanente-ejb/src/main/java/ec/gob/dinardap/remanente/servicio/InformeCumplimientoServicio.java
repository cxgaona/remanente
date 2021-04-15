package ec.gob.dinardap.remanente.servicio;


import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.InformeCumplimiento;
import java.util.List;


@Local
public interface InformeCumplimientoServicio extends GenericService<InformeCumplimiento, Integer> { 
    public List<InformeCumplimiento> getInformesCumplimientoPorInstitucionAñoTipo(Integer institucionId, Integer año, String tipo);    
    public void editInformeCumplimiento(InformeCumplimiento informeCumplimiento, SftpDto sftpDto);
    public byte[] descargarArchivo(SftpDto sftpDto);

}
