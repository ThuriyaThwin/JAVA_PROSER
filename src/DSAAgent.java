
import java.util.Random;
import com.sosnoski.util.stack.IntStack;

abstract public class DSAAgent extends AbstractAgent {
	int[][] weight_table[];
	//MessageBox<MessageOK> ok_message_box; 
	MessageBox<MessageImprove> improve_message_box; 
	//boolean quasi_local_minimum = false;
	//boolean can_move = false;
	//IntStack coflicting_vars;  // TODO: this saves constraint checks but maybe should be counted
	//int new_value;
	AbstractAgent agents_global_table[];
	boolean completed=false; // will be set to false when not done
	//int my_improve;
	
	int current_conflicts_count = 0;
	int delta;
	double p=0.3; // the probability to change the current value
	boolean is_improve = false;
	Random rand_generator;
	int seed = 4;
	
	public DSAAgent(int id, Problem problem, int max_cycles,  AbstractAgent agents_table[]) {
		super(id, problem, max_cycles, (AbstractAgent[]) agents_table);

		this.agents_global_table =  agents_table;
		weight_table = new int [no_of_neighbors][][];
		//ok_message_box = new MessageBox<MessageOK>();
		improve_message_box = new MessageBox<MessageImprove>();
        
		for (int i = 0; i < no_of_neighbors; i++) {
        	int neighbor_id = neighbor_map.get(i);
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
		rand_generator = new Random(seed);
		value = 1; /* TODO : change this to a random value */
		current_conflicts_count = evalueate(value); 
		delta = current_conflicts_count; 
	}
	
	public void run() {
		while (! completed) {
			send_improve(); // send my improve val if any
			wait_improve(); // also check for assigning a new value
			cycle_count++;
			if (cycle_count == max_cycles)
				completed=true;
	   }
	}
	
	
	
		
	protected void send_improve() {			
		MessageImprove message = new MessageImprove(id, 0, value, termination_counter);/* there is no need to the my_improve for this algorithm */
		
		for (int i = 0 ; i < no_of_neighbors; i++) {
		    int neighbor_id = neighbor_map.get(i);
		    ((DBAAgent)agents_global_table[neighbor_id]).improve_message_box.send_message(message);
		}
	}
	
	
	protected void read_neighbors_data(){
		int check_res = 0;
		
		for(int counter = 0; counter < no_of_neighbors; counter++) {
			MessageImprove message = improve_message_box.read_message();
			
			termination_counter = Math.min(termination_counter, message.termination_counter);	
			
			if (problem.check(id, value, message.id, message.imporove_val)) {
				check_res = 0;
			}
			else {
				check_res = 1;
			}
			weight_table[message.id][value][message.imporove_val] = check_res;
		}	
	}
	
	protected int get_lowest_delta_value(){
		/*
		 * returns a new value (the lowest delta value)
		 * if there is one. 
		 * otherwise the current value.
		 * also updates the delta. 
		 * */
		
		is_improve = false;
		int best_value = value;
		int after_read_conflicts_count;
		
		for (int val = 0 ; val < d ; val++) {
			if (val == value)
				continue;
			after_read_conflicts_count = evalueate(val);
			
			if (after_read_conflicts_count < current_conflicts_count) {
				is_improve = true;	
				delta = current_conflicts_count - after_read_conflicts_count;
				current_conflicts_count = after_read_conflicts_count;
				best_value = val;	
			}
		}
		return best_value;
	}
	
	
	protected void change_value_with_prob(int v,double p){
		int i = rand_generator.nextInt(100);
		
		if (i <= (p * 100)){
			/* need to change the value*/
			value = v;
		}
	}
	
	abstract void select_next_value(boolean is_improve, int v, double p); 
	
	protected void wait_improve() {
		read_neighbors_data();
		int v = get_lowest_delta_value();
		select_next_value(is_improve, v, p);
		
	}
	

	protected int evalueate(int current_val) {
		int eval = 0;
		for (int i=0; i < no_of_neighbors; i++) {
			eval += weight_table[i][current_val][agent_view[i]];
		}
		
		return eval;
	}
	
	
}
