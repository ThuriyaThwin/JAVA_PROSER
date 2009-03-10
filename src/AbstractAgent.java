
import java.util.HashMap;
import java.util.HashSet;

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
	AbstractAgent agents_global_table[];
	MessageBox<MessageOK> ok_message_box; 
	
	// these are used in order to decide when  to stop
	protected int termination_counter; // when reaches n can stop
	protected boolean consistent; // no reason to change value 
	protected int larger_neighbors_index; // this will point to index in agent_view where 
	                                  // after it all agents will have an id larger then currents agent
	HashMap<Integer,Integer> neighbor_map; // map index to id
	HashMap<Integer,Integer> neighbor_id_map; // map id to index
	
	// Variables for AnyTime implementation 
	public final static int NULL = -1;
	int bfs_parent_id;
	HashSet<Integer> bfs_children;
	int bfs_height;
	int bfs_distance;
	int best = NULL;
	int best_index = NULL;
	int current_step = 0;
	int cost_i; // this cost of current step;

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
		my_str = "<html><center>id:" + id + "<p>" + "val:" +  value;
		
		return my_str;
	}
	
	public int get_id() {
		return id;
	}
	
	public void set_bfs_parent(int parent_id) {
		bfs_parent_id = parent_id;
	}
	
	public void add_bfs_child(int child_id) {
         if (bfs_children == null)
        	 bfs_children = new HashSet<Integer>();
         
         bfs_children.add(child_id);
	}
	
	public void set_bfs_distance(int dist) {
		bfs_distance = dist;
	}
	
	public void set_bfs_height (int height) {
		bfs_height = height;
	}
	

	public void any_time_send_ok() {
		MessageOK message = new MessageOK(id, value);
		MessageOKAnyTimeParent parent_message = new MessageOKAnyTimeParent (id, value, cost_i);
		MessageOKAnyTimeSon child_message = new MessageOKAnyTimeSon(id, value, best_index);
		
		for (int i = 0 ; i < no_of_neighbors; i++) {
		    int neighbor_id = neighbor_map.get(i);
		    if (bfs_children.contains(neighbor_id)) {
		    	agents_global_table[neighbor_id].ok_message_box.send_message(message);
		    }
		    else if (bfs_parent_id == neighbor_id) {
		    	agents_global_table[neighbor_id].ok_message_box.send_message(parent_message);
		    }
		    else
		    	agents_global_table[neighbor_id].ok_message_box.send_message(child_message);
		}
		
	}
	
	public void any_time_wait_ok() {
		
		for(int counter = 0; counter < no_of_neighbors; counter++) {
			MessageOK message = ok_message_box.read_message();
			int neighbor_index = neighbor_id_map.get(message.id);
			
			agent_view[neighbor_index] = message.current_value;
		}
  
	}
}
