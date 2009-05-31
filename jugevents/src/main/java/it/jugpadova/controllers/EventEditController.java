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
package it.jugpadova.controllers;

import it.jugpadova.blo.EventBo;
import it.jugpadova.po.Event;
import it.jugpadova.po.Registration;
import it.jugpadova.po.Speaker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.parancoe.plugins.security.SecurityBo;
import org.parancoe.web.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/event/*.form")
@SessionAttributes({"event", "speaker"})

public class EventEditController {

    public static final String FORM_VIEW = "event/edit";
    public static final String SPEAKER_FORM_VIEW = "event/speaker/edit";
    public static final String SESSION_EVENT = "event";
    public static final String SESSION_SPEAKER = "speaker";
    public static final String ORIGINAL_SESSION_SPEAKER = "original_speaker";

    private static final Logger logger =
            Logger.getLogger(EventEditController.class);
    @Autowired
    private EventBo eventBo;
    @Autowired
    private SecurityBo securityBo;

    @InitBinder
    public void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(Date.class, "registration.startRegistration",
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy HH:mm"),
                true));
        binder.registerCustomEditor(Date.class, "registration.endRegistration",
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy HH:mm"),
                true));
        binder.registerCustomEditor(byte[].class,
                new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(value="/event/edit.form", method = RequestMethod.POST)
    @Validation(view = FORM_VIEW)
    public String save(@ModelAttribute("event") Event event,
            BindingResult result, SessionStatus status) {
    	
    	eventBo.checkUserAuthorization(event);
        eventBo.save(event);
        status.setComplete();
        return "redirect:show.html?id=" + event.getId();
    }
    
    
    
    @RequestMapping(value="/event/edit.form", method = RequestMethod.GET)
    public ModelAndView form(@RequestParam(value = "id", required =
            false) Long id, @RequestParam(value = "copyId", required = false) Long copyId, Model model, HttpServletRequest req) {
        Event event = null;
       
        if (id != null) {
            event = eventBo.retrieveEvent(id);
            if (event.getRegistration() == null) {
                event.setRegistration(new Registration());
            }
            eventBo.checkUserAuthorization(event);
        } else if (copyId != null) {
            Event sourceEvent = eventBo.retrieveEvent(copyId);
            event = eventBo.buildEventFromTemplate(sourceEvent, (String)req.getAttribute("lang"));
            Registration registration = new Registration();
            registration.setStartRegistration(new Date());
            registration.setEndRegistration(registration.getStartRegistration());
            event.setRegistration(registration);
        } else {
            event = new Event();
            event.setOwner(eventBo.getServicesBo().getCurrentJugger());
            Registration registration = new Registration();
            registration.setStartRegistration(new Date());
            registration.setEndRegistration(registration.getStartRegistration());
            event.setRegistration(registration);
        }
        
        return mvEvent(event);
    }
  

    @RequestMapping(value="/event/eventspeaker.form", method = RequestMethod.POST)
    public ModelAndView eventToSpeaker(@ModelAttribute(SESSION_EVENT)Event event, @RequestParam(value = "indexSpeaker", required=false)Long indexSpeaker, HttpSession session) {
        
    	Speaker speaker = null;
		if (indexSpeaker != null) {
			speaker = event.getSpeakers().get(index(indexSpeaker));
			speaker.setIndexSpeaker(indexSpeaker);
			session.setAttribute(ORIGINAL_SESSION_SPEAKER, speaker.copyOfInstance());	
		} else {
			speaker = new Speaker();						
		}
		ModelAndView mv = new ModelAndView(SPEAKER_FORM_VIEW);
		mv.addObject(SESSION_SPEAKER, speaker);					
		return mv;
    }
    
    @RequestMapping(value="/event/speakerevent.form", method = RequestMethod.POST)
    @Validation(view = SPEAKER_FORM_VIEW)
    public ModelAndView speakerToEvent(@ModelAttribute(SESSION_SPEAKER) Speaker speaker, BindingResult result, HttpSession session) {
        //inserting element into session   
    	Event event = (Event)session.getAttribute(SESSION_EVENT);
    	if(speaker.getIndexSpeaker()==null)    		
    	{
    		event.getSpeakers().add(speaker);    	
    	}
    	speaker.setEvent(event);
    	speaker.setIndexSpeaker(null);
    	clearSpeakerSession(session);
    	return mvEvent(event);
    }  
    
    
    
    @RequestMapping(value = "/event/removespeaker.form", method = RequestMethod.GET)
	public String removeSpeaker(
			@RequestParam(value = "indexSpeaker", required = true) Long indexSpeaker,
			@ModelAttribute(SESSION_EVENT) Event event) {
		List<Speaker> speakers = event.getSpeakers();
		logger.debug("Number of speakers in session: "+speakers.size());		
		speakers.remove(index(indexSpeaker));				
		return FORM_VIEW;
	}
    @RequestMapping(value = "/event/backtoevent.form", method = RequestMethod.GET)
    public ModelAndView backToEvent(@ModelAttribute(SESSION_EVENT)Event event, HttpSession session, @RequestParam(value = "indexSpeaker", required =
        false) Long indexSpeaker) {    
    	
    	if(indexSpeaker!=null)
    	{
    		Speaker originalSpeaker = (Speaker)session.getAttribute(ORIGINAL_SESSION_SPEAKER);
    		event.getSpeakers().remove(index(indexSpeaker));    				
    		event.getSpeakers().add(originalSpeaker);
    		originalSpeaker.setIndexSpeaker(null);
    	}    	    	
    	clearSpeakerSession(session);
    	return mvEvent(event);
    }
    
    private  ModelAndView mvEvent(Event event)
    {
    	ModelAndView mv = new ModelAndView(FORM_VIEW);
    	mv.addObject("event", event);
    	return mv; 
    }
    
    private int index(Long indexSpeaker)
    {
    	return indexSpeaker.intValue() - 1;
    }
    
    private void clearSpeakerSession(HttpSession session)
    {
    	session.setAttribute(SESSION_SPEAKER, null);
    	session.setAttribute(ORIGINAL_SESSION_SPEAKER, null);
    }
    
   
    
    
    
    
    
    
}
