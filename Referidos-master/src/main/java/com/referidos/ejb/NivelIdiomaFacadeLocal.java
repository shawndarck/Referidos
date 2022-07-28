/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.ejb;

import com.referidos.model.NivelIdioma;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface NivelIdiomaFacadeLocal {

    void create(NivelIdioma nivelIdioma);

    void edit(NivelIdioma nivelIdioma);

    void remove(NivelIdioma nivelIdioma);

    NivelIdioma find(Object id);

    List<NivelIdioma> findAll();

    List<NivelIdioma> findRange(int[] range);

    int count();
    
}
