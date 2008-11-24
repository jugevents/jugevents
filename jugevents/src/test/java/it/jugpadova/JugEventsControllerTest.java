package it.jugpadova;

import it.jugpadova.mock.MockServicesBo;
import it.jugpadova.po.Event;
import it.jugpadova.po.EventLink;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Jugger;
import it.jugpadova.po.Participant;
import it.jugpadova.po.ReliabilityRequest;
import it.jugpadova.po.Speaker;
import it.jugpadova.po.SpeakerCoreAttributes;
import org.parancoe.plugins.security.Authority;
import org.parancoe.plugins.security.User;
import org.parancoe.plugins.world.Continent;
import org.parancoe.plugins.world.Country;
import org.parancoe.web.test.ControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerAdapter;

/**
 * Base test class for test of JUG Events controllers.
 *
 * @author lucio
 */
public abstract class JugEventsControllerTest extends ControllerTest {
    @Autowired
    @Qualifier("methodHandler")
    protected HandlerAdapter handler;
    @Autowired
    protected MockServicesBo servicesBo;

    @Override
    public void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        servicesBo.setAuthenticatedUsername(null);
    }
    
    @Override
    public Class[] getFixtureClasses() {
//        return new Class[]{Continent.class, Country.class, User.class, Authority.class, JUG.class, ReliabilityRequest.class, Jugger.class, Event.class, EventLink.class, Participant.class};
        return new Class[]{Continent.class, Country.class,  Authority.class, User.class, JUG.class, ReliabilityRequest.class, Jugger.class, Event.class, EventLink.class, Participant.class, SpeakerCoreAttributes.class, Speaker.class};
    }
}
