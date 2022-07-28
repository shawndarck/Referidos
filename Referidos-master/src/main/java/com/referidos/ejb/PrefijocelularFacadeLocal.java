/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.ejb;

import com.referidos.model.Prefijocelular;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author juan
 */
@Local
public interface PrefijocelularFacadeLocal {

    void create(Prefijocelular prefijocelular);

    void edit(Prefijocelular prefijocelular);

    void remove(Prefijocelular prefijocelular);

    Prefijocelular find(Object id);

    List<Prefijocelular> findAll();

    List<Prefijocelular> findRange(int[] range);

    int count();
    
}
