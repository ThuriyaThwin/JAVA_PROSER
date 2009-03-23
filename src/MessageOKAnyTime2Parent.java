
public class MessageOKAnyTime2Parent extends MessageOK {
	public int cost_i;
	public int step_no;
	
	public MessageOKAnyTime2Parent (int id, int current_value, int cost_i, int step_no, int real_step_no) {		
		super(id, current_value, real_step_no); 
		this.cost_i = cost_i;
		this.step_no = step_no;
	}
}
