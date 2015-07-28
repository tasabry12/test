/*                   Copyright (c) 2013 Venere Net S.p.A.
 *                             All Rights Reserved
 *
 * This software is the confidential and proprietary information of
 * Venere Net S.p.A. ("Confidential  Information"). You  shall not disclose
 * such  Confidential Information and shall use it only in accordance with
 * the terms  of the license agreement you entered into with Venere Net S.p.A.
 *
 * VENERE NET S.P.A. MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * OR NON-INFRINGEMENT. VENERE NET S.P.A. SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS
 * SOFTWARE OR ITS DERIVATIVES.
 */
package com.venere.ace.utility;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * This class is a decorator of {@link Properties}.
 * <p>It uses {@link LinkedHashMap} instead of {@link Hashtable}
 * to preserve properties order when loading from streams.</p>
 */
public class OrderedProperties extends Properties {
    private Map<String,String> entries=new LinkedHashMap<String,String>();
 
    public Enumeration keys() { return Collections.enumeration(entries.keySet()); }
 
    public Enumeration elements() { return Collections.enumeration(entries.values()); }
 
    public boolean contains(Object value) { return entries.containsValue(value);    }
 
    public void putAll(Map<? extends Object, ? extends Object> map) {
        entries.putAll((Map<? extends String, ? extends String>)map);
    }
 
    public int size() { return entries.size();    }
 
    public boolean isEmpty() { return entries.isEmpty();    }
 
    public boolean containsKey(Object key) { return entries.containsKey(key);    }
 
    public boolean containsValue(Object value) {        return entries.containsValue(value);    }
 
    public String get(Object key) { return entries.get(key);    }
 
    public String put(Object key, Object value) { return entries.put((String)key, (String) value);    }
 
    public Object remove(Object key) { return entries.remove(key);    }
 
    public void clear() { entries.clear();    }
 
    public Set keySet() { return entries.keySet();    }
 
    public Collection values() { return entries.values();    }
 
    public Set entrySet() { return entries.entrySet();    }
 
    public boolean equals(Object o) { return entries.equals(o);    }
 
    public int hashCode() { return entries.hashCode();    }
}
