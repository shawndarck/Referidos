/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.controller;

import com.referidos.ejb.CiudadFacadeLocal;
import com.referidos.ejb.PaisFacadeLocal;
import com.referidos.ejb.RolFacadeLocal;
import com.referidos.ejb.TipoDocumentoFacadeLocal;
import com.referidos.ejb.UsuarioFacadeLocal;
import com.referidos.model.Ciudad;
import com.referidos.model.Pais;
import com.referidos.model.Rol;
import com.referidos.model.TipoDocumento;
import com.referidos.model.Usuario;
import com.referidos.utilities.Encrypt;
import com.referidos.utilities.MailCreacionUsuario;
import com.referidos.utilities.MailRecuperarClave;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import static org.passay.NumberRangeRule.ERROR_CODE;
import org.passay.PasswordGenerator;
import org.primefaces.PrimeFaces;

@Named(value = "usuarioSesion")
@SessionScoped
public class UsuarioSesion implements Serializable {

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private RolFacadeLocal rolFacadeLocal;
    @EJB
    private PaisFacadeLocal paisFacadeLocal;
    @EJB
    private CiudadFacadeLocal ciudadFacadeLocal;
    @EJB
    private TipoDocumentoFacadeLocal tipoDocumentoFacadeLocal;

    @Inject
    private Rol rol;
    @Inject
    private Usuario usuReg;
    @Inject
    private Usuario usuLog;
    @Inject
    private Usuario usuTemporal;
    @Inject
    private Pais pais;
    @Inject
    private Ciudad ciudad;
    @Inject
    private TipoDocumento tipoDocumento;

    private int fk_rol;
    private int numeroRol;
    private String login;
    private String claveIn;
    private String claveNueva;
    private String claveConfirmacion;
    private boolean esAdministrador;

    //Format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private List<Usuario> usuarios;
    private List<Usuario> usuariosHabilitados;
    private List<Usuario> usuariosInHabilitados;
    private List<Pais> paises;
    private List<Ciudad> ciudades;
    private List<TipoDocumento> tiposDocumentos;
    private List<Rol> roles;
    private List<Object> objetos;

    @PostConstruct
    public void init() {
        usuarios = new ArrayList<>();
        usuariosHabilitados = new ArrayList<>();
        usuariosInHabilitados = new ArrayList<>();
        usuariosHabilitados = usuarioFacadeLocal.filtrarUsuariosHabilitados();
        usuariosInHabilitados = usuarioFacadeLocal.filtrarUsuariosInHabilitados();
        usuarios.addAll(usuariosHabilitados);
        usuarios.addAll(usuariosInHabilitados);
        roles = new ArrayList<>();
        paises = paisFacadeLocal.findAll();
        ciudades = ciudadFacadeLocal.findAll();
        tiposDocumentos = tipoDocumentoFacadeLocal.findAll();
    }

    public List<Usuario> cargaUsuario() {
        usuariosHabilitados = usuarioFacadeLocal.filtrarUsuariosHabilitados();
        usuariosInHabilitados = usuarioFacadeLocal.filtrarUsuariosInHabilitados();
        usuarios.addAll(usuariosHabilitados);
        usuarios.addAll(usuariosInHabilitados);
        return usuarios;
    }

