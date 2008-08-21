package it.jugpadova.controllers;

import it.jugpadova.JugEventsControllerTest;
import it.jugpadova.dao.EventDao;
import it.jugpadova.mock.MockServicesBo;
import it.jugpadova.po.Event;
import java.util.Date;
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
    public static final String FORM_VIEW = "event/edit";
    public static final String MODEL_ATTRIBUTE = "event";
    @Autowired
    private EventEditController controller;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private MockServicesBo servicesBo;

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
        assertEquals(DONE_VIEW+testEvent.getId(), mv.getViewName());
        Object reqModel = mv.getModel().get(MODEL_ATTRIBUTE);
        assertNotNull(reqModel);
        System.out.println(((Event) reqModel).getStartDate().getTime());
        assertEquals(new Date(1219183200000L), ((Event) reqModel).getStartDate());
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
                "The "+MODEL_ATTRIBUTE+" jugger attribute shouldn't have been removed from the session",
                sesModel);
        this.endTransaction();
        this.startNewTransaction();
        Event event = eventDao.get(testEvent.getId());
        assertEquals(new Date(1195858800000L), event.getStartDate());
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

    private ModelAndView callForm(String eventId) throws Exception {
        servicesBo.setAuthenticatedUsername("lucio");
        req.setMethod("GET");
        req.setRequestURI(FORM_REQUEST_URI);
        req.setParameter("id", eventId);
        ModelAndView mv = handler.handle(req, res, controller);
        this.endTransaction();
        this.startNewTransaction();
        return mv;
    }
    
    private Event getTestEvent() {
        List<Event> events =
                eventDao.searchByCriteria(DetachedCriteria.forClass(Event.class).add(Restrictions.eq("title",
                "JUG Padova Meeting #38")));
        return events.get(0);
    }
}
