/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "dias_no_laborables")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiasNoLaborables.findAll", query = "SELECT d FROM DiasNoLaborables d"),
    @NamedQuery(name = "DiasNoLaborables.findByDiasNoLaborablesId", query = "SELECT d FROM DiasNoLaborables d WHERE d.diasNoLaborablesId = :diasNoLaborablesId"),
    @NamedQuery(name = "DiasNoLaborables.findByAnio", query = "SELECT d FROM DiasNoLaborables d WHERE d.anio = :anio"),
    @NamedQuery(name = "DiasNoLaborables.findByMes", query = "SELECT d FROM DiasNoLaborables d WHERE d.mes = :mes"),
    @NamedQuery(name = "DiasNoLaborables.findByDia", query = "SELECT d FROM DiasNoLaborables d WHERE d.dia = :dia"),
    @NamedQuery(name = "DiasNoLaborables.findByDescripcion", query = "SELECT d FROM DiasNoLaborables d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DiasNoLaborables.findByEstado", query = "SELECT d FROM DiasNoLaborables d WHERE d.estado = :estado")})
public class DiasNoLaborables implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dias_no_laborables_id")
    private Integer diasNoLaborablesId;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "dia")
    private Integer dia;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;

    public DiasNoLaborables() {
    }

    public DiasNoLaborables(Integer diasNoLaborablesId) {
        this.diasNoLaborablesId = diasNoLaborablesId;
    }

    public DiasNoLaborables(Integer diasNoLaborablesId, String estado) {
        this.diasNoLaborablesId = diasNoLaborablesId;
        this.estado = estado;
    }

    public Integer getDiasNoLaborablesId() {
        return diasNoLaborablesId;
    }

    public void setDiasNoLaborablesId(Integer diasNoLaborablesId) {
        this.diasNoLaborablesId = diasNoLaborablesId;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
