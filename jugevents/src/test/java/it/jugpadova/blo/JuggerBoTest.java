/**
 * 
 */
package it.jugpadova.blo;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.exception.UserAlreadyEnabledException;

import org.parancoe.plugins.security.User;

/**
 * @author Enrico Giurin
 *
 */
public class JuggerBoTest extends JugEventsBaseTest {
	private JuggerBo juggerBo;
	private ServicesBo servicesBo;

    public JuggerBoTest() {
    	juggerBo = (JuggerBo) ctx.getBean("juggerBo");    
    	servicesBo = (ServicesBo) ctx.getBean("servicesBo"); 
    }
    public void testNewUser() 
    {
    	try {
    		User user =juggerBo.newUser("new user");    		
    		assertEquals("new user", user.getUsername());
		} catch (Exception e) {
			assertTrue(e instanceof UserAlreadyEnabledException);
		}
    	
    }//end of method
    
    public void testThresholdAccess()
    {
    	assertEquals(1,0d, servicesBo.getThresholdAccess());       	
    }

}
