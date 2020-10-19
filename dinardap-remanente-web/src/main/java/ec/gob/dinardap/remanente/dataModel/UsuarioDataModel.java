/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dataModel;

import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author christian.gaona
 */
public class UsuarioDataModel extends ListDataModel<UsuarioDTO> implements SelectableDataModel<UsuarioDTO> {

    public UsuarioDataModel() {
    }

    public UsuarioDataModel(List<UsuarioDTO> data) {
        super(data);
    }

    @Override
    public UsuarioDTO getRowData(String rowKey) {
        List<UsuarioDTO> usuarioDTOList = (List<UsuarioDTO>) getWrappedData();

        for (UsuarioDTO udto : usuarioDTOList) {
            if (udto.getUsuario().getUsuarioId().equals(rowKey)) {
                return udto;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(UsuarioDTO udto) {
        return udto.getUsuario().getUsuarioId();
    }

}
