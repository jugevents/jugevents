// Copyright 2006-2007 The Parancoe Team
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

import it.jugpadova.Blos;
import it.jugpadova.Daos;
import it.jugpadova.bean.Registration;
import it.jugpadova.po.Event;
import it.jugpadova.po.Participant;
import it.jugpadova.util.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.parancoe.web.BaseFormController;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.octo.captcha.service.CaptchaService;
import it.jugpadova.exception.RegistrationNotOpenException;

public abstract class ParticipantRegistrationController extends BaseFormController {

    private static final Logger logger =
            Logger.getLogger(ParticipantRegistrationController.class);
    private CaptchaService captchaService;

    @Override
    protected void initBinder(HttpServletRequest req,
            ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest req,
            HttpServletResponse res, Object command,
            BindException errors) throws Exception {
        Registration registration = (Registration) command;
        if (registration.getEvent().getId() == null) {
            return genericError("No valid event");
        }
        String baseUrl =
                "http://" + req.getServerName() + ":" + req.getServerPort() +
                req.getContextPath();
        List<Participant> prevParticipant = blo().getEventBo().
                searchParticipantByEmailAndEventId(registration.getParticipant().
                getEmail(), registration.getEvent().getId());
        if (prevParticipant.size() == 0) {
            blo().getEventBo().
                    register(registration.getEvent(),
                    registration.getParticipant(), baseUrl);
            ModelAndView mv = onSubmit(command, errors);
            Utilities.addMessageArguments(mv, registration.getEvent().getTitle(),
                    registration.getParticipant().getEmail());
            return mv;
        } else {
            Participant p = prevParticipant.get(0);
            if (p.getConfirmed().booleanValue()) {
                return Utilities.getMessageView("participant.registration.yetRegistered");
            } else {
                blo().getEventBo().
                        refreshRegistration(registration.getEvent(), p, baseUrl);
                ModelAndView mv = onSubmit(command, errors);
                Utilities.addMessageArguments(mv,
                        registration.getEvent().getTitle(),
                        registration.getParticipant().getEmail());
                return mv;
            }
        }
    }

    @Override
    protected Object formBackingObject(HttpServletRequest req) throws Exception {
        Registration result = new Registration();
        result.setParticipant(new Participant());
        String sid = req.getParameter("event.id");
        if (sid != null) {
            Event event = blo().getEventBo().retrieveEvent(Long.parseLong(sid));
            if (event != null) {
                if (!event.getRegistrationOpen()) {
                    throw new RegistrationNotOpenException("The registration isn't open, you can't register to the event",
                            event);
                }
                result.setEvent(event);
                // for event showing fragment
                req.setAttribute("event", event);
            } else {
                throw new IllegalArgumentException("No event with id " + sid);
            }
        } else {
            throw new IllegalArgumentException("The event must be specified");
        }
        result.setCaptchaId(req.getSession().getId());
        result.setCaptchaService(captchaService);
        return result;
    }

    public CaptchaService getCaptchaService() {
        return captchaService;
    }

    public void setCaptchaService(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    public Logger getLogger() {
        return logger;
    }

    protected abstract Daos dao();

    protected abstract Blos blo();
}
