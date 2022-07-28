/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.ejb;

import com.referidos.model.Idioma;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface IdiomaFacadeLocal {

    void create(Idioma idioma);

    void edit(Idioma idioma);

    void remove(Idioma idioma);

    Idioma find(Object id);

    List<Idioma> findAll();

    List<Idioma> findRange(int[] range);

    int count();
    
}
