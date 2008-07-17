// Copyright 2006-2007 The JUG Events Team
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
import it.jugpadova.po.Jugger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Admin
 *
 */
@Controller
@RequestMapping("/jugger/*.html")
public class JuggerController {

    private static Logger logger =
            Logger.getLogger(JuggerController.class);

    @Autowired
    private JuggerBo juggerBo;
    
    @RequestMapping
    public ModelAndView confirmUpdateJugger(HttpServletRequest req,
            HttpServletResponse res) {
        Long id = new Long(req.getParameter("id"));
        Jugger jugger = juggerBo.retrieveJugger(id);
        ModelAndView mv =
                new ModelAndView("jugger/confirmUpdateJugger");
        mv.addObject("jugger", jugger);
        return mv;
    }

}
