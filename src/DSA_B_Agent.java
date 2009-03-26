import general.Problem;


public class DSA_B_Agent extends DSAAgent{
	
	public DSA_B_Agent(int id, Problem problem, int max_cycles, double p, boolean any_time) {
		super(id, problem, max_cycles, p, any_time);
	}

	void select_next_value(int delta, int v, double p){
		if (delta > 0){
			change_value_with_prob(v,p);
		}	
		else if ((delta== 0) && (0 != current_conflicts_count)){
			/* there is at least one conflict */
			change_value_with_prob(v,p);
		}		
	}
}
