// Copyright 2006-2008 The Parancoe Team
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package it.jugpadova.dao;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;
import it.jugpadova.po.SpeakerCoreAttributes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Enrico Giurin
 *
 */
public class SpeakerDaoTest extends JugEventsBaseTest {
    @Autowired
    private SpeakerDao speakerDao;
    @Autowired
    private SpeakerCoreAttributesDao speakerCoreAttributesDao;
    
    public void testFindAll() {
        List<Speaker> speakers = speakerDao.findAll();
        assertEquals(speakers.size(), 3);
    }
    
    public void testFindEventsByPartialSpeakerEmail()
    {
    	 List<Event> events = speakerDao.findEventsByPartialSpeakerEmail("lucio%");
         assertEquals(2, events.size());       	 
    }
    
    public void testFindSpeakersByPartialEventTitle()
    {
    	List<Speaker> speakers = speakerDao.findSpeakersByPartialEventTitle("Springframework%");
    	assertEquals(2, speakers.size());
    	Speaker speakerLucio = speakerDao.findByResume("%is the president%").get(0);
    	Speaker speakerEnrico = speakerDao.findByResume("%electronic engineer%").get(0);
    	assertTrue(speakers.contains(speakerLucio)); 
    	assertTrue(speakers.contains(speakerEnrico)); 
    	assertEquals("enricogiurin@gmail.com", speakerEnrico.getSpeakerCoreAttributes().getEmail());
    	assertEquals("http://benfante.blogspot.com/", speakerLucio.getSpeakerCoreAttributes().getUrl());
    	assertEquals("benfante", speakerLucio.getSpeakerCoreAttributes().getSkypeId());
    }
    
    public void testInsertDuplicateKey()
    {
    	SpeakerCoreAttributes duplicateSpeaker = new SpeakerCoreAttributes("enrico", "giurin", "aaa@bbb.com");    	
    	//I was expecting this method would throw an exception, not only logging the error
    	speakerCoreAttributesDao.store(duplicateSpeaker);
    	//TODO I cannot check any results as I have an hibernate exception
    	//assertEquals(1, speakerCoreAttributesDao.findByFirstNameAndLastName("enrico", "giurin").size());
    	//TODO check why the method findAll returns null...
    	//assertEquals(2, speakerCoreAttributesDao.findAll().size());
    }
			
		

}
