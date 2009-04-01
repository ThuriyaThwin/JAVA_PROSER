
package code;

import java.util.*;



/**
 * holds mapping form errors count to equations that had that error count.
 * @author Elad l.
 *
 */
public class Summary{
	
	List<String> one_err = new ArrayList<String>();
	List<String> two_err = new ArrayList<String>();
	List<String> wrong_answer = new ArrayList<String>(); // 3 errors
	
	public Summary(){}
	
	/**
	 * add the equation to the internal DB.
	 * @param equation
	 * @param error_count - one of [0,1,2,3]
	 */
	public void add(String equation, int error_count)
	{
		if (0 == error_count) return ; /* no need to add */
		
		List<String> add_list = this.one_err;
		
		if (2 == error_count)
		{
			add_list = this.two_err;
		}
		
		if (3 == error_count)
		{
			add_list = this.wrong_answer;
		}
		
		add_list.add(equation);
	}
	
	/**
	 * 
	 * @param error_count - one of [0,1,2,3]
	 * @return  a strings list of the equations that had error_count errors. 
	 */
	public List<String> get_ansewrs_with_try_number(int error_count)
	{
		if (0 == error_count) return new  ArrayList<String>();
		
		if (2 == error_count)
		{
			return this.two_err;
		}
		
		if (3 == error_count)
		{
			return this.wrong_answer;
		}
		
		return this.one_err; 
	}
}
