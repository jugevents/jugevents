package it.jugpadova;

import it.jugpadova.blo.EventBo;
import it.jugpadova.blo.JuggerBlo;
import it.jugpadova.dao.EventDao;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.dao.ParticipantDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Jugger;
import org.parancoe.plugins.security.Authorities;
import org.parancoe.plugins.security.UserProfile;
import org.parancoe.web.test.BaseTest;

public class SanityTest extends BaseTest {
    public SanityTest() {
        super();
    }

    public void testDaos() {
        Object daos = ctx.getBean("daos");
        assertNotNull("Can't retrieve daos bean", daos);
        assertTrue("Wrong class for the daos bean ("+daos.getClass().getName()+")", daos instanceof Daos);

        Object eventDao = ctx.getBean("eventDao");
        assertNotNull("Can't retrieve eventDao bean", eventDao);
        assertTrue("Wrong class for the eventDao bean ("+eventDao.getClass().getName()+")", eventDao instanceof EventDao);

        Object juggerDao = ctx.getBean("juggerDao");
        assertNotNull("Can't retrieve juggerDao bean", juggerDao);
        assertTrue("Wrong class for the juggerDao bean ("+juggerDao.getClass().getName()+")", juggerDao instanceof JuggerDao);

        Object participantDao = ctx.getBean("participantDao");
        assertNotNull("Can't retrieve participantDao bean", participantDao);
        assertTrue("Wrong class for the participantDao bean ("+participantDao.getClass().getName()+")", participantDao instanceof ParticipantDao);
        
    }

    public void testBlos() {
        Object blos = ctx.getBean("blos");
        assertNotNull("Can't retrieve blos bean", blos);
        assertTrue("Wrong class for the blos bean ("+blos.getClass().getName()+")", blos instanceof Blos);

        Object eventBo = ctx.getBean("eventBO");
        assertNotNull("Can't retrieve eventBo bean", eventBo);
        assertTrue("Wrong class for the eventBo bean ("+eventBo.getClass().getName()+")", eventBo instanceof EventBo);

        Object juggerBo = ctx.getBean("juggerBlo");
        assertNotNull("Can't retrieve juggerBlo bean", juggerBo);
        assertTrue("Wrong class for the juggerBlo bean ("+juggerBo.getClass().getName()+")", juggerBo instanceof JuggerBlo);        
    }

    public void testDaosThroughDaos() {
        Object odaos = ctx.getBean("daos");
        assertNotNull("Can't retrieve daos bean", odaos);
        assertTrue("Wrong class for the daos bean ("+odaos.getClass().getName()+")", odaos instanceof Daos);
        Daos daos = (Daos)odaos;
        assertNotNull(daos.getEventDao());
        assertNotNull(daos.getJuggerDao());
        assertNotNull(daos.getParticipantDao());
    }

    public void testBlosThroughBlos() {
        Object oblos = ctx.getBean("blos");
        assertNotNull("Can't retrieve blos bean", oblos);
        assertTrue("Wrong class for the blos bean ("+oblos.getClass().getName()+")", oblos instanceof Blos);
        Blos blos = (Blos)oblos;
        assertNotNull(blos.getEventBo());
        assertNotNull(blos.getJuggerBO());
    }

    public void testControllers() {
        checkSpringBean("homeController");
        checkSpringBean("eventController");
        checkSpringBean("eventEditController");
        checkSpringBean("juggerController");
        checkSpringBean("registrationController");
        
    }

    private void checkSpringBean(String id) {
        assertNotNull("Il bean con id '" + id + "' non è stato configurato in Spring", ctx
                        .getBean(id));
    }

    @Override
    public Class[] getFixtureClasses() {
        return new Class[] {Jugger.class, UserProfile.class, Authorities.class, Event.class};
    }
}
