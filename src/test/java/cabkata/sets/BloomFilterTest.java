package cabkata.sets;

import org.junit.Test;

import cabkata.sets.BloomFilterImpl.MemoryUnit;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BloomFilterTest {

	@Test
	public void testBloomFilter(){
		BloomFilterImpl filter = new BloomFilterImpl(64, MemoryUnit.KILO_BYTE);
		String[] words = new String[]{"carlo", "jessica", "peter"};
		for(String word : words){
			filter.addWord(word);			
		}
		
		assertThat("Should contain carlo", filter.containsWord("carlo"), is(true));
		assertThat("Should contain carlo", filter.containsWord("carlos"), is(false));
		assertThat("Should contain carlo", filter.containsWord("jessica"), is(true));
	}
}
