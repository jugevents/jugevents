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

import it.jugpadova.bean.NewJugger;
import it.jugpadova.bean.RequireReliability;
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
import it.jugpadova.dao.JUGDao;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Jugger;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.parancoe.plugins.security.UserDao;
import org.parancoe.web.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

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
    @Resource
    private JuggerDao juggerDao;
    @Resource
    private UserDao userDao;
    @Resource
    private JUGDao jugDao;

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(byte[].class,
                new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view = FORM_VIEW, continueOnErrors = true)
    protected ModelAndView save(HttpServletRequest req,
            @ModelAttribute(JUGGER_ATTRIBUTE) NewJugger jc,
            BindingResult result, SessionStatus status) throws IOException {
        final Jugger jugger = jc.getJugger();
        validateJugger(jugger, result);
        if (result.hasErrors()) {
            return new ModelAndView(FORM_VIEW);
        }
        juggerBo.newJugger(jugger, Utilities.getBaseUrl(req),
                jc.getRequireReliability().isRequireReliability(),
                jc.getRequireReliability().getComment());
        if (jugger.getJug().getCountry() == null) {
            jugger.getJug().setCountry(new Country());
        }
        ModelAndView mv =
                new ModelAndView(
                "redirect:/home/message.html?messageCode=jugger.registration.sentMail");
        Utilities.addMessageArguments(mv, jugger.getEmail());
        status.setComplete();
        return mv;
    }

    private void validateJugger(final Jugger jugger, BindingResult result) {
        if (jugger != null) {
            if (jugger.getUser() != null && StringUtils.isNotBlank(jugger.
                    getUser().getUsername())) {
                // check if username is already presents
                String username = jugger.getUser().getUsername();
                if (userDao.findByUsername(username) != null) {
                    result.rejectValue("jugger.user.username",
                            "useralreadypresents",
                            "User with username: " + username
                            + " already presents in the database!");
                }
            }
            if (StringUtils.isNotBlank(jugger.getEmail())) {
                // check if it exists yet a jugger with the same email
                Jugger prevJugger = juggerDao.findByEmail(jugger.getEmail());
                if (prevJugger != null) {
                    result.rejectValue("jugger.email", "emailalreadypresent",
                            "An user tried to register with an email that exists yet");
                }
            }
            JUG newJUG = jugger.getJug();
            if (newJUG != null) {
                JUG jug = jugDao.findByName(newJUG.getName());
                if (jug == null) { // it's a new JUG
                    final String internalFriendlyName =
                            newJUG.getInternalFriendlyName();
                    if (StringUtils.isNotBlank(internalFriendlyName)) {
                        JUG friendlyJug = jugDao.findByInternalFriendlyName(
                                internalFriendlyName);
                        if (friendlyJug != null) {
                            result.rejectValue("jugger.jug.internalFriendlyName",
                                    "friendlyNameAlreadyPresent",
                                    "An user tried to create a JUG with a friendly name that exists yet");
                        }
                    }
                }
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(WebRequest req, Model model) {
        NewJugger jc = Utilities.newJuggerCaptcha();
        jc.setCaptchaId(req.getSessionId());
        jc.setCaptchaService(captchaService);
        jc.setRequireReliability(new RequireReliability());
        model.addAttribute(JUGGER_ATTRIBUTE, jc);
        return FORM_VIEW;
    }
}
