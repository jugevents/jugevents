/**
 * 
 */
package it.jugpadova.dao;
import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;



/**
 * @author Enrico Giurin
 *
 */
public class EventDaoTest extends JugEventsBaseTest {
	@Autowired
    private EventDao eventDao;
	
	public void testFindByTitle() {
		List<Event> events = eventDao.findByTitle("Springframework%");
		assertEquals(1, events.size());
		Event eventSF = events.get(0);
		List<Speaker> speakers = eventSF.getSpeakers();
		assertEquals(2, speakers.size());
		for (Speaker speaker : speakers) {
			System.out.println(speaker);
		}
		
	}

}
