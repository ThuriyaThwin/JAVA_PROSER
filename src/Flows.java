

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;



/************************************************************************************
This class contains the main running flows
make_samples() and run_tests() should be run.

make_sampes() – creates the problems in directory input
run_tests() – runs FC-CBJ and FC-CBJ-DAC and prints the results to files
run_queens() – runs an n queen problem (n is hard coded)

*************************************************************************************/
public class Flows {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//run_queens("DBAAgent");
		run_queens("DSA_A_Agent");
	    //make_samples();
		//run_tests();
		//run_example();
	}
	
   // "Play" with this to run with diffrent p1/p2 values
	
   private static double p2_min = 0.1;
   private static double p2_max = 0.91;
   private static double p2_jump = 0.05;
   
   private static double p1_min = 0.3;
   private static double p1_max = 0.7;
   private static double p1_jump = 0.2;
   
   private static int samples_count=60;
   
	//creates the problems in directory input
	// make sure directory input exists before running
	public static void make_samples() {
			
		for (double p1 = p1_min; p1 <= p1_max; p1+= p1_jump) {
			for (double p2 = p2_min; p2 <= p2_max; p2+= p2_jump) {
				for (int i = 0; i < samples_count; i++) {
					String fileName = "input/case.p1=" + p1 + "_p2=" + p2 + "_i=" + i;
					System.out.println("creating " + fileName);
					//Problem problem = new Problem(15,10,p1,p2);
					Problem problem = new Problem(15, 10, p1,p2);
					problem.save2File(fileName);
				}
			}
		}
		
	}
	
	
	// make sure directory input exists before running
	/*
	public static void run_tests() {
		
		int p1_index = 0;
		int p2_index = 0;
		
		String dbt_class_names[] = 
				{
				   "FC_Cbj", // will be run separately at beginning (loop will be on the rest for running
				    "mzirin_dbt.Dbt", "mzirin_dbt.Dbt_minDomain", "mzirin_dbt.Dbt_maxConflicts", 
				  	"mzirin_dbt.Dbt_FC1", "mzirin_dbt.Dbt_FC1_minDomain", "mzirin_dbt.Dbt_FC1_maxConflicts",
				  	"mzirin_dbt.Dbt_FC1_currentMaxConflicts", "mzirin_dbt.Dbt_FC1_currentMinConflicts",
				  	"mzirin_dbt.Dbt_FC2", "mzirin_dbt.Dbt_FC2_minDomain", "mzirin_dbt.Dbt_FC2_maxConflicts",
				  	"mzirin_dbt.Dbt_FC3"
				};
		
		int num_of_alg = dbt_class_names.length;
		
		int num_of_p2 = 1 + (int) Math.ceil((p2_max-p2_min)/p2_jump);
		int num_of_p1 = 1 + (int) Math.ceil((p1_max-p1_min)/p1_jump);
	    int assignments[][][] = new int[num_of_alg][num_of_p1][num_of_p2];
		
		int checks[][][] = new int[num_of_alg][num_of_p1][num_of_p2];
		int solution_count[][] = new int[num_of_p1][num_of_p2];
		
		int i;
		
		for (i = 0; i < samples_count; i++) {
			p1_index = 0;
			for (double p1 = p1_min; p1 <= p1_max; p1+= p1_jump, p1_index++) {
				p2_index=0;
				for (double p2 = p2_min; p2 <= p2_max; p2+= p2_jump, p2_index++) {
					
					String inputFileName = "input/case.p1=" + p1 + "_p2=" + p2 + "_i=" + i;
					// read problem
					Problem problem = new Problem(inputFileName);
					
					// keeps constraint checks of FC_Cbj since prblem is reused
					int FC_Cbj_constraint_checks;
					
					// run FC_Cbj;
					System.out.println("Running FC_Cbj p1=" + p1 + " p2=" + p2 + " i=" + i);
					FC_Cbj solver_FC_Cbj = new FC_Cbj(problem);
					
					Definitions.StatOptions status1 = solver_FC_Cbj.bcssp();
					
					FC_Cbj_constraint_checks = problem.constraint_checks;
					
					// verify solution
					if (status1 == Definitions.StatOptions.SOLUTION && ! solver_FC_Cbj.check_results()) {
							System.out.println("Bug !!! FC-CBJ result is wrong");
							System.exit(1);
					}
					
					// keep results for report
					assignments[0][p1_index][p2_index] += solver_FC_Cbj.assignments;
					checks[0][p1_index][p2_index] += FC_Cbj_constraint_checks;
					if (status1 == Definitions.StatOptions.SOLUTION) {
						solution_count[p1_index][p2_index]++;
					}
					
					for (int alg_no = 1; alg_no < num_of_alg; alg_no++) {
						String alg_name = dbt_class_names[alg_no];
						// reset constraint checks in order to count for FC_Cbj Dac
						problem.constraint_checks = 0;
										
						
						
						System.out.println("Running " + alg_name + " p1=" + p1 + " p2=" + p2 + " i=" + i);
	
					
						DbtAbstractSolver solver_Dbt = null;
						try {
							
							Class cls =  Class.forName(alg_name);
						    Class partypes[] = new Class[1];
						    partypes[0] = Problem.class;
						    Constructor ct = cls.getConstructor(partypes);
						    Object arglist[] = new Object[1];
						    arglist[0] = problem;
						    solver_Dbt = (DbtAbstractSolver) ct.newInstance(arglist);
					    }
					    catch (Throwable e) {
					      System.err.println(e);
					    }
	
	
						Definitions.StatOptions status2 = solver_Dbt.solve();
						
						// verify solution
	
						assignments[alg_no][p1_index][p2_index] += solver_Dbt.assignments;
						checks[alg_no][p1_index][p2_index] += problem.constraint_checks;
						
				        
						if (status2 == Definitions.StatOptions.SOLUTION && ! solver_Dbt.check_results()) {
							System.out.println("Bug !!! Dbt result is wrong");
							
							System.out.print("DBT Result");
							solver_Dbt.printV(System.out);
							System.out.println();
							
							if (status1 == Definitions.StatOptions.SOLUTION) { 
								System.out.print("FC-CBJ Result");
								solver_FC_Cbj.printV(System.out);
								System.out.println();
							}
							
							problem.printProblem(System.out);
							
							System.exit(1);
						}
					
				        if (status1 != status2) {
							System.out.println("Bug !!! FC-CBJ and Dbt deffer");
							if (status2 != Definitions.StatOptions.SOLUTION ) {
								System.out.println("DBT failed");
								solver_FC_Cbj.printV(System.out);
								System.out.println();
							}
							
							System.exit(1);
	
						}
						
	
					} // end of alg loop
				} // end of p2 loop	
			} // end of p1 loop
		} // end of i loop (go to next sample)
	
		/********************
         * print reports
         ********************/ 	
		/*
		p1_index = 0;
		p2_index = 0;
		for (double p1 = p1_min; p1 <= p1_max; p1+= p1_jump, p1_index++)  {
	    	String reportName = "output/report.p1=" + p1 + ".csv";
    		PrintStream results;
			try {
				results = new PrintStream(new FileOutputStream(reportName));
                
				// Print file header
    		    results.print("p2,");
		    	for (int alg_no = 0; alg_no < num_of_alg; alg_no++) {
					String alg_name = dbt_class_names[alg_no];
					results.print("assignments_" + alg_name+ ",CC_"+ alg_name + ",");
		    	}
		    	results.println("solveable");
			
    		    p2_index = 0;
    		    for (double p2 = p2_min; p2 <= p2_max; p2+= p2_jump, p2_index++) {
		    		results.print(p2 + ",");
    		    	for (int alg_no = 0; alg_no < num_of_alg; alg_no++) {
				    		results.print(assignments[alg_no][p1_index][p2_index]/i+ ",");
				    		results.print(checks[alg_no][p1_index][p2_index]/i+ ",");
    		    	}
		    		results.println(solution_count[p1_index][p2_index]);
		    	}
			}
			catch (Exception e) {
				System.out.println("problem with file " + reportName);
				e.printStackTrace();
				System.exit(1);
			}
		
	    }
			
	}
	
	
	*/
	
	/*
	
	public static void run_example() {

		
		Problem problem = new Problem(6,3, "example1.txt");
		long start = System.currentTimeMillis();

		Dbt_FC2_minDomain solver = new Dbt_FC2_minDomain(problem);
		
		if (solver.solve() == Definitions.StatOptions.SOLUTION)
			solver.printV(System.out);
		else 
			System.out.println("There is no solution");
		
		long end = System.currentTimeMillis();
		
		System.out.println("Execution time was "+(end-start)+" ms.");
		System.out.println("assignments =" + solver.assignments);
		System.out.println("constraint checks =" + problem.constraint_checks);

		problem.printProblem(System.out);
	}
	*/
	
	
	public static void run_queens(String AgentAlgorith) {
		//Problem problem = new Problem(15,10, 0.5, 0.5);
		// problem.save2File("input/problem.data1");
		
		//Problem problem = new Problem("input/problem.data1");
		long start = System.currentTimeMillis();

		Problem problem = new Problem(10);
		AgentSolver solver = new AgentSolver(problem, AgentAlgorith, 2000);
		solver.solve();
		
        // TODO - need to check if there is a solution 
	    solver.printV(System.out);

	    
		long end = System.currentTimeMillis();
		System.out.println("Execution time was "+(end-start)+" ms.");

		//problem.printProblem(System.out);
	}
	
}
