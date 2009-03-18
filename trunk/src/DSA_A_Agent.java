import general.Problem;


public class DSA_A_Agent extends DSAAgent{
	
	public DSA_A_Agent(int id, Problem problem, int max_cycles,  AbstractAgent agents_table[],  double p, boolean any_time) {
		super(id, problem, max_cycles, (AbstractAgent[]) agents_table, p, any_time);
	}

	void select_next_value(boolean is_improve, int v, double p){
		if (is_improve){
			/* delta > 0 */
			//TODO System.out.println("in select_next_value");
			change_value_with_prob(v,p);
		}		
	}
}
