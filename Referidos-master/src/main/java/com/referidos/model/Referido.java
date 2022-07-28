/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juan
 */
@Entity
@Table(name = "referido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Referido.findAll", query = "SELECT r FROM Referido r"),
    @NamedQuery(name = "Referido.findByIdReferido", query = "SELECT r FROM Referido r WHERE r.idReferido = :idReferido"),
    @NamedQuery(name = "Referido.findByNombre", query = "SELECT r FROM Referido r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Referido.findByApellido", query = "SELECT r FROM Referido r WHERE r.apellido = :apellido"),
    @NamedQuery(name = "Referido.findByTelefonoFijo", query = "SELECT r FROM Referido r WHERE r.telefonoFijo = :telefonoFijo"),
    @NamedQuery(name = "Referido.findByCelular", query = "SELECT r FROM Referido r WHERE r.celular = :celular"),
    @NamedQuery(name = "Referido.findByDireccion", query = "SELECT r FROM Referido r WHERE r.direccion = :direccion"),
    @NamedQuery(name = "Referido.findByHojaVida", query = "SELECT r FROM Referido r WHERE r.hojaVida = :hojaVida"),
    @NamedQuery(name = "Referido.findByDescripcion", query = "SELECT r FROM Referido r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "Referido.findByDocumento", query = "SELECT r FROM Referido r WHERE r.documento = :documento"),
    @NamedQuery(name = "Referido.findByCorreo", query = "SELECT r FROM Referido r WHERE r.correo = :correo"),
    @NamedQuery(name = "Referido.findByFechaRegistro", query = "SELECT r FROM Referido r WHERE r.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Referido.findByDescripcionRechazo", query = "SELECT r FROM Referido r WHERE r.descripcionRechazo = :descripcionRechazo")})
public class Referido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_referido")
    private Integer idReferido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "apellido")
    private String apellido;
    @Size(max = 45)
    @Column(name = "telefonoFijo")
    private String telefonoFijo;
    @Size(max = 15)
    @Column(name = "celular")
    private String celular;
    @Size(max = 45)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 100)
    @Column(name = "hojaVida")
    private String hojaVida;
    @Size(max = 300)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 45)
    @Column(name = "documento")
    private String documento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 200)
    @Column(name = "descripcion_rechazo")
    private String descripcionRechazo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fKReferido")
    private List<Notificacion> notificacionList;
    @JoinColumn(name = "FKArea", referencedColumnName = "id_area")
    @ManyToOne(optional = false)
    private Area fKArea;
    @JoinColumn(name = "FKCiudad", referencedColumnName = "id_ciudad")
    @ManyToOne(optional = false)
    private Ciudad fKCiudad;
    @JoinColumn(name = "FKEstado", referencedColumnName = "id_estado_referido")
    @ManyToOne(optional = false)
    private Estadoreferido fKEstado;
    @JoinColumn(name = "FKIdioma", referencedColumnName = "id_idioma")
    @ManyToOne
    private Idioma fKIdioma;
    @JoinColumn(name = "FKNivelIdioma", referencedColumnName = "id_nivel")
    @ManyToOne
    private NivelIdioma fKNivelIdioma;
    @JoinColumn(name = "FKPrefijo", referencedColumnName = "id_prefijo")
    @ManyToOne
    private Prefijocelular fKPrefijo;
    @JoinColumn(name = "FKReferido", referencedColumnName = "id_tipo_referido")
    @ManyToOne(optional = false)
    private Tiporeferido fKReferido;
    @JoinColumn(name = "FKTipoDocumento", referencedColumnName = "id_tipo_documento")
    @ManyToOne
    private TipoDocumento fKTipoDocumento;
    @JoinColumn(name = "FKAnalista", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario fKAnalista;
    @JoinColumn(name = "FKConsultor", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario fKConsultor;

    public Referido() {
    }

    public Referido(Integer idReferido) {
        this.idReferido = idReferido;
    }

    public Referido(Integer idReferido, String nombre, String apellido, String correo, Date fechaRegistro) {
        this.idReferido = idReferido;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdReferido() {
        return idReferido;
    }

    public void setIdReferido(Integer idReferido) {
        this.idReferido = idReferido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHojaVida() {
        return hojaVida;
    }

    public void setHojaVida(String hojaVida) {
        this.hojaVida = hojaVida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDescripcionRechazo() {
        return descripcionRechazo;
    }

    public void setDescripcionRechazo(String descripcionRechazo) {
        this.descripcionRechazo = descripcionRechazo;
    }

    @XmlTransient
    public List<Notificacion> getNotificacionList() {
        return notificacionList;
    }

    public void setNotificacionList(List<Notificacion> notificacionList) {
        this.notificacionList = notificacionList;
    }

    public Area getFKArea() {
        return fKArea;
    }

    public void setFKArea(Area fKArea) {
        this.fKArea = fKArea;
    }

    public Ciudad getFKCiudad() {
        return fKCiudad;
    }

    public void setFKCiudad(Ciudad fKCiudad) {
        this.fKCiudad = fKCiudad;
    }

    public Estadoreferido getFKEstado() {
        return fKEstado;
    }

    public void setFKEstado(Estadoreferido fKEstado) {
        this.fKEstado = fKEstado;
    }

    public Idioma getFKIdioma() {
        return fKIdioma;
    }

    public void setFKIdioma(Idioma fKIdioma) {
        this.fKIdioma = fKIdioma;
    }

    public NivelIdioma getFKNivelIdioma() {
        return fKNivelIdioma;
    }

    public void setFKNivelIdioma(NivelIdioma fKNivelIdioma) {
        this.fKNivelIdioma = fKNivelIdioma;
    }

    public Prefijocelular getFKPrefijo() {
        return fKPrefijo;
    }

    public void setFKPrefijo(Prefijocelular fKPrefijo) {
        this.fKPrefijo = fKPrefijo;
    }

    public Tiporeferido getFKReferido() {
        return fKReferido;
    }

    public void setFKReferido(Tiporeferido fKReferido) {
        this.fKReferido = fKReferido;
    }

    public TipoDocumento getFKTipoDocumento() {
        return fKTipoDocumento;
    }

    public void setFKTipoDocumento(TipoDocumento fKTipoDocumento) {
        this.fKTipoDocumento = fKTipoDocumento;
    }

    public Usuario getFKAnalista() {
        return fKAnalista;
    }

    public void setFKAnalista(Usuario fKAnalista) {
        this.fKAnalista = fKAnalista;
    }

    public Usuario getFKConsultor() {
        return fKConsultor;
    }

    public void setFKConsultor(Usuario fKConsultor) {
        this.fKConsultor = fKConsultor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReferido != null ? idReferido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Referido)) {
            return false;
        }
        Referido other = (Referido) object;
        if ((this.idReferido == null && other.idReferido != null) || (this.idReferido != null && !this.idReferido.equals(other.idReferido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.referidos.model.Referido[ idReferido=" + idReferido + " ]";
    }
    
}
