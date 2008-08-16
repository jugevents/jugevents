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

import it.jugpadova.blo.JuggerBo;
import it.jugpadova.blo.ServicesBo;
import it.jugpadova.po.Jugger;
import it.jugpadova.util.Utilities;

import javax.servlet.http.HttpServletRequest;

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

/**
 * Controller for managing reliability of the jugger. 
 * @author Enrico Giurin
 *
 */
@Controller
@RequestMapping("/adminjugger/reliability.form")
@SessionAttributes(ReliabilityEditController.ATTRIBUTE_JUGGER)
public class ReliabilityEditController {

    public static final String ATTRIBUTE_JUGGER = "jugger";
    public static final String FORM_VIEW = "jugger/admin/reliability";
    @Autowired
    private JuggerBo juggerBo;
    @Autowired
    private ServicesBo servicesBo;

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view = FORM_VIEW)
    protected String update(HttpServletRequest request,
            @ModelAttribute(ATTRIBUTE_JUGGER) Jugger jugger,
            BindingResult result, SessionStatus status) {
        servicesBo.updateReliability(jugger, Utilities.getBaseUrl(request));
        status.setComplete();
        return "redirect:/adminjugger/juggersearch.form";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(@RequestParam("jugger.user.username") String username,
            Model model) {
        Jugger jugger = juggerBo.searchByUsername(username);
        model.addAttribute(ATTRIBUTE_JUGGER, jugger);
        return FORM_VIEW;
    }
}
