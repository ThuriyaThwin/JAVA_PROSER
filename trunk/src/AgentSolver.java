

/*****************************************************************************************
 * this file is a the base for all DBT solver classes it contains the method solve  the variables 
 * that all solvers should contain. 
 * It also contains some general help methods for printing solution and checking it 
 * Created Feb 2009
 * @author Miriam Kreisler
 *****************************************************************************************/
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jung_addtions.BFSTreeCreator;


import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import general.Problem;



public class AgentSolver {
	protected Problem problem;    // the problem to solve
    protected int v[]; // the ordered array with all solution
	protected int n;  // these can be taken from problem but put here to simplify code
	protected int d;
	protected boolean use_any_time; // should anytime be used?
	
	
	// Result data collcected from childern
	public int any_time_max_index;
	public int messages_sent;
	public int max_messages_sent;
	public int ncccs;
	
	// TODO - shouldn't be public
	public AbstractAgent agents[];
	
    /**
     * the graph will be used for display
     */
	UndirectedSparseGraph<AbstractAgent,Number> graph;
	UndirectedSparseGraph<Number,Number> bfs_graph;
    GraphPanel<AbstractAgent,Number>  graph_panel;    
    GraphPanel<Number, Number> bfs_graph_panel;  
    
    
	/**
	 * Init variables common to all implementations
	 * @param problem
	 */
 
	public AgentSolver(Problem problem, String agentType, int max_cycles, double p, boolean any_time) {
		this.problem = problem;
		use_any_time = any_time;
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
			    agents[i] = (AbstractAgent) ct[0].newInstance(i, problem, max_cycles, agents, p, use_any_time);
		    }
		    catch (Throwable e) {
		      System.err.println(e);
		      System.exit(1);
		    }

		}
		
		setup_graph();	
		if (use_any_time)
		     setup_any_time();
	}
	
	private void setup_graph() {
        // create a simple graph for the demo
        graph = new UndirectedSparseGraph<AbstractAgent,Number>();
    

        int max_edge_id = 0;
		for (int i = 0; i < agents.length; i++) {
			graph.addVertex(agents[i]);
			int neighbors[] = agents[i].get_neighbors();
			for (int j = 0; j <  neighbors.length ; j++) {
				int neighbor_id = neighbors[j];
				if (graph.findEdge(agents[i], agents[neighbor_id]) == null)
				   graph.addEdge(max_edge_id++, agents[i], agents[neighbor_id]);
			}
		}
		
		graph_panel = new GraphPanel<AbstractAgent, Number>(graph);
	}
	
	public void setup_any_time() {
		ArrayList<AbstractAgent> unvisited = new ArrayList<AbstractAgent> ();
		bfs_graph = new UndirectedSparseGraph<Number,Number>(); 
		int bfs_edge_id = 0;
		for (int i = 0; i < agents.length ;i++)
			unvisited.add(agents[i]);
		
		while (! unvisited.isEmpty()) {
			BFSTreeCreator<AbstractAgent, Number> bfs_labeler = new BFSTreeCreator<AbstractAgent, Number> ();
			AbstractAgent root = unvisited.get(0);
			bfs_labeler.labelDistances(graph, root);
			List<AbstractAgent> visited = bfs_labeler.getVerticesInOrderVisited();			
		
			Iterator<AbstractAgent> iter = visited.iterator();
			int tree_height = bfs_labeler.getBFSTreeHight();
	
		    //build DFS Tree
			while (iter.hasNext()) {
				AbstractAgent  current = iter.next();
				unvisited.remove(current);
				AbstractAgent parent  = bfs_labeler.getParnt(current);	
				int current_distance = bfs_labeler.getDistance(graph, current);
				current.set_bfs_params(current_distance, (tree_height-current_distance));
				if (parent == null) {
					current.set_bfs_parent(AbstractAgent.NULL);
					bfs_graph.addVertex(current.id);
				}
				else {
					current.set_bfs_parent(parent.id);
					parent.add_bfs_child(current.id);
					bfs_graph.addEdge(bfs_edge_id++,parent.id, current.id);
				}
			}
		}
		
		bfs_graph_panel = new GraphPanel<Number, Number>(bfs_graph);
	}
	
	public GraphPanel<AbstractAgent,Number> get_panel() {
		return graph_panel;
	}
	
	
	public GraphPanel<Number,Number> get_bfs_panel() {
		return bfs_graph_panel;
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
			any_time_max_index = Math.max(any_time_max_index, agents[i].best_index); 
			messages_sent += agents[i].messages_sent;
			max_messages_sent = Math.max(max_messages_sent, agents[i].messages_sent);
			ncccs = Math.max(ncccs,agents[i].ncccs);
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
					return false;
				}
		    	   
			}
		}
		
		return status;
	}
	
	// check that the results that are currently in v satisfy the constraints in problem
	public int count_conflicts() {
		int conflicts = 0;
		boolean status = true;
		for (int i = 0; i < problem.getN(); i++) {
			for (int j = i+1; j < problem.getN(); j++) {
				if (! problem.check(i, v[i], j, v[j])) {
					conflicts++;
				}
		    	   
			}
		}
		
		return conflicts;
	}
	
}
	


