package code;


import java.util.*; 
import javax.swing.JOptionPane;
import java.util.List;
import code.Equation;
import code.Summary;
import code.Config;
import code.ExitApplication;



/**
 * Handle the UI and includes the main function.
 * @author Elad l.
 *
 */
public class UserInterface implements Config
{

	Summary summary = new Summary(); // a summary obj 
	Equation equations[];
	
	
	/**
	 * Ctor
	 * @param count
	 */
	public UserInterface (int count)
	{
		equations = new Equation[count];
		for (int i=0; i<equations.length;i++)
		{
			equations[i] = new Equation();
		}
	}
	
	/**
	 * 
	 * @param i - the id of the equation to handle
	 * @throws ExitApplication - need to exit the game.
	 */
	private void handle_equation(int i) throws ExitApplication
	{
		int error_count = 0;
		int res;
		String ans;
		
		while ( error_count != ERR_COUNT_LIMIT )
		{
			try
			{
				ans = JOptionPane.showInputDialog(null, equations[i].toString());
				res = Integer.parseInt(ans);
			}
			catch (Exception e)
			{
				/* not a numeric answer - exit for loop */
				throw new ExitApplication();
			}
			if (!equations[i].is_answer_ok(res))
			{
				error_count++;
			}
			else
			{
				/* answer right */
				break;  
			}
		}
		
		if(error_count == ERR_COUNT_LIMIT)
		{
		 /* need to show the result */
		JOptionPane.showMessageDialog(null, equations[i].toString() + " = " + equations[i].get_res());
		}
	     
		// update the summary status
		summary.add(equations[i].toString(), error_count);
	}

	
	/**
	 * 
	 * @param error_count
	 * @return - the string repr for the summary for the equations that had error_count errors.
	 */
	private String get_summary_for_error_count(int error_count)
	{
		String summary_output = "wrong at " + error_count + " try:\n";
		
		List<String> l1 = this.summary.get_ansewrs_with_try_number(error_count);
		
		Iterator itr = l1.iterator(); 
		while(itr.hasNext()) { 
		    summary_output += itr.next().toString() + "\n";
		}
		return summary_output;
	}

	
	
	/**
	 * show the summary as asked.
	 */
	public void show_summary()
	{
		String summary_output = get_summary_for_error_count(1);
		summary_output += "\n\n" + get_summary_for_error_count(2);
		summary_output += "amount of not answered right = " 
			+ this.summary.get_ansewrs_with_try_number(ERR_COUNT_LIMIT).size();
		
		JOptionPane.showMessageDialog(null, summary_output);
	}
	
	
		
	/**
	 * start's to handle all equations
	 */
	public void start()
	{
        for (int i=0; i<equations.length ;i++)
		{
			try
			{
				handle_equation(i);
			}
			catch(ExitApplication e)
			{
				return;
			}	
		}
	}
	
	/**
	 * The main function.
	 * @param args
	 */
	public static void main(String[] args) {
		UserInterface UI = new UserInterface(4);
		UI.start();
		UI.show_summary();
		}

}
