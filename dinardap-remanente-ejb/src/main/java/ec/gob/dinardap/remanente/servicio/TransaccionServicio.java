package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import java.util.List;

@Local
public interface TransaccionServicio extends GenericService<Transaccion, Integer> {

    public void editTransaccion(Transaccion transaccion, SftpDto sftpDto);

    public void editTransaccion(Transaccion transaccion);

    public Transaccion crearTransaccion(Transaccion transaccion, SftpDto sftpDto);

    public Transaccion getTransaccionByInstitucionFechaTipo(Integer idInstitucion, Integer anio, Integer mes, Integer tipo);

    public List<Transaccion> getTransaccionByInstitucionAñoMes(Integer institucionId, Integer año, Integer mes, Integer remanenteMensualID);

    public byte[] descargarArchivo(SftpDto sftpDto);

}
