/**
 * 
 */
package it.jugpadova.blo;

import java.util.List;

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
       
        speakerCoreAttributesDao.store(speaker.getSpeakerCoreAttributes());
        speakerDao.store(speaker);
    }
	
	public void save(List<Speaker> speakerList)
	{
		if(speakerList == null)
			return;
		for (Speaker speaker : speakerList) {
			save(speaker);
		}
	}


}
