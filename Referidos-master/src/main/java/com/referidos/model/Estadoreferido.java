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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juan
 */
@Entity
@Table(name = "estadoreferido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estadoreferido.findAll", query = "SELECT e FROM Estadoreferido e"),
    @NamedQuery(name = "Estadoreferido.findByIdEstadoReferido", query = "SELECT e FROM Estadoreferido e WHERE e.idEstadoReferido = :idEstadoReferido"),
    @NamedQuery(name = "Estadoreferido.findByDescripcion", query = "SELECT e FROM Estadoreferido e WHERE e.descripcion = :descripcion")})
public class Estadoreferido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estado_referido")
    private Integer idEstadoReferido;
    @Size(max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fKEstado")
    private List<Referido> referidoList;

    public Estadoreferido() {
    }

    public Estadoreferido(Integer idEstadoReferido) {
        this.idEstadoReferido = idEstadoReferido;
    }

    public Integer getIdEstadoReferido() {
        return idEstadoReferido;
    }

    public void setIdEstadoReferido(Integer idEstadoReferido) {
        this.idEstadoReferido = idEstadoReferido;
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
        hash += (idEstadoReferido != null ? idEstadoReferido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estadoreferido)) {
            return false;
        }
        Estadoreferido other = (Estadoreferido) object;
        if ((this.idEstadoReferido == null && other.idEstadoReferido != null) || (this.idEstadoReferido != null && !this.idEstadoReferido.equals(other.idEstadoReferido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.referidos.model.Estadoreferido[ idEstadoReferido=" + idEstadoReferido + " ]";
    }
    
}
