// Copyright 2006-2008 The JUG Events Team
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
/**
 * 
 */
package it.jugpadova.dao;

import java.util.List;

import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;

import org.parancoe.persistence.dao.generic.Compare;
import org.parancoe.persistence.dao.generic.CompareType;
import org.parancoe.persistence.dao.generic.Dao;
import org.parancoe.persistence.dao.generic.GenericDao;

/**
 * DAO associated to Speaker po.
 * @author Enrico Giurin
 *
 */
@Dao(entity=Speaker.class)
public interface SpeakerDao extends GenericDao<Speaker, Long> {
	/**
	 * Retrieves all the events in which the speaker, identified by partial email, has been teacher.
	 * @param speakerEmail
	 * @return
	 */
	List<Event>  findEventsByPartialSpeakerEmail(String speakerEmail);
	
	/**
	 * Retrieves all the speakers  which have presented in events
	 * identified by partial eventTitle.
	 * @param eventTitle
	 * @return
	 */
	List<Speaker> findSpeakersByPartialEventTitle(String eventTitle);
	/**
	 * Retrieves all the speakers identified by a partial resume.
	 * @param resume
	 * @return
	 */
	List<Speaker> findByResume(@Compare(CompareType.ILIKE) String resume);

}
