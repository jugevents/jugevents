/**
 * 
 */
package it.jugpadova.controllers;

import it.jugpadova.Blos;
import it.jugpadova.Daos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.parancoe.web.BaseMultiActionController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for jugger administration functionalities.
 * 
 * @author Enrico Giurin
 * 
 */
public abstract class JuggerAdminController extends BaseMultiActionController {
	private static Logger logger = Logger
			.getLogger(JuggerAdminController.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.parancoe.web.BaseMultiActionController#getLogger()
	 */
	@Override
	public Logger getLogger() {
		// TODO Auto-generated method stub
		return logger;
	}

	
	/**
	 * Delete jugger.
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) {
		String username = req.getParameter("username");
		blo().getJuggerBO().delete(username);
		return new ModelAndView("redirect:/adminjugger/juggersearch.form");

	}

	/**
	 * Jugger details
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView viewJugger(HttpServletRequest req,
			HttpServletResponse res) {
		String username = req.getParameter("username");
		ModelAndView mv = new ModelAndView("jugger/admin/viewJugger");
		mv.addObject("jugger", blo().getJuggerBO().searchByUsername(username));
		return mv;
	}

	/**
	 * Enable Jugger
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView enableJugger(HttpServletRequest req,
			HttpServletResponse res) {
		String username = req.getParameter("username");
		blo().getJuggerBO().enableJugger(username);
		return new ModelAndView("redirect:/adminjugger/juggersearch.form");

	}

	/**
	 * Disable jugger.
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView disableJugger(HttpServletRequest req,
			HttpServletResponse res) {
		String username = req.getParameter("username");
		blo().getJuggerBO().disableJugger(username);
		return new ModelAndView("redirect:/adminjugger/juggersearch.form");

	}

	protected abstract Daos dao();

	protected abstract Blos blo();
}
