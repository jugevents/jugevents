package it.jugpadova.blo.ajax;

import it.jugpadova.Blos;
import it.jugpadova.blo.ServicesBo;

/**
 * This class operates as a facade for the methods defined in the BO.
 * Manages security concerns using aop and acegi annotations, in order to
 * block unauthorized access to BO methods.
 * @author Enrico Giurin
 *
 */
public class SecuredAjaxBo implements AjaxBo {
	private Blos blos = null;

	

	public Blos getBlos() {
		return blos;
	}



	public void setBlos(Blos blos) {
		this.blos = blos;
	}



	public String requireReliabilityOnExistingJugger(String emailJugger,
			String motivation, String baseURL) {
		return blos.getServicesBo().requireReliabilityOnExistingJugger(emailJugger, motivation, baseURL);
	}

}
