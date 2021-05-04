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
public enum EstadoInventarioDeclaracionEnum {
    GENERADO((short)1),
    COMPLETO((short)2),
    RECHAZADO((short)3),
    APROBADO((short)4);
    
    private final Short estadoInventarioDeclaracion;

    private EstadoInventarioDeclaracionEnum(short estadoInventarioDeclaracion) {
        this.estadoInventarioDeclaracion = estadoInventarioDeclaracion;
    }

    public short getEstadoInventarioDeclaracion() {
        return estadoInventarioDeclaracion;
    }
    public static List<Short> getEstadoInventarioDeclaracionList(){
        List<Short> estadoInventarioDeclaracionList = new ArrayList<Short>();
        for (EstadoInventarioDeclaracionEnum value : EstadoInventarioDeclaracionEnum.values()) {
            estadoInventarioDeclaracionList.add(value.estadoInventarioDeclaracion);
        }
        return estadoInventarioDeclaracionList;
    }
}
