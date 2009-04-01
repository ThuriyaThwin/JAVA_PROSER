


package code;
import code.HitsMiss;
import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Elad l.
 * Implements the Business Logic(bl) of the game.
 *
 */
public class BusinessLogic {
	public ArrayList<Integer> numbers ; //the numbers to guess 
	Random generator;
	int size; // the "size" of the game = how many numbers to guess
	
	/**
	 * Ctor
	 * @param size
	 */
	BusinessLogic(int size)
	{
		this.size = size;
		generator  = new Random();
		numbers = new ArrayList<Integer>();
	}
	
	/**
	 * 
	 * @return a rand digit that not selected yet
	 */
	private int get_not_selected_random_digit()
	{
		int res;
		
		do
		{
			res = generator.nextInt(10); /* every digit from 0-9 */
		}while(numbers.contains(res));
		
		return res;
	}
	
	/**
	 * sets the numbers to guess for the current game 
	 */
	public void set_random_choice()
	{
		for (int i=0 ; i<size ; i++)
		{
			numbers.add(get_not_selected_random_digit());
		}
	}
	
	/**
	 * 
	 * @param answer - the user input guess.
	 * @return HitsMiss obj that represents the miss & hits count for the input ans.
	 */
	public HitsMiss handle_answer(ArrayList<Integer> answer)
	{
		HitsMiss hm = new HitsMiss();
		
		for (int i=0 ; i<size ; i++)
		{
			if(numbers.get(i).equals(answer.get(i)))
			{
				hm.hits_count++;
				continue;
			}
			
			if(numbers.contains(answer.get(i)))
			{
				hm.miss_count++;
			}
		}
		return hm;
	}
	
	
	/**
	 * the string repr of the class
	 */
	public String toString()
	{
		return numbers.toString(); 
	}
}
