import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import edu.uci.ics.jung.graph.Graph;


public abstract class AbstractAgent implements Runnable{
	protected int id; // the id of current agent
	protected int agent_view[]; // agent view for neighbors
	                                              // tree map will enable going over only part of children
	protected int d;
	protected int n;
	protected int value;   
	protected int cycle_count;    // what cycle was reached?
	protected int max_cycles;    // after how many cycles to terminate if solution not found?
	protected int messages_sent;  // how many messages did current agent send
	protected int ncccs;          // What is the NCCCS of current agent
	protected Problem problem;
	protected int no_of_neighbors; // for fast looping over all neighbors
	
	// these are used in order to decide when  to stop
	protected int termination_counter; // when reaches n can stop
	protected boolean consistent; // no reason to change value 
	protected int larger_neighbors_index; // this will point to index in agent_view where 
	                                  // after it all agents will have an id larger then currents agent
	HashMap<Integer,Integer> neighbor_map; // map index to id
	HashMap<Integer,Integer> neighbor_id_map; // map id to index
	
    /**
     * the graph will be used for display
     */
    Graph<AbstractAgent,Number> graph;

	public AbstractAgent(int id, Problem problem, int max_cycles, AbstractAgent agents_table[]) {
		d = problem.getD();
		n = problem.getN();
		this.max_cycles = max_cycles;
		this.id = id;
		this.problem = problem;
		termination_counter = 0;
		consistent = false;
		
		
		
		//create map to idenitfy where in agent_view will agent's value be found
		neighbor_map = new HashMap<Integer, Integer>();
		neighbor_id_map = new HashMap<Integer, Integer>();
		no_of_neighbors = 0;
		for (int i = 0; i < n; i++) {
			if (i == id) {
				larger_neighbors_index = no_of_neighbors;
				continue;
			}
				
			if(problem.has_conflict(id, i)) {
			    neighbor_map.put(no_of_neighbors, i);
			    neighbor_id_map.put(i, no_of_neighbors);
			    no_of_neighbors++;
			}
		}
		
		agent_view = new int[no_of_neighbors+1];
		value = (int) (Math.random() * d);
		cycle_count = 0;
		
		setup_graph();
	}
	
	private void setup_graph() {
		
	}
	
	public int[] get_neighbors() {
		Object [] neighbors = neighbor_id_map.keySet().toArray();
		int[] result = new int[neighbors.length]; 
		
		for (int i = 0; i < neighbors.length; i++)
			result[i] = (Integer) neighbors[i];
		
		return result;
		
	}
	
	public String toString()
	{
		String my_str;
		my_str = "id:" + id + "\n" + "val:" +  value;
		
		return my_str;
	}

}
