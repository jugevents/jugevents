package it.jugpadova.controllers;

import org.parancoe.web.test.ControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class HomeControllerTest extends ControllerTest {
    
    @Autowired
    private HomeController controller;
        
    public void testNotNull() {
        assertNotNull(controller);
    }
    
    public void testWelcome() throws Exception {
        req = new MockHttpServletRequest("GET", "/home/welcome.html");
        ModelAndView mv = controller.welcome(req, res);
        assertEquals("welcome", mv.getViewName());
        assertNotNull(mv.getModel().get("something"));
    }
}
