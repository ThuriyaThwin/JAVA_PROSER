/**
 * OK message for DBA (Will be renamed if can't be used by DSA)
 */

public class MessageOK {

	public int id; // the id of sender
	public int current_value; // the current value of sender
	// TODO for debug
	int step_no;
	
	public MessageOK(int id, int current_value, int step_no) {
		this.current_value = current_value;
		this.id = id;
		this.step_no = step_no;

	}

}
