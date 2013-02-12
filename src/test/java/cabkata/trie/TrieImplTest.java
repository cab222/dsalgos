package cabkata.trie;

import java.util.Map;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TrieImplTest {

	@Test
	public void testTrie(){
		TrieImpl<Long> trie = new TrieImpl<Long>();
		trie.put("carlo", Long.valueOf(20));
		trie.put("carla", Long.valueOf(25));
		trie.put("carls", Long.valueOf(25));
		trie.put("car", Long.valueOf(15));
		trie.put("jess", Long.valueOf(20));
		Map<String, Long> items = trie.get("car");
		assertThat(items.size(), is(4));
		assertThat(items.get("carlo"), is(20L));
		assertThat(trie.get("barb").size(), is(0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTrieBoundaries() {
		TrieImpl<Long> trie = new TrieImpl<Long>();
		trie.get("");
	}
}
