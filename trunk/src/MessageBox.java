/**
 * A generic message box that allows passing objects between different threads
 */
import java.util.*;
public class MessageBox<M> {
	Stack<M> messages;
	
	MessageBox() {
		messages = new Stack<M>();
	}
	
	synchronized void send_message(M message) {
		messages.push(message);
		notify();
	}
	
	synchronized M read_message() {
		while(messages.isEmpty()) {
			try {
			   wait();
			}
			catch (InterruptedException e) {
				// shouldn't get here
				System.out.println("read_message interuupted");
				System.exit(1);
			}
		}
		
		return messages.pop();
	}
	

}
