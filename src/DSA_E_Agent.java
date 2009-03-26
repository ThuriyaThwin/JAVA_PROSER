import general.Problem;


public class DSA_E_Agent extends DSAAgent{
	
	public DSA_E_Agent(int id, Problem problem, int max_cycles, double p, boolean any_time) {
		super(id, problem, max_cycles, p, any_time);
	}

	void select_next_value(int delta, int v, double p){
		if(delta > 0){
			value = v;
		}
		else if (delta == 0)
			change_value_with_prob(v,p);		
	}
}
