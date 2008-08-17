package it.jugpadova.controllers;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.po.Jugger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.parancoe.web.annotation.ParancoeAnnotationMethodHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Enrico Giurin
 *
 */
public class ReliabilityEditControllerTest extends JugEventsBaseTest {
    @Autowired
    private ReliabilityEditController controller;
    @Autowired
    private JuggerDao juggerDao;
    @Autowired
    private ParancoeAnnotationMethodHandlerAdapter methodHandler;

    public void testConfiguration() {
        assertNotNull(controller);
    }
    
    public void testUpdate() throws Exception {
        HttpServletRequest req = new MockHttpServletRequest("POST", "/adminjugger/reliability.form");
        HttpServletResponse res = new MockHttpServletResponse();
        Jugger jugger = juggerDao.findByEmail("fabrizio@gianneschi.it");
        req.getSession().setAttribute("jugger", jugger);
        ModelAndView mv = methodHandler.handle(req, res, controller);
        assertEquals("redirect:/adminjugger/juggersearch.form", mv.getViewName());
    }
    
    public void testForm() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/adminjugger/reliability.form");
        req.setParameter("jugger.user.username", "enrico");
        HttpServletResponse res = new MockHttpServletResponse();
        ModelAndView mv = methodHandler.handle(req, res, controller);
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
}
