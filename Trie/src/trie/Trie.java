package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) 
	{
		/** COMPLETE THIS METHOD **/
				
		TrieNode root = new TrieNode(null,null,null);
		
		if(allWords.length == 0)
		{
			return root;
		}
		
		Indexes fw = new Indexes(0, (short)0, (short)(allWords[0].length()-1));
		TrieNode first_word = new TrieNode(fw, null, null);
		root.firstChild = first_word;
		
		for(int i = 1; i < allWords.length; i++)
		{
				
			Indexes z = new Indexes( i, (short)0, (short)(allWords[i].length()-1) );
			TrieNode y = new TrieNode(z, null, null);
			TrieNode w = first_word;
				
			
			while(true)
			{
	
				int padam = w.substr.wordIndex;
				short lastIndex = w.substr.endIndex;
				short begIndex = w.substr.startIndex;
				short prefix = prefix(allWords[padam], allWords[i]);
				
				if(allWords[padam].charAt(begIndex) == allWords[i].charAt(begIndex))
				{
					if(w.firstChild == null)
					{
						Indexes new1 = new Indexes(padam,begIndex,lastIndex);
						TrieNode child = new TrieNode(new1, null, null);
						w.firstChild = child;
						w.substr.endIndex = (short)(prefix - 1);
						child.substr.startIndex = prefix;
						y.substr.startIndex = prefix;
						child.sibling = y;
						break;
					}
					else if((short)(prefix-1) < lastIndex)
					{
						Indexes new1 = new Indexes(padam, prefix, lastIndex);
						TrieNode child = new TrieNode(new1, w.firstChild, y);
						w.firstChild = child;
						w.substr.endIndex = (short)(prefix-1);
						y.substr.startIndex = prefix;
						break;
					}
					else
					{
						w = w.firstChild;
					}
				}
				else
				{
					if(w.sibling == null)
					{
						w.sibling = y;
						y.substr.startIndex = prefix;
						break;
					}
					w = w.sibling;
				}
			}
		}
		
		return root;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<TrieNode> arraylist = new ArrayList<TrieNode>();
		TrieNode root2 = root.firstChild;
		
		while(true)
		{			
			
			if(root2 == null)
			{
				break;
			}
			
			
			short lastIndex = root2.substr.endIndex;
			short begIndex = root2.substr.startIndex;
			
			int padam = root2.substr.wordIndex;
			
			if(allWords[padam].charAt(begIndex) == prefix.charAt(begIndex))
			{
				if(prefix(prefix,allWords[padam].substring(0, lastIndex+1)) == prefix.length())
				{
					if(root2.firstChild == null)
					{
						arraylist.add(root2);
						return arraylist;
					}
					
					root2 = root2.firstChild;
					while(true)
					{
						while(true)
						{							
							if(root2 == null)
							{
								break;
							}
							else if(root2.firstChild == null)
							{
								arraylist.add(root2);
							}
							else
							{
								arraylist.add(0,root2);
							}
							
							root2 = root2.sibling;
						}
						
						if(arraylist.get(0).firstChild == null)
						{
							return arraylist;
						}
						root2 = arraylist.get(0).firstChild;
						arraylist.remove(0);
						arraylist.trimToSize();
					}
				}
				else
				{
					root2 = root2.firstChild;
				}
			}
			else
			{
				root2 = root2.sibling;				
			}
		}
			return null;
	}
	
	public static void print(TrieNode root, String[] allWords) 
	{
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) 
	{
		if (root == null) 
		{
			return;
		}
		for (int i=0; i < indent-1; i++) 
		{
			System.out.print("    ");
		}
		
		if (root.substr != null) 
		{
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) 
		{
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) 
		{
			System.out.println("root");
		} 
		else 
		{
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) 
		{
			for (int i=0; i < indent-1; i++) 
			{
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
	private static short prefix(String word1, String word2)
	{
		int short_ = word1.length();
		if(word1.length() > word2.length())
		{
			short_ = word2.length();
		}
		
		short i = 0;
		for(int x = 0; x<short_;x++)
		{
			if(word1.charAt(x) == word2.charAt(x))
			{
				i++;
			}
		}

		return i;
	}
 }
