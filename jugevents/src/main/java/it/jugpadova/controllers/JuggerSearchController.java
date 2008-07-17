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

import it.jugpadova.bean.JuggerSearch;
import it.jugpadova.blo.JuggerBo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to manage search actions on Jugger
 * @author Enrico Giurin
 *
 */
@Controller
@RequestMapping("/adminjugger/juggersearch.form")
public class JuggerSearchController {
    @Autowired
    private JuggerBo juggerBo;

    @ModelAttribute("juggerSearch")
    public JuggerSearch createBackingObject() {
        return new JuggerSearch();
    }

    @RequestMapping
    public ModelAndView search(
            @ModelAttribute("juggerSearch") JuggerSearch juggerSearch) {
        ModelAndView mv = new ModelAndView("jugger/admin/juggers");
        mv.addObject("juggers", juggerBo.searchJugger(juggerSearch));
        return mv;
    }
}
