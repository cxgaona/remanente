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
public enum TipoLibroEnum {
    PROPIEDAD((Integer)1),
    MERCANTIL((Integer)2),
    REPERTORIO_PROPIEDAD((Integer)3),
    REPERTORIO_MERCANTIL((Integer)4),
    INDICE_GENERAL_PROPIEDAD((Integer)5),
    INDICE_GENERAL_MERCANTIL((Integer)6);
    
    private final Integer tipoLibro;

    private TipoLibroEnum(Integer tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    public Integer getTipoLibro() {
        return tipoLibro;
    }
    public static List<Integer> getTipoLibroList(){
        List<Integer> tipoLibroList = new ArrayList<Integer>();
        for (TipoLibroEnum value : TipoLibroEnum.values()) {
            tipoLibroList.add(value.tipoLibro);
        }
        return tipoLibroList;
    }
}
