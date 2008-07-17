package it.jugpadova.controllers;

import org.parancoe.web.test.ControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Enrico Giurin
 *
 */
public class ReliabilityEditControllerTest extends ControllerTest {
    @Autowired
    private ReliabilityEditController controller;

    public void testConfiguration() {
        assertNotNull(controller);
    }
    
//    public void testGET() throws Exception {
//        req = new MockHttpServletRequest("GET", "/adminjugger/reliability.form");
//        req.setParameter("jugger.user.username", "enrico");
//        ModelAndView mv = controller.handleRequest(req, res);
//
//    }
}
