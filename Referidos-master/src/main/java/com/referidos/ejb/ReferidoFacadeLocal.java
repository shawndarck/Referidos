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
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface ReferidoFacadeLocal {

    void create(Referido referido);

    void edit(Referido referido);

    void remove(Referido referido);

    Referido find(Object id);

    List<Referido> findAll();

    List<Referido> findRange(int[] range);

    int count();

    public List<Referido> filtroReferidosConsultor(Estadoreferido ref, Usuario usu);

    public Referido encontarReferido(String correoRef);

    public List<Referido> filtrarReferidosAsignados(Estadoreferido ref, Usuario usu);

    public List<Referido> filtrarReferidosPersonalizadosAnalista(Estadoreferido ref, Usuario usu, Idioma idi, NivelIdioma nivIdi, Tiporeferido tipRef);

    public List<Referido> filtrarReferidosEstados(Estadoreferido ref);

    public List<Referido> filtrarReferidosPersonalizadosLider(Estadoreferido ref, Idioma idi, NivelIdioma nivIdi, Tiporeferido tipRef);

    public List<Referido> filtroReferidosConsultorInicial(Usuario usu);

    public Referido encontrarUsuarioDocumento(String documento);

    public boolean crearReferido(Referido refIn, int fk_ciudad, int fk_consultor, int fk_referido, int fk_area, NivelIdioma fk_nivel_idioma, int fk_tipo_documento, Idioma fk_idioma, Prefijocelular fk_prefijo, Date fecha_actual);

}
