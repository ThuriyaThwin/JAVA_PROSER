
public class MessageOKAnyTimeParent extends MessageOK {
	public int cost_i;
	
	public MessageOKAnyTimeParent (int id, int current_value, int cost_i) {		
		super(id, current_value); 
		this.cost_i = cost_i;
	}
}
