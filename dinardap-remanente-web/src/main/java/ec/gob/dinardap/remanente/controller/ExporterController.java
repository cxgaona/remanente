/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.controller;

import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author christian.gaona
 */
@ManagedBean
@ApplicationScoped
public class ExporterController implements Serializable {

    private static final long serialVersionUID = 20120316L;

    private Boolean customExporter;

    public ExporterController() {
        customExporter = true;
    }

    public Boolean getCustomExporter() {
        return customExporter;
    }

    public void setCustomExporter(Boolean customExporter) {
        this.customExporter = customExporter;
    }
}
