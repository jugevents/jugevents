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

import it.jugpadova.bean.NewJugger;
import it.jugpadova.bean.RequireReliability;
import it.jugpadova.exception.EmailAlreadyPresentException;
import it.jugpadova.exception.UserAlreadyPresentsException;
import it.jugpadova.util.Utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.parancoe.plugins.world.Country;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.octo.captcha.service.CaptchaService;
import it.jugpadova.blo.JuggerBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springmodules.validation.bean.BeanValidator;

/**
 * 
 * @author Enrico Giurin
 * 
 */
@Controller
@RequestMapping("/jugger/registration.form")
@SessionAttributes(JuggerRegistrationController.JUGGER_ATTRIBUTE)
public class JuggerRegistrationController {

    private static final Logger logger = Logger.getLogger(
            JuggerRegistrationController.class);
    public static final String FORM_VIEW = "jugger/newJugger";
    public static final String JUGGER_ATTRIBUTE = "jugger";
    @Autowired
    private JuggerBo juggerBo;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    @Qualifier("validator")
    private BeanValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(byte[].class,
                new ByteArrayMultipartFileEditor());
    }

//    @Override
//    protected void onBind(HttpServletRequest request, Object command)
//            throws Exception {
//        NewJugger jc = (NewJugger) command;
//        jc.getJugger().getUser().setPassword("xxx");
//    }

    @RequestMapping(method = RequestMethod.POST)
    protected ModelAndView save(HttpServletRequest req,
            @ModelAttribute(JUGGER_ATTRIBUTE) NewJugger jc,
            BindingResult result, SessionStatus status) throws IOException {
        try {
            validator.validate(jc, result);
            if (result.hasErrors()) {
                return new ModelAndView(FORM_VIEW);
            }
            juggerBo.newJugger(jc.getJugger(),
                    Utilities.getBaseUrl(req),
                    jc.getRequireReliability().isRequireReliability(),
                    jc.getRequireReliability().getComment());
        } catch (EmailAlreadyPresentException e) {
            result.rejectValue("jugger.email", "emailalreadypresent",
                    e.getMessage());
            logger.error(e);
            return new ModelAndView(FORM_VIEW);
        } catch (UserAlreadyPresentsException e) {
            result.rejectValue("jugger.user.username", "useralreadypresents",
                    e.getMessage());
            logger.error(e);
            return new ModelAndView(FORM_VIEW);
        } finally {
            if (jc.getJugger().getJug().getCountry() == null) {
                jc.getJugger().getJug().setCountry(new Country());
            }
        }
        ModelAndView mv = new ModelAndView("redirect:/home/message.html?messageCode=jugger.registration.sentMail");
        Utilities.addMessageArguments(mv, jc.getJugger().getEmail());
        status.setComplete();
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(@ModelAttribute(JUGGER_ATTRIBUTE) NewJugger jugger) {
        return FORM_VIEW;
    }

    @ModelAttribute(JUGGER_ATTRIBUTE)
    public NewJugger formBackingObject(WebRequest req) {
        NewJugger jc = Utilities.newJuggerCaptcha();
        jc.setCaptchaId(req.getSessionId());
        jc.setCaptchaService(captchaService);
        jc.setRequireReliability(new RequireReliability());
        return jc;
    }
}
