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

import com.octo.captcha.service.CaptchaService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import it.jugpadova.Blos;
import it.jugpadova.Daos;
import it.jugpadova.bean.Registration;
import it.jugpadova.po.Event;
import it.jugpadova.po.Jugger;
import it.jugpadova.po.Participant;


import org.parancoe.web.BaseFormController;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Enrico Giurin
 *
 */
public abstract class JuggerEditController extends BaseFormController {
    
    private final static Logger logger = Logger.getLogger(JuggerEditController.class);
    private CaptchaService captchaService;
    
    protected void initBinder(HttpServletRequest req, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"),true));
    }
    
    /* questo viene chiamato solo in caso di una post a jugger/edit.form */
    protected ModelAndView onSubmit(HttpServletRequest req, HttpServletResponse res, Object command, BindException errors) throws Exception {
        Jugger jugger = (Jugger) command;
        dao().getJuggerDao().create(jugger);
        /*
        if (jugger.getEvent().getId() == null) {
            return genericError("No valid event");
        }
        blo().getEventBo().register(registration.getEvent(),
                registration.getParticipant());
                */
        return onSubmit(command, errors); // restituisce succesView
    }
    
    protected Object formBackingObject(HttpServletRequest req) throws Exception {
        //set list of countries into request
    //	List<Country> list =  dao().getCountryDao().findAll();
    //    req.setAttribute("countries", list);
    	return new Jugger();
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