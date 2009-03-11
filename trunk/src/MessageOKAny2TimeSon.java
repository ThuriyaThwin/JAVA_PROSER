
// This is the message the will be sent to BFS child when Any Time is implemented
public class MessageOKAny2TimeSon extends MessageOK {
	public int best_index;
	
	public MessageOKAny2TimeSon(int id, int current_value, int best_index) {		
		super(id, current_value); 
		this.best_index = best_index;
	}
}
