
// This is the message the will be sent to BFS child when Any Time is implemented
public class MessageOKAnyTime2Son extends MessageOK {
	public int best_index;
	
	public MessageOKAnyTime2Son(int id, int current_value, int best_index, int step_no) {		
		super(id, current_value, step_no); 
		this.best_index = best_index;
	}
}
