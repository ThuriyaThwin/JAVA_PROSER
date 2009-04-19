/**
 * 
 */
package code;
import java.util.*;

import javax.swing.JOptionPane;

/**
 * @author Elad l.
 *
 */
public class MySet {
	private Set<Integer> set;
	
	public MySet ()
	{
		set.clear(); /* just for case.. */
	}
	
	/**
	 * Sets the given array vars as the set's vars. 
	 * @param input_set
	 */
	public MySet(int [] input_set)
	{
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
		set.addAll((Collection<Integer>)other_set);
	}
	
	public void show()
	{
		for (int  i : this.set) {
			System.out.println(i);
		}
	}

	public static void main(String[] args)
	{
	    int input[] = {1, 2, 3, 4, 5};
		MySet s1 = new MySet();
		MySet s2 = new MySet(input);
		
		s1.show();
		s2.show();
		
	}

}
