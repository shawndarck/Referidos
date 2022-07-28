/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.controller;

import com.referidos.ejb.AreaFacadeLocal;
import com.referidos.ejb.CiudadFacadeLocal;
import com.referidos.ejb.EstadoreferidoFacadeLocal;
import com.referidos.ejb.IdiomaFacadeLocal;
import com.referidos.ejb.NivelIdiomaFacadeLocal;
import com.referidos.ejb.NotificacionFacadeLocal;
import com.referidos.ejb.PaisFacadeLocal;
import com.referidos.ejb.PrefijocelularFacadeLocal;
import com.referidos.ejb.ReferidoFacadeLocal;
import com.referidos.ejb.RolFacadeLocal;
import com.referidos.ejb.TipoDocumentoFacadeLocal;
import com.referidos.ejb.TiporeferidoFacadeLocal;
import com.referidos.ejb.UsuarioFacadeLocal;
import com.referidos.model.Area;
import com.referidos.model.Ciudad;
import com.referidos.model.Estadoreferido;
import com.referidos.model.Idioma;
import com.referidos.model.NivelIdioma;
import com.referidos.model.Notificacion;
import com.referidos.model.Pais;
import com.referidos.model.Prefijocelular;
import com.referidos.model.Referido;
import com.referidos.model.Rol;
import com.referidos.model.TipoDocumento;
import com.referidos.model.Tiporeferido;
import com.referidos.model.Usuario;
import com.referidos.utilities.MailAvisoAnalista;
import com.referidos.utilities.MailAvisoConsultor;
import com.referidos.utilities.MailAvisoReferido;
import com.referidos.utilities.MailNotificacion;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 *
 * @author juan
 */
@Named(value = "referidoSesion")
@SessionScoped
public class ReferidoSesion implements Serializable {

    @EJB
    private ReferidoFacadeLocal referidoFacadeLocal;

    @EJB
    private EstadoreferidoFacadeLocal estadoreferidoFacadeLocal;

    @EJB
    private CiudadFacadeLocal ciudadFacadeLocal;

    @EJB
    private AreaFacadeLocal areaFacadeLocal;

    @EJB
    private IdiomaFacadeLocal idiomaFacadeLocal;

    @EJB
    private NivelIdiomaFacadeLocal nivelIdiomaFacadeLocal;

    @EJB
    private TipoDocumentoFacadeLocal tipoDocumentoFacadeLocal;

    @EJB
    private TiporeferidoFacadeLocal tiporeferidoFacadeLocal;

    @EJB
    private NotificacionFacadeLocal notificacionFacadeLocal;

    @EJB
    private PaisFacadeLocal paisFacadeLocal;

    @EJB
    private RolFacadeLocal rolFacadeLocal;

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    @EJB
    private PrefijocelularFacadeLocal prefijocelularFacadeLocal;

    private Referido refReg;
    private Referido refTemporal;

    @Inject
    private Estadoreferido estado;
    @Inject
    private Estadoreferido estadoTemporal;
    @Inject
    private Pais pais;
    @Inject
    private Ciudad ciudad;
    @Inject
    private Tiporeferido tipoReferido;
    @Inject
    private Area area;
    @Inject
    private Idioma idioma;
    @Inject
    private NivelIdioma nivelIdioma;
    @Inject
    private TipoDocumento tipoDocumento;
    @Inject
    private Estadoreferido estadoAsignado;
    @Inject
    private UsuarioSesion usuLogeado;
    @Inject
    private NotificacionSesion notIn;
    @Inject
    private Notificacion notificacion;
    @Inject
    private Usuario analista;
    @Inject
    private Usuario analistaSeleccionado;
    @Inject
    private Rol rolAnalista;
    @Inject
    private Prefijocelular prefijoCelular;

    private List<Referido> referidos;
    private List<Referido> referidosPersonalizados;
    private List<Referido> referidosEspera;
    private List<Referido> referidosAsignadosLider;
    private List<Referido> referidosAceptados;
    private List<Referido> referidosCancelados;
    private List<Referido> referidosLiberados;
    private List<Referido> referidosContratados;
    private List<Estadoreferido> estados;
    private List<Ciudad> ciudades;
    private List<Area> areas;
    private List<Tiporeferido> tiposReferidos;
    private List<Referido> referidosAsignados;
    private List<NivelIdioma> nivelesIdiomas;
    private List<Idioma> idiomas;
    private List<TipoDocumento> tiposDocumentos;
    private List<Pais> paises;
    private List<Usuario> analistas;
    private List<Prefijocelular> prefijos;

