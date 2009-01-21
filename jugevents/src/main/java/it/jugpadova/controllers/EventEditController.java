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
import it.jugpadova.blo.SpeakerBo;
import it.jugpadova.po.Event;
import it.jugpadova.po.Registration;
import it.jugpadova.po.Speaker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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

@Controller
@RequestMapping("/event/edit.form")
@SessionAttributes({EventEditController.EVENT_ATTRIBUTE})
public class EventEditController {

    public static final String FORM_VIEW = "event/edit";
    private static final Logger logger =
            Logger.getLogger(EventEditController.class);
    
    public static final String EVENT_ATTRIBUTE = "event";
    @Autowired
    private EventBo eventBo;
    @Autowired
    private SpeakerBo speakerBo;

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

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view = FORM_VIEW)
    public String save(@ModelAttribute("event") Event event,
            BindingResult result, SessionStatus status, HttpServletRequest req) {
        eventBo.save(event);
        saveSpeakers(req);
        req.getSession().removeAttribute(SpeakerEditController.SPEAKER_LIST_ATTRIBUTE);
        status.setComplete();
        return "redirect:show.html?id=" + event.getId();
    }
    
    private void saveSpeakers(HttpServletRequest req) 
    {
    	ArrayList<Speaker> speakerList = SpeakerEditController.speakerList(req);
    	if(speakerList == null)
    	{
    		logger.debug("Attribute "+SpeakerEditController.SPEAKER_LIST_ATTRIBUTE+" not found in session");
    		return;
    	}
    	for(Speaker s: speakerList)
    	{
    		speakerBo.save(s);
    	}
    }
    

    @RequestMapping(method = RequestMethod.GET)
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
        model.addAttribute(EVENT_ATTRIBUTE, event);        
        return FORM_VIEW;
    }
}
