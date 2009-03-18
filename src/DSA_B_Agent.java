import general.Problem;


public class DSA_B_Agent extends DSAAgent{
	
	public DSA_B_Agent(int id, Problem problem, int max_cycles,  AbstractAgent agents_table[],  double p, boolean any_time) {
		super(id, problem, max_cycles, (AbstractAgent[]) agents_table, p, any_time);
	}

	void select_next_value(boolean is_improve, int v, double p){
		if (0 != current_conflicts_count ){
			/* there is at least one conflict */
			change_value_with_prob(v,p);
		}		
	}
}
