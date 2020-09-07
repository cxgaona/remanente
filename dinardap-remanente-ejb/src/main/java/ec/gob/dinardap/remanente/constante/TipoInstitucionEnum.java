/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.constante;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christian.gaona
 */
public enum TipoInstitucionEnum {
    DINARDAP((Integer)1),
    REGIONAL((Integer)2),
    GAD((Integer)3),
    RMX_AUTONOMIA_FINANCIERA((Integer)4),
    RMX_SIN_AUTONOMIA_FINANCIERA((Integer)5);
    
    private final Integer tipoInstitucion;

    private TipoInstitucionEnum(Integer tipoInstitucion) {
        this.tipoInstitucion = tipoInstitucion;
    }

    public Integer getTipoInstitucion() {
        return tipoInstitucion;
    }
    public static List<Integer> getTipoInstitucionList(){
        List<Integer> tipoInstitucionList = new ArrayList<Integer>();
        for (TipoInstitucionEnum value : TipoInstitucionEnum.values()) {
            tipoInstitucionList.add(value.tipoInstitucion);
        }
        return tipoInstitucionList;
    }
}
