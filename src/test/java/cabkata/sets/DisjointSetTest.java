package cabkata.sets;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public final class DisjointSetTest {

    @Test
    public void testDisjointSet()
    {
        DisjointSet<String> ds = new DisjointSet<String>();
        String man = "man";
        String woman = "woman";
        Set<String> manSet = ds.add(man);
        assertEquals(manSet.size(), 1);
        Set<String> womanSet = ds.add(woman);
        assertEquals(womanSet.size(), 1);
        Set<String> people = ds.union(man, woman);
        assertEquals(people.size(), 2);

        assertEquals(ds.get(man).size(), 2);
        assertEquals(ds.get(woman).size(), 2);
        assertEquals(ds.get(woman), ds.get(man));
    }
}
