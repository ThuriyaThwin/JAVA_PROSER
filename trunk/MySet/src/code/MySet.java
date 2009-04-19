/**
 * 
 */
package code;
import java.util.*;

import javax.swing.JOptionPane;

/**
 * MySet class that repr a set with the needed opers.
 * @author Elad l.
 *
 */
public class MySet {
	
	/**
	 *  Note - decided to have a set member instead of extended the set class,
	 *         for more flexibility. 
	 *         also because the different name needed in the API.   
	 */
	private HashSet<Integer> set; 
	
	public MySet ()
	{
		set = new HashSet<Integer>();
	}
	
	/**
	 * Sets the given array vars as the set's vars. 
	 * @param input_set
	 */
	public MySet(int [] input_set)
	{
		set = new HashSet<Integer>();
		
		for (int i : input_set)
		{
			set.add(i);
		}
	}
	
	/**
	 * Sets the set to be the union of the current set and the given set
	 * @param other_set
	 */
	public void union(MySet other_set)
	{
		set.addAll(other_set.set);
	}
	
	
	/**
	 * Sets the set to be the intersect of the current set and the given set
	 * @param other_set
	 */
	public void intersect(MySet other_set)
	{
		set.retainAll(other_set.set);
	}
	
	/**
	 * @param other_set
	 * @return true iff the other_set is a sub set of the current set.
	 */
	public boolean subset(MySet other_set)
	{
		return set.containsAll(other_set.set);
	}
	
	/**
	 * 
	 * @param x
	 * @return true iff the current set contains x.
	 */
	public boolean isMember(int x)
	{
		return set.contains(x);
	}
	 
	/**
	 * insert the given var to the set.
	 * @param x
	 */
	public void insert(int x)
	{
		set.add(x);
	}
	

	/**
	 * delete the given var from the set.
	 * @param x
	 */
	public void delete(int x)
	{
		set.remove(x);
	}
	
	
	/**
	 * the string repr of the class
	 */
	public  String toString()
	{
		String res = "";
		
		if (set.isEmpty())
		{
			return res + "Empty set";
		}
		
		for (int  i : this.set) {
			res +=(i + "\n");
		}
		return res;
	}
	
	
	/**
	 * 
	 * @param other_set
	 * @return true iff this = other_set
	 */
	public boolean equals(MySet other_set)
	{
		if (set.size() != other_set.set.size())
		{
			return false;
		}
		
		for (int  i : set) {
			if (!other_set.set.contains(i))
			{
				/* found a mismatch */
				return false;
			}
		}
		return true;
	}

	
	/**
	 * a util for generate a random number in rage [start - end]
	 * @param start
	 * @param end
	 * @param generator
	 * @return a rand num in the range [starat - end]
	 */
	public static int get_rand_num(int start, int end, Random generator)
	{	
		return generator.nextInt(end-start)+start;
	}
	
	
	public static void main(String[] args)
	{
	    int even[] = {2,4,6,8,10,12,14,16,18,20};
	    int odd[] = {1,3,5,7,9,11,13,15,17,19};
	    
		MySet even_set = new MySet(even);
		MySet odd_set = new MySet(odd);
	
		
		System.out.println("even set is : \n" + even_set + "--------");
		System.out.println("odd set is : \n" + odd_set + "--------");
		
		even_set.union(odd_set);
		
		System.out.println("after even_set.union(odd_set);");
		System.out.println("even set is : \n" + even_set + "--------");
		System.out.println("odd set is : \n" + odd_set + "--------");
		
		even_set.intersect(odd_set);
		
		System.out.println("after even_set.intersect(odd_set);");
		System.out.println("even set is : \n" + even_set + "--------");
		System.out.println("odd set is : \n" + odd_set + "--------");
		
		if (even_set.subset(odd_set))
		{
			System.out.println("odd_set is now subset of the even_set");
		}
		else
		{
			System.out.println("odd_set is now  NOT a subset of the even_set");
		}
		
		
		if (even_set.equals(odd_set))
		{
			System.out.println("odd_set is now equals the even_set");
		}
		else
		{
			System.out.println("odd_set is now  NOT equals the even_set");
		}
		
		Random generator;
		generator = new Random();
		int x = get_rand_num(21,100,generator);
		
		even_set.insert(x);
		System.out.println("even set is : \n" + even_set + "--------");
		
		for (int i=0;i<4;i++)
		{
			x = get_rand_num(1,20,generator);
			if(even_set.isMember(x))
			{
				even_set.delete(x);
			}
		}
		System.out.println("even set is : \n" + even_set + "--------");
		
		
		if (even_set.equals(odd_set))
		{
			System.out.println("odd_set is now equals the even_set");
		}
		else
		{
			System.out.println("odd_set is now  NOT equals the even_set");
		}
		
	}

}
