/**
 * 
 */
package it.jugpadova.controllers;

import java.util.EnumSet;

import org.parancoe.web.test.ControllerTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Enrico Giurin
 *
 */
public class ReliabilityEditControllerTest extends ControllerTest {
	private ReliabilityEditController controller = null;
	  
	
	
	 @Override
	    public void setUp() throws Exception {
	        super.setUp();    // non togliere questa riga
	        controller = (ReliabilityEditController) ctx.getBean("reliabilityEditController");
	    }
	 
	 
	 public void testGET() throws Exception {
	        req = new MockHttpServletRequest("GET", "/adminjugger/reliability.form");
	        req.setParameter("jugger.user.username", "enrico");
	        ModelAndView mv = controller.handleRequest(req, res);
	        EnumSet es =(EnumSet)req.getAttribute("statusList");
	        assertTrue(es.size()==5);
	        
	    }

}
