package it.jugpadova.controllers;

import it.jugpadova.JugEventsControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class HomeControllerTest extends JugEventsControllerTest {
    
    @Autowired
    private HomeController controller;
        
    public void testNotNull() {
        assertNotNull(controller);
        assertNotNull(handler);
    }
    
    public void testWelcome() throws Exception {
        req.setRequestURI("/home/welcome.html");
        ModelAndView mv = handler.handle(req, res, controller);
        assertEquals("welcome", mv.getViewName());
        assertNotNull(mv.getModel().get("something"));
    }
}
