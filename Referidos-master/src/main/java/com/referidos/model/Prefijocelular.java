/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juan
 */
@Entity
@Table(name = "prefijocelular")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prefijocelular.findAll", query = "SELECT p FROM Prefijocelular p"),
    @NamedQuery(name = "Prefijocelular.findByIdPrefijo", query = "SELECT p FROM Prefijocelular p WHERE p.idPrefijo = :idPrefijo"),
    @NamedQuery(name = "Prefijocelular.findByDescripcion", query = "SELECT p FROM Prefijocelular p WHERE p.descripcion = :descripcion")})
public class Prefijocelular implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_prefijo")
    private Integer idPrefijo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "fKPrefijo")
    private List<Referido> referidoList;

    public Prefijocelular() {
    }

    public Prefijocelular(Integer idPrefijo) {
        this.idPrefijo = idPrefijo;
    }

    public Prefijocelular(Integer idPrefijo, String descripcion) {
        this.idPrefijo = idPrefijo;
        this.descripcion = descripcion;
    }

    public Integer getIdPrefijo() {
        return idPrefijo;
    }

    public void setIdPrefijo(Integer idPrefijo) {
        this.idPrefijo = idPrefijo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Referido> getReferidoList() {
        return referidoList;
    }

    public void setReferidoList(List<Referido> referidoList) {
        this.referidoList = referidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrefijo != null ? idPrefijo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prefijocelular)) {
            return false;
        }
        Prefijocelular other = (Prefijocelular) object;
        if ((this.idPrefijo == null && other.idPrefijo != null) || (this.idPrefijo != null && !this.idPrefijo.equals(other.idPrefijo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.referidos.model.Prefijocelular[ idPrefijo=" + idPrefijo + " ]";
    }
    
}
