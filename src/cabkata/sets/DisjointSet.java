package cabkata.sets;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class DisjointSet<V> {
    private final Map<V, Set<V>> data;

    public DisjointSet()
    {
        data = new HashMap<V, Set<V>>();
    }
    
    public Set<V> add(V v) 
    {
        Set<V> set = data.get(v);
        if (set == null) {
             set = new HashSet<V>();
             set.add(v);
             data.put(v, set);
        }
        return Collections.unmodifiableSet(set);
     }

    public Set<V> union(V item1, V item2)
    {
        if(item1 == null || item2 == null)
        {
            throw new IllegalArgumentException("Union elements may not be null");
        }
            
        Set<V> set1 = data.get(item1);
        Set<V> set2 = data.get(item2);
        
        if(set1 == null || set2 == null)
        {
            throw new IllegalArgumentException("Union elements do not already exist");
        }
        
        set1.addAll(set2);
        for (V v : set2)
        {
            data.put(v, set1);
        }
        return Collections.unmodifiableSet(set1);
    }

    public Set<V> get(V v)
    {
        return Collections.unmodifiableSet(data.get(v));
    }
}
