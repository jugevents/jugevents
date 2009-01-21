/**
 * 
 */
package it.jugpadova.blo;

import it.jugpadova.dao.SpeakerCoreAttributesDao;
import it.jugpadova.dao.SpeakerDao;
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
	private SpeakerCoreAttributesDao speakerCoreAttributesDao;
	
	
	public void save(Speaker speaker) {
        boolean isNew = false;
        if (speaker.getId() == null) {
            isNew = true;
        }
        speakerCoreAttributesDao.store(speaker.getSpeakerCoreAttributes());
        speakerDao.store(speaker);
    }


}
