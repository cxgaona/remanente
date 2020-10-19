/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dataModel;

import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 *
 * @author christian.gaona
 */
public class LazyUsuarioDataModel extends LazyDataModel<UsuarioDTO> {

    private List<UsuarioDTO> datasource;

    public LazyUsuarioDataModel(List<UsuarioDTO> datasource) {
        this.datasource = datasource;
    }

    @Override
    public UsuarioDTO getRowData(String rowKey) {
        for (UsuarioDTO usuarioDTO : datasource) {
            if (usuarioDTO.getUsuario().getUsuarioId().equals(rowKey)) {
                return usuarioDTO;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(UsuarioDTO usuarioDTO) {
        return usuarioDTO.getUsuario().getUsuarioId();
    }

    @Override
//    public List<UsuarioDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//    public List<UsuarioDTO> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) {
    public List<UsuarioDTO> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {    
    
        List<UsuarioDTO> data = new ArrayList<>();

        //filter
        for (UsuarioDTO usuarioDTO : datasource) {
            boolean match = true;

            if (filters != null) {
                for (Object meta : filters.values()) {
                    try {
                        String filterField = meta.toString();
                        Object filterValue = meta.getClass();
                        String fieldValue = String.valueOf(usuarioDTO.getClass().getField(filterField).get(usuarioDTO));

                        if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (Exception e) {
                        match = false;
                    }
                }
            }

            if (match) {
                data.add(usuarioDTO);
            }
        }

        //sort
        if (multiSortMeta != null && !multiSortMeta.isEmpty()) {
            for (SortMeta meta : multiSortMeta.subList(first, first)) {
                Collections.sort(data, new LazySorter(meta.getSortField(), meta.getSortOrder()));
            }
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }
}
