/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dataModel;

import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author christian.gaona
 */
public class LazySorter implements Comparator<UsuarioDTO> {

    private String sortField;

    private SortOrder sortOrder;

    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int compare(UsuarioDTO usuarioDTO1, UsuarioDTO usuarioDTO2) {
        try {
            Object value1 = UsuarioDTO.class.getField(this.sortField).get(usuarioDTO1);
            Object value2 = UsuarioDTO.class.getField(this.sortField).get(usuarioDTO2);

            int value = ((Comparable) value1).compareTo(value2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
