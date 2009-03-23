
public class AgentInfo {
	public MessageBox<MessageOK> ok_message_box_in;
	public MessageBox<MessageOK> ok_message_box_out;
	public int id;
	
	public AgentInfo(int id, MessageBox<MessageOK> ok_message_box_in, MessageBox<MessageOK> ok_message_box_out) {
		this.id = id;
		this.ok_message_box_in = ok_message_box_in;
		this.ok_message_box_out = ok_message_box_out;
		
		// TODO remove 
		if (ok_message_box_out == null) {
			System.out.println("ok_message_box_out is null");
		}
	}
}
