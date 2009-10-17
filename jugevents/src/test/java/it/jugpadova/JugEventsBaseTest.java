package it.jugpadova;

import it.jugpadova.po.Event;
import it.jugpadova.po.EventLink;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Jugger;
import it.jugpadova.po.Participant;
import it.jugpadova.po.ReliabilityRequest;

import it.jugpadova.po.Speaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import org.parancoe.plugins.security.Authority;
import org.parancoe.plugins.security.User;
import org.parancoe.plugins.world.Continent;
import org.parancoe.plugins.world.Country;
import org.parancoe.web.test.BaseTest;

/**
 * Base test class for test of JUG Events.
 *
 * @author lucio
 */
public abstract class JugEventsBaseTest extends BaseTest {
    private static final Logger logger = Logger.getLogger(
            JugEventsBaseTest.class);

    @Override
    public Class[] getFixtureClasses() {
        return new Class[]{Continent.class, Country.class,  Authority.class, User.class, JUG.class, ReliabilityRequest.class, Jugger.class, Event.class, EventLink.class, Participant.class,  Speaker.class};
    }

    protected void writeTestFile(String prefix, String suffix, byte[] bytes)
            throws IOException {
        final File tempFile = File.createTempFile(prefix, suffix);
        OutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
            out.write(bytes);
            out.flush();
            logger.info("Wrote test file " + tempFile.getAbsolutePath());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException iOException) {
                }
            }
        }
    }

}
