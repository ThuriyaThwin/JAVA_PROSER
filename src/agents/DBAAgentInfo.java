package agents;

import messages.*;



public class DBAAgentInfo extends AgentInfo {
	public MessageBox<MessageImprove> improve_message_box;
	public DBAAgentInfo(int id, MessageBox<MessageOK> ok_message_box_in, MessageBox<MessageOK> ok_message_box_out, MessageBox<MessageImprove> improve_message_box) {
		super(id, ok_message_box_in, ok_message_box_out);
		this.improve_message_box = improve_message_box;
	}

}
