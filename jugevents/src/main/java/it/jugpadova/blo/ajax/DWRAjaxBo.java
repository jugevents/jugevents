package it.jugpadova.blo.ajax;

/**
 * DWR  implementation of AjaxBo. This class has been modelled like a facade to
 * forward call to the SecuredAjaxBo.
 * @author Enrico Giurin
 *
 */
public class DWRAjaxBo implements AjaxBo {
	private AjaxBo ajaxBo = null;

	public AjaxBo getAjaxBo() {
		return ajaxBo;
	}

	public void setAjaxBo(AjaxBo ajaxBo) {
		this.ajaxBo = ajaxBo;
	}

	public String requireReliabilityOnExistingJugger(String emailJugger,
			String motivation, String baseURL) {
		
		return ajaxBo.requireReliabilityOnExistingJugger(emailJugger, motivation, baseURL);
	}

}
