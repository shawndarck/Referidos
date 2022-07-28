/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.controller;

import com.referidos.ejb.NotificacionFacadeLocal;
import com.referidos.ejb.UsuarioFacadeLocal;
import com.referidos.model.Notificacion;
import com.referidos.utilities.MailCreacionUsuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author juan
 */
@Named(value = "notificacionSesion")
@ViewScoped
public class NotificacionSesion implements Serializable {
    
    @EJB
    private NotificacionFacadeLocal notificacionFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    
    private List<Notificacion> notificaciones;
    private List<Notificacion> notificacionesLeidas;
    
    private int contador;
    
    private Short estadoIn;
    
    @Inject
    private Notificacion not;
    @Inject
    private UsuarioSesion usuLogeado;
    
    @PostConstruct
    public void init(){
        notificaciones = new ArrayList<>();
        notificacionesLeidas = new ArrayList<>();
        contador = 0;
        estadoIn = Short.parseShort("1");
        notificaciones = notificacionFacadeLocal.encontrarPendientes(usuLogeado.getUsuLog(), Short.parseShort("1")); 
        contador = notificaciones.size();
    }
    
    public void leerNotificacion(Notificacion notIn){
        try {
            notIn.setEstado(Short.parseShort("0"));
            notificacionFacadeLocal.edit(notIn);
            comprobarNotificacion();
        } catch (Exception e) {
        }
    }
    
    public List<Notificacion> cambiarLeidos(){
        notificacionesLeidas.clear();
        return notificacionesLeidas = notificacionFacadeLocal.encontrarPendientes(usuLogeado.getUsuLog(), Short.parseShort("0"));
    }

    public void comprobarNotificacion() {    
        contador = 0;
        notificaciones = notificacionFacadeLocal.encontrarPendientes(usuLogeado.getUsuLog(), Short.parseShort("1")); 
        contador = notificaciones.size();
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public Notificacion getNot() {
        return not;
    }

    public void setNot(Notificacion not) {
        this.not = not;
    }    

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public Short getEstadoIn() {
        return estadoIn;
    }

    public void setEstadoIn(Short estadoIn) {
        this.estadoIn = estadoIn;
    }

}
