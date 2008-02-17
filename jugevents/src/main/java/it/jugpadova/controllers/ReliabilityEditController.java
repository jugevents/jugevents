/**
 * 
 */
package it.jugpadova.controllers;

import java.util.EnumMap;
import java.util.EnumSet;

import it.jugpadova.Blos;
import it.jugpadova.Daos;
import it.jugpadova.bean.EditJugger;
import it.jugpadova.po.Jugger;
import it.jugpadova.po.ReliabilityRequest;
import it.jugpadova.util.RRStatus;
import it.jugpadova.util.Utilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.parancoe.web.BaseFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Enrico Giurin
 *
 */
public abstract class ReliabilityEditController extends BaseFormController {

	/* (non-Javadoc)
	 * @see org.parancoe.web.BaseFormController#getLogger()
	 */
	@Override
	public Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		Jugger jugger = (Jugger)command;
		//ReliabilityRequest rr = jugger.getReliabilityRequest();
		blo().getServicesBo().updateReliability(jugger, Utilities.getBaseUrl(request));
		return onSubmit(command, errors);
			
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		String username = request.getParameter("jugger.user.username");        
        Jugger jugger = blo().getJuggerBO().searchByUsername(username);        
        request.setAttribute("statusList", EnumSet.allOf(RRStatus.class));
        return jugger;
	}   
	
	protected abstract Daos dao();

    protected abstract Blos blo();

}
