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

import it.jugpadova.bean.PasswordRecovery;
import it.jugpadova.blo.JuggerBo;
import it.jugpadova.po.Jugger;
import it.jugpadova.util.Utilities;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.bean.BeanValidator;

/**
 * Controller for password recovery.
 *
 * @author Enrico Giurin
 *
 */
@Controller
@RequestMapping("/passwordRecovery.form")
public class PasswordRecoveryController {

    private static final Logger logger =
            Logger.getLogger(PasswordRecoveryController.class);
    public static final String FORM_VIEW = "pwdrecovery/passwordRecovery";
    public static final String PASSWORD_RECOVERY_ATTRIBUTE = "passwordRecovery";
    @Autowired
    private JuggerBo juggerBo;
    @Autowired
    @Qualifier("validator")
    private BeanValidator validator;

    @RequestMapping(method = RequestMethod.POST)
    protected ModelAndView send(
            @ModelAttribute(PASSWORD_RECOVERY_ATTRIBUTE) PasswordRecovery passwordRecovery,
            BindingResult result, HttpServletRequest req) throws Exception {

        validator.validate(passwordRecovery, result);
        if (result.hasErrors()) {
            return new ModelAndView(FORM_VIEW);
        }

        String email = passwordRecovery.getEmail();
        logger.debug("email: " + email);
        Jugger jugger = juggerBo.searchByEmail(email);
        if (jugger == null) {
            result.rejectValue("email", "juggerNotFoundByEmail");
            return new ModelAndView(FORM_VIEW);
        }
        if (jugger.getUser().isEnabled() == false) {
            result.rejectValue("email", "juggerBlocked");
            return new ModelAndView(FORM_VIEW);
        }
        juggerBo.passwordRecovery(jugger, Utilities.getBaseUrl(req));
        ModelAndView mv =
                new ModelAndView(
                "redirect:/home/message.html?messageCode=jugger.pwdchng.sentMail");
        Utilities.addMessageArguments(mv, jugger.getEmail());
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(
            @ModelAttribute(PASSWORD_RECOVERY_ATTRIBUTE) PasswordRecovery passwordRecovery) {
        return FORM_VIEW;
    }

    @ModelAttribute(PASSWORD_RECOVERY_ATTRIBUTE)
    protected PasswordRecovery formBackingObject(HttpServletRequest req) throws
            Exception {
        return new PasswordRecovery();
    }
}

