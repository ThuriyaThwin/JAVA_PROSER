
import general.Problem;

import java.util.Random;
import com.sosnoski.util.stack.IntStack;

abstract public class DSAAgent extends AbstractAgent {
	boolean completed=false; // will be set to false when not done
	int current_conflicts_count;
	int delta;

	boolean is_improve = false;
	Random rand_generator;
	int seed = 4;
	
	public DSAAgent(int id, Problem problem, int max_cycles,  AbstractAgent agents_table[], double p, boolean any_time) {
		super(id, problem, max_cycles, (AbstractAgent[]) agents_table, p, any_time);

		this.agents_global_table =  agents_table;
		weight_table = new int [no_of_neighbors][][];
		ok_message_box = new MessageBox<MessageOK>();
		//improve_message_box = new MessageBox<MessageImprove>();
        
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
		//rand_generator = new Random(seed);
		rand_generator = new Random();
		value = rand_generator.nextInt(d); 
		current_conflicts_count = evalueate(value); 
		delta = current_conflicts_count; 
	}
	
	public void do_alg(int cycles) {
		while (! completed) {
			//System.out.println("doing cycle");
			send_ok(); 
			wait_ok(); 
			if (cycle_count == (cycles-1))
				completed=true;

	   }
		//System.out.println("after the run function");
	}
			
	
	protected int get_lowest_delta_value(){
		/*
		 * returns a new value (the lowest delta value)
		 * if there is one. 
		 * otherwise the current value.
		 * also updates the delta. 
		 * */
		
		is_improve = false;
		int best_value = -1;
		int after_read_conflicts_count;
		int new_conflicts_count = Integer.MAX_VALUE;
		
		for (int val = 0 ; val < d ; val++) {
			if (val == value)
				continue;
			after_read_conflicts_count = evalueate(val);
			//TODO System.out.println("current_conflicts_count = " + current_conflicts_count);
			if (after_read_conflicts_count < new_conflicts_count) {
				is_improve = true;	
				delta = current_conflicts_count - after_read_conflicts_count;
				new_conflicts_count = after_read_conflicts_count;
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
	
	protected void wait_ok() {
		read_neighbors_ok();
		int v = get_lowest_delta_value();
		select_next_value(is_improve, v, p);
		current_conflicts_count = evalueate(value);
	}
	
}
