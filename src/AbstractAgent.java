
import general.Problem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public abstract class AbstractAgent implements Runnable{
	protected int id; // the id of current agent
	protected int[][] weight_table[]; // how much does each conflict cost
	protected int agent_view[]; // agent view for neighbors
	                                              // tree map will enable going over only part of children
	protected int d;
	protected int n;
	protected int value;   
	public int step_no;    // what cycle was reached?
	protected int max_cycles;    // after how many cycles to terminate if solution not found?
	public int messages_sent;  // how many messages did current agent send
	public int ncccs;          // What is the NCCCS of current agent
	protected Problem problem;
	protected double p; // the probability to change the current value is actualy used only by DSA
						// but we are setting it in all agents in order to enable common interface 
	protected int no_of_neighbors; // for fast looping over all neighbors
	
	// these are used in order to decide when  to stop
	protected int termination_counter; // when reaches n can stop
	protected boolean consistent; // no reason to change value
	AgentInfo neighbors[];
	protected int larger_neighbors_index; // this will point to index in agent_view where 
	                                  // after it all agents will have an id larger then currents agent
	HashMap<Integer,Integer> neighbor_id_map; // map id to index
	
	protected abstract void do_alg(int cycles);
	
	// Variables for AnyTime implementation 
	protected boolean any_time=false;
	public final static int NULL = -1;
	private int bfs_parent_id = NULL;
	private HashSet<Integer> bfs_children;
	private int bfs_height;
	private int bfs_dist;
	private int best = NULL;
	public  int best_index = NULL;
	private int best_cost = Integer.MAX_VALUE; // this is used only by root

	private int cost_i[]; // this cost of steps history
	private int val_i[]; // the value history
	private int val_i_len;

	public AbstractAgent(int id, Problem problem, int max_cycles, double p, boolean any_time) {
		d = problem.getD();
		n = problem.getN();
		this.max_cycles = max_cycles;
		this.id = id;
		this.problem = problem;
		termination_counter = 0;
		consistent = false;
		this.any_time = any_time;
		this.p = p;

		//create map to idenitfy where in agent_view will agent's value be found
		neighbor_id_map = new HashMap<Integer, Integer>();
		
		value = (int) (Math.random() * d);
		step_no = -1;

	}
	
	public void init (AgentInfo neighbors[], int larger_neighbors_index ){
		this.neighbors = neighbors;
		no_of_neighbors = neighbors.length;
		this.larger_neighbors_index = larger_neighbors_index;
		weight_table = new int [no_of_neighbors][][];
		
		for (int i = 0; i < no_of_neighbors ; i++) {
			int neighbor_id = neighbors[i].id;
		    neighbor_id_map.put(neighbor_id, i);
        	weight_table[i] = new int[d][d];
        	
        	for (int v1 = 0; v1 < d; v1++)
        		for (int v2 = 0; v2 < d; v2++) {
        			ncccs++;
        			if (problem.check(id, v1, neighbor_id, v2)) {
        				weight_table[i][v1][v2] = 0;
        			}
        			else {
        				weight_table[i][v1][v2] = 1;
        			}
        				
        		}
        	
        }
		agent_view = new int[no_of_neighbors+1];
	}
	
	public int[] get_neighbors() {
		if (no_of_neighbors == 0)
			return new int[0];

		int[] result = new int[no_of_neighbors]; 
		
		for (int i = 0; i < no_of_neighbors; i++) {
			result[i] = neighbors[i].id;
		}	
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
	
	public void set_bfs_params (int dist, int height) {
		bfs_height = height;
		bfs_dist = dist;

        cost_i = new int[bfs_height];
        
        // need to start with maximal values in order to make sure that there 
        // are no false results
        for (int i = 0 ; i < bfs_height; i++) {
        	cost_i[i]= Integer.MAX_VALUE;
        }
        
        val_i_len = height + 2*dist;
        val_i = new int[val_i_len];
        
        for (int i = 0 ; i < val_i_len; i++) {
        	val_i[i]= NULL;
        }
        
        bfs_children = new HashSet<Integer>();
	}
	

	protected void send_ok() {			
		step_no++;
		
		if (any_time) {
			any_time_send_ok();
		}
		else {
			MessageOK message = new MessageOK(id, value, step_no);
			
			for (int i = 0 ; i < no_of_neighbors; i++) {
			    neighbors[i].ok_message_box_out.send_message(message);
			    messages_sent++;
			}
		}
	}
	
	public void any_time_send_ok() {
		
		MessageOK message = new MessageOK(id, value, step_no);
		int cost = Integer.MAX_VALUE;
		
		int i = step_no - bfs_height;
		
		if (i >= 0) {
			cost = cost_i[i%bfs_height];
		}

		MessageOKAnyTime2Parent parent_message = new MessageOKAnyTime2Parent (id, value, cost, i, step_no);
		MessageOKAnyTime2Son child_message = new MessageOKAnyTime2Son(id, value, best_index, step_no);
		
		for (int k = 0 ; k < no_of_neighbors; k++) {
		    int neighbor_id = neighbors[k].id;

		    if (bfs_children.contains(neighbor_id)) {
		    	neighbors[k].ok_message_box_out.send_message(child_message);
		    }
		    else if (bfs_parent_id == neighbor_id) {
		    	neighbors[k].ok_message_box_out.send_message(parent_message);
		    }
		    else {
		    	neighbors[k].ok_message_box_out.send_message(message);
		    }
		    messages_sent++;

		}
		
		
	}
	
	protected void read_neighbors_ok(){

		if (any_time) {
			any_time_read_neighbors_ok();
		}
		else {
			for(int neighbor_index = 0; neighbor_index < no_of_neighbors; neighbor_index++) {
				MessageOK message = neighbors[neighbor_index].ok_message_box_in.read_message();	
				agent_view[neighbor_index] = message.current_value;
			}
		}
	}
	
	public void any_time_read_neighbors_ok() {
		
		int i = step_no - bfs_height + 1;
		
		for(int neighbor_index = 0; neighbor_index < no_of_neighbors; neighbor_index++) {
			MessageOK message = neighbors[neighbor_index].ok_message_box_in.read_message();
			
			agent_view[neighbor_index] = message.current_value;
			
			if (message.id == bfs_parent_id) {
				MessageOKAnyTime2Son parent_message = (MessageOKAnyTime2Son) message;
				
				if (parent_message.best_index != best_index) {
					best_index = parent_message.best_index ;
					best = val_i[best_index%val_i_len];
				}
			}
			else if (bfs_children.contains(message.id)) {
				MessageOKAnyTime2Parent child_message = (MessageOKAnyTime2Parent) message;
				if (child_message.step_no >= 0)
				    cost_i[child_message.step_no%bfs_height] += child_message.cost_i;
			}
		}
  
		
		val_i[step_no%val_i_len] = value;
	    cost_i[step_no%bfs_height] = evalueate(value);

		if (i >= 0) {
			//root
			if ((bfs_parent_id == NULL) && (cost_i[i%bfs_height] < best_cost)) {
				best_cost = cost_i[i%bfs_height];
				best = val_i[i%val_i_len];
				best_index = i;
			}
		
		}
		
	}
	
	protected int evalueate(int current_val) {
		int eval = 0;
		for (int i=0; i < no_of_neighbors; i++) {
			ncccs++;
			if (weight_table[i][current_val][agent_view[i]] > 0)
			eval += 1;
		}
		
		return eval;
	}
	
	public void run() {
		// if there are no conflicts then the first value can be selected and nothing needs to be checked
		if (no_of_neighbors == 0) {
			value = 0;	
		}
		else {
	        do_alg(max_cycles + bfs_dist + bfs_height);
	        if (any_time) {
	           post_alg_steps();
	           value = best;
	        }   
        }
		
	}
	
	public void post_alg_steps() {
        for (int k = 0; k < (bfs_dist + bfs_height) ; k++) {
        	step_no++;
            // send message to children
	        MessageOKAnyTime2Son child_message = new MessageOKAnyTime2Son(id, value, best_index, -1);
			
	        Iterator<Integer> iter = bfs_children.iterator();
	        while (iter.hasNext()) {
	        	int child_id = iter.next().intValue();
	        	int child_index = neighbor_id_map.get(child_id);
	        	neighbors[child_index].ok_message_box_out.send_message(child_message);
	        	messages_sent++;
	        }

        	// read parent message
        	if (bfs_parent_id != NULL) {
        		int parent_index = neighbor_id_map.get(bfs_parent_id);
	        	MessageOK message = neighbors[parent_index].ok_message_box_in.read_message();
	        	if (message.id != bfs_parent_id) {
					System.out.println("Bug !!! got a non parent message at post_steps");
					System.out.println("message id is " + message.id + " id is" + id);
					System.out.println("my step is: " + step_no + "his step is: " + message.step_no);
					//System.exit(1);
					continue;
	        	}
	        	
	        	MessageOKAnyTime2Son parent_message = (MessageOKAnyTime2Son) message;			
				if (parent_message.best_index != best_index) {
					best_index = parent_message.best_index ;
					best = val_i[best_index%val_i_len];
				}
        	}        	   
			

        }
	}
	
	public boolean equals(Object other) {
		AbstractAgent otherVar = (AbstractAgent)  other;
	      
	      return (otherVar.id == this.id);
	}
	
	// TODO remove this is for debug only
	public void print_agent_view() {
		String str = "agent view of id " + id +":";
		for(int i = 0; i < no_of_neighbors; i++) {
			int neighbor_id = neighbors[i].id;
			str += "  id=" + neighbor_id+ " val=" + agent_view[i]; 
		}
		System.out.println(str);
		
	}
}
