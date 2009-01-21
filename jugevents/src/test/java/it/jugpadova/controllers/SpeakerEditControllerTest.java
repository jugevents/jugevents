package it.jugpadova.controllers;

import it.jugpadova.JugEventsControllerTest;
import it.jugpadova.dao.EventDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * Tests on the SpeakerEditController.
 * 
 * @author Enrico Giurin
 */
public class SpeakerEditControllerTest extends JugEventsControllerTest {

    public static final String DONE_VIEW = "redirect:../edit.form?id=";
    public static final String FORM_REQUEST_URI = "/event/speaker/edit.form";
    public static final String FORM_VIEW = "event/speaker/edit";
    public static final String MODEL_ATTRIBUTE = "speaker";
    @Autowired
    private SpeakerEditController controller;
    
    @Autowired
    private EventDao eventDao;
    

    public void testConfiguration() {
        assertNotNull(controller);
    }
    
    
    @SuppressWarnings("unchecked")
	public void testPost() throws Exception {
        Event testEvent = getTestEvent();
        callForm();
        resetRequestAndResponse();
        req.setMethod("POST");
        req.setRequestURI(FORM_REQUEST_URI);
        req.setParameter("speakerCoreAttributes.firstName", "Enrico");
        ModelAndView mv = handler.handle(req, res, controller);
        assertEquals(DONE_VIEW + testEvent.getId(), mv.getViewName());
        Object reqModel = mv.getModel().get(MODEL_ATTRIBUTE);
        assertNotNull(reqModel);
        ArrayList<Speaker> speakers = (ArrayList<Speaker>)req.getSession(false).getAttribute(SpeakerEditController.SPEAKER_LIST_ATTRIBUTE);
        assertEquals(1, speakers.size());
        assertEquals("Enrico", speakers.get(0).getSpeakerCoreAttributes().getFirstName());
    }

    private ModelAndView callForm() throws Exception {       
        req.setMethod("GET");
        req.setRequestURI(FORM_REQUEST_URI);        
        req.getSession().setAttribute(EventEditController.EVENT_ATTRIBUTE, getTestEvent());
        ModelAndView mv = handler.handle(req, res, controller);
        this.endTransaction();
        this.startNewTransaction();
        return mv;
    }
    
    
    private Event getTestEvent() {
        List<Event> events =
                eventDao.searchByCriteria(DetachedCriteria.forClass(Event.class).add(Restrictions.eq("title",
                "Future Meeting")));
        return events.get(0);
    }
    
   
}
