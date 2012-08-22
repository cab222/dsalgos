package cabkata.tree;

public class BinarySearchTree<V extends Comparable<? super V>> {
	Node<V> root;

	static class Node<V extends Comparable<? super V>> {
		V value;
		Node<V> parent;
		Node<V> left;
		Node<V> right;

		public Node(V value, Node<V> parent)
		{
			this.value = value;
			this.parent = parent;
		}

		@Override
		public String toString()
		{
			return value.toString();
		}
	}

	Node<V> successor(Node<V> node)
	{
		if(node.right != null)
		{
			return minimumNode(node.right);
		}
		
		while(node.parent != null && node != node.parent.left)
		{
			node = node.parent;
		}
		
		return node.parent;
	}
	
	public void remove(V v)
	{
		if (v == null)
		{
			throw new UnsupportedOperationException("Deletion of null value");
		}
		
		Node<V> node = find(v, root);
		if (node == null)
			return;
		
		if(node.left == null)
		{
			replaceNodeWithNode(node, node.right);	
		}
		else if(node.right == null)
		{
			replaceNodeWithNode(node, node.left);
		}	
		else
		{
			Node<V> successor = successor(node);
			//the successor is either immediate right, 
			//or successor is left most leaf in the subtree
			//if that's the case, we need to take that leaf's right child's
			//and replace it with the successor
			if (node.right != successor)
			{
				replaceNodeWithNode(successor, successor.right);
				successor.right = node.right;
				successor.right.parent = successor;
			}
			replaceNodeWithNode(node, successor);
			successor.left = node.left;
			successor.left.parent = successor;
		}
	}

	/*
	 *	takes oldNode's parent, and replace link to oldNode with newNode
	 *  update newNodes parent 
	 */
	private void replaceNodeWithNode(Node<V> oldNode, Node<V> newNode)
	{
		if(oldNode.parent == null)
		{
			root = newNode;	
		}
		else if(oldNode.parent.right == oldNode)
		{
			oldNode.parent.right = newNode;								
		}
		else
		{
			oldNode.parent.left = newNode;
		}			
		
		if(newNode != null)
		{
			newNode.parent = oldNode.parent;
		}	
	}

	public V minimum()
	{
		return root == null ? null : minimumNode(root).value;
	}

	public V maximum()
	{
		return root == null ? null : maximumNode(root).value;
	}

	private Node<V> minimumNode(Node<V> node)
	{
		Node<V> min = node;
		while(min.left != null)
		{
			min = min.left;
		}
		return min;
	}
	
	private Node<V> maximumNode(Node<V> node)
	{
		Node<V> max = node;
		while(max.right != null)
		{
			max = max.right;
		}
		return max;
	}
	
	Node<V> find(V v, Node<V> node)
	{
		if(node == null)
		{
			return null;
		}
		
		if (v.compareTo(node.value) == -1)
		{
			return find(v, node.left);
		}
		else if(v.compareTo(node.value) == 0)
		{
			return node;
		}
		else
		{
			return find(v, node.right);
		}
	}

	public void insert(V v)
	{
		if (v == null)
		{
			throw new UnsupportedOperationException("Insertion of null value");
		}

		if (root == null)
		{
			root = new Node<V>(v, null);
			return;
		}

		insert(v, root);
	}

	private void insert(V v, Node<V> node)
	{
		if (v.compareTo(node.value) == -1)
		{
			if (node.left == null)
			{
				node.left = new Node<V>(v, node);
				return;
			}
			else
			{
				insert(v, node.left);
			}
		}
		else
		{
			if (node.right == null)
			{
				node.right = new Node<V>(v, node);
				return;
			}
			else
			{
				insert(v, node.right);
			}
		}
	}

	public void printInorder(StringBuffer buffer)
	{
		if (root != null)
		{
			printInorder(root, buffer);
		}
	}

	private void printInorder(Node<V> v, StringBuffer buffer)
	{
		if (v == null)
			return;

		printInorder(v.left, buffer);
		buffer.append(v.value).append(" ");
		printInorder(v.right, buffer);
	}
}
