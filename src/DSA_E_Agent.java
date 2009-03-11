
public class DSA_E_Agent extends DSAAgent{
	
	public DSA_E_Agent(int id, Problem problem, int max_cycles,  AbstractAgent agents_table[], boolean any_time) {
		super(id, problem, max_cycles, (AbstractAgent[]) agents_table, any_time);
	}

	void select_next_value(boolean is_improve, int v, double p){
		if(is_improve){
			value = v;
			return;
		}
		/* delta=0 */
		change_value_with_prob(v,p);		
	}
}
