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

import it.jugpadova.blo.EventBo;
import it.jugpadova.po.Event;

import it.jugpadova.po.Registration;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.bean.BeanValidator;

@Controller
@RequestMapping("/event/edit.form")
@SessionAttributes("event")
public class EventEditController {
    public static final String FORM_VIEW = "event/edit";

    private static final Logger logger =
            Logger.getLogger(EventEditController.class);
    @Autowired
    private EventBo eventBo;
    @Autowired
    @Qualifier("validator")
    private BeanValidator validator;

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
    public String save(@ModelAttribute("event") Event event,
            BindingResult result, SessionStatus status) {
        try {
            validator.validate(event, result);
            if (result.hasErrors()) {
                return FORM_VIEW;
            }
            eventBo.save(event);
            status.setComplete();
            return "redirect:show.html?id="+event.getId();
        } catch (Exception e) {
            result.reject("error.generic");
            logger.error("Error saving the event " + event, e);
            return FORM_VIEW;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(@ModelAttribute("event") Event event) {
        return FORM_VIEW;
    }

    @ModelAttribute("event")
    protected Event formBackingObject(@RequestParam(value = "id", required =
            false) Long id) {
        Event result = null;
        if (id != null) {
            result = eventBo.retrieveEvent(id);
            if (result.getRegistration() == null) {
                result.setRegistration(new Registration());
            }
            eventBo.checkUserAuthorization(result);
        } else {
            result = new Event();
            Registration registration = new Registration();
            registration.setStartRegistration(new Date());
            registration.setEndRegistration(registration.getStartRegistration());
            result.setRegistration(registration);
        }
        return result;
    }
}
