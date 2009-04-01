package code;


import java.util.*; 
import javax.swing.JOptionPane;
import code.ExitApplication;
import code.BusinessLogic;
import code.Config;

/**
 * Implement the User Interface and function as the main manager.
 * also includes the main function.
 * @author Elad l.
 *
 */
public class UserInterface implements Config {
	private BusinessLogic bl; // the game logic
	private int hits_count = 0;
	private ArrayList<HitsMiss> all_trys; // the guess's history
	
	/**
	 * Ctor
	 */
	public UserInterface()
	{
		bl = new BusinessLogic(Config.NUM_COUNT);
		all_trys = new ArrayList<HitsMiss>();
	}
	
	/**
	 * 
	 * @return true iff the user guess all numbers. 
	 * hits_count == Config.NUM_COUNT
	 */
	private boolean is_game_over()
	{
		return (hits_count == Config.NUM_COUNT); 
	}
	
	/**
	 * 
	 * @param ans - repr of a guess
	 * @return ArrayList (of len (Config.NUM_COUNT)) that repr the guess numbers.
	 */
	private ArrayList<Integer> string_to_array_list(String ans)
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		
		for(int i=0; i < ans.length(); i++)
		{
			res.add( Integer.valueOf(ans.substring(i, i+1)) );
		}
		return res;
	}
	
	/**
	 * 
	 * @param nums - a guess repr
	 * @return - true iff not all numbers are different.
	 */
	private boolean is_repeat_num(ArrayList<Integer> nums)
	{	
		for(int i=0 ; i< nums.size(); i++)
		{
			int currnt_num = nums.get(i);
			
			if(nums.lastIndexOf(currnt_num) != i) /* the number appear again */
				return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return - the guess history as string
	 */
	private String history_info_str()
	{
		String res="";
		
		for (int i=0;i<all_trys.size();i++)
		{
			res += "(" + i + "):" + all_trys.get(i) + "\n"; 
			
		}
		return res;
	}
	
	/**
	 * handles the user guess's
	 * @throws ExitApplication - need to exit the game.
	 */
	private void handle_answers() throws ExitApplication
	{
		String ans = "";
	
		while(!is_game_over())
		{
			try
			{
				ans = JOptionPane.showInputDialog(null, "please take a guess (" + Config.NUM_COUNT+ " numbers):");
			}
			catch (Exception e)
			{
				/* not a numeric answer - exit for loop */
				throw new ExitApplication();
			}
			
			if (ans.length() != Config.NUM_COUNT)
			{
				JOptionPane.showMessageDialog(null, "wrong length input (should be " + Config.NUM_COUNT + " )");
				continue;
			}
			
			ArrayList<Integer> ans_as_array = string_to_array_list(ans);
			JOptionPane.showMessageDialog(null, ans_as_array.toString());
			
			if (is_repeat_num(ans_as_array))
			{
				JOptionPane.showMessageDialog(null, "you have enterd the same number more then once..");
				continue;
			}
			
			HitsMiss hm =  bl.handle_answer(ans_as_array);
			hm.guess = ans;
			
			all_trys.add(hm);
			JOptionPane.showMessageDialog(null, history_info_str());
			this.hits_count = hm.hits_count;
			
		} 
		/* end game - all hits! */
		JOptionPane.showMessageDialog(null, "well done! all hits! ("+ ans +")" );

	}
	
	/**
	 * starts a new game.
	 */
	public void start_game()
	{
		bl.set_random_choice();
		try
		{
			handle_answers();
		}
		catch (ExitApplication e)
		{
			return;
		}
	}
	
	
	/**
	 * The main function.
	 * @param args
	 */
	public static void main(String[] args) {
		UserInterface UI = new UserInterface();
		int ans;
		
		do
		{
			UI.start_game();
			ans = JOptionPane.showConfirmDialog(null, "play agin?");
		}while(ans == JOptionPane.YES_OPTION);

	}

}
