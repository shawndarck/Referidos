/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.ejb;

import com.referidos.model.Estadoreferido;
import com.referidos.model.Idioma;
import com.referidos.model.NivelIdioma;
import com.referidos.model.Prefijocelular;
import com.referidos.model.Referido;
import com.referidos.model.Tiporeferido;
import com.referidos.model.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author juan
 */
@Stateless
public class ReferidoFacade extends AbstractFacade<Referido> implements ReferidoFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReferidoFacade() {
        super(Referido.class);
    }
    
    @Override
    public List<Referido> filtroReferidosConsultor(Estadoreferido ref, Usuario usu) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT r FROM Referido r WHERE r.fKEstado = :ref AND r.fKConsultor = :usu");
            qt.setParameter("ref", ref);
            qt.setParameter("usu", usu);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Referido> filtroReferidosConsultorInicial(Usuario usu) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT r FROM Referido r WHERE r.fKConsultor = :usu");
            qt.setParameter("usu", usu);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Referido encontrarUsuarioDocumento(String documento){
        em.getEntityManagerFactory().getCache().evictAll();
        Query q = em.createNamedQuery("Referido.findByDocumento", Usuario.class).setParameter("documento", documento);
        
        List<Referido> listado = q.getResultList();
        
        if(!listado.isEmpty()){
            return listado.get(0);
        }
        return null;
    }
    
    @Override
    public boolean crearReferido(Referido refIn, int fk_ciudad, int fk_consultor, int fk_referido, int fk_area, NivelIdioma fk_nivel_idioma, int fk_tipo_documento, Idioma fk_idioma, Prefijocelular fk_prefijo, Date fecha_actual) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query q = em.createNativeQuery("INSERT INTO referido (nombre, apellido, telefonoFijo, celular, direccion, hojaVida, descripcion, FKCiudad, FKAnalista, FKConsultor, FKReferido, FKArea, FKEstado, documento, correo, FKNivelIdioma, FKTipoDocumento, fecha_registro, FKIdioma, FKPrefijo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            q.setParameter(1, refIn.getNombre());
            q.setParameter(2, refIn.getApellido());
            q.setParameter(3, refIn.getTelefonoFijo());
            q.setParameter(4, refIn.getCelular());
            q.setParameter(5, refIn.getDireccion());
            q.setParameter(6, refIn.getHojaVida());
            q.setParameter(7, refIn.getDescripcion());
            q.setParameter(8, fk_ciudad);
            q.setParameter(9, null);
            q.setParameter(10, fk_consultor);
            q.setParameter(11, fk_referido);
            q.setParameter(12, fk_area);
            q.setParameter(13, 1);
            q.setParameter(14, refIn.getDocumento());
            q.setParameter(15, refIn.getCorreo());
            q.setParameter(16, fk_nivel_idioma.getIdNivel());
            q.setParameter(17, fk_tipo_documento);
            q.setParameter(18, fecha_actual);
            q.setParameter(19, fk_idioma.getIdIdioma());
            q.setParameter(20, fk_prefijo.getIdPrefijo());
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Referido encontarReferido(String correoRef) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query q = em.createNamedQuery("Referido.findByCorreo", Referido.class).setParameter("correo", correoRef);
        
            List<Referido> listado = q.getResultList();
        
            if(!listado.isEmpty()){
                return listado.get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Referido> filtrarReferidosAsignados(Estadoreferido ref, Usuario usu) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT r FROM Referido r WHERE r.fKEstado = :ref AND r.fKAnalista = :usu");
            qt.setParameter("ref", ref);
            qt.setParameter("usu", usu);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Referido> filtrarReferidosPersonalizadosAnalista(Estadoreferido ref, Usuario usu, Idioma idi, NivelIdioma nivIdi, Tiporeferido tipRef) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT r FROM Referido r WHERE r.fKEstado = :ref AND r.fKAnalista = :usu AND r.fKNivelIdioma = :nivIdi AND r.fKIdioma = :idi AND r.fKReferido = :tipRef");
            qt.setParameter("ref", ref);
            qt.setParameter("usu", usu);
            qt.setParameter("nivIdi", nivIdi);
            qt.setParameter("idi", idi);
            qt.setParameter("tipRef", tipRef);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Referido> filtrarReferidosPersonalizadosLider(Estadoreferido ref, Idioma idi, NivelIdioma nivIdi, Tiporeferido tipRef) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT r FROM Referido r WHERE r.fKEstado = :ref  AND r.fKNivelIdioma = :nivIdi AND r.fKIdioma = :idi AND r.fKReferido = :tipRef");
            qt.setParameter("ref", ref);
            qt.setParameter("nivIdi", nivIdi);
            qt.setParameter("idi", idi);
            qt.setParameter("tipRef", tipRef);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Referido> filtrarReferidosEstados(Estadoreferido ref) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT r FROM Referido r WHERE r.fKEstado = :ref");
            qt.setParameter("ref", ref);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
