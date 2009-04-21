package code;
import java.util.*;
import java.text.*;



/**
 * This class repr a Birthday.
 * @author Elad l.
 *
 */
public class BirthDate
{
	private Date date;
	
	// the use for the calendar is because sum of the Date obj are deprecated.
	private Calendar cal=Calendar.getInstance(); 
	
	/**
	 * empty ctor.
	 */
	public BirthDate()
	{
		date = new Date();
	}
	
	/**
	 * Ctor - init the obj by date given.
	 * @param date
	 */
	public BirthDate(Date date)
	{
		this.date = date;
	}
	
	/**
	 * Ctor - init the obj by date given as string.
	 * @param dateString in format "dd/MM/yy"
	 */
	public BirthDate (String dateString)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy"); 
	    try
	    {
	    	this.date = format.parse(dateString);
	    }
	    catch(ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + dateString + "\"");
        }
	}
	
	/**
	 * 
	 * @return the date day.
	 */
	public Date getDate()
	{
		return date;
	}
	
	/**
	 * a seter for the date day.
	 * @param newDate
	 */
	public void setDate(Date newDate)
	{
		date = newDate;
	}
	
	/**
	 * the string repr of the obj.
	 */
	public String toString()
	{
		// the use for the calendar is because sum of the Date obj are deprecated.
		cal.setTime(date);
        return String.format("%d/%d/%d", cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR)); 
	}
}
