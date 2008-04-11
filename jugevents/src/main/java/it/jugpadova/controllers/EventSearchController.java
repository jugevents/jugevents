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
import it.jugpadova.bean.EventSearch;
import it.jugpadova.po.Event;
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

public abstract class EventSearchController extends BaseFormController {

    private static final Logger logger =
            Logger.getLogger(EventSearchController.class);

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
        EventSearch eventSearch = (EventSearch) command;
        List<Event> events = blo().getEventBo().search(eventSearch);
        ModelAndView mv = onSubmit(command, errors);  
        mv.addObject("servicesBo", blo().getServicesBo());
        mv.addObject("events", events);
        if (events.isEmpty()) {
            mv.addObject("showNoResultsMessage", "true");
        }
        return mv;
    }

    @Override
    protected boolean isFormSubmission(HttpServletRequest request) {
        return true;
    }

    
    
    protected abstract Daos dao();

    protected abstract Blos blo();
}
