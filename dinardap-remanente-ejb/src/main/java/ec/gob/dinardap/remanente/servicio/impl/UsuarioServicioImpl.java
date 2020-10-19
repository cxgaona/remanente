package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.dao.GenericDao;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.seguridad.dao.UsuarioDao;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

@Stateless(name = "UsuarioServicio")
public class UsuarioServicioImpl extends GenericServiceImpl<Usuario, Integer> implements UsuarioServicio {

    @EJB
    private UsuarioDao usuarioDao;

    @Override
    public GenericDao<Usuario, Integer> getDao() {
        return usuarioDao;
    }

    private List<Integer> getInstitucionPadre(Institucion institucion, List<Integer> institucionIdList){
        if(institucion.getInstitucion()!=null){

            institucionIdList.add(institucion.getInstitucionId());
            return getInstitucionPadre(institucion.getInstitucion(), institucionIdList);
        } else {
            institucionIdList.add(institucion.getInstitucionId());
            return institucionIdList;
        }
    }

    private List<Integer> getInstitucionTipos(Institucion institucion, List<Integer> tipoInstitucionList){
        if(institucion.getInstitucion()!=null){
            tipoInstitucionList.add(institucion.getTipoInstitucion().getTipoInstitucionId());
            return getInstitucionTipos(institucion.getInstitucion(), tipoInstitucionList);
        }else{
            tipoInstitucionList.add(institucion.getTipoInstitucion().getTipoInstitucionId());
            return tipoInstitucionList;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Usuario> getUsuarioByIstitucionRol(Institucion institucion, Integer rolAsignado, Integer rolSolicitante, RemanenteCuatrimestral remanenteCuatrimestral) {
        List<Usuario> userList = new ArrayList<Usuario>();
        List<Integer> institucionIdList = new ArrayList<Integer>();
        List<Integer> tipoInstitucionList = new ArrayList<Integer>();
        if (rolSolicitante.equals(2)) {
            institucion = remanenteCuatrimestral.getRemanenteAnual().getInstitucion();
        }
        institucionIdList = getInstitucionPadre(institucion, institucionIdList);
        tipoInstitucionList = getInstitucionTipos(institucion, tipoInstitucionList);
        userList = usuarioDao.obtenerUsuariosPorInstitucionTipoPerfil(institucionIdList, tipoInstitucionList, rolAsignado);

        return userList;
    }

    @Override
    public List<Usuario> getUsuarioByIstitucionRolInventario(Institucion institucion, Integer rolAsignado, Integer rolSolicitante, InventarioAnual inventarioAnual) {
        List<Usuario> userList = new ArrayList<Usuario>();
        List<Integer> institucionIdList = new ArrayList<Integer>();
        List<Integer> tipoInstitucionList = new ArrayList<Integer>();
        if (rolSolicitante.equals(9)) {
            institucion= inventarioAnual.getInstitucion();
        }
        institucionIdList = getInstitucionPadre(institucion, institucionIdList);
        tipoInstitucionList = getInstitucionTipos(institucion, tipoInstitucionList);       
        userList = usuarioDao.obtenerUsuariosPorInstitucionTipoPerfil(institucionIdList, tipoInstitucionList, rolAsignado);
               
        return userList;
    }

}