    private int fkCiudad = 0;
    private int fkArea = 0;
    private int fkEstado = 0;
    private int fkTipoReferido = 0;
    private int fkNivelIdioma = 0;
    private int fkTipoDocumento = 0;
    private int fkIdioma = 0;

    private String ingles;
    private String tipoDoc;
    private String selector;
    private String hvRenombrado;

    private StreamedContent file;
    private Part archivoHV;
    private File rutaHV;
    private File rutaHVCarga;
    private InputStream inputStream;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private final String ruta = "/referidos";

    @PostConstruct
    public void init() {
        refReg = new Referido();
        refTemporal = new Referido();
        estadoAsignado = estadoreferidoFacadeLocal.find(2);
        referidosPersonalizados = new ArrayList<>();
        paises = new ArrayList<>();
        paises = paisFacadeLocal.findAll();
        referidos = new ArrayList<>();
        referidos = referidoFacadeLocal.filtroReferidosConsultorInicial(usuLogeado.getUsuLog());
        estado = estadoreferidoFacadeLocal.find(2);
        referidosPersonalizados = referidoFacadeLocal.filtrarReferidosAsignados(estado, usuLogeado.getUsuLog());
        referidosAsignados = referidoFacadeLocal.filtrarReferidosAsignados(estado, usuLogeado.getUsuLog());
        nivelesIdiomas = nivelIdiomaFacadeLocal.findAll();
        idiomas = idiomaFacadeLocal.findAll();
        tiposDocumentos = tipoDocumentoFacadeLocal.findAll();
        estados = estadoreferidoFacadeLocal.findAll();
        estado = new Estadoreferido();
        areas = areaFacadeLocal.findAll();
        tiposReferidos = tiporeferidoFacadeLocal.findAll();
        prefijos = prefijocelularFacadeLocal.findAll();
    }