    public void cambiarClave() {
        try {
            final String claveEncriptacion = "Ae3sc*RQoaSADCNAB-13Cr5r+";
            if (claveNueva.equals(claveConfirmacion)) {
                Encrypt encriptador = new Encrypt();
                String encriptado = encriptador.encriptar(claveNueva, claveEncriptacion);
                usuLog.setContrasena(encriptado);
                usuarioFacadeLocal.edit(usuLog);
                claveNueva = "";
                claveConfirmacion = "";
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Clave cambiada',"
                        + "  text: 'Tu contraseña ha sido modificada con exito',"
                        + "  icon: 'success',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            } else {
                claveNueva = "";
                claveConfirmacion = "";
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Las claves no coinciden',"
                        + "  text: 'Ingresa contraseñas que coincidana',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            }
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error fatal!',"
                    + "  text: 'Contacte con soporte',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
        }
    }

    public void recuperarClave() {
        try {
            usuReg = usuarioFacadeLocal.recuperarClave(login);
            if (usuReg != null) {
                final String claveEncriptacion = "Ae3sc*RQoaSADCNAB-13Cr5r+";
                Encrypt encriptador = new Encrypt();
                String clave = encriptador.desencriptar(usuReg.getContrasena(), claveEncriptacion);
                MailRecuperarClave.recuperarClaves(usuReg.getNombre(), usuReg.getCorreoCinte(), clave);
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Correo enviado!',"
                        + "  text: 'Revisa tu bandeja de entrada',"
                        + "  icon: 'success',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            } else {
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'No se ha encontrado el correo!',"
                        + "  text: 'Escribe tu correo personal',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            }

        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'No se ha encontrado el correo!',"
                    + "  text: 'Escribe tu correo personal',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
        }

    }

    public void validarUsuario(int rolIn) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        try {
            final String claveEncriptacion = "Ae3sc*RQoaSADCNAB-13Cr5r+";
            Encrypt encriptador = new Encrypt();
            int it = 0;
            usuLog = usuarioFacadeLocal.encontrarUsuarioCorreoCinte(login);
            if (usuLog != null) {
                if (usuLog.getEstado() == Short.parseShort("1")) {
                    String clave = encriptador.encriptar(claveIn, claveEncriptacion);
                    if (usuLog.getCorreoCinte().equals(login) && usuLog.getContrasena().equals(clave)) {
                        for (Rol rolIt : usuLog.getRolList()) {
                            rol = usuLog.getRolList().get(it);
                            if (rolIt.getIdRol().equals(4)) {
                                rol = rolFacadeLocal.find(4);
                                rolIn = 4;
                                break;
                            }
                        }
                        if (rol.getIdRol().equals(rolIn)) {
                            switch (rolIn) {
                                case 1: {
                                    FacesContext fc = FacesContext.getCurrentInstance();
                                    fc.getExternalContext().redirect("faces/lider/index.xhtml");
                                    esAdministrador = false;
                                    break;
                                }
                                case 2: {
                                    FacesContext fc = FacesContext.getCurrentInstance();
                                    fc.getExternalContext().redirect("faces/analista/index.xhtml");
                                    esAdministrador = false;
                                    break;
                                }
                                case 3: {
                                    FacesContext fc = FacesContext.getCurrentInstance();
                                    fc.getExternalContext().redirect("faces/consultor/index.xhtml");
                                    esAdministrador = false;
                                    break;
                                }
                                case 4: {
                                    FacesContext fc = FacesContext.getCurrentInstance();
                                    fc.getExternalContext().redirect("faces/administrador/index.xhtml");
                                    esAdministrador = true;
                                    break;
                                }
                                default:
                                    PrimeFaces.current().executeScript("Swal.fire({"
                                            + "  title: 'No pertenece a este rol!',"
                                            + "  text: 'Accede a tu rol',"
                                            + "  icon: 'error',"
                                            + "  confirmButtonText: 'Ok'"
                                            + "})");
                            }
                        } else {
                            PrimeFaces.current().executeScript("Swal.fire({"
                                    + "  title: 'No perteneces a este rol!',"
                                    + "  text: 'Accede a tu rol',"
                                    + "  icon: 'error',"
                                    + "  confirmButtonText: 'Ok'"
                                    + "})");
                        }
                    } else {
                        PrimeFaces.current().executeScript("Swal.fire({"
                                + "  title: 'Correo o contraseña incorrectas!',"
                                + "  text: 'Digita las credenciales correctas',"
                                + "  icon: 'error',"
                                + "  confirmButtonText: 'Ok'"
                                + "})");
                    }

                } else if (usuLog.getEstado() == Short.parseShort("3")) {
                    usuLog.setEstado(Short.parseShort("1"));
                    usuarioFacadeLocal.edit(usuLog);
                    PrimeFaces.current().executeScript("Swal.fire({"
                            + "  title: 'Tu usuario ha sido activado!',"
                            + "  text: 'Vuelve a iniciar sesión',"
                            + "  icon: 'success',"
                            + "  confirmButtonText: 'Ok'"
                            + "})");
                } else {
                    PrimeFaces.current().executeScript("Swal.fire({"
                            + "  title: 'Usuario deshabilitado!',"
                            + "  text: 'Contacte con un administrador!',"
                            + "  icon: 'error',"
                            + "  confirmButtonText: 'Ok'"
                            + "})");
                }
            } else {
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Correo o contraseña incorrectas!',"
                        + "  text: 'Digita las credenciales correctas',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            }
            rol = new Rol();
        } catch (IOException e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error fatal!',"
                    + "  text: 'Contacte con soporte',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
        }

    }

    public void validarUsuarioSesion() throws IOException {
        if (usuLog == null || usuLog.getCorreoCinte() == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().invalidateSession();
            fc.getExternalContext().redirect("../index.xhtml");
        }
    }

    public void cerrarSesion() throws IOException {
        usuLog = null;
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().invalidateSession();
        fc.getExternalContext().redirect("../index.xhtml");
    }

    public void guardarNumeroRolTemporal(int numIn) {
        numeroRol = numIn;
    }

    public void registrarUsuario(int rolEntrada) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
        final String claveEncriptacion = "Ae3sc*RQoaSADCNAB-13Cr5r+";
        String correoCinte;
        Encrypt encriptador = new Encrypt();
        String encriptado;
        String clavePassay;
        usuTemporal = usuarioFacadeLocal.encontrarUsuarioCorreoCinte(usuReg.getCorreoCinte());
        if (usuTemporal == null) {
            clavePassay = generarClavePassay();

            encriptado = encriptador.encriptar(clavePassay, claveEncriptacion);
            usuReg.setContrasena(encriptado);
            correoCinte = usuReg.getCorreoCinte();
            usuReg.setFKCiudad(ciudad);
            usuReg.setFKTipoDocumento(tipoDocumento);
            usuReg.setEstado(Short.parseShort("3"));
            usuarioFacadeLocal.create(usuReg);
            usuReg = usuarioFacadeLocal.encontrarUsuarioCorreoCinte(correoCinte);
            switch (rolEntrada) {
                case 1: {
                    usuarioFacadeLocal.asignarRol(usuReg.getIdUsuario(), 1);
                    break;
                }
                case 2: {
                    usuarioFacadeLocal.asignarRol(usuReg.getIdUsuario(), 2);
                    break;
                }
                case 3: {
                    usuarioFacadeLocal.asignarRol(usuReg.getIdUsuario(), 3);
                    break;
                }
            }
            MailCreacionUsuario.recuperarClaves(usuReg.getNombre(), usuReg.getCorreoCinte(), clavePassay);
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Usuario Registrado!',"
                    + "  text: 'El usuario recien creado! debe consultar su bandeja de entrada de su email de cinte recuerde que debe iniciar sesión para que se active su usuario',"
                    + "  icon: 'success',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
            if (rolEntrada == 3) {

            } else {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.getExternalContext().redirect("usuarios.xhtml");
            }

        } else {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Usuario repetido!',"
                    + "  text: 'Correo cinte o personal ya registrado',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
        }
        clavePassay = "";
        usuTemporal = new Usuario();
        usuReg = new Usuario();
        usuTemporal = new Usuario();
        usuariosHabilitados.clear();
        usuariosInHabilitados.clear();
        usuarios.clear();
        usuariosHabilitados = usuarioFacadeLocal.filtrarUsuariosHabilitados();
        usuariosInHabilitados = usuarioFacadeLocal.filtrarUsuariosInHabilitados();
        usuarios.addAll(usuariosHabilitados);
        usuarios.addAll(usuariosInHabilitados);
    }

    public void guardarTemporal(Usuario u) {
        usuTemporal = u;
    }

    public void actualizarUsuario() {
        try {
            ciudad = ciudadFacadeLocal.find(ciudad.getIdCiudad());
            tipoDocumento = tipoDocumentoFacadeLocal.find(tipoDocumento.getIdTipoDocumento());
            usuLog.setFKCiudad(ciudad);
            usuLog.setFKTipoDocumento(tipoDocumento);
            usuarioFacadeLocal.edit(usuLog);
            ciudad = new Ciudad();
            tipoDocumento = new TipoDocumento();
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Usuario editado!',"
                    + "  text: 'El usuario ha sido modificado',"
                    + "  icon: 'success',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().redirect("perfil.xhtml");

        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error de edición!',"
                    + "  text: 'Intenta editar más tarde',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
        }
    }

    public void cambiarEstado(Usuario usuIn) {
        if (usuIn.getEstado() == Short.parseShort("1")) {
            usuIn.setEstado(Short.parseShort("0"));
        } else {
            usuIn.setEstado(Short.parseShort("1"));
        }
        PrimeFaces.current().executeScript("Swal.fire({"
                + "  title: 'Cambio de estado!',"
                + "  text: 'Se ha cambiado el estado del usuario',"
                + "  icon: 'info',"
                + "  confirmButtonText: 'Ok'"
                + "})");
        usuarioFacadeLocal.edit(usuIn);
    }

    //Passay
    public String generarClavePassay() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData caracteresMinuscula = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(caracteresMinuscula);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData caracteresEspeciales = new CharacterData() {
            @Override
            public String getErrorCode() {
                return ERROR_CODE;
            }

            @Override
            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(caracteresEspeciales);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }

    public void renderCiudades() {
        ciudades = ciudadFacadeLocal.encontrarCiudades(pais);
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public int getFk_rol() {
        return fk_rol;
    }

    public void setFk_rol(int fk_rol) {
        this.fk_rol = fk_rol;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClaveIn() {
        return claveIn;
    }

    public void setClaveIn(String claveIn) {
        this.claveIn = claveIn;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getUsuReg() {
        return usuReg;
    }

    public void setUsuReg(Usuario usuReg) {
        this.usuReg = usuReg;
    }

    public Usuario getUsuLog() {
        return usuLog;
    }

    public void setUsuLog(Usuario usuLog) {
        this.usuLog = usuLog;
    }

    public Usuario getUsuTemporal() {
        return usuTemporal;
    }

    public void setUsuTemporal(Usuario usuTemporal) {
        this.usuTemporal = usuTemporal;
    }

    public Collection<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<Pais> getPaises() {
        return paises;
    }

    public void setPaises(List<Pais> paises) {
        this.paises = paises;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
    }

    public List<TipoDocumento> getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(List<TipoDocumento> tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public String getClaveNueva() {
        return claveNueva;
    }

    public void setClaveNueva(String claveNueva) {
        this.claveNueva = claveNueva;
    }

    public String getClaveConfirmacion() {
        return claveConfirmacion;
    }

    public void setClaveConfirmacion(String claveConfirmacion) {
        this.claveConfirmacion = claveConfirmacion;
    }

    public boolean isEsAdministrador() {
        return esAdministrador;
    }

    public void setEsAdministrador(boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
    }

}
