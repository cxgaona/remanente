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
public enum PerfilEnum {
    SEGURIDAD_ADMINISTRADOR((Integer)1),
    ADMINISTRADOR((Integer)2),
    REGISTRADOR((Integer)3),
    VERIFICADOR((Integer)4),
    VALIDADOR((Integer)5);
    
    private final Integer perfilId;

    private PerfilEnum(Integer perfilId) {
        this.perfilId = perfilId;
    }

    public Integer getPerfilId() {
        return perfilId;
    }
    public static List<Integer> getPerfilIdList(){
        List<Integer> perfilIdList = new ArrayList<Integer>();
        for (PerfilEnum value : PerfilEnum.values()) {
            perfilIdList.add(value.perfilId);
        }
        return perfilIdList;
    }
}