    public void cargarHV() {
        try {
            if (archivoHV != null) {
                if (archivoHV.getSize() > 900000) {
                    PrimeFaces.current().executeScript("Swal.fire({"
                            + "  title: 'Error!',"
                            + "  text: 'No se puede cargar este archivo, pór su tamaño',"
                            + "  icon: 'error',"
                            + "  confirmButtonText: 'Por favor intente mas tarde'"
                            + "})");
                } else if (archivoHV.getContentType().equalsIgnoreCase("application/pdf") || archivoHV.getContentType().equalsIgnoreCase("application/pdf") || archivoHV.getContentType().equalsIgnoreCase("application/pdf")) {
                    //Dirección IP
                    FacesContext fc = FacesContext.getCurrentInstance();

                    File carpeta = new File(ruta);
                    if (!carpeta.exists()) {
                        carpeta.mkdirs();
                    }

                } else {
                    PrimeFaces.current().executeScript("Swal.fire({"
                            + "  title: 'Error!',"
                            + "  text: 'Tipo de archivo no permitido, recuerde la extencion es "
                            + ".Pdf',"
                            + "  icon: 'error',"
                            + "  confirmButtonText: 'Por favor intente mas tarde'"
                            + "})");
                }

            } else {
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Error!',"
                        + "  text: 'No se puede realizar esta peticion',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'Por favor intente mas tarde'"
                        + "})");

            }

        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error!',"
                    + "  text: 'No se puede realizar esta peticion',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Por favor intente mas tarde'"
                    + "})");
        }

    }

    public void descargarHV() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            String file = refTemporal.getHojaVida();
            File ficheroXLS = new File(ruta + "/" + file);
            FileInputStream fis = new FileInputStream(ficheroXLS);
            byte[] bytes = new byte[4096];
            int read = -1;

            if (!ficheroXLS.exists()) {
                ficheroXLS.mkdirs();
            }

            if (!ctx.getResponseComplete()) {
                String fileName = ruta + file;
                //String contentType = "application/vnd.ms-excel";
                String contentType = "application/pdf";
                //String contentType =  context.getMimeType(fileName);

                HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
                response.setContentType(contentType);
                response.setContentLength((int) ficheroXLS.length());
                response.setHeader("Content-Disposition", "attachment;filename=\"" + ficheroXLS.getName() + "\"");
                OutputStream out = response.getOutputStream();
                while ((read = fis.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                //out.flush();
                rutaHV = ficheroXLS;
                out.close();
                fis.close();
                ctx.responseComplete();
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Hoja de vida descargada!',"
                        + "  text: 'Tu hoja de vida ha sido descargada con exito',"
                        + "  icon: 'success',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            } else {
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Error!',"
                        + "  text: 'Pdf no encontrado',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            }
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error fatal!',"
                    + "  text: 'No se puede descargar este pdf',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
        }
    }

    public String filtrarReferidosEspera() {
        estadoTemporal = new Estadoreferido();
        estadoTemporal = estadoreferidoFacadeLocal.find(1);
        referidosEspera = referidoFacadeLocal.filtrarReferidosEstados(estadoTemporal);
        return "referidoEspera.xhtml";
    }

    public String filtrarReferidosAsignados() {
        estadoTemporal = new Estadoreferido();
        estadoTemporal = estadoreferidoFacadeLocal.find(2);
        referidosAsignados = referidoFacadeLocal.filtrarReferidosEstados(estadoTemporal);
        return "referidoAsignado.xhtml";
    }

    public String filtrarReferidosAceptados() {
        estadoTemporal = new Estadoreferido();
        estadoTemporal = estadoreferidoFacadeLocal.find(3);
        referidosAceptados = referidoFacadeLocal.filtrarReferidosEstados(estadoTemporal);
        return "referidoAceptado.xhtml";
    }

    public String filtrarReferidosRechazados() {
        estadoTemporal = new Estadoreferido();
        estadoTemporal = estadoreferidoFacadeLocal.find(4);
        referidosCancelados = referidoFacadeLocal.filtrarReferidosEstados(estadoTemporal);
        return "referidoRechazado.xhtml";
    }

    public String filtrarReferidosLiberados() {
        estadoTemporal = new Estadoreferido();
        estadoTemporal = estadoreferidoFacadeLocal.find(5);
        referidosLiberados = referidoFacadeLocal.filtrarReferidosEstados(estadoTemporal);
        return "referidoLiberado.xhtml";
    }

    public String filtrarReferidosContratados() {
        estadoTemporal = new Estadoreferido();
        estadoTemporal = estadoreferidoFacadeLocal.find(6);
        referidosContratados = referidoFacadeLocal.filtrarReferidosEstados(estadoTemporal);
        return "referidoContratado.xhtml";
    }

    public String filtroReferidosAnalistaInicial() {
        estado = estadoreferidoFacadeLocal.find(2);
        referidosAsignados = referidoFacadeLocal.filtrarReferidosAsignados(estado, usuLogeado.getUsuLog());
        estado = new Estadoreferido();
        return "referido.xhtml";
    }

    public String filtroReferidosAnalistaTotales() {
        referidos = referidoFacadeLocal.findAll();
        return "referidosTotales.xhtml";
    }

    public void filtrarReferidosAnalista() {
        try {
            referidosAsignados.clear();
            estado = estadoreferidoFacadeLocal.find(2);
            referidosAsignados = referidoFacadeLocal.filtrarReferidosPersonalizadosAnalista(estado, usuLogeado.getUsuLog(), idioma, nivelIdioma, tipoReferido);
            limpiezaMultiFiltro();
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Referidos filtrados con exito!',"
                    + "  text: 'Haga clik en ok para continuar',"
                    + "  icon: 'info',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error al filtrar!',"
                    + "  text: 'Haga clik en ok para continuar',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        }
    }

    public void multifiltro() {
        switch (estadoTemporal.getIdEstadoReferido()) {
            case 1: {
                referidosEspera = referidoFacadeLocal.filtrarReferidosPersonalizadosLider(estadoTemporal, idioma, nivelIdioma, tipoReferido);
                mensajeReferidosFiltrados();
                break;
            }
            case 2: {
                referidosAsignados = referidoFacadeLocal.filtrarReferidosPersonalizadosLider(estadoTemporal, idioma, nivelIdioma, tipoReferido);
                mensajeReferidosFiltrados();
                break;
            }
            case 3: {
                referidosAceptados = referidoFacadeLocal.filtrarReferidosPersonalizadosLider(estadoTemporal, idioma, nivelIdioma, tipoReferido);
                mensajeReferidosFiltrados();
                break;
            }
            case 4: {
                referidosCancelados = referidoFacadeLocal.filtrarReferidosPersonalizadosLider(estadoTemporal, idioma, nivelIdioma, tipoReferido);
                mensajeReferidosFiltrados();
                break;
            }
            case 5: {
                referidosLiberados = referidoFacadeLocal.filtrarReferidosPersonalizadosLider(estadoTemporal, idioma, nivelIdioma, tipoReferido);
                mensajeReferidosFiltrados();
                break;
            }
            case 6: {
                referidosContratados = referidoFacadeLocal.filtrarReferidosPersonalizadosLider(estadoTemporal, idioma, nivelIdioma, tipoReferido);
                mensajeReferidosFiltrados();
                break;
            }
            default: {
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Error al filtrar!',"
                        + "  text: 'Haga clik en ok para continuar',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'ok'"
                        + "})");
                break;
            }
        }
    }

    public void limpiezaMultiFiltro() {
        estado = new Estadoreferido();
        idioma = new Idioma();
        nivelIdioma = new NivelIdioma();
        tipoReferido = new Tiporeferido();
    }

    public void filtrarReferidoEstado() {
        try {
            if (estado.getIdEstadoReferido().equals(5)) {
                referidos = referidoFacadeLocal.filtroReferidosConsultorInicial(usuLogeado.getUsuLog());
                mensajeReferidosFiltrados();
            } else {
                referidos.clear();
                referidos = referidoFacadeLocal.filtroReferidosConsultor(estado, usuLogeado.getUsuLog());
                estado = new Estadoreferido();
                mensajeReferidosFiltrados();
            }
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error al filtrar!',"
                    + "  text: 'Haga clik en ok para continuar',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        }
    }

    public void mensajeReferidosFiltrados() {
        PrimeFaces.current().executeScript("Swal.fire({"
                + "  title: 'Referidos filtrados con exito!',"
                + "  text: 'Haga clik en ok para continuar',"
                + "  icon: 'info',"
                + "  confirmButtonText: 'ok'"
                + "})");
    }

    public void renderCiudades() {
        ciudades = ciudadFacadeLocal.encontrarCiudades(pais);
    }

    public void guardarTemporal(Referido refIn) {
        refTemporal = refIn;
    }

    public void recuperarReferidoAnalista(Referido refIn) {
        rolAnalista = new Rol();
        refTemporal = refIn;
        rolAnalista = rolFacadeLocal.find(2);
        analistas = usuarioFacadeLocal.filtrarAnalistas(rolAnalista);
    }

    public void registrarReferido() {
        try {
            refTemporal = referidoFacadeLocal.encontrarUsuarioDocumento(refReg.getDocumento());
            if (refTemporal == null) {
                if (archivoHV == null) {
                    PrimeFaces.current().executeScript("Swal.fire({"
                            + "  title: 'Carga la hoja de vida (pdf)!',"
                            + "  text: 'Carga una hoja de vida para poder registrar un referido',"
                            + "  icon: 'error',"
                            + "  confirmButtonText: 'Por favor intente mas tarde'"
                            + "})");
                } else {
                    File carpeta = new File(ruta);

                    try (InputStream is = archivoHV.getInputStream()) {
                        Calendar hoy = Calendar.getInstance();
                        String renombrar = sdf.format(hoy.getTime()) + ".";
                        renombrar += FilenameUtils.getExtension(archivoHV.getSubmittedFileName());
                        //Files.copy(is, (new File(carpeta, renombrar)).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(is, (new File(carpeta, renombrar)).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        refReg.setHojaVida(renombrar);

                    } catch (Exception e) {
                        PrimeFaces.current().executeScript("Swal.fire({"
                                + "  title: 'Error!',"
                                + "  text: 'No se puede realizar esta peticion',"
                                + "  icon: 'error',"
                                + "  confirmButtonText: 'Por favor intente mas tarde'"
                                + "})");

                    }

                    if (nivelIdioma.getIdNivel() == 0) {
                        idioma = null;
                    }
                    if (nivelIdioma.getIdNivel() == 0) {
                        nivelIdioma = null;
                    }
                    if (prefijoCelular.getIdPrefijo() == 0) {
                        prefijoCelular = null;
                    }
                    // Creación de referido
                    estado = estadoreferidoFacadeLocal.find(1);
                    refReg.setFKAnalista(null);
                    refReg.setFKCiudad(ciudad);
                    refReg.setFKConsultor(usuLogeado.getUsuLog());
                    refReg.setFKReferido(tipoReferido);
                    refReg.setFKArea(area);
                    refReg.setFKEstado(estado);
                    refReg.setFKIdioma(idioma);
                    refReg.setFKNivelIdioma(nivelIdioma);
                    refReg.setFKTipoDocumento(tipoDocumento);
                    refReg.setFKPrefijo(prefijoCelular);
                    refReg.setFechaRegistro(obtenerFechaActual());
                    referidoFacadeLocal.create(refReg);
                    
                    //Creación de notificación
                        refTemporal = referidoFacadeLocal.encontarReferido(refReg.getCorreo());
                        notificacion.setFKUsuario(usuLogeado.getUsuLog());
                        notificacion.setFKReferido(refTemporal);//Crear busqueda de último elemento
                        notificacion.setEstado(Short.parseShort("1"));
                        notificacion.setFechaRegistro(obtenerFechaActual());
                        notificacion.setDescripcion("Referido registrado! nombre de referido:");
                        MailAvisoReferido.mailAvisoReferido(refTemporal.getFKConsultor().getNombre(), refTemporal.getFKConsultor().getApellido(), refTemporal.getCorreo(), refTemporal.getNombre(), refTemporal.getApellido(), "has sido referido en cinte", "Te ha referido ", "Recuerda estar pendiente de tu bandeja de entrada en los proximos días");
                        notificacionFacadeLocal.create(notificacion);
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/SistemaReferidos/faces/consultor/referido.xhtml");
                        refTemporal = new Referido();
                        refReg = new Referido();
                        notificacion = new Notificacion();
                        prefijoCelular = new Prefijocelular();
                        ciudad = new Ciudad();
                        idioma = new Idioma();
                        nivelIdioma = new NivelIdioma();
                        tipoReferido = new Tiporeferido();
                        area = new Area();
                        estado = new Estadoreferido();
                        tipoDocumento = new TipoDocumento();
                        referidos = referidoFacadeLocal.filtroReferidosConsultorInicial(usuLogeado.getUsuLog());
                }
            } else {
                PrimeFaces.current().executeScript("Swal.fire({"
                        + "  title: 'Referido Duplicado!',"
                        + "  text: 'Por favor registra un consultor distinto',"
                        + "  icon: 'error',"
                        + "  confirmButtonText: 'Ok'"
                        + "})");
            }

        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error fatal!',"
                    + "  text: 'Regresa más tarde',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'Ok'"
                    + "})");
        }
    }

    public void asignarAnalista() {
        try {
            analistaSeleccionado = usuarioFacadeLocal.find(analista.getIdUsuario());
            notificacion.setFKUsuario(usuLogeado.getUsuLog());
            notificacion.setFKReferido(refTemporal);//Crear busqueda de último eleme    nto
            notificacion.setEstado(Short.parseShort("1"));
            notificacion.setFechaRegistro(obtenerFechaActual());
            notificacion.setDescripcion("Referido asignado! nombre de referido: ");
            MailAvisoAnalista.mailAvisoAnalista(analistaSeleccionado.getNombre(), analistaSeleccionado.getApellido(), analistaSeleccionado.getCorreoCinte(), refTemporal.getNombre(), refTemporal.getApellido(), "Te han asignado un referido!", "Te han asignado ", "Recuerda estar pendiente de tus referidos asignados");
            notificacionFacadeLocal.create(notificacion);
            notificacion = new Notificacion();
            notificacion.setFKUsuario(analista);
            notificacion.setFKReferido(refTemporal);//Crear busqueda de último elemento
            notificacion.setEstado(Short.parseShort("1"));
            notificacion.setFechaRegistro(obtenerFechaActual());
            notificacion.setDescripcion("Te han asignado un referido! nombre de referido: ");
            notificacionFacadeLocal.create(notificacion);
            refTemporal.setFKAnalista(analista);
            refTemporal.setFKEstado(estadoAsignado);
            referidoFacadeLocal.edit(refTemporal);
            filtrarReferidosEspera();
            refTemporal = new Referido();
            analista = new Usuario();
            analistaSeleccionado = new Usuario();
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Referido asignado!',"
                    + "  text: 'El referido ahora está asignado',"
                    + "  icon: 'success',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error al asignar referido!',"
                    + "  text: 'Haga clik en ok para continuar',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        }
    }

    public void liberarReferido() {
        try {
            estado = estadoreferidoFacadeLocal.find(5);
            refTemporal.setFKEstado(estado);
            refTemporal.setFKAnalista(null);
            referidoFacadeLocal.edit(refTemporal);
            estado = new Estadoreferido();
            refTemporal = new Referido();
            estado = estadoreferidoFacadeLocal.find(2);
            referidosAsignados = referidoFacadeLocal.filtrarReferidosAsignados(estado, usuLogeado.getUsuLog());
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Referido liberado!',"
                    + "  text: 'El referido ahora está liberado',"
                    + "  icon: 'success',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error al liberar refirido!',"
                    + "  text: 'Intenta liberarlo más tarde',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        }
    }

    public void contratarReferido() {
        try {
            estado = estadoreferidoFacadeLocal.find(6);
            refTemporal.setFKEstado(estado);
            MailAvisoConsultor.mailAvisoConsultor(refTemporal.getFKConsultor().getNombre(), refTemporal.getFKConsultor().getApellido(), refTemporal.getFKConsultor().getCorreoCinte(), refTemporal.getNombre(), refTemporal.getApellido(), "Han contratado a tu referido cinte", "han contratado al referido", "Recuerda seguir refiriendo en cinte!");
            referidoFacadeLocal.edit(refTemporal);
            estado = new Estadoreferido();
            refTemporal = new Referido();          
            estado = estadoreferidoFacadeLocal.find(2);
            referidosAsignados = referidoFacadeLocal.filtrarReferidosAsignados(estado, usuLogeado.getUsuLog());
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Referido contratado!',"
                    + "  text: 'El referido ahora está contratado',"
                    + "  icon: 'success',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error al contratar refirido!',"
                    + "  text: 'Intenta contratar más tarde',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        }
    }

    public void cambiarEstadoRechazado() {
        try {
            estado = estadoreferidoFacadeLocal.find(4);
            refTemporal.setFKEstado(estado);
            String descripción = refTemporal.getFKEstado().getDescripcion();
            MailAvisoConsultor.mailAvisoConsultor(refTemporal.getFKConsultor().getNombre(), refTemporal.getFKConsultor().getApellido(), refTemporal.getFKConsultor().getCorreoCinte(), refTemporal.getNombre(), refTemporal.getApellido(), "Han rechazado a tu referido cinte", "han rechazado al referido", "Recuerda seguir refiriendo en cinte!");
            MailAvisoReferido.mailAvisoReferido(refTemporal.getFKConsultor().getNombre(), refTemporal.getFKConsultor().getApellido(), refTemporal.getCorreo(), refTemporal.getNombre(), refTemporal.getApellido(), "has sido rechazado como referido en cinte", "Te ha referido ", "Recuerda estar pendiente de tu bandeja de entrada, podrías ser aceptado en un futuro!"); 
            referidoFacadeLocal.edit(refTemporal);  //Hacer un if con las facades         
            notificacion.setFKUsuario(usuLogeado.getUsuLog());
            notificacion.setFKReferido(refTemporal);//Crear busqueda de último elemento
            notificacion.setEstado(Short.parseShort("1"));
            notificacion.setFechaRegistro(obtenerFechaActual());
            notificacion.setDescripcion("Referido rechazado! nombre de referido: ");
            notificacionFacadeLocal.create(notificacion);
            notificacion = new Notificacion();
            refTemporal = new Referido();
            estado = new Estadoreferido();
            filtrarReferidosEspera();
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Referido rechazado!',"
                    + "  text: 'El referido ahora esta rechazado',"
                    + "  icon: 'success',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error al rechazar referido!',"
                    + "  text: 'Haga clik en ok para continuar',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        }
    }

    public void cambiarEstadoRedimirEspera() {
        try {
            estado = estadoreferidoFacadeLocal.find(5);
            refTemporal.setFKAnalista(null);
            refTemporal.setFKEstado(estado);
            String descripción = refTemporal.getFKEstado().getDescripcion();
            referidoFacadeLocal.edit(refTemporal);  //Hacer un if con las facades          
            notificacion.setFKUsuario(usuLogeado.getUsuLog());
            notificacion.setFKReferido(refTemporal);//Crear busqueda de último elemento
            notificacion.setEstado(Short.parseShort("1"));
            notificacion.setFechaRegistro(obtenerFechaActual());
            notificacion.setDescripcion("Referido redimido en liberados! nombre de referido: ");
            notificacionFacadeLocal.create(notificacion);
            notificacion = new Notificacion();
            refTemporal = new Referido();
            estado = new Estadoreferido();
            filtrarReferidosRechazados();
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Referido redimido!',"
                    + "  text: 'El referido ahora esta en liberados',"
                    + "  icon: 'success',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error al redimir!',"
                    + "  text: 'Haga clik en ok para continuar',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        }
    }

    public void actualizarPerfilReferido(){
        try {
            refTemporal.setFKReferido(tipoReferido);
            referidoFacadeLocal.edit(refTemporal);
            refTemporal = new Referido();
            tipoReferido = new Tiporeferido();
            estado = estadoreferidoFacadeLocal.find(2);
            referidosAsignados = referidoFacadeLocal.filtrarReferidosAsignados(estado, usuLogeado.getUsuLog());
            estado = new Estadoreferido();
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Perfil de referido actualizado!',"
                    + "  text: 'El referido ahora esta rechazado',"
                    + "  icon: 'success',"
                    + "  confirmButtonText: 'ok'"
                    + "})");            
        } catch (Exception e) {
            PrimeFaces.current().executeScript("Swal.fire({"
                    + "  title: 'Error al actualizar perfil de referido!',"
                    + "  text: 'Haga clik en ok para continuar',"
                    + "  icon: 'error',"
                    + "  confirmButtonText: 'ok'"
                    + "})");
        }
    }
    
    public Date obtenerFechaActual() {
        try {
            DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String fechaActual = d.format(LocalDateTime.now());
            SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date fechaFormateada = formato.parse(fechaActual);
            return fechaFormateada;
        } catch (ParseException e) {
            return null;
        }
    }

    public List<Referido> listarReferidosAsignados() {
        try {
            estadoAsignado = estadoreferidoFacadeLocal.find(2);
            return referidosAsignados = referidoFacadeLocal.filtrarReferidosAsignados(estadoAsignado, usuLogeado.getUsuLog());
        } catch (Exception e) {
            return null;
        }
    }

    public Referido getRefReg() {
        return refReg;
    }

    public void setRefReg(Referido refReg) {
        this.refReg = refReg;
    }

    public List<Referido> getReferidos() {
        return referidos;
    }

    public void setReferidos(List<Referido> referidos) {
        this.referidos = referidos;
    }

    public Estadoreferido getEstado() {
        return estado;
    }

    public void setEstado(Estadoreferido estado) {
        this.estado = estado;
    }

    public List<Estadoreferido> getEstados() {
        return estados;
    }

    public void setEstados(List<Estadoreferido> estados) {
        this.estados = estados;
    }

    public int getFkCiudad() {
        return fkCiudad;
    }

    public void setFkCiudad(int fkCiudad) {
        this.fkCiudad = fkCiudad;
    }

    public int getFkArea() {
        return fkArea;
    }

    public void setFkArea(int fkArea) {
        this.fkArea = fkArea;
    }

    public int getFkEstado() {
        return fkEstado;
    }

    public void setFkEstado(int fkEstado) {
        this.fkEstado = fkEstado;
    }

    public int getFkTipoReferido() {
        return fkTipoReferido;
    }

    public void setFkTipoReferido(int fkTipoReferido) {
        this.fkTipoReferido = fkTipoReferido;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public String getIngles() {
        return ingles;
    }

    public void setIngles(String ingles) {
        this.ingles = ingles;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public List<Tiporeferido> getTiposReferidos() {
        return tiposReferidos;
    }

    public void setTiposReferidos(List<Tiporeferido> tiposReferidos) {
        this.tiposReferidos = tiposReferidos;
    }

    public List<Referido> getReferidosAsignados() {
        return referidosAsignados;
    }

    public void setReferidosAsignados(List<Referido> referidosAsignados) {
        this.referidosAsignados = referidosAsignados;
    }

    public Referido getRefTemporal() {
        return refTemporal;
    }

    public void setRefTemporal(Referido refTemporal) {
        this.refTemporal = refTemporal;
    }

    public Part getArchivoHV() {
        return archivoHV;
    }

    public void setArchivoHV(Part archivoHV) {
        this.archivoHV = archivoHV;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public int getFkNivelIdioma() {
        return fkNivelIdioma;
    }

    public void setFkNivelIdioma(int fkNivelIdioma) {
        this.fkNivelIdioma = fkNivelIdioma;
    }

    public int getFkTipoDocumento() {
        return fkTipoDocumento;
    }

    public void setFkTipoDocumento(int fkTipoDocumento) {
        this.fkTipoDocumento = fkTipoDocumento;
    }

    public int getFkIdioma() {
        return fkIdioma;
    }

    public void setFkIdioma(int fkIdioma) {
        this.fkIdioma = fkIdioma;
    }

    public List<NivelIdioma> getNivelesIdiomas() {
        return nivelesIdiomas;
    }

    public void setNivelesIdiomas(List<NivelIdioma> nivelesIdiomas) {
        this.nivelesIdiomas = nivelesIdiomas;
    }

    public List<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    public List<TipoDocumento> getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(List<TipoDocumento> tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public NivelIdioma getNivelIdioma() {
        return nivelIdioma;
    }

    public void setNivelIdioma(NivelIdioma nivelIdioma) {
        this.nivelIdioma = nivelIdioma;
    }

    public List<Referido> getReferidosPersonalizados() {
        return referidosPersonalizados;
    }

    public void setReferidosPersonalizados(List<Referido> referidosPersonalizados) {
        this.referidosPersonalizados = referidosPersonalizados;
    }

    public Tiporeferido getTipoReferido() {
        return tipoReferido;
    }

    public void setTipoReferido(Tiporeferido tipoReferido) {
        this.tipoReferido = tipoReferido;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Pais> getPaises() {
        return paises;
    }

    public void setPaises(List<Pais> paises) {
        this.paises = paises;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Usuario getAnalista() {
        return analista;
    }

    public void setAnalista(Usuario analista) {
        this.analista = analista;
    }

    public List<Usuario> getAnalistas() {
        return analistas;
    }

    public void setAnalistas(List<Usuario> analistas) {
        this.analistas = analistas;
    }

    public Prefijocelular getPrefijoCelular() {
        return prefijoCelular;
    }

    public void setPrefijoCelular(Prefijocelular prefijoCelular) {
        this.prefijoCelular = prefijoCelular;
    }

    public List<Prefijocelular> getPrefijos() {
        return prefijos;
    }

    public void setPrefijos(List<Prefijocelular> prefijos) {
        this.prefijos = prefijos;
    }

    public List<Referido> getReferidosEspera() {
        return referidosEspera;
    }

    public void setReferidosEspera(List<Referido> referidosEspera) {
        this.referidosEspera = referidosEspera;
    }

    public List<Referido> getReferidosAsignadosLider() {
        return referidosAsignadosLider;
    }

    public void setReferidosAsignadosLider(List<Referido> referidosAsignadosLider) {
        this.referidosAsignadosLider = referidosAsignadosLider;
    }

    public List<Referido> getReferidosAceptados() {
        return referidosAceptados;
    }

    public void setReferidosAceptados(List<Referido> referidosAceptados) {
        this.referidosAceptados = referidosAceptados;
    }

    public List<Referido> getReferidosCancelados() {
        return referidosCancelados;
    }

    public void setReferidosCancelados(List<Referido> referidosCancelados) {
        this.referidosCancelados = referidosCancelados;
    }

    public File getRutaHV() {
        return rutaHV;
    }

    public void setRutaHV(File rutaHV) {
        this.rutaHV = rutaHV;
    }

    public List<Referido> getReferidosLiberados() {
        return referidosLiberados;
    }

    public void setReferidosLiberados(List<Referido> referidosLiberados) {
        this.referidosLiberados = referidosLiberados;
    }

    public List<Referido> getReferidosContratados() {
        return referidosContratados;
    }

    public void setReferidosContratados(List<Referido> referidosContratados) {
        this.referidosContratados = referidosContratados;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

}
