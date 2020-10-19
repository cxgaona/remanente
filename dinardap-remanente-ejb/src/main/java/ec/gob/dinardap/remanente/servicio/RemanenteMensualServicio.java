package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.dto.RemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;

@Local
public interface RemanenteMensualServicio extends GenericService<RemanenteMensual, Integer> {

    public List<RemanenteMensualDTO> getRemanenteMensualByInstitucion(Integer institucionId, Integer año);

    public List<RemanenteMensual> getRemanenteMensualProrroga();

    public void editRemanenteMensual(RemanenteMensual remanenteMensual);

    public void editRemanenteMensual(RemanenteMensual remanenteMensual, SftpDto sftpDto);

    public void crearVersionRemanenteMensual(RemanenteMensual remanenteMensualOrigen);

    public RemanenteMensual obtenerVersionRemanenteMensual(Integer remanenteMensualOrigen);

    public RemanenteMensual getUltimoRemanenteMensual(Integer institucionId, Integer año, Integer mes);

    public byte[] descargarArchivo(SftpDto sftpDto);

}
