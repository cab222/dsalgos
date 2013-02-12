package cabkata.trie;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A Trie tree. 
 * 
 * Adds a word in O(n), where n is the number of characters in the word
 * 
 * Retrieves words matching a prefix in O(p) + O(n), 
 * where p is the number of characters in the prefix and n is the uniq # of characters in all words
 * that follow a prefix 
 * 
 * @author carlobarbara
 *
 * @param <T>
 */
public class TrieImpl<T> {
	private static class Node<T>{
		private T value;		
		private Map<Character, Node<T>> edges = new HashMap<Character, Node<T>>();
	}
	
	private Node<T> root;
	public TrieImpl(){
		root = new Node<T>();
	}
	
	/**
	 * Throws a RuntimeExeption if word is null or empty
	 * @param word
	 * @param value
	 */
	public void put(String word, T value){
		if(word == null || word.isEmpty()){
			throw new IllegalArgumentException("Word can't be null or empty");
		}
		char[] chars = word.toCharArray();
		Node<T> node = root;
		for(int i = 0; i < chars.length; i++){
			Character key = Character.valueOf(chars[i]);
			if(node.edges.containsKey(key)){
				node = node.edges.get(key);
			}
			else{
			   Node<T> newNode = new Node<T>();
			   node.edges.put(key, newNode);
			   node = newNode;
			}						
		}
		node.value = value;
	}
	
	/**
	 * Throws a RuntimeExeption if prefix is null or empty
	 * 
	 * @param prefix
	 * @return Map of words to the value T, where each word starts with prefix
	 */
	public Map<String, T> get(String prefix){
		if(prefix == null || prefix.isEmpty()){
			throw new IllegalArgumentException("Prefix can't be null or empty");
		}

		Map<String, T> items = new HashMap<String, T>();
		char[] chars = prefix.toCharArray();
		Node<T> node = root;
		boolean hasMoreEdges = !root.edges.isEmpty();

		for(int i = 0; i < chars.length && hasMoreEdges; i++){
			Character key = Character.valueOf(chars[i]);
			if(node.edges.containsKey(key)){
				node = node.edges.get(key); 
			}
			else{
				hasMoreEdges = false;
				node = null;
			}
		}
		
		//Use a depth first traverse instead of breadth, so we can build up the words dynamically
		//this saves memory instead of storing the word at each node
		//if performance is key, storing the word may be better, as it will lead to less GCing.
		if(node != null){
			visit(node, items, new StringBuilder(prefix));
		}
		
		return items;
	}
	
	private void visit(Node<T> node, Map<String, T> items, StringBuilder sb){
		if(node.value != null)
		{
			items.put(sb.toString(), node.value);
		}
		
		for(Entry<Character, Node<T>> entry : node.edges.entrySet()){	
			StringBuilder builder = new StringBuilder(sb);
			builder.append(entry.getKey());
			visit(entry.getValue(), items, builder);
		}
	}
}

