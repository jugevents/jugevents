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
    public static final String DONE_VIEW =
            "redirect:/adminjugger/juggersearch.form";
    public static final String FORM_REQUEST_URI = "/adminjugger/reliability.form";
    public static final String FORM_VIEW = "jugger/admin/reliability";
    public static final String MODEL_ATTRIBUTE = "jugger";
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
        req.setRequestURI(FORM_REQUEST_URI);
        req.setParameter("reliability", "1.0");
        ModelAndView mv = handler.handle(req, res, controller);
        assertEquals(DONE_VIEW, mv.getViewName());
        Object reqModel = mv.getModel().get(MODEL_ATTRIBUTE);
        assertNotNull(reqModel);
        assertEquals(1.0, ((Jugger)reqModel).getReliability());
        Object sesModel = req.getSession().getAttribute(MODEL_ATTRIBUTE);
        assertNull("The jugger attribute should have been removed from the session", sesModel);
    }

    public void testUpdateFailedForValidation() throws Exception {
        callForm("fabrizio");        
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI(FORM_REQUEST_URI);
        req.setParameter("reliability", "1.1");
        ModelAndView mv = handler.handle(req, res, controller);
        assertEquals(FORM_VIEW, mv.getViewName());
        Object sesModel = req.getSession().getAttribute(MODEL_ATTRIBUTE);
        assertNotNull("The jugger attribute shouldn't have been removed from the session", sesModel);
        this.endTransaction();
        this.startNewTransaction();
        Jugger jugger = juggerDao.searchByUsername("fabrizio");
        assertEquals(0.0, jugger.getReliability());        
    }
    
    public void testForm() throws Exception {
        ModelAndView mv = callForm("enrico");
        Object reqModel = mv.getModel().get(MODEL_ATTRIBUTE);
        assertNotNull(reqModel);
        assertTrue("The object is not of type Jugger", reqModel instanceof Jugger);
        Object sesModel = req.getSession().getAttribute(MODEL_ATTRIBUTE);
        assertNotNull(sesModel);
        assertTrue("The object is not of type Jugger", sesModel instanceof Jugger);
        assertSame(reqModel, sesModel);
        assertEquals("enrico", ((Jugger)reqModel).getUser().getUsername());
        assertEquals(FORM_VIEW, mv.getViewName());
    }
    
    private ModelAndView callForm(String username) throws Exception {
        req.setMethod("GET");
        req.setRequestURI(FORM_REQUEST_URI);
        req.setParameter("jugger.user.username", username);
        ModelAndView mv =  handler.handle(req, res, controller);
        this.endTransaction();
        this.startNewTransaction();
        return mv;
    }
}
