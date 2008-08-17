package it.jugpadova.controllers;

import it.jugpadova.JugEventsControllerTest;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.po.Jugger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Enrico Giurin
 *
 */
public class ReliabilityEditControllerTest extends JugEventsControllerTest {
    @Autowired
    private ReliabilityEditController controller;
    @Autowired
    private JuggerDao juggerDao;

    public void testConfiguration() {
        assertNotNull(controller);
    }
    
    public void testUpdate() throws Exception {
        callForm("fabrizio");        
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI("/adminjugger/reliability.form");
        req.setParameter("reliability", "1.0");
        ModelAndView mv = handler.handle(req, res, controller);
        assertEquals("redirect:/adminjugger/juggersearch.form", mv.getViewName());
        Object oJugger = mv.getModel().get("jugger");
        assertNotNull(oJugger);
        assertEquals(1.0, ((Jugger)oJugger).getReliability());
        Object sJugger = req.getSession().getAttribute("jugger");
        assertNull("The jugger attribute should have been removed from the session", sJugger);
    }

    public void testUpdateFailedForValidation() throws Exception {
        callForm("fabrizio");        
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI("/adminjugger/reliability.form");
        req.setParameter("reliability", "1.1");
        ModelAndView mv = handler.handle(req, res, controller);
        assertEquals("jugger/admin/reliability", mv.getViewName());
        Object sJugger = req.getSession().getAttribute("jugger");
        assertNotNull("The jugger attribute shouldn't have been removed from the session", sJugger);
        this.endTransaction();
        this.startNewTransaction();
        Jugger jugger = juggerDao.searchByUsername("fabrizio");
        assertEquals(0.0, jugger.getReliability());        
    }
    
    public void testForm() throws Exception {
        ModelAndView mv = callForm("enrico");
        Object oJugger = mv.getModel().get("jugger");
        assertNotNull(oJugger);
        assertTrue("The object is not of type Jugger", oJugger instanceof Jugger);
        Object sJugger = req.getSession().getAttribute("jugger");
        assertNotNull(sJugger);
        assertTrue("The object is not of type Jugger", sJugger instanceof Jugger);
        assertSame(oJugger, sJugger);
        assertEquals("enrico", ((Jugger)oJugger).getUser().getUsername());
        assertEquals("jugger/admin/reliability", mv.getViewName());
    }
    
    private ModelAndView callForm(String username) throws Exception {
        req.setMethod("GET");
        req.setRequestURI("/adminjugger/reliability.form");
        req.setParameter("jugger.user.username", username);
        ModelAndView mv =  handler.handle(req, res, controller);
        this.endTransaction();
        this.startNewTransaction();
        return mv;
    }
}
