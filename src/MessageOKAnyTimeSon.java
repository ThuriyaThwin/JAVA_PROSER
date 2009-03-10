
// This is the message the will be sent to BFS child when Any Time is implemented
public class MessageOKAnyTimeSon extends MessageOK {
	public int best_index;
	
	public MessageOKAnyTimeSon(int id, int current_value, int best_index) {		
		super(id, current_value); 
		this.best_index = best_index;
	}
}
