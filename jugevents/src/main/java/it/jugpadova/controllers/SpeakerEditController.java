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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeSet;

import it.jugpadova.blo.SpeakerBo;
import it.jugpadova.dao.SpeakerCoreAttributesDao;
import it.jugpadova.dao.SpeakerDao;
import it.jugpadova.exception.ConversationException;
import it.jugpadova.exception.ParancoeAccessDeniedException;
import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;
import it.jugpadova.po.SpeakerCoreAttributes;
import it.jugpadova.util.EditStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.parancoe.web.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;



@Controller
@RequestMapping("/event/speaker/edit.form")
@SessionAttributes(SpeakerEditController.SPEAKER_ATTRIBUTE)


public class SpeakerEditController {

    public static final String FORM_VIEW = "event/speaker/edit";
    private static final Logger logger =
            Logger.getLogger(SpeakerEditController.class);
    public static final String SPEAKER_ATTRIBUTE = "speaker";
    public static final String SPEAKER_LIST_ATTRIBUTE = "speakerList";
    @Autowired
    private SpeakerDao speakerDao;   
    
    static ArrayList<Speaker> speakerList(HttpServletRequest req)
    {
    	HttpSession session = req.getSession(false);
    	ArrayList<Speaker> speakerList = (ArrayList<Speaker>)session.getAttribute(SPEAKER_LIST_ATTRIBUTE);
    	if(speakerList == null)
    	{
    		speakerList = new ArrayList<Speaker>();
    		session.setAttribute(SPEAKER_LIST_ATTRIBUTE, speakerList);
    	}
    	return speakerList;
    }
    
    
    private Event getEventFromSession(HttpServletRequest req)
    {
    	HttpSession session = req.getSession(false);
    	return (Event)session.getAttribute(EventEditController.EVENT_ATTRIBUTE);
    }
    

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view = FORM_VIEW)
    public String save(@ModelAttribute(SPEAKER_ATTRIBUTE) Speaker speaker, BindingResult result, SessionStatus status, HttpServletRequest req) {
        //inserting element into session   
    	Event event = getEventFromSession(req);    	
    	speaker.setEvent(event);
    	speaker.setStatus(EditStatus.MODIFIED);
    	speakerList(req).add(speaker);
    	status.setComplete();
        return "redirect:../edit.form?id=" + event.getId();
    }

    @RequestMapping(method = RequestMethod.GET)
	public String form(@RequestParam(value = "id", required = false) Long id,
			Model model, HttpServletRequest req) {
    	Event event = getEventFromSession(req);  
    	if(event == null)
    	{
    		throw new ConversationException("There is no event edited");
    	}
		Speaker speaker = null;
		if (id != null) {
			speaker = speakerDao.get(id);
		} else {
			speaker = new Speaker();			
			speaker.setSpeakerCoreAttributes(new SpeakerCoreAttributes());
		}
		model.addAttribute(SPEAKER_ATTRIBUTE, speaker);
		return FORM_VIEW;
	}
}
