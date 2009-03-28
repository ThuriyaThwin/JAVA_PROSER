package agents;
import general.Problem;


public class DSA_D_Agent extends DSAAgent{
	
	public DSA_D_Agent(int id, int max_cycles, double p, boolean any_time, int d, int n) {
		super(id, max_cycles, p, any_time, d, n);
	}

	void select_next_value(int delta, int v, double p){
		if (delta > 0){
			value = v;
		}	
		else if ((delta == 0) && (0 != current_conflicts_count)){
			change_value_with_prob(v,p);
		}		
	}
}
