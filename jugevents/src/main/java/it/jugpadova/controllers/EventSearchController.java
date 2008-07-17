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

import it.jugpadova.bean.EventSearch;
import it.jugpadova.blo.EventBo;
import it.jugpadova.po.Event;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/event/search.form")
public class EventSearchController {

    private static final Logger logger =
            Logger.getLogger(EventSearchController.class);
    @Autowired
    private EventBo eventBo;

    @ModelAttribute("eventSearch")
    public EventSearch createBackingObject() {
        return new EventSearch();
    }

    @RequestMapping
    protected ModelAndView search(
            @ModelAttribute("eventSearch") EventSearch eventSearch) {
        List<Event> events = eventBo.search(eventSearch);
        ModelAndView mv = new ModelAndView("event/search");
        mv.addObject("events", events);
        mv.addObject("showNoResultsMessage", Boolean.toString(events.isEmpty()));
        return mv;
    }
}
