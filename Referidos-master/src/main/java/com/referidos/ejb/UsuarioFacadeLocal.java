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
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();

    public Usuario encontrarUsuarioCorreoCinte(String correo);

    public List<Usuario> filtrarAnalistas(Rol rolIn);

    public Usuario encontrarUsuarioCorreoPersonal(String correo);

    public Usuario encontrarUsuarioCorreoCnte(String correo);

    public boolean crearUsuario(Usuario usuIn, int fk_ciudad, int fk_tipo_documento);

    public boolean asignarRol(int usuIn, int rolIn);

    public Usuario recuperarClave(String correoIn);

    public List<Usuario> filtrarUsuariosHabilitados();

    public List<Usuario> filtrarUsuariosInHabilitados();
    
}
