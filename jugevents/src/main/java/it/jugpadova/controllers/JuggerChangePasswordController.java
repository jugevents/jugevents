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
import it.jugpadova.exception.UserNotEnabledException;
import it.jugpadova.po.Jugger;
import it.jugpadova.util.Utilities;


import org.apache.log4j.Logger;
import org.parancoe.web.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springmodules.validation.bean.BeanValidator;

@Controller
@RequestMapping("/jugger/changePassword.form")
@SessionAttributes({JuggerChangePasswordController.JUGGER_ATTRIBUTE,
    JuggerChangePasswordController.ENABLE_JUGGER_ATTRIBUTE
})
public class JuggerChangePasswordController {

    private static final Logger logger =
            Logger.getLogger(JuggerChangePasswordController.class);
    public static final String FORM_VIEW = "pwdrecovery/setpwd";
    public static final String JUGGER_ATTRIBUTE = "jugger";
    public static final String ENABLE_JUGGER_ATTRIBUTE = "enablejugger";
    @Autowired
    private JuggerBo juggerBo;

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view = FORM_VIEW, attributes = {ENABLE_JUGGER_ATTRIBUTE})
    protected ModelAndView send(@ModelAttribute(JUGGER_ATTRIBUTE) Jugger jugger,
            @ModelAttribute(ENABLE_JUGGER_ATTRIBUTE) EnableJugger enableJugger,
            BindingResult result, SessionStatus status) {
        try {
            status.setComplete();
            juggerBo.changePassword(jugger, enableJugger.getPassword());
        } catch (UserNotEnabledException uaee) {
            logger.info("Trying to change " + jugger.getUser().getUsername() +
                    " password, but this user is disabled");
            return Utilities.getMessageView("jugger.pwdchng.failed.disabled", jugger.getUser().
                    getUsername());
        } catch (Exception e) {
            logger.error(e, e);
            return Utilities.getMessageView("jugger.pwdchng.failed");
        }
        ModelAndView mv =
                new ModelAndView(
                "redirect:/home/message.html?messageCode=jugger.pwdchng.success");
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(@RequestParam("username") String username,
            @RequestParam("code") String changePasswordCode, Model model) throws 
            Exception {
        Jugger jugger = juggerBo.searchByUsernameAndChangePasswordCode(username,
                changePasswordCode);
        if (jugger == null) {
            logger.warn("Trying to change the password of the " + username +
                    " user, but it doesn't exist or the change password code (" +
                    changePasswordCode + ") doesn't correspond");
            throw new Exception("The " + username +
                    " user doesn't exist, or the change password code (" +
                    changePasswordCode + ") doesn't correspond");
        }
        model.addAttribute(JUGGER_ATTRIBUTE, jugger);
        model.addAttribute(ENABLE_JUGGER_ATTRIBUTE, new EnableJugger());
        return FORM_VIEW;
    }
}
