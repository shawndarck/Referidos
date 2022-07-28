/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.ejb;

import com.referidos.model.Referido;
import com.referidos.model.Rol;
import com.referidos.model.Usuario;
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
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    @Override
    public Usuario encontrarUsuarioCorreoCinte(String correo){
        em.getEntityManagerFactory().getCache().evictAll();
        Query q = em.createNamedQuery("Usuario.findByCorreoCinte", Usuario.class).setParameter("correoCinte", correo);
        
        List<Usuario> listado = q.getResultList();
        
        if(!listado.isEmpty()){
            return listado.get(0);
        }
        return null;
    }
    
    @Override
    public boolean crearUsuario(Usuario usuIn, int fk_ciudad, int fk_tipo_documento) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query q = em.createNativeQuery("INSERT INTO usuario ( nombre, apellido, celular, telefonoFijo, correoPersonal, correoCinte, contrasena, documento, FKCiudad, FKTipoDocumento, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '1')");
            q.setParameter(1, usuIn.getNombre());
            q.setParameter(2, usuIn.getApellido());
            q.setParameter(3, usuIn.getTelefonoFijo());
            q.setParameter(4, usuIn.getCelular());
            q.setParameter(5, usuIn.getCorreoPersonal());
            q.setParameter(6, usuIn.getCorreoCinte());
            q.setParameter(7, usuIn.getContrasena());
            q.setParameter(8, usuIn.getDocumento());
            q.setParameter(9, fk_ciudad);
            q.setParameter(10, fk_tipo_documento);
            q.setParameter(11, Short.parseShort("3"));
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean asignarRol(int usuIn, int rolIn) {
        try {
            Query q = em.createNativeQuery("INSERT INTO usuario_has_rol (FKUsuario, FKRol) VALUES (?, ?)");
            q.setParameter(1, usuIn);
            q.setParameter(2, rolIn);
            q.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Usuario> filtrarAnalistas(Rol rolIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT a FROM Usuario a WHERE a.rolList = :rolIn AND a.estado = :est");
            qt.setParameter("rolIn", rolIn);
            qt.setParameter("est", Short.parseShort("1"));
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Usuario> filtrarUsuariosHabilitados() {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT a FROM Usuario a WHERE a.estado = :est");
            qt.setParameter("est", Short.parseShort("1"));
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Usuario> filtrarUsuariosInHabilitados() {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT a FROM Usuario a WHERE a.estado = :est");
            qt.setParameter("est", Short.parseShort("0"));
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Usuario encontrarUsuarioCorreoPersonal(String correo){
        em.getEntityManagerFactory().getCache().evictAll();
        Query q = em.createNamedQuery("Usuario.findByCorreoPersonal", Usuario.class).setParameter("correoPersonal", correo);
        
        List<Usuario> listado = q.getResultList();
        
        if(!listado.isEmpty()){
            return listado.get(0);
        }
        return null;
    }
    
    @Override
    public Usuario encontrarUsuarioCorreoCnte(String correo){
        em.getEntityManagerFactory().getCache().evictAll();
        Query q = em.createNamedQuery("Usuario.findByCorreoCinte", Usuario.class).setParameter("correoCinte", correo);
        
        List<Usuario> listado = q.getResultList();
        
        if(!listado.isEmpty()){
            return listado.get(0);
        }
        return null;
    }
    
    @Override
    public Usuario recuperarClave(String correoIn) {
        try {
            Query qt = em.createQuery("SELECT u FROM Usuario u WHERE u.correoCinte = :correo");
            qt.setParameter("correo", correoIn);
            return (Usuario) qt.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
}
