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
	
	public BirthDate()
	{
		date = new Date();
	}
	
	public BirthDate(Date date)
	{
		this.date = date;
	}
	
	/**
	 * 
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
	
	public Date getDate()
	{
		return date;
	}
	
	public String toString()
	{
		return  date.toString();
	}
}
