import general.Problem;


public class DSA_D_Agent extends DSAAgent{
	
	public DSA_D_Agent(int id, Problem problem, int max_cycles, double p, boolean any_time) {
		super(id, problem, max_cycles, p, any_time);
	}

	void select_next_value(boolean is_improve, int v, double p){
		if(is_improve){
			value = v;
			return;
		}
		
		if (0 != current_conflicts_count){
			change_value_with_prob(v,p);
		}		
	}
}
