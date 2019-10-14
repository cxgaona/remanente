package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.BandejaDao;
import ec.gob.dinardap.remanente.modelo.Bandeja;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import java.util.ArrayList;
import java.util.Date;

@Stateless(name = "BandejaServicio")
public class BandejaServicioImpl extends GenericServiceImpl<Bandeja, Integer> implements BandejaServicio {

    @EJB
    private BandejaDao bandejaDao;

    @Override
    public GenericDao<Bandeja, Integer> getDao() {
        return bandejaDao;
    }

    @Override
    public List<Bandeja> getBandejaByUsuarioAñoMes(Integer usuarioId, Integer anio, Integer mes) {
        List<Bandeja> bandejaList = new ArrayList<Bandeja>();
        String[] criteriaNombres = {"usuarioAsignadoId.usuarioId", "remanenteCuatrimestral.remanenteAnual.anio", "remanenteMensualId.mes"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {usuarioId, anio, mes};
        String[] orderBy = {"fechaRegistro"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        bandejaList = findByCriterias(criteria);
        return bandejaList;
    }

    @Override
    public void crearBandeja(Bandeja bandeja) {
        this.create(bandeja);
    }

    @Override
    public void editBandeja(Bandeja bandeja) {
        this.update(bandeja);
    }

    @Override
    public void generarNotificacion(List<Usuario> usuarioAsignadoList, Integer usuarioSolicitanteId,
            Integer remanenteCuatrimestralId, Integer remanenteAnualId, InstitucionRequerida institucion,
            Integer remanenteMensualId, String descripcion, String estado) {
        for (Usuario userAsignado : usuarioAsignadoList) {
            Bandeja bandeja = new Bandeja();
            Usuario us = new Usuario();
            us.setUsuarioId(usuarioSolicitanteId);
            RemanenteCuatrimestral rc = new RemanenteCuatrimestral();
            rc.setRemanenteCuatrimestralPK(new RemanenteCuatrimestralPK(remanenteCuatrimestralId, remanenteAnualId, institucion.getInstitucionId()));            
            RemanenteMensual rm = new RemanenteMensual();
            rm.setRemanenteMensualId(remanenteMensualId);
            bandeja.setUsuarioSolicitanteId(us);
            bandeja.setRemanenteCuatrimestral(rc);
            if(remanenteMensualId==0){
                bandeja.setRemanenteMensualId(null);
            }else{
                bandeja.setRemanenteMensualId(rm);
            }            
            
            bandeja.setDescripcion(descripcion);
            bandeja.setEstado(estado);
            bandeja.setLeido(Boolean.FALSE);
            bandeja.setFechaRegistro(new Date());
            bandeja.setUsuarioAsignadoId(userAsignado);
            create(bandeja);
            System.out.println("Mensaje enviado a : " + userAsignado.getNombre());
            //implementar envio de correo electrónico           
        }

    }

}
