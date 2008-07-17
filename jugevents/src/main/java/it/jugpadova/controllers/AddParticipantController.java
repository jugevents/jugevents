// Copyright 2006-2008 The Parancoe Team
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

import it.jugpadova.bean.Registration;
import it.jugpadova.blo.EventBo;
import it.jugpadova.dao.ParticipantDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Participant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
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
import org.springmodules.validation.bean.BeanValidator;

@Controller
@RequestMapping("/event/addParticipant.form")
@SessionAttributes(AddParticipantController.REGISTRATION_ATTRIBUTE)
public class AddParticipantController {

    private static final Logger logger =
            Logger.getLogger(AddParticipantController.class);
    public static final String FORM_VIEW = "event/participants";
    public static final String REGISTRATION_ATTRIBUTE = "registration";
    @Autowired
    private EventBo eventBo;
    @Autowired
    private ParticipantDao participantDao;
    @Autowired
    @Qualifier("validator")
    private BeanValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
    }

//    protected void onBindAndValidate(HttpServletRequest req, Object command,
//            BindException errors) throws Exception {
//        if (errors.hasErrors()) {
//            Registration registration = (Registration) command;
//            Event event = eventBo.retrieveEvent(registration.getEvent().
//                    getId());
//            List<Participant> participants =
//                    eventBo.searchConfirmedParticipantsByEventId(event.getId());
//            req.setAttribute("event", event);
//            req.setAttribute("participants", participants);
//            req.setAttribute("showAddNewPartecipantDiv", "true");
//        }
//    }

    @RequestMapping(method = RequestMethod.POST)
    protected ModelAndView onSubmit(HttpServletRequest req,
            @ModelAttribute(REGISTRATION_ATTRIBUTE) Registration registration,
            BindingResult result, SessionStatus status) throws Exception {
        validator.validate(registration, result);
        if (result.hasErrors()) {
            List<Participant> participants =
                    eventBo.searchConfirmedParticipantsByEventId(registration.getEvent().getId());
            req.setAttribute("event", registration.getEvent());
            req.setAttribute("participants", participants);
            req.setAttribute("showAddNewPartecipantDiv", "true");
            return new ModelAndView(FORM_VIEW);
        }
        List<Participant> prevParticipant = eventBo.searchParticipantByEmailAndEventId(registration.getParticipant().
                getEmail(), registration.getEvent().getId());
        if (prevParticipant.size() == 0) {
            eventBo.addParticipant(registration.getEvent(),
                    registration.getParticipant());
        } else {
            Participant p = prevParticipant.get(0);
            p.setConfirmed(Boolean.TRUE);
            participantDao.store(p);
            logger.info("Confirmed participant with id=" + p.getId());
        }
        ModelAndView mv = new ModelAndView("redirect:participants.html?id=" + registration.getEvent().
                getId());
        status.setComplete();
        return mv;
    }

    @ModelAttribute(REGISTRATION_ATTRIBUTE)
    protected Registration formBackingObject(@RequestParam("event.id") Long id,
            HttpServletRequest req) {
        Registration result = new Registration();
        result.setParticipant(new Participant());
        Event event = eventBo.retrieveEvent(id);
        if (event != null) {
            result.setEvent(event);
            // for event showing fragment
            req.setAttribute("event", event);
        } else {
            throw new IllegalArgumentException("No event with id " + id);
        }
        return result;
    }
}
