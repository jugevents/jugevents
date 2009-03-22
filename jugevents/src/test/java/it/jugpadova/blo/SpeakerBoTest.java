package it.jugpadova.blo;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.dao.EventDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class SpeakerBoTest extends JugEventsBaseTest {

	@Autowired
    private EventDao eventDao;
	@Autowired
    private SpeakerBo speakerBo;

	public void testSaveSpeakers() {
		
		Event springEvent = getTestEvent();
		List<Speaker> speakers = springEvent.getSpeakers();
		assertEquals(2, speakers.size());
		speakers.remove(0);
		speakers.remove(0);
		assertEquals(0, speakers.size());
		speakerBo.saveSpeakers(springEvent.getId(), springEvent.getSpeakers());
		springEvent = getTestEvent();
		assertEquals(0, springEvent.getSpeakers().size());		
	}

	private Event getTestEvent()
	{
		return eventDao.findByTitle("Springframework%").get(0);
	}

}
