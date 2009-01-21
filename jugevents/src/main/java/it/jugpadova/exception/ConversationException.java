/**
 * 
 */
package it.jugpadova.exception;

/**
 * Thrown when the user tries to reach a specific url directly without having followed the normal execution flow.
 * @author Enrico Giurin
 *
 */
public class ConversationException extends RuntimeException {

	/**
	 * 
	 */
	public ConversationException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ConversationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ConversationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConversationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
