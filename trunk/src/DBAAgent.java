
import general.Problem;

import com.sosnoski.util.stack.IntStack;


public class DBAAgent extends AbstractAgent {

	MessageBox<MessageImprove> improve_message_box; 
	boolean quasi_local_minimum = false;
	boolean can_move = false;
	IntStack coflicting_vars;  // TODO: this saves constraint checks but maybe should be counted
	int new_value;
	boolean completed=false; // will be set to false when not done
	int my_improve;	

	public DBAAgent(int id, Problem problem, int max_cycles,  AbstractAgent agents_table[],  double p, boolean any_time) {
		super(id, problem, max_cycles, (AbstractAgent[]) agents_table, p, any_time);

		this.agents_global_table =  agents_table;
		weight_table = new int [no_of_neighbors][][];
		ok_message_box = new MessageBox<MessageOK>();
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
        
		
	}
	
	public void do_alg(int cycles) {
		while (! completed) {
			dba_send_ok();	
			if (completed)
			   break;
			wait_ok();
			send_improve();
			wait_improve();
			
			if (cycle_count == cycles)
				completed=true;
		
			/*
			try {
				Thread.sleep(1);
			}
			catch (InterruptedException e){
				
			}*/
	   }
	}
	
	private void dba_send_ok() {
		if (consistent) {
			termination_counter++;
			if (termination_counter >= n) {
				// TODO: notify all neighbor that solution was found
				completed = true;
				return;
			}
		}
		
		if (quasi_local_minimum) {
	        for (int i = 0; i < no_of_neighbors; i++) {
				int neighbor_val = agent_view[i];
				if (weight_table[i][value][neighbor_val] != 0)
					weight_table[i][value][neighbor_val]++;
			}
		}
		
		if (can_move) {
			value = new_value;
		}

		send_ok();
		
	}
	
	private void wait_ok() {
		read_neighbors_ok();
	}
	
	private void send_improve() {
		int current_eval = dba_evalueate(value);
		int best_eval = current_eval;

		
		for (int val = 0 ; val < d ; val++) {
			if (val == value)
				continue;
			int test_eval = dba_evalueate(val);
			
			if (test_eval < best_eval) {
				best_eval = test_eval;
				new_value = val;	
			}
		}
		
		my_improve = current_eval - best_eval;
		
		if (my_improve > 0) {
			can_move = true;
			quasi_local_minimum = false;
		}
		else {
			can_move = false;
			quasi_local_minimum = true;
		}
		
		
		// Send the message
		MessageImprove message = new MessageImprove(id, my_improve, current_eval, termination_counter);
		
		for (int i = 0 ; i < no_of_neighbors; i++) {
		    int neighbor_id = neighbor_map.get(i);
		    ((DBAAgent)agents_global_table[neighbor_id]).improve_message_box.send_message(message);
		    messages_sent++;
		}
	}
	
	private void wait_improve() {
		for(int counter = 0; counter < no_of_neighbors; counter++) {
			MessageImprove message = improve_message_box.read_message();
			
			termination_counter = Math.min(termination_counter, message.termination_counter);
			if (message.imporove_val > my_improve) {
				can_move = false;
				quasi_local_minimum = false;
			}
			
			if ((message.imporove_val == my_improve) && (id > message.id)){
				can_move = false;
				// if my improve was 0 quasi_local_minimum will remain false
				// otherwise it will remain true
			}
			
			if (message.current_eval > 0) {
				consistent = false;
			}
		}
	}
	
	protected int dba_evalueate(int current_val) {
		int eval = 0;
		for (int i=0; i < no_of_neighbors; i++) {
			eval += weight_table[i][current_val][agent_view[i]];
		}
		
		return eval;
	}
	
}
