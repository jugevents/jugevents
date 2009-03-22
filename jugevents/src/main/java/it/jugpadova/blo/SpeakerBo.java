/**
 * 
 */
package it.jugpadova.blo;

import java.util.List;

import it.jugpadova.dao.EventDao;

import it.jugpadova.dao.SpeakerDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business logic for the speaker management.
 * @author Enrico Giurin
 *
 */
@Component
public class SpeakerBo {
	@Autowired
	private SpeakerDao speakerDao;
	
	@Autowired
    private EventDao eventDao;
	
	
	private void save(Speaker speaker) {      
        speakerDao.store(speaker);
    }
	
	public void saveSpeakers(Long  eventId, List<Speaker> speakers)
	{
		//remove all the stored speakers		
		List<Speaker> storedSpeakers = speakerDao.allByEvent(eventId);
		for (Speaker speaker : storedSpeakers) {
			speakerDao.delete(speaker);
		}
		//update the new speakers		
		if(speakers == null)
			return;
		Event event = eventDao.get(eventId);
		for (Speaker speaker : speakers) {
			speaker.setEvent(event);
			save(speaker);
		}
	}
	
	


}
