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
import it.jugpadova.dao.SpeakerDao;
import it.jugpadova.exception.ConversationException;
import it.jugpadova.po.Event;
import it.jugpadova.po.Registration;
import it.jugpadova.po.Speaker;
import it.jugpadova.po.SpeakerCoreAttributes;
import it.jugpadova.util.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/event/*.form")
@SessionAttributes({"event", "speaker"})

public class EventEditController {

    public static final String FORM_VIEW = "event/edit";
    public static final String SPEAKER_FORM_VIEW = "event/speaker/edit";
    public static final String SESSION_EVENT = "event";
    public static final String SESSION_SPEAKER = "speaker";

    private static final Logger logger =
            Logger.getLogger(EventEditController.class);
    @Autowired
    private EventBo eventBo;
    @Autowired
    private SpeakerDao speakerDao; 

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
    }

    @RequestMapping(value="/event/edit.form", method = RequestMethod.POST)
    @Validation(view = FORM_VIEW)
    public String save(@ModelAttribute("event") Event event,
            BindingResult result, SessionStatus status) {
        eventBo.save(event);
        status.setComplete();
        return "redirect:show.html?id=" + event.getId();
    }
    
    
    
    @RequestMapping(value="/event/edit.form", method = RequestMethod.GET)
    public String form(@RequestParam(value = "id", required =
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
            Registration registration = new Registration();
            registration.setStartRegistration(new Date());
            registration.setEndRegistration(registration.getStartRegistration());
            event.setRegistration(registration);
        }
        
        //TODO just to retrieve speakers from hibernate...maybe this call happens in the jsp, evaluate to remove it
        //event.getSpeakers();
        model.addAttribute("event", event);
        return FORM_VIEW;
    }
  
    
    
    @RequestMapping(value="/event/speakerevent.form", method = RequestMethod.POST)
    @Validation(view = SPEAKER_FORM_VIEW)
    public ModelAndView speakerToEvent(@ModelAttribute(SESSION_SPEAKER) Speaker speaker,  BindingResult result, SessionStatus status, HttpServletRequest req) {
        //inserting element into session   
    	Event event = (Event)req.getSession().getAttribute("event");
    	speaker.setEvent(event);
    	List<Speaker> speakers = event.getSpeakers();
    	if(speakers.contains(speaker)) {
    		//remove the existing speaker in the list, not the new one
    		speakers.remove(speaker);
    	}
    	speakers.add(speaker);   	
    	//TODO remove speaker from session
    	//find a better way to remove the attribute speaker from the session
    	req.getSession().setAttribute("speaker", null);
    	ModelAndView mv = new ModelAndView(FORM_VIEW);
    	mv.addObject("event", event);
    	return mv;
    	
    }
    
    @RequestMapping(value="/event/eventspeaker.form", method = RequestMethod.POST)
    public ModelAndView eventToSpeaker(@ModelAttribute("event")Event event, @RequestParam(value = "speakerId", required=false)Long speakerId) {
        
    	Speaker speaker = null;
		if (speakerId != null) {
			speaker = Utilities.getSpeaker(speakerId, event.getSpeakers());
		} else {
			speaker = new Speaker();			
			speaker.setSpeakerCoreAttributes(new SpeakerCoreAttributes());
		}
		ModelAndView mv = new ModelAndView(SPEAKER_FORM_VIEW);
		mv.addObject(SESSION_SPEAKER, speaker);
		return mv;
    }
    
    @RequestMapping(value = "/event/removespeaker.form", method = RequestMethod.GET)
	public String removeSpeakerFromSession(
			@RequestParam(value = "speakerId", required = true) Long speakerId,
			@ModelAttribute(SESSION_EVENT) Event event) {
		List<Speaker> speakers = event.getSpeakers();
		Speaker speakerToRemove = Utilities.getSpeaker(speakerId, speakers);
		if (speakerToRemove == null) {
			// TODO javascript error message or error message
		} else {
			int index = speakers.indexOf(speakerToRemove);
			speakers.remove(index);
		}
		return FORM_VIEW;
	}

    
   
    
    
    
    
    
    
}
