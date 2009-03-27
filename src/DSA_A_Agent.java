import general.Problem;


public class DSA_A_Agent extends DSAAgent{
	
	public DSA_A_Agent(int id, Problem problem, int max_cycles,  boolean any_time) {
		super(id, problem, max_cycles, any_time);
	}

	void select_next_value(int delta, int v, double p){
		if (delta > 0){
			change_value_with_prob(v,p);
		}		
	}
}
