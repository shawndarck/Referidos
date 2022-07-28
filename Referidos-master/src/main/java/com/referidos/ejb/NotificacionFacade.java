/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.ejb;

import com.referidos.model.Notificacion;
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
public class NotificacionFacade extends AbstractFacade<Notificacion> implements NotificacionFacadeLocal {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificacionFacade() {
        super(Notificacion.class);
    }
    
    @Override
    public List<Notificacion> encontrarPendientes(Usuario usuIn, Short estIn) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query qt = em.createQuery("SELECT n FROM Notificacion n WHERE n.fKUsuario = :usuIn AND n.estado = :estIn");
            qt.setParameter("usuIn", usuIn);
            qt.setParameter("estIn", estIn);
            return qt.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
