/**
 * 
 */
package it.jugpadova.exception;

/**
 * Thrown to signal a generic application exception.
 * @author Enrico Giurin
 *
 */
public class JUGEventsException extends RuntimeException {

	/**
	 * 
	 */
	public JUGEventsException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public JUGEventsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public JUGEventsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JUGEventsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
