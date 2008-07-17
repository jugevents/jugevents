package it.jugpadova.blo;

import it.jugpadova.Conf;
import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.exception.UserAlreadyEnabledException;

import org.parancoe.plugins.security.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Enrico Giurin
 *
 */
public class JuggerBoTest extends JugEventsBaseTest {

    @Autowired
    private JuggerBo juggerBo;
    @Autowired
    private Conf appConf;

    public void testNewUser() {
        try {
            User user = juggerBo.newUser("new user");
            assertEquals("new user", user.getUsername());
        } catch (Exception e) {
            assertTrue(e instanceof UserAlreadyEnabledException);
        }

    }//end of method

    public void testThresholdAccess() {
        assertEquals(1, 0d, appConf.getThresholdAccess());
    }
}
