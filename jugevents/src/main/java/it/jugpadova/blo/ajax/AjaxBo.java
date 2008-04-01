/**
 * 
 */
package it.jugpadova.blo.ajax;

import org.acegisecurity.annotation.Secured;

/**
 * Defines all the dwr methods, that is, all the methods that is possible to
 * call using ajax-dwr protocol.
 * The methods are secured using acegi annotations.
 * access.
 * @author Enrico Giurin
 *
 */
public interface AjaxBo {
	
	
	@Secured ({"ROLE_JUGGER"})
	String requireReliabilityOnExistingJugger(String emailJugger,
            String motivation, String baseURL);

}
