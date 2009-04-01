package code;


import java.util.Random;

/**
 * repr the Equation as needed.
 * @author Elad l.
 *
 */
public class  Equation {
	final int limit = 10;
	int x;
	int y;
	
	/**
	 * Ctor - generates random x and y in range (0-9)
	 */
	public Equation()
	{
		Random generator = new Random();
		this.x = generator.nextInt(limit+1);
		this.y = generator.nextInt(limit+1);
	}
	

	/**
	 * Custom toString() Method.
	 */
	public String toString() {
		return  this.x + " * " + this.y;
	}

	/**
	 * 
	 * @return the equation's results
	 */
	public int get_res()
	{
		return this.x * this.y;
	}
	
	/**
	 * 
	 * @param answer
	 * @return - true iff the input answer is right. 
	 */
	public boolean is_answer_ok(int answer)
	{
		return (this.x * this.y) == answer ;
	}
}
