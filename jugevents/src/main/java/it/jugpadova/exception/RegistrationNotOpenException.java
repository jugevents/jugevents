package it.jugpadova.exception;

import it.jugpadova.po.Event;

/**
 * Thrown when you try to register to an event and the registration is closed
 * @author Lucio Benfante
 *
 */
public class RegistrationNotOpenException extends RuntimeException {

    private Event event;

    public Event getEvent() {
        return event;
    }

    public RegistrationNotOpenException(Event event) {
        this.event = event;
    }

    /**
     * @param message
     */
    public RegistrationNotOpenException(String message, Event event) {
        super(message);
        this.event = event;
    }
}
