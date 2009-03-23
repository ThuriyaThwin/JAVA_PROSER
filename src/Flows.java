

import general.Definitions;
import general.Problem;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import javax.swing.JFrame;

import prosser.FC_Cbj;


import java.util.Date;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;


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
		//run_queens("DBAAgent", 4, 20000);
		//run_queens("DSA_A_Agent", 4, 20000);
	    run_gui_test("DSA_B_Agent", 7, 1000);
		//run_gui_test("DBAAgent", 10, 100);
	    //make_samples();
		//run_tests();
		//run_example();
		
		//make_random_samples();
		//run_random_tests(1, 1000);
	}
	
	
    /**
     * a driver for this demo
     */
    public static void run_gui_test(String AgentAlgorith, int queens_count, int cycle_count) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Problem problem = new Problem(queens_count);
        //Problem problem = new Problem(10, 10, 0.1,0.5);
        problem.save2File("problem_save.prb");
        //Problem problem = new Problem("problem_save.prb");
		AgentSolver solver = new AgentSolver(problem, AgentAlgorith, cycle_count, 0.1, true);
		
        f.setLayout(new GridLayout(1,2));
        f.getContentPane().add(solver.get_panel());
        if (solver.use_any_time)
            f.getContentPane().add(solver.get_bfs_panel());
        f.pack();
        f.setVisible(true);
        
		solver.solve();
		
        f.setLayout(new GridLayout(1,2));
        f.getContentPane().add(solver.get_panel());
        if (solver.use_any_time)
            f.getContentPane().add(solver.get_bfs_panel());
        f.pack();
        f.setVisible(true);
        
    	System.out.println("ncccs = " + solver.ncccs);
        if (solver.check_results()) {
        	System.out.println("result ok");
        }
        else {
        	System.out.println("number of conflicts is : " + solver.count_conflicts());
        }
	    solver.printV(System.out);

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
	

	private static String random_input_dir = "random_input";
	private static String random_out_dir = "random_output";

	public static void make_random_samples(int no_of_random_samples) {

			
		File input_dir = new File(random_input_dir);
		if ((! input_dir.isDirectory()) && (! input_dir.mkdir())) {
			System.out.println("Error createing dir " + random_input_dir);
			System.exit(1);
		}
		
		for (int i = 0; i < no_of_random_samples; i++) {
			String fileName = random_input_dir + "/case." + i;
			double p1 = Math.random();
			double p2 = Math.random();
			System.out.println("creating " + fileName);
			Problem problem = new Problem(15, 10, p1,p2);
			problem.save2File(fileName);
		}
	}

	private static double p_min = 0.1;
	private static double p_max = 0.91;
	private static double p_jump = 0.05;
	   
	public static void run_random_tests(int no_of_random_samples,  int cycle_count) {
		String agent_class_names[] = {
			"DSA_A_Agent", "DSA_B_Agent", "DSA_C_Agent", "DSA_D_Agent", 
			"DSA_E_Agent", "DBAAgent"  	    
		};
		
		int num_of_p = 1 + (int) Math.ceil((p2_max-p2_min)/p2_jump);
		int num_of_alg = 2*agent_class_names.length;
	    int conflicts_at_end[][] = new int[num_of_alg][num_of_p]; // need to avrage at end
	    int failures[][] = new int[num_of_alg][num_of_p]; // when was there a solution but it was not found
	    int any_time_index[][] =   new int[num_of_alg][num_of_p]; 
	    int total_messages[][] = new int[num_of_alg][num_of_p]; 
	    int max_messages[][] = new int[num_of_alg][num_of_p]; 
	    int ncccs[][] = new int[num_of_alg][num_of_p]; 
	    int solvable = 0;
	    //TODO
	    Problem problem = new Problem(0,0,0,0);

		for (int i = 0; i < no_of_random_samples; i++) {
			int p_index = 0;
			for (double p = p_min; p <= p_max; p+= p_jump, p_index++) {
				String inputFileName = random_input_dir + "/case." + i;
				// read problem
				problem = new Problem(inputFileName);
					
				// run FC_Cbj;
				System.out.println("Running FC_Cbj for case case" + i);
				FC_Cbj solver_FC_Cbj = new FC_Cbj(problem);
					
				Definitions.StatOptions fc_cbj_status = solver_FC_Cbj.bcssp();
					
				// verify solution
				if (fc_cbj_status == Definitions.StatOptions.SOLUTION && ! solver_FC_Cbj.check_results()) {
						System.out.println("Bug !!! FC-CBJ result is wrong");
						System.exit(1);
				}
				
				if (fc_cbj_status == Definitions.StatOptions.SOLUTION)
					solvable++;
					
					
				for (int alg_no = 0; alg_no < num_of_alg ; alg_no++) {
						String alg_name = agent_class_names[alg_no%agent_class_names.length];
						boolean any_time = false;
						if (alg_no >= agent_class_names.length)
							any_time = true;
						
						System.out.println("Running " + alg_name + " for case #" + i + " p is " + p + " any time is " + any_time);
						AgentSolver solver = new AgentSolver(problem, alg_name, cycle_count, p, false);
						solver.solve();	
						
						int conflicts = solver.count_conflicts();
					    conflicts_at_end[alg_no][p_index] += conflicts;
					    if ((fc_cbj_status == Definitions.StatOptions.SOLUTION) &&  (conflicts != 0))
					    		failures[alg_no][p_index]++;
					    any_time_index[alg_no][p_index] += solver.any_time_max_index;
					    total_messages[alg_no][p_index] += solver.messages_sent; 
					    max_messages[alg_no][p_index] += solver.max_messages_sent; 
					    ncccs[alg_no][p_index] = solver.ncccs;
				} // end of alg loop
			} // end of p loop	
		} // end of i loop (go to next sample)
	
		/********************
         * print reports
         ********************/ 
		/* elad start */
		
		String reportFileName = random_out_dir + "/random_report" +  "_N#-" + problem.getN() + "_D#-" + problem.getD() + ".xls";

		String cell_headers[] = {
				"EMPTY",
				"conflicts_at_end",
				"failures",
				"any_time_index",
				"total_messages", 
				"max_messages",
				"ncccs"  	    
			};
		try{
			// create the xls file
			WritableWorkbook workbook = Workbook.createWorkbook(new File(reportFileName));
			
		
			int x=0,y=0, sheet_no=0;
			WritableSheet sheet;
			Label label;
			int p_index = 0;
			
			for (int alg_no = 0; alg_no < num_of_alg ; alg_no++) {
				String alg_name = agent_class_names[alg_no%agent_class_names.length];
				boolean any_time = false;
				if (alg_no >= agent_class_names.length) {
					any_time = true;
					alg_name = alg_name + "_any_time";
				}
				
				sheet = workbook.createSheet(alg_name, sheet_no++);
				x=0;
				y=0;
				p_index = 0;
				label = new Label(x, y, "p");
				sheet.addCell(label);
				
				for (int i=1;i< cell_headers.length;i++){
					label = new Label(i, y, cell_headers[i]);
					sheet.addCell(label);
				}
				
				
				for (double p = p_min; p <= p_max; p+= p_jump, p_index++)  {
					Number number = new Number(x,++y,p);
					sheet.addCell(number);
					
				sheet.addCell(new Number(x+1,y,conflicts_at_end[alg_no][p_index]/no_of_random_samples));
				sheet.addCell(new Number(x+2,y,failures[alg_no][p_index]));
				sheet.addCell(new Number(x+3,y,any_time_index[alg_no][p_index]/no_of_random_samples));
				sheet.addCell(new Number(x+4,y,total_messages[alg_no][p_index]/no_of_random_samples));
				sheet.addCell(new Number(x+5,y,max_messages[alg_no][p_index]/no_of_random_samples));
				sheet.addCell(new Number(x+6,y,ncccs[alg_no][p_index]/no_of_random_samples));
				}	
				
			}
			workbook.write();
			workbook.close();
		}
		catch (Exception e) {
			System.out.println("problem with file excel api" + reportFileName );
			e.printStackTrace();
			//System.exit(1);
		}	
		/* elad end */
		
		
	   //TODO Miriam Start 	
		
		String measure_names[] = {
				"conflicts_at_end",
				"failures",
				"any_time_index",
				"total_messages", 
				"max_messages",
				"ncccs"  	    
			};
		
		
		int [][] measure_arrays[] = {
				conflicts_at_end,
				failures,
				any_time_index,
				total_messages,
				max_messages,
				ncccs
		};
		
		WritableSheet sheet;
		
		reportFileName = random_out_dir + "/random_report" +  "_N#" + problem.getN() + "_D#" + problem.getD() + ".xls";

		try{
			// create the xls file
			WritableWorkbook workbook = Workbook.createWorkbook(new File(reportFileName));
			

			
			int sheet_no=0;
			
			
			for (int m=0; m < measure_arrays.length; m++) {
				sheet = workbook.createSheet(measure_names[m], sheet_no++);
				Label label =  new Label(0, 0, "p");
				sheet.addCell(label);
				
				for (int alg_no = 0; alg_no < num_of_alg ; alg_no++) {
					String alg_name = agent_class_names[alg_no%agent_class_names.length];
					boolean any_time = false;
					if (alg_no >= agent_class_names.length) {
						any_time = true;
						alg_name = alg_name + "_any_time";
					}
					
				    label = new Label(alg_no+1, 0, alg_name);
					sheet.addCell(label);
				}
			
			    int p_index = 0;
				for (double p = p_min; p <= p_max; p+= p_jump, p_index++)  {
					Number number = new Number(0,p_index+1,p);
					sheet.addCell(number);
				
					for (int alg_no = 0; alg_no < num_of_alg ; alg_no++) {
						String alg_name = agent_class_names[alg_no%agent_class_names.length];
						boolean any_time = false;
						if (alg_no >= agent_class_names.length) {
							any_time = true;
							alg_name = alg_name + "_any_time";
						}
				
					if (measure_names[m].equals("failures")) {
						number = new Number(alg_no+1, p_index+1, failures[alg_no][p_index]);
					}
					else {
						number = new Number(alg_no+1, p_index+1, measure_arrays[m][alg_no][p_index]/no_of_random_samples);
					}
					
					sheet.addCell(number);
					}
				}
			}	
				
			workbook.write();
			workbook.close();
		}
		catch (Exception e) {
			System.out.println("problem with file excel api" + reportFileName );
			e.printStackTrace();
			//System.exit(1);
		}
		
		// TODO Miriam end
		File output_dir = new File(random_out_dir);
		if ((! output_dir.isDirectory()) && (! output_dir.mkdir())) {
			System.out.println("Error createing dir " + random_out_dir);
			System.exit(1);
		}

		String reportName = random_out_dir + "/random_report.csv";
		PrintStream results; 
		try {
			results = new PrintStream(new FileOutputStream(reportName));
		
			// Print file header
			results.print("p,");
			for (int alg_no = 0; alg_no < num_of_alg ; alg_no++) {
				String alg_name = agent_class_names[alg_no%agent_class_names.length];
				boolean any_time = false;
				if (alg_no >= agent_class_names.length) {
					any_time = true;
					alg_name = alg_name + "_any_time";
				}
				results.print(alg_name+ "_conflicts_at_end, " );
				results.print(alg_name+ "_failures, " );
				results.print(alg_name+ "_any_time_index, " );
				results.print(alg_name+ "_total_messages, " );
				results.print(alg_name+ "_max_messages, " );
				results.print(alg_name+ "_ncccs, " );
			}
			results.println();
			
			int p_index = 0;
			for (double p = p_min; p <= p_max; p+= p_jump, p_index++)  {
				results.print(p + ",");
				for (int alg_no = 0; alg_no < num_of_alg; alg_no++) {   		    	    
	    		    	    results.print(conflicts_at_end[alg_no][p_index]/no_of_random_samples+ ",");
	    		    	    results.print(failures[alg_no][p_index]+ ",");
	    		    	    results.print(any_time_index[alg_no][p_index]/no_of_random_samples+ ",");
	    		    	    results.print(total_messages[alg_no][p_index]/no_of_random_samples+ ",");
	    		    	    results.print(max_messages[alg_no][p_index]/no_of_random_samples+ ",");
	    		    	    results.print(ncccs[alg_no][p_index]/no_of_random_samples+ ",");
	    		    	    
				}
			    		results.println();
			} // end loop over p
	
			
		    } 
		catch (Exception e) {
			System.out.println("problem with file " + reportName);
			e.printStackTrace();
			System.exit(1);
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
	
	
	public static void run_queens(String AgentAlgorith,int queens_count, int cycle_count) {
		//Problem problem = new Problem(15,10, 0.5, 0.5);
		// problem.save2File("input/problem.data1");
		
		//Problem problem = new Problem("input/problem.data1");
		long start = System.currentTimeMillis();

		Problem problem = new Problem(queens_count);
		AgentSolver solver = new AgentSolver(problem, AgentAlgorith, cycle_count, 0.2, true);
		solver.solve();
		
        // TODO - need to check if there is a solution 
	    solver.printV(System.out);

	    
		long end = System.currentTimeMillis();
		System.out.println("Execution time was "+(end-start)+" ms.");

		//problem.printProblem(System.out);
	}
	
}
