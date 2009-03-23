
public class DBAAgentInfo extends AgentInfo {
	public MessageBox improve_message_box;
	public DBAAgentInfo(int id, MessageBox ok_message_box_in, MessageBox ok_message_box_out, MessageBox improve_message_box) {
		super(id, ok_message_box_in, ok_message_box_out);
		this.improve_message_box = improve_message_box;
	}

}
