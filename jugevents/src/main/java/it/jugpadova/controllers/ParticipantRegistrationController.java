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

import it.jugpadova.bean.Registration;
import it.jugpadova.po.Event;
import it.jugpadova.po.Participant;
import it.jugpadova.util.Utilities;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.octo.captcha.service.CaptchaService;
import it.jugpadova.blo.EventBo;
import it.jugpadova.exception.RegistrationNotOpenException;
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
@RequestMapping("/event/registration.form")
@SessionAttributes(ParticipantRegistrationController.REGISTRATION_ATTRIBUTE)
public class ParticipantRegistrationController {

    private static final Logger logger =
            Logger.getLogger(ParticipantRegistrationController.class);
    public static final String FORM_VIEW = "event/registration";
    public static final String REGISTRATION_ATTRIBUTE = "registration";
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private EventBo eventBo;

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view = FORM_VIEW)
    protected ModelAndView onSubmit(HttpServletRequest req,
            @ModelAttribute(REGISTRATION_ATTRIBUTE) Registration registration,
            BindingResult result, SessionStatus status) throws Exception {
        ModelAndView mv = null;
        String baseUrl = Utilities.getBaseUrl(req);
        List<Participant> prevParticipant = eventBo.searchParticipantByEmailAndEventId(registration.getParticipant().
                getEmail(), registration.getEvent().getId());
        if (prevParticipant.size() == 0) {
            eventBo.register(registration.getEvent(),
                    registration.getParticipant(), baseUrl);
            mv =
                    new ModelAndView(
                    "redirect:/home/message.html?messageCode=participant.registration.sentMail");
            Utilities.addMessageArguments(mv, registration.getEvent().getTitle(),
                    registration.getParticipant().getEmail());
        } else {
            Participant p = prevParticipant.get(0);
            if (p.getConfirmed().booleanValue()) {
                status.setComplete();
                mv = Utilities.getMessageView(
                        "participant.registration.yetRegistered");
            } else {
                eventBo.refreshRegistration(registration.getEvent(), p, baseUrl);
                mv =
                        new ModelAndView(
                        "redirect:/home/message.html?messageCode=participant.registration.sentMail");
                Utilities.addMessageArguments(mv,
                        registration.getEvent().getTitle(),
                        registration.getParticipant().getEmail());
            }
        }
        status.setComplete();
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected String form(@RequestParam("event.id") Long id,
            HttpServletRequest req, Model model) {
        Registration result = new Registration();
        result.setParticipant(new Participant());
        Event event = eventBo.retrieveEvent(id);
        if (event != null) {
            if (!event.getRegistrationOpen()) {
                throw new RegistrationNotOpenException(
                        "The registration isn't open, you can't register to the event",
                        event);
            }
            result.setEvent(event);
            // for event showing fragment
            req.setAttribute("event", event);
        } else {
            throw new IllegalArgumentException("No event with id " + id);
        }
        result.setCaptchaId(req.getSession().getId());
        result.setCaptchaService(captchaService);
        model.addAttribute(REGISTRATION_ATTRIBUTE, result);
        return FORM_VIEW;
    }
}
