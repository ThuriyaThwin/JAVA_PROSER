
public class DSA_A_Agent extends DSAAgent{
	
	public DSA_A_Agent(int id, Problem problem, int max_cycles,  AbstractAgent agents_table[]) {
		super(id, problem, max_cycles, (AbstractAgent[]) agents_table);
	}

	void select_next_value(boolean is_improve, int v, double p){
		if (is_improve){
			/* delta > 0 */
			System.out.println("in select_next_value");
			change_value_with_prob(v,p);
		}		
	}
}
