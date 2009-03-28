package agents;

import general.Problem;
import messages.*;
import agents.*;

import com.sosnoski.util.stack.IntStack;


public class DBAAgent extends AbstractAgent {

	public MessageBox<MessageImprove> improve_message_box; 
	boolean quasi_local_minimum = false;
	boolean can_move = false;
	IntStack coflicting_vars;  // TODO: this saves constraint checks but maybe should be counted
	int new_value;
	int my_improve;	

	public DBAAgent(int id, int max_cycles, double p, boolean any_time, int d, int n) {
		super(id, max_cycles, p, any_time, d, n);

		improve_message_box = new MessageBox<MessageImprove>();
        
	}
	
	public void do_alg(int cycles) {
		while (! completed) {
			dba_send_ok();	
			if (completed)
			   break;
			wait_ok();
			send_improve();
			wait_improve();
			
			if (step_no == cycles)
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
				// We don't need to notify all neighbors that solution was find since their termination counter is the same 
				// as ours (if it is not on of us will change value
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
	
	@SuppressWarnings("unchecked")
	private void send_improve() {
		int current_eval = dba_evalueate(value);
		int best_eval = current_eval;

		if (current_eval != 0) {
			termination_counter = 0;
			consistent = false;
		}
		else
			consistent = true;
		
		
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
		    ((DBAAgentInfo) neighbors[i]).improve_message_box.send_message(message);
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
			ncccs++;
			int e = weight_table[i][current_val][agent_view[i]];
			eval += e;
		}
		
		return eval;
	}
	
}
