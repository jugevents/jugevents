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
import it.jugpadova.po.ReliabilityRequest;
import it.jugpadova.util.RRStatus;
import it.jugpadova.util.Utilities;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Controller for managing reliability of the jugger. 
 * @author Enrico Giurin
 *
 */
@Controller
@RequestMapping("/adminjugger/reliability.form")
@SessionAttributes("jugger")
public class ReliabilityEditController {

    public static final String FORM_VIEW = "jugger/admin/reliability";
    @Autowired
    private JuggerBo juggerBo;
    @Autowired
    private ServicesBo servicesBo;

    @RequestMapping(method = RequestMethod.POST)
    protected String update(HttpServletRequest request,
            @ModelAttribute("jugger") Jugger jugger, BindingResult result) {
        servicesBo.updateReliability(jugger, Utilities.getBaseUrl(request));
        return "redirect:/adminjugger/juggersearch.form";

    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(@ModelAttribute("jugger") Jugger jugger) {
        return FORM_VIEW;
    }

    @ModelAttribute("jugger")
    protected Jugger formBackingObject(
            @RequestParam("jugger.user.username") String username) {
        Jugger jugger = juggerBo.searchByUsername(username);
        if (jugger != null & jugger.getReliabilityRequest() == null) {
            ReliabilityRequest reliabilityRequest = new ReliabilityRequest();
            reliabilityRequest.setStatus(RRStatus.NOT_REQUIRED.value);
            jugger.setReliabilityRequest(reliabilityRequest);
        }
        return jugger;
    }
}
