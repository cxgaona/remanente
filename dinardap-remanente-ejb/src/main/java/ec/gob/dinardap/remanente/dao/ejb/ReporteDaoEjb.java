package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.ReporteDao;
import ec.gob.dinardap.remanente.dto.ConteoTramitesDTO;
import ec.gob.dinardap.remanente.dto.UltimoEstadoDTO;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "ReporteDao")
public class ReporteDaoEjb extends RemanenteGenericDao<RemanenteMensual, Integer> implements ReporteDao {

    public ReporteDaoEjb() {
        super(RemanenteMensual.class);
    }

    @Override
    public List<UltimoEstadoDTO> getUltimoEstado(Integer año) {
        StringBuilder sql = new StringBuilder("SELECT "
                + "i.institucion_id AS \"institucionId\", "
                + "i.nombre AS \"nombre\", "
                + "CASE WHEN i.tipo_institucion_id=4 THEN dr.nombre "
                + "WHEN i.tipo_institucion_id=5 THEN dr1.nombre end AS \"direccionRegional\", "
                + "erm.descripcion AS \"estado\", "
                + "erm.fecha_registro AS \"fechaRegistro\", "
                + "rm.mes AS \"mes\" "
                + "FROM ec_dinardap_remanente.estado_remanente_mensual erm "
                + "INNER JOIN ec_dinardap_remanente.remanente_mensual rm ON rm.remanente_mensual_id=erm.remanente_mensual_id "
                + "INNER JOIN ec_dinardap_seguridad.institucion i ON i.institucion_id=rm.institucion_id "
                + "INNER JOIN ec_dinardap_seguridad.institucion dr ON dr.institucion_id=i.ins_institucion_id "
                + "INNER JOIN ec_dinardap_seguridad.institucion gad ON gad.institucion_id=i.ins_institucion_id "
                + "INNER JOIN ec_dinardap_seguridad.institucion dr1 ON dr1.institucion_id=gad.ins_institucion_id "
                + "WHERE estado_remanente_mensual_id IN ( "
                + "SELECT max(estado_remanente_mensual_id) "
                + "FROM ec_dinardap_remanente.estado_remanente_mensual erm  "
                + "WHERE erm.remanente_mensual_id IN "
                + "(SELECT max(rm.remanente_mensual_id) "
                + "FROM ec_dinardap_remanente.remanente_mensual rm "
                + "INNER JOIN ec_dinardap_remanente.remanente_anual ra ON rm.remanente_anual_id=ra.remanente_anual_id "
                + "WHERE ra.anio=" + año + " "
                + "GROUP BY rm.institucion_id, mes, ra.remanente_anual_id,ra.anio ORDER BY rm.institucion_id,anio,mes "
                + ")"
                + "GROUP BY remanente_mensual_id ORDER BY remanente_mensual_id "
                + ")ORDER BY i.institucion_id, rm.mes;");
        Query query = em.createNativeQuery(sql.toString());

        List<Object[]> dataList = query.getResultList();
        List<UltimoEstadoDTO> ultimoEstadoDTOList = new ArrayList<UltimoEstadoDTO>();
        UltimoEstadoDTO ultimoEstadoDTO;

        if (dataList != null && !dataList.isEmpty()) {
            ultimoEstadoDTO = new UltimoEstadoDTO();
            for (Object[] item : dataList) {
                ultimoEstadoDTO = new UltimoEstadoDTO();
                try {
                    ultimoEstadoDTO.setInstitucionId(Integer.parseInt(item[0].toString()));
                    ultimoEstadoDTO.setNombre(item[1].toString());
                    ultimoEstadoDTO.setDireccionRegional(item[2].toString());
                    ultimoEstadoDTO.setEstado(item[3].toString());
                    ultimoEstadoDTO.setFechaRegistro(new SimpleDateFormat("yyy-MM-dd hh:mm:ss").parse(item[4].toString()));
                    ultimoEstadoDTO.setMes(Integer.parseInt(item[5].toString()));
                    ultimoEstadoDTOList.add(ultimoEstadoDTO);
                } catch (ParseException ex) {
                    Logger.getLogger(ReporteDaoEjb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return ultimoEstadoDTOList;
    }

    @Override
    public List<ConteoTramitesDTO> getConteoTramites(Integer año) {
        StringBuilder sql = new StringBuilder("SELECT ir.nombre, rm.mes, tr.actividad_registral, tr.tipo, count(tr.tipo) "
                + "FROM ec_dinardap_remanente.tramite tr "
                + "INNER JOIN ec_dinardap_remanente.transaccion t on t.transaccion_id=tr.transaccion_id "
                + "INNER JOIN ec_dinardap_remanente.remanente_mensual rm on t.remanente_mensual_id=rm.remanente_mensual_id "
                + "INNER JOIN ec_dinardap_seguridad.institucion ir on ir.institucion_id=rm.institucion_id "
                + "WHERE t.remanente_mensual_id in ( "
                + "SELECT max(rm.remanente_mensual_id) "
                + "FROM ec_dinardap_remanente.remanente_mensual rm "
                + "INNER JOIN ec_dinardap_remanente.remanente_anual ra ON rm.remanente_anual_id=ra.remanente_anual_id "
                + "WHERE ra.anio = " + año + " "
                + "GROUP BY rm.institucion_id, mes, ra.remanente_anual_id,ra.anio ORDER BY rm.institucion_id,anio,mes ) "
                + "GROUP BY ir.institucion_id, rm.mes, tr.actividad_registral, tr.tipo "
                + "ORDER BY ir.institucion_id, rm.mes, tr.actividad_registral, tr.tipo");
        Query query = em.createNativeQuery(sql.toString());

        List<Object[]> dataList = query.getResultList();
        List<ConteoTramitesDTO> conteoTramitesDTOList = new ArrayList<ConteoTramitesDTO>();
        ConteoTramitesDTO conteoTramitesDTO;

        if (dataList != null && !dataList.isEmpty()) {
            for (Object[] item : dataList) {
                conteoTramitesDTO = new ConteoTramitesDTO();
                conteoTramitesDTO.setNombreInstitucion(item[0].toString());
                conteoTramitesDTO.setMes(Integer.parseInt(item[1].toString()));
                conteoTramitesDTO.setActividadRegistral(item[2].toString());
                conteoTramitesDTO.setTipoTramite(item[3].toString());
                conteoTramitesDTO.setConteo(Integer.parseInt(item[4].toString()));
                conteoTramitesDTOList.add(conteoTramitesDTO);
            }
        }
        return conteoTramitesDTOList;
    }

}
