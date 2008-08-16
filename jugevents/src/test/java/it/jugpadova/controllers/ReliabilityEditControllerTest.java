package it.jugpadova.controllers;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.po.Jugger;
import javax.servlet.http.HttpServletRequest;
import org.parancoe.web.test.ControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;

/**
 * @author Enrico Giurin
 *
 */
public class ReliabilityEditControllerTest extends JugEventsBaseTest {
    @Autowired
    private ReliabilityEditController controller;
    @Autowired
    private JuggerDao juggerDao;

    public void testConfiguration() {
        assertNotNull(controller);
    }
    
    public void testUpdate() {
        HttpServletRequest req = new MockHttpServletRequest("POST", "/adminjugger/reliability.form");
        Jugger jugger = juggerDao.findByEmail("fabrizio@gianneschi.it");
        BindingResult result = new BeanPropertyBindingResult(jugger, "jugger");
        SessionStatus status = new SimpleSessionStatus();
        String view = controller.update(req, jugger, result, status);
        assertEquals("redirect:/adminjugger/juggersearch.form", view);
    }
    
//    public void testGET() throws Exception {
//        req = new MockHttpServletRequest("GET", "/adminjugger/reliability.form");
//        req.setParameter("jugger.user.username", "enrico");
//        ModelAndView mv = controller.handleRequest(req, res);
//
//    }
}
