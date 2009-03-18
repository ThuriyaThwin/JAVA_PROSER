import general.Problem;


public class DSA_C_Agent extends DSAAgent{
	
	public DSA_C_Agent(int id, Problem problem, int max_cycles,  AbstractAgent agents_table[],  double p, boolean any_time) {
		super(id, problem, max_cycles, (AbstractAgent[]) agents_table, p, any_time);
	}

	void select_next_value(boolean is_improve, int v, double p){
			/* no matter what */
			change_value_with_prob(v,p);		
	}
}
