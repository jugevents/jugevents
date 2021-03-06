package it.jugpadova.blo;

import it.jugpadova.JugEventsBaseTest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Thest on the EventBo class
 * 
 * @author Lucio Benfante
 *
 */
public class EventBoTest extends JugEventsBaseTest {

    @Autowired
    private EventBo eventBo;

    public void testGetBadgeCode() {
        String result =
                eventBo.getBadgeCode(
                "<div class=\"jeb_event_even\"><div class=\"jeb_date\"><span class=\"jeb_text\">26-apr-2008</span></div><div class=\"jeb_title\"><span class=\"jeb_text\"><a href=\"http://localhost:8081/jugevents/event/show.html?id=1\">Meeting #50</a></span></div><div class=\"jeb_description\"><span class=\"jeb_text\"><p>Meeting dei JUG Padova\n<br />\r<br />Prova\r\n<br />\n<br />prova\n<br />\\n</p></span></div></div>");
        String expected =
                "var badge='';\nbadge += '<div class=\"jeb_event_even\"><div class=\"jeb_date\"><span class=\"jeb_text\">26-apr-2008</span></div><div class=\"jeb_title\"><span class=\"jeb_text\"><a href=\"http://localhost:8081/jugevents/event/show.html?id=1\">Meeting #50</a></span></div><div class=\"jeb_description\"><span class=\"jeb_text\"><p>Meeting dei JUG Padova\\n<br />\\n<br />Prova\\n<br />\\n<br />prova\\n<br />\\n</p></span></div></div>';\ndocument.write(badge);";
        assertEquals(expected, result);
    }

    public void testFindPartialLocation() {
        List locations = eventBo.findPartialLocation("DEI", "lucio");
        assertSize(2, locations);
    }    
    
}
