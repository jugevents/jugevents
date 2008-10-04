package it.jugpadova;

import it.jugpadova.blo.EventBo;
import it.jugpadova.blo.FilterBo;
import it.jugpadova.blo.JuggerBo;
import it.jugpadova.dao.EventDao;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.dao.ParticipantDao;
import org.parancoe.web.test.BaseTest;

public class SanityTest extends BaseTest {

    public SanityTest() {
    }

    public void testDaos() {
        Object daos = getApplicationContext().getBean("daos");
        assertNotNull("Can't retrieve daos bean", daos);
        assertTrue("Wrong class for the daos bean (" + daos.getClass().getName() +
                ")", daos instanceof Daos);

        Object eventDao = getApplicationContext().getBean("eventDao");
        assertNotNull("Can't retrieve eventDao bean", eventDao);
        assertTrue("Wrong class for the eventDao bean (" +
                eventDao.getClass().getName() + ")",
                eventDao instanceof EventDao);

        Object juggerDao = getApplicationContext().getBean("juggerDao");
        assertNotNull("Can't retrieve juggerDao bean", juggerDao);
        assertTrue("Wrong class for the juggerDao bean (" +
                juggerDao.getClass().getName() + ")",
                juggerDao instanceof JuggerDao);

        Object participantDao = getApplicationContext().getBean("participantDao");
        assertNotNull("Can't retrieve participantDao bean", participantDao);
        assertTrue("Wrong class for the participantDao bean (" +
                participantDao.getClass().getName() + ")",
                participantDao instanceof ParticipantDao);
    }

//    public void testBlos() {
//        Object blos = getApplicationContext().getBean("blos");
//        assertNotNull("Can't retrieve blos bean", blos);
//        assertTrue("Wrong class for the blos bean (" + blos.getClass().getName() +
//                ")", blos instanceof Blos);
//
//        Object eventBo = getApplicationContext().getBean("eventBo");
//        assertNotNull("Can't retrieve eventBo bean", eventBo);
//        assertTrue("Wrong class for the eventBo bean (" +
//                eventBo.getClass().getName() + ")",
//                eventBo instanceof EventBo);
//
//        Object juggerBo = getApplicationContext().getBean("juggerBo");
//        assertNotNull("Can't retrieve juggerBo bean", juggerBo);
//        assertTrue("Wrong class for the juggerBlo bean (" +
//                juggerBo.getClass().getName() + ")",
//                juggerBo instanceof JuggerBo);
//
//        Object filterBo = getApplicationContext().getBean("filterBo");
//        assertNotNull("Can't retrieve filterBO bean", filterBo);
//        assertTrue("Wrong class for the filterOo bean (" +
//                filterBo.getClass().getName() + ")",
//                filterBo instanceof FilterBo);
//        checkSpringBean("eventResourceBo");
//    }

    public void testDaosThroughDaos() {
        Object odaos = getApplicationContext().getBean("daos");
        assertNotNull("Can't retrieve daos bean", odaos);
        assertTrue("Wrong class for the daos bean (" + odaos.getClass().
                getName() + ")", odaos instanceof Daos);
        Daos daos = (Daos) odaos;
        assertNotNull(daos.getEventDao());
        assertNotNull(daos.getJuggerDao());
        assertNotNull(daos.getParticipantDao());
    }

//    public void testBlosThroughBlos() {
//        Object oblos = getApplicationContext().getBean("blos");
//        assertNotNull("Can't retrieve blos bean", oblos);
//        assertTrue("Wrong class for the blos bean (" + oblos.getClass().
//                getName() + ")", oblos instanceof Blos);
//        Blos blos = (Blos) oblos;
//        assertNotNull(blos.getEventBo());
//        assertNotNull(blos.getJuggerBO());
//        assertNotNull(blos.getJuggerBO());
//        assertNotNull(blos.getFilterBo());
//    }

    public void testControllers() {
        checkSpringBean("addParticipantController");
        checkSpringBean("adminController");
        checkSpringBean("binController");
        checkSpringBean("confirmController");
        checkSpringBean("eventController");
        checkSpringBean("eventEditController");
        checkSpringBean("eventSearchController");
        checkSpringBean("homeController");
        checkSpringBean("JCaptchaController");
        checkSpringBean("juggerAdminController");
        checkSpringBean("juggerChangePasswordController");
        checkSpringBean("juggerController");
        checkSpringBean("juggerEditController");
        checkSpringBean("juggerEnableController");
        checkSpringBean("juggerRegistrationController");
        checkSpringBean("juggerSearchController");
        checkSpringBean("participantRegistrationController");
        checkSpringBean("passwordRecoveryController");
        checkSpringBean("reliabilityEditController");
        checkSpringBean("serviceController");
    }
    /**
     * Checks the correct configuration for the security.
     */
    public void testSecurity() {
    	/*
    	DaoAuthenticationProvider dap = (DaoAuthenticationProvider)getApplicationContext().getBean("daoAuthenticationProvider");
    	//assertTrue(dap.getPasswordEncoder() instanceof Md5PasswordEncoder);
    	//System.out.println(dap.getPasswordEncoder().getClass().getName());
    	String pluginSecurityFilterDefinitions = (String)getApplicationContext().getBean("pluginSecurityFilterDefinitions");
    	assertTrue(pluginSecurityFilterDefinitions.contains("/adminjugger/**=ROLE_ADMIN"));
       */
    }

    private void checkSpringBean(String id) {
        assertNotNull("Il bean con id '" + id +
                "' non è stato configurato in Spring", getApplicationContext().getBean(id));
    }
}
