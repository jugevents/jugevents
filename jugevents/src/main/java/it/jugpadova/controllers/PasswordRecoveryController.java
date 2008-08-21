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

import it.jugpadova.bean.PasswordRecovery;
import it.jugpadova.blo.JuggerBo;
import it.jugpadova.po.Jugger;
import it.jugpadova.util.Utilities;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.parancoe.web.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for password recovery.
 *
 * @author Enrico Giurin
 *
 */
@Controller
@RequestMapping("/passwordRecovery.form")
@SessionAttributes(PasswordRecoveryController.PASSWORD_RECOVERY_ATTRIBUTE)
public class PasswordRecoveryController {

    private static final Logger logger =
            Logger.getLogger(PasswordRecoveryController.class);
    public static final String FORM_VIEW = "pwdrecovery/passwordRecovery";
    public static final String PASSWORD_RECOVERY_ATTRIBUTE = "passwordRecovery";
    @Autowired
    private JuggerBo juggerBo;

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view = FORM_VIEW)
    protected ModelAndView send(
            @ModelAttribute(PASSWORD_RECOVERY_ATTRIBUTE) PasswordRecovery passwordRecovery,
            BindingResult result, HttpServletRequest req, SessionStatus status) throws Exception {

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
        status.setComplete();
        return mv;
    }

    @ModelAttribute(PASSWORD_RECOVERY_ATTRIBUTE)
    @RequestMapping(method = RequestMethod.GET)
    protected String form(Model model) throws
            Exception {
        model.addAttribute(PASSWORD_RECOVERY_ATTRIBUTE, new PasswordRecovery());
        return FORM_VIEW;
    }
}

