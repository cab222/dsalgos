package cabkata.sets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DisjointSet<V> {
    private Map<V, Set<V>> data = new HashMap<V, Set<V>>();

    public Set<V> add(V v)
    {
        if (!data.containsKey(v))
        {
            data.put(v, new HashSet<V>());
            data.get(v).add(v);
        }
        return data.get(v);
    }

    public Set<V> union(V item1, V item2)
    {
        Set<V> set1 = data.get(item1);
        Set<V> set2 = data.get(item2);
        if (set1 == null)
        {
            set1 = add(item1);
        }

        if (set2 == null)
        {
            set2 = add(item2);
        }

        set1.addAll(set2);
        for (V v : set2)
        {
            data.put(v, set1);
        }
        return set1;
    }

    public Set<V> get(V v)
    {
        return data.get(v);
    }
}
