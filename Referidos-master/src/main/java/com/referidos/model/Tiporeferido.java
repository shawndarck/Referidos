/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "tiporeferido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiporeferido.findAll", query = "SELECT t FROM Tiporeferido t"),
    @NamedQuery(name = "Tiporeferido.findByIdTipoReferido", query = "SELECT t FROM Tiporeferido t WHERE t.idTipoReferido = :idTipoReferido"),
    @NamedQuery(name = "Tiporeferido.findByDescipcion", query = "SELECT t FROM Tiporeferido t WHERE t.descipcion = :descipcion")})
public class Tiporeferido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_referido")
    private Integer idTipoReferido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descipcion")
    private String descipcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fKReferido")
    private List<Referido> referidoList;

    public Tiporeferido() {
    }

    public Tiporeferido(Integer idTipoReferido) {
        this.idTipoReferido = idTipoReferido;
    }

    public Tiporeferido(Integer idTipoReferido, String descipcion) {
        this.idTipoReferido = idTipoReferido;
        this.descipcion = descipcion;
    }

    public Integer getIdTipoReferido() {
        return idTipoReferido;
    }

    public void setIdTipoReferido(Integer idTipoReferido) {
        this.idTipoReferido = idTipoReferido;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
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
        hash += (idTipoReferido != null ? idTipoReferido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiporeferido)) {
            return false;
        }
        Tiporeferido other = (Tiporeferido) object;
        if ((this.idTipoReferido == null && other.idTipoReferido != null) || (this.idTipoReferido != null && !this.idTipoReferido.equals(other.idTipoReferido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.referidos.model.Tiporeferido[ idTipoReferido=" + idTipoReferido + " ]";
    }
    
}
