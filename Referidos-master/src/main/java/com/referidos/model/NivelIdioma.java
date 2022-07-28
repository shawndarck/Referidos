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
@Table(name = "nivel_idioma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelIdioma.findAll", query = "SELECT n FROM NivelIdioma n"),
    @NamedQuery(name = "NivelIdioma.findByIdNivel", query = "SELECT n FROM NivelIdioma n WHERE n.idNivel = :idNivel"),
    @NamedQuery(name = "NivelIdioma.findByDescripcion", query = "SELECT n FROM NivelIdioma n WHERE n.descripcion = :descripcion")})
public class NivelIdioma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_nivel")
    private Integer idNivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "fKNivelIdioma")
    private List<Referido> referidoList;

    public NivelIdioma() {
    }

    public NivelIdioma(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public NivelIdioma(Integer idNivel, String descripcion) {
        this.idNivel = idNivel;
        this.descripcion = descripcion;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
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
        hash += (idNivel != null ? idNivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelIdioma)) {
            return false;
        }
        NivelIdioma other = (NivelIdioma) object;
        if ((this.idNivel == null && other.idNivel != null) || (this.idNivel != null && !this.idNivel.equals(other.idNivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.referidos.model.NivelIdioma[ idNivel=" + idNivel + " ]";
    }
    
}
