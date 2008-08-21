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

import it.jugpadova.bean.EnableJugger;
import it.jugpadova.blo.JuggerBo;
import it.jugpadova.exception.UserAlreadyEnabledException;
import it.jugpadova.po.Jugger;
import it.jugpadova.util.Utilities;

import org.apache.log4j.Logger;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/jugger/enable.form")
@SessionAttributes({JuggerEnableController.JUGGER_ATTRIBUTE,
    JuggerEnableController.ENABLE_JUGGER_ATTRIBUTE
})
public class JuggerEnableController {

    private static final Logger logger =
            Logger.getLogger(JuggerEnableController.class);
    public static final String FORM_VIEW = "jugger/registration/setpwd";
    public static final String JUGGER_ATTRIBUTE = "jugger";
    public static final String ENABLE_JUGGER_ATTRIBUTE = "enablejugger";
    @Autowired
    private JuggerBo juggerBo;

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view=FORM_VIEW, attributes={ENABLE_JUGGER_ATTRIBUTE})
    protected ModelAndView send(@ModelAttribute(JUGGER_ATTRIBUTE) Jugger jugger,
            @ModelAttribute(ENABLE_JUGGER_ATTRIBUTE) EnableJugger enableJugger,
            BindingResult result, SessionStatus status) {

        try {
            status.setComplete();
            juggerBo.enableJugger(jugger, enableJugger.getPassword());
        } catch (UserAlreadyEnabledException uaee) {
            return Utilities.getMessageView("jugger.registration.already");
        } catch (Exception e) {
            logger.error(e, e);
            return Utilities.getMessageView("jugger.registration.failed");
        }
        ModelAndView mv =
                new ModelAndView(
                "redirect:/home/message.html?messageCode=jugger.registration.success");
        Utilities.addMessageArguments(mv, jugger.getFirstName());
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(@RequestParam("username") String username,
            @RequestParam("code") String confirmationCode, Model model) throws Exception {
        Jugger jugger =
                juggerBo.searchByUsernameAndConfirmationCode(username,
                confirmationCode);
        if (jugger == null) {
            logger.warn("Trying to enable " + username +
                    " user, but it doesn't exist or the confirmation code (" +
                    confirmationCode + ") doesn't correspond");
            throw new Exception("The " + username +
                    " user doesn't exist, or the confirmation code (" +
                    confirmationCode + ") doesn't correspond");
        }
        model.addAttribute(JUGGER_ATTRIBUTE, jugger);
        model.addAttribute(ENABLE_JUGGER_ATTRIBUTE, new EnableJugger());
        return FORM_VIEW;
    }
}
