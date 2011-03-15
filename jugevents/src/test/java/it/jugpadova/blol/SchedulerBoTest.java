package it.jugpadova.blol;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.dao.EventDao;
import it.jugpadova.dao.ParticipantDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Participant;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;

public class SchedulerBoTest extends JugEventsBaseTest {

    @Resource
    private SchedulerBo schedulerBo;
    @Resource
    private EventDao eventDao;
    @Resource
    private ParticipantDao participantDao;

    public void testRemindToParticipants() {
        Event futureEvent = retrieveFutureEvent();

        futureEvent.setActiveReminder(true);

        Date currentDate = new Date();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(currentDate);
        cal.add(GregorianCalendar.MINUTE, 1);
        futureEvent.setStartDate(cal.getTime());
        futureEvent.updateReminderDate();

        assertTrue(futureEvent.getReminderDate().compareTo(futureEvent.
                getStartDate()) < 0);
        assertTrue(currentDate.compareTo(futureEvent.getStartDate()) < 0);

        List<Participant> partcipantsToRemind = participantDao.
                findParticipantsToBeReminded();
        assertEquals(2, partcipantsToRemind.size());

        schedulerBo.remindToParticipants();
        partcipantsToRemind = participantDao.findParticipantsToBeReminded();
        assertEquals(0, partcipantsToRemind.size());
    }

    private Event retrieveFutureEvent() {
        List<Event> futureEvent = eventDao.findByTitle("Future Meeting");
        assertSize(1, futureEvent);
        return futureEvent.get(0);
    }

    /**
     * Checking if the class is AOP/CGLIB instrumented. It's needed for
     * annotation-based transaction management.
     */
    public void testIsInstrumented() {
        assertFalse(schedulerBo.getClass().getName().equals(SchedulerBo.class.
                getName()));
    }
}
