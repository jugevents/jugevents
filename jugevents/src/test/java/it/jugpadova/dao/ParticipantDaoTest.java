/*
 *  Copyright 2009 Jug Events Team.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package it.jugpadova.dao;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.po.Event;
import it.jugpadova.po.Participant;
import java.util.List;
import javax.annotation.Resource;

/**
 * Tests on the ParticipantDao methods (and queries)
 *
 * @author Lucio Benfante
 */
public class ParticipantDaoTest extends JugEventsBaseTest {

    @Resource
    private ParticipantDao participantDao;
    @Resource
    private EventDao eventDao;

    /**
     * Test of findConfirmedParticipantsByEventId method, of class ParticipantDao.
     */
    public void testFindConfirmedParticipantsByEventId() {
        List<Event> futureEvent = retrieveFutureEvent();
        List<Participant> participants =
                participantDao.findConfirmedParticipantsByEventId(futureEvent.
                get(0).getId());
        assertSize(2, participants);
    }

    /**
     * Test of findNotConfirmedParticipantsByEventId method, of class ParticipantDao.
     */
    public void testFindNotConfirmedParticipantsByEventId() {
        List<Event> futureEvent = retrieveFutureEvent();
        List<Participant> participants =
                participantDao.findNotConfirmedParticipantsByEventId(futureEvent.get(0).
                getId());
        assertSize(2, participants);
    }

    /**
     * Test of findCancelledParticipantsByEventId method, of class ParticipantDao.
     */
    public void testFindCancelledParticipantsByEventId() {
        List<Event> futureEvent = retrieveFutureEvent();
        List<Participant> participants = participantDao.
                findCancelledParticipantsByEventId(futureEvent.get(0).getId());
        assertSize(2, participants);
    }

    /**
     * Test the consistency of the lists of participants.
     *
     */
    public void testParticipantListsConsistency() {
        List<Event> futureEvent = retrieveFutureEvent();
        Long eventId = futureEvent.get(0).getId();
        List<Participant> all = participantDao.findParticipantsByEventId(eventId);
        List<Participant> confirmed =
                participantDao.findConfirmedParticipantsByEventId(eventId);
        List<Participant> notConfirmed =
                participantDao.findNotConfirmedParticipantsByEventId(eventId);
        List<Participant> cancelled = participantDao.
                findCancelledParticipantsByEventId(eventId);
        assertEquals(all.size(), confirmed.size()+notConfirmed.size()+cancelled.size());
    }

    /**
     * Test of findPresentParticipantsByEventId method, of class ParticipantDao.
     */
    public void testFindPresentParticipantsByEventId() {
    }

    /**
     * Test of findWinningParticipantsByEventId method, of class ParticipantDao.
     */
    public void testFindWinningParticipantsByEventId() {
    }

    /**
     * Test of findNonwinningParticipantsByEventId method, of class ParticipantDao.
     */
    public void testFindNonwinningParticipantsByEventId() {
    }

    private List<Event> retrieveFutureEvent() {
        List<Event> futureEvent = eventDao.findByTitle("Future Meeting");
        assertSize(1, futureEvent);
        return futureEvent;
    }
}
