package cabkata.sets;
/**
 * Taken from http://codekata.pragprog.com/2007/01/code_kata_backg.html
 * 
 * There are many circumstances where we need to find out if something is a member of a set, 
 * and many algorithms for doing it. If the set is small, you can use bitmaps. When they get larger, 
 * hashes are a useful technique. But when the sets get big, we start bumping in to limitations. 
 * Holding 250,000 words in memory for a spell checker might be too big an overhead if your target 
 * environment is a PDA or cell phone. Keeping a list of web-pages visited might be extravagant 
 * when you get up to tens of millions of pages. Fortunately, there?s a technique that can help.
 * 
 * Bloom filters are a 30-year-old statistical way of testing for membership in a set. 
 * They greatly reduce the amount of storage you need to represent the set, but at a price: 
 * they?ll sometimes report that something is in the set when it isn?t (but it?ll never do the opposite; 
 * if the filter says that the set doesn?t contain your object, you know that it doesn?t). 
 * And the nice thing is you can control the accuracy; the more memory you?re prepared to give the algorithm, 
 * the fewer false positives you get. I once wrote a spell checker for a PDP-11 which stored a dictionary of 
 * 80,000 words in 16kbytes, and I very rarely saw it let though an incorrect word. (Update: I must have mis-remembered 
 * these figures, because they are not in line with the theory. Unfortunately, 
 * I can no longer read the 8" floppies holding the source, so I can?t get the correct numbers. 
 * Let?s just say that I got a decent sized dictionary, along with the spell checker, all in under 64k.)
 * 
 * Bloom filters are very simple. Take a big array of bits, initially all zero. 
 * Then take the things you want to look up (in our case we?ll use a dictionary of words). 
 * Produce ?n? independent hash values for each word. Each hash is a number which is used to set the 
 * corresponding bit in the array of bits. Sometimes there?ll be clashes, where the bit will already be set from some other word. 
 * This doesn?t matter.
 * 
 * To check to see of a new word is already in the dictionary, perform the same hashes on it that you used to load the bitmap. 
 * Then check to see if each of the bits corresponding to these hash values is set. 
 * If any bit is not set, then you never loaded that word in, and you can reject it.
 * 
 * The Bloom filter reports a false positive when a set of hashes for a word all end up corresponding to 
 * wbits that were set previously by other words. In practice this doesn?t happen too often as long as the bitmap 
 * isn?t too heavily loaded with one-bits (clearly if every bit is one, then it?ll give a false positive on every lookup). 
 * There?s a discussion of the math in Bloom filters at www.cs.wisc.edu/~cao/papers/summary-cache/node8.html.
 * 
 */
public class BloomFilterImpl {
	private byte[] bytes;
	private long totalBitsStored;
	private final static long BYTE_BITS_SIZE = 8L;
	private final static long BYTE_KILO_SIZE = 1024L;
	
	public enum MemoryUnit{
		KILO_BYTE{
			public long toBits(long size){return size * BYTE_KILO_SIZE * BYTE_BITS_SIZE;}
			public long toBytes(long size){return size * BYTE_KILO_SIZE;}
		},
		BYTE{
			public long toBits(long size){return size * BYTE_BITS_SIZE;}
			public long toBytes(long size){return size;}
		};
		
	    public long toBits(long size) {
	        throw new AbstractMethodError();
	    }
	    
	    public long toBytes(long size) {
	        throw new AbstractMethodError();
	    }
	}
	
	public BloomFilterImpl(int size, MemoryUnit memoryUnit){
		int bytesRequired = (int) memoryUnit.toBytes(size); 
		totalBitsStored = MemoryUnit.BYTE.toBits(bytesRequired);
		bytes = new byte[bytesRequired];	
	}
	
	private boolean getBit(int bitIndex){
		int byteArrayIdx = (int) (bitIndex / BYTE_BITS_SIZE);
		long memoryBucket = bytes[byteArrayIdx];
		
		int mask = getMask(bitIndex);
		
		return (mask & memoryBucket) != 0;
	}
	
	private void setBit(int bitIndex, boolean on){
		int byteArrayIdx = (int) (bitIndex / BYTE_BITS_SIZE);
		byte memoryBucket = bytes[byteArrayIdx];		
		int mask = getMask(bitIndex);
		
		if(!on){
			mask = ~mask;
		}
		memoryBucket |= mask;
		bytes[byteArrayIdx] = memoryBucket;
	}

	private int getMask(int bitIndex) {
		int bitIdxOfByte = (int) (bitIndex % BYTE_BITS_SIZE);
		return 1 << bitIdxOfByte;
	}
	
	public void addWord(String word){
		setBit(getBitIdx(word), true);
	}

	private int getBitIdx(String word) {
		return (int) ( Math.abs(word.hashCode()) % totalBitsStored);
	}

	public boolean containsWord(String word){
		return getBit(getBitIdx(word));
	}
}
