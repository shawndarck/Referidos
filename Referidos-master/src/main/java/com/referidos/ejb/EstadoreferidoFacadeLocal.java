/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.ejb;

import com.referidos.model.Estadoreferido;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface EstadoreferidoFacadeLocal {

    void create(Estadoreferido estadoreferido);

    void edit(Estadoreferido estadoreferido);

    void remove(Estadoreferido estadoreferido);

    Estadoreferido find(Object id);

    List<Estadoreferido> findAll();

    List<Estadoreferido> findRange(int[] range);

    int count();
    
}
