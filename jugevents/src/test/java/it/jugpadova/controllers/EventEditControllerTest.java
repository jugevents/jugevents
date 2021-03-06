package it.jugpadova.controllers;

import it.jugpadova.JugEventsControllerTest;
import it.jugpadova.dao.EventDao;
import it.jugpadova.dao.SpeakerDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;

import java.util.Calendar;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * Tests on the EventEditController.
 * 
 * @author Lucio Benfante
 */
public class EventEditControllerTest extends JugEventsControllerTest {

    public static final String DONE_VIEW = "redirect:show.html?id=";
    public static final String FORM_REQUEST_URI = "/event/edit.form";
    public static final String REQUEST_REMOVE_SPEAKER_URI = "/event/removespeaker.form";
    public static final String FORM_VIEW = "event/edit";
    public static final String MODEL_ATTRIBUTE = "event";
    @Autowired
    private EventEditController controller;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private SpeakerDao speakerDao;
    

    public void testConfiguration() {
        assertNotNull(controller);
    }

    public void testUpdate() throws Exception {
        Event testEvent = getTestEvent();
        callForm(testEvent.getId().toString());
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI(FORM_REQUEST_URI);
        req.setParameter("startDate", "20/08/2008");
        ModelAndView mv = handler.handle(req, res, controller);
        assertEquals(DONE_VIEW + testEvent.getId(), mv.getViewName());
        Object reqModel = mv.getModel().get(MODEL_ATTRIBUTE);
        assertNotNull(reqModel);
        assertEquals(getExpectedTime(2008, 07, 20),
                ((Event) reqModel).getStartDate().getTime());
        Object sesModel = req.getSession().getAttribute(MODEL_ATTRIBUTE);
        assertNull("The " + MODEL_ATTRIBUTE +
                " attribute should have been removed from the session", sesModel);
    }

    public void testUpdateFailedForValidation() throws Exception {
        Event testEvent = getTestEvent();
        callForm(testEvent.getId().toString());
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI(FORM_REQUEST_URI);
        req.setParameter("startDate", "");
        ModelAndView mv = handler.handle(req, res, controller);
        assertEquals(FORM_VIEW, mv.getViewName());
        Object sesModel = req.getSession().getAttribute(MODEL_ATTRIBUTE);
        assertNotNull(
                "The " + MODEL_ATTRIBUTE +
                " jugger attribute shouldn't have been removed from the session",
                sesModel);
        this.endTransaction();
        this.startNewTransaction();
        Event event = eventDao.get(testEvent.getId());
        assertEquals(getExpectedTime(2007, 10, 24), event.getStartDate().getTime());
    }

    public void testForm() throws Exception {
        Event testEvent = getTestEvent();
        ModelAndView mv = callForm(testEvent.getId().toString());
        Object reqModel = mv.getModel().get(MODEL_ATTRIBUTE);
        assertNotNull(reqModel);
        assertTrue("The object is not of type Event", reqModel instanceof Event);
        Object sesModel = req.getSession().getAttribute(MODEL_ATTRIBUTE);
        assertNotNull(sesModel);
        assertTrue("The object is not of type Event", sesModel instanceof Event);
        assertSame(reqModel, sesModel);
        assertEquals(testEvent.getId(), ((Event) reqModel).getId());
        assertEquals(FORM_VIEW, mv.getViewName());
    }
    
    
   
    
    public void testNewEventWithSpeakers() throws Exception
    {
    	callForm(null);
    	resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI("/event/eventspeaker.form");
        req.setParameter("title", "test-title");
        req.setParameter("location", "test-location");
        req.setParameter("startDate", "20/08/2010");
        req.setParameter("endDate", "20/08/2010");        
        handler.handle(req, res, controller);
        
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI("/event/speakerevent.form");
        req.setParameter("firstName", "Enrico");
        req.setParameter("lastName", "Giurin");
        req.setParameter("email", "enrico@giurin.com");
        req.setParameter("resume", "myResume");
        handler.handle(req, res, controller);
        
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI(FORM_REQUEST_URI);
        handler.handle(req, res, controller);        
        
        Event event = eventDao.findByTitle("test-title").get(0);
        assertEquals(event.getLocation(), "test-location");
        List<Speaker> speakers = speakerDao.allByEvent(event.getId());
        assertEquals(1, speakers.size());
        assertEquals(speakers.get(0).getFirstName(), "Enrico");        
    }   
    
    
    public void testDeleteSpeakersFromExistingEvent() throws Exception
    {
    	Event testEvent = getSpringFrameworkTestEvent();
    	List<Speaker> oldSpeakers = testEvent.getSpeakers();
    	assertEquals(2, oldSpeakers.size());
    	callForm(testEvent.getId().toString());
    	
        
        //remove all  speakers
        for(int j=0;j<2;j++)
        {
        	 resetRequestAndResponse();
        	 req.setMethod("GET");
             req.setRequestURI(REQUEST_REMOVE_SPEAKER_URI);
             req.setParameter("indexSpeaker", new Long(1).toString());
             handler.handle(req, res, controller);
        }       
        //save the event
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI(FORM_REQUEST_URI);
        handler.handle(req, res, controller);   
        
        //check data
        Event event = getSpringFrameworkTestEvent();
        List<Speaker> speakers = speakerDao.allByEvent(event.getId());
        //no speakers bound to this event
        assertEquals(0, speakers.size());
        List<Speaker> allSpeakers = speakerDao.findAll();    
        //only 1 speaker still alive
        assertEquals(1, allSpeakers.size());
        assertEquals("Lucio Benfante &egrave; il presidente del JUG Padova e responsabile di jugevents.", allSpeakers.get(0).getResume());
       
        
    }   
    
    
   
/**
  *********************  Utilities methods  *****************************
 */
    private ModelAndView callForm(String eventId) throws Exception {
        servicesBo.setAuthenticatedUsername("admin");
        req.setMethod("GET");
        req.setRequestURI(FORM_REQUEST_URI);
        req.setParameter("id", eventId);
        ModelAndView mv = handler.handle(req, res, controller);
        this.endTransaction();
        this.startNewTransaction();
        return mv;
    }

    private long getExpectedTime(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);
        long expectedTime = cal.getTimeInMillis();
        return expectedTime;
    }

    private Event getTestEvent() {
        List<Event> events =
                eventDao.searchByCriteria(DetachedCriteria.forClass(Event.class).add(Restrictions.eq("title",
                "JUG Padova Meeting #38")));
        return events.get(0);
    }
    
    private Event getSpringFrameworkTestEvent() {
        List<Event> events =
                eventDao.searchByCriteria(DetachedCriteria.forClass(Event.class).add(Restrictions.eq("title",
                "Springframework Meeting 2007")));
        return events.get(0);
    }
}
