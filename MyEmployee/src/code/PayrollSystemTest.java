package code;
import java.util.Calendar;

public class PayrollSystemTest 
{
   static final int BONUS = 200;
	
   public static void main( String args[] ) 
   {
      // create subclass objects
      SalariedEmployee salariedEmployee = 
         new SalariedEmployee( "John", "Smith", "111-11-1111", 800.00,new BirthDate("11/04/2009") );
      HourlyEmployee hourlyEmployee = 
         new HourlyEmployee( "Karen", "Price", "222-22-2222", 16.75, 40 ,new BirthDate("11/04/2009"));
      CommissionEmployee commissionEmployee = 
         new CommissionEmployee( 
         "Sue", "Jones", "333-33-3333", 10000, .06 ,new BirthDate("11/07/2009"));
      BasePlusCommissionEmployee basePlusCommissionEmployee = 
         new BasePlusCommissionEmployee( 
         "Bob", "Lewis", "444-44-4444", 5000, .04, 300 ,new BirthDate("11/06/1944"));
      PieceWorker pieceWorker = new PieceWorker( "ran", "tal", "989898", 2.55, 2000, new BirthDate("11/04/1999"));
    		   
    		   

      
      // create four-element Employee array
      Employee employees[] = new Employee[ 5 ]; 

      // initialize array with Employees
      employees[ 0 ] = salariedEmployee;
      employees[ 1 ] = hourlyEmployee;
      employees[ 2 ] = commissionEmployee; 
      employees[ 3 ] = basePlusCommissionEmployee;
      employees[ 4 ] = pieceWorker;
      

      System.out.println( "Employees processed polymorphically:\n" );
      
      // generically process each element in array employees
      for ( Employee currentEmployee : employees ) 
      {
         System.out.println( currentEmployee ); // invokes toString

         // determine whether element is a BasePlusCommissionEmployee
         if ( currentEmployee instanceof BasePlusCommissionEmployee ) 
         {
            // downcast Employee reference to 
            // BasePlusCommissionEmployee reference
            BasePlusCommissionEmployee employee = 
               ( BasePlusCommissionEmployee ) currentEmployee;

            double oldBaseSalary = employee.getBaseSalary();
            employee.setBaseSalary( 1.10 * oldBaseSalary );
            System.out.printf( 
               "new base salary with 10%% increase is: $%,.2f\n",
               employee.getBaseSalary() );
         } // end if

         
         
         System.out.printf( 
            "earned $%,.2f\n", currentEmployee.earnings() );
         
         /* get the birth date month */
         Calendar cal=Calendar.getInstance(); // date.getX is deprecated.
         cal.setTime(currentEmployee.getBirthDate().getDate());
         
         /* check for a birthday in this month.*/
         if (cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH))
         {
        	 //need to note the bonus adding.
        	 System.out.printf( 
        	            "**** Happy BirthDay %s! ***** new earning is $%,.2f\n",currentEmployee.getFirstName(), currentEmployee.earnings()+ BONUS );
         }
         System.out.println("-------------------------------------------------------\n");
      } // end for
   } // end main
} // end class PayrollSystemTest
