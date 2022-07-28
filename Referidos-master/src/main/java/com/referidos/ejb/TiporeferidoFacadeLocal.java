/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.ejb;

import com.referidos.model.Tiporeferido;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface TiporeferidoFacadeLocal {

    void create(Tiporeferido tiporeferido);

    void edit(Tiporeferido tiporeferido);

    void remove(Tiporeferido tiporeferido);

    Tiporeferido find(Object id);

    List<Tiporeferido> findAll();

    List<Tiporeferido> findRange(int[] range);

    int count();
    
}
