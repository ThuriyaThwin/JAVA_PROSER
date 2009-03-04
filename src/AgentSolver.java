

/*****************************************************************************************
 * this file is a the base for all DBT solver classes it contains the method solve  the variables 
 * that all solvers should contain. 
 * It also contains some general help methods for printing solution and checking it 
 * Created Feb 2009
 * @author Miriam Kreisler
 *****************************************************************************************/
import java.io.PrintStream;
import java.lang.reflect.Constructor;


public class AgentSolver {
	protected Problem problem;    // the problem to solve
    protected int v[]; // the ordered array with all solution
	protected int n;  // these can be taken from probelem but put here to simplify code
	protected int d;
	protected AbstractAgent agents[];

	/**
	 * Init variables common to all implementations
	 * @param problem
	 */
 
	public AgentSolver(Problem problem, String agentType, int max_cycles) {
		this.problem = problem;
		n = problem.getN();
		d = problem.getD();
		v = new int[n];

		//int id, Problem problem, int max_cycles, AbstractAgent agents_table[]
		agents = new AbstractAgent[n];
		for (int i = 0; i < n; i++) {
			try {
				Class cls =  Class.forName(agentType);	    
			    Constructor ct[] = cls.getDeclaredConstructors();
			    //agents[i] = new DBAAgent(i, problem, max_cycles, agents);
			    agents[i] = (AbstractAgent) ct[0].newInstance(i, problem, max_cycles, agents);
		    }
		    catch (Throwable e) {
		      System.err.println(e);
		      System.exit(1);
		    }

		}	
	}
	
	public void solve() {
		
		Thread threads[] = new Thread[n];
		
		// setup threads
		for (int i = 0; i < n; i++) {
			threads[i] = new Thread(agents[i]);
		}	
		
		// run threads
		for (int i = 0; i < n; i++) {
			threads[i].start();
		}	
		
		// wait for all threads
		for (int i = 0; i < n; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {
				System.out.println("got excetion in join");
			}
		}
		

        // setup values		
		for (int i = 0; i < n; i++) {
			v[i] = agents[i].value;
		}	
		
		if (check_results()) {
			System.out.println("results ok");
		}
		else
		{
			System.out.println("results wrong");
		}
	}
	

	// print the results to a PrintStream (you can use System.out
	public void printV(PrintStream output) {
		output.print("Assignment=");
		for (int i = 0; i < n; i++)
			output.print("<" + i + "," + v[i] + ">,");
	}
	
	// check that the solutions are the same as in anothr Bcssp
	// used to make sure that FC-CBJ-DAC and FC-CBJ return the same
	// results (was also used with CBT
	public boolean CompareResults(AgentSolver other) {
		for (int i = 0; i < problem.getN(); i++) {
		    if (v[i] != other.v[i]) 
		    	return false;
		}
		
		return true;
	}
	
	
	// check that the results that are currently in v satisfy the constraints in problem
	public boolean check_results() {
	
		boolean status = true;
		for (int i = 0; i < problem.getN(); i++) {
			for (int j = i+1; j < problem.getN(); j++) {
				if (! problem.check(i, v[i], j, v[j])) {
					status=false;
					System.out.println("Conflict ===> "+ i + ", " + j);
				}
		    	   
			}
		}
		
		return status;
	}
	
}
	


