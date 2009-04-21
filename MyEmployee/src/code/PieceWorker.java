/**
 * @author Elad l.
 */

package code;
import code.BirthDate;


/**
 * repr a piece worker.
 * @author Elad l.
 *
 */
public class PieceWorker extends Employee 
{
	private double wage; // wage per hour
	private int count; // the pieces count

    /**
     * Ctor.
     * @param first
     * @param last
     * @param ssn
     * @param pieceWage
     * @param count (pieces count)
     * @param bd (BirthDate)
     */
	public PieceWorker( String first, String last, String ssn, 
						double pieceWage, int count, BirthDate bd )
	{
		super( first, last, ssn, bd );
		setWage(pieceWage); // validate and store wage
		setCount(count); // validate and count
	} 


	/**
	 * the wage setter
	 * @param pieceWage
	 */
	public void setWage( double pieceWage )
	{
	   wage = ( pieceWage < 0.0 ) ? 0.0 : pieceWage;
	} 
	
	
	/**
	 * the wage getter 
	 * @return the wage
	 */
	public double getWage()
	{
	   return wage;
	} 
	
	
	/**
	 * the count setter.
	 * in case of negative count, set to zero.
	 * @param piece_count
	 */
	public void setCount( int piece_count )
	{
	   count = piece_count < 0 ? 0 : piece_count;
	} 
	
	/**
	 * the count getter
	 * @return the count
	 */
	public int getCount()
	{
	   return count;
	} 
	
	
	/**
	 * calculate earnings; override abstract method earnings in Employee.
	 * @return the worker earnings (count * wage).
	 */
	public double earnings()
	{
	   return count * wage;
	} 
	

	/**
	 * @return String representation of this object 
	 */
	public String toString()
	{
	   return String.format( "pieces worker employee: %s\n%s: $%,.2f; %s: %d", 
	      super.toString(), "piece wage", wage, 
	      "piece count", count );
	} 
} 
	
