package code;


/**
 * Includes hits & miss counts, and the guess string repr.
 * @author Elad l.
 *
 */
public class HitsMiss {
	public int hits_count ;
	public int miss_count ;
	public String guess;
	
	public HitsMiss()
	{
		hits_count = 0;
		miss_count = 0;	
	}
	
	public String toString()
	{
		return "guess was: " + guess + " hits:" + hits_count + " miss:" + miss_count ; 
	}
}
