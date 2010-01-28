// Copyright 2006-2007 The Parancoe Team
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

import it.jugpadova.po.Participant;

import java.util.List;

import org.parancoe.persistence.dao.generic.Dao;
import org.parancoe.persistence.dao.generic.GenericDao;


@Dao(entity = Participant.class)
public interface ParticipantDao extends GenericDao<Participant, Long> {

     List<Participant> findByEmailAndConfirmationCodeAndConfirmed(String email, String confirmationCode, Boolean confirmed);
     Participant findByEmailAndConfirmationCode(String email, String confirmationCode);

     List<Participant> findConfirmedParticipantsByEventId(Long id);
     List<Participant> findConfirmedParticipantsByEventIdOrderByLastNameAndFirstName(Long id);
     List<Participant> findNotConfirmedParticipantsByEventId(Long id);
     List<Participant> findCancelledParticipantsByEventId(Long id);
     List<Participant> findParticipantByEmailAndEventId(String email, Long eventId);
     List<Participant> findParticipantsByEventId(Long eventId);
     List<Participant> findPresentParticipantsByEventId(Long eventId);

     List<Participant> findWinningParticipantsByEventId(Long eventId);

     List<Participant> findNonwinningParticipantsByEventId(Long eventId);
    
    List<Participant> findParticipantsToBeReminded();
}