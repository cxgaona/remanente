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
public enum EstadoInventarioAnualEnum {
    GENERADO((short)1),
    COMPLETO((short)2),
    RECHAZADO((short)3),
    APROBADO((short)4);
    
    private final Short estadoInventarioAnual;

    private EstadoInventarioAnualEnum(short estadoInventarioAnual) {
        this.estadoInventarioAnual = estadoInventarioAnual;
    }

    public short getEstadoInventarioAnual() {
        return estadoInventarioAnual;
    }
    public static List<Short> getEstadoInventarioAnualList(){
        List<Short> estadoInventarioAnualList = new ArrayList<Short>();
        for (EstadoInventarioAnualEnum value : EstadoInventarioAnualEnum.values()) {
            estadoInventarioAnualList.add(value.estadoInventarioAnual);
        }
        return estadoInventarioAnualList;
    }
}
