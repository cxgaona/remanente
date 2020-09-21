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
public enum SistemaIdEnum {
    REMANENTES_SISTEMA_ID((Integer)2);
    
    private final Integer SistemaId;

    private SistemaIdEnum(Integer SistemaId) {
        this.SistemaId = SistemaId;
    }

    public Integer getSistemaId() {
        return SistemaId;
    }    
}
