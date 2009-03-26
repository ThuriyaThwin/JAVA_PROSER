
import general.Problem;

import java.util.Random;

abstract public class DSAAgent extends AbstractAgent {
	boolean completed=false; // will be set to false when not done
	int current_conflicts_count;
	int delta;

	boolean is_improve = false;
	Random rand_generator;
	int seed = 4;
	
	public DSAAgent(int id, Problem problem, int max_cycles,  double p, boolean any_time) {
		super(id, problem, max_cycles, p, any_time);

		//improve_message_box = new MessageBox<MessageImprove>();
        		
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
			if (step_no == cycles)
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
	
	abstract void select_next_value(int delta, int v, double p); 
	
	protected void wait_ok() {
		read_neighbors_ok();
		int v = get_lowest_delta_value();
		select_next_value(delta, v, p);
		current_conflicts_count = evalueate(value);
	}
	
}
