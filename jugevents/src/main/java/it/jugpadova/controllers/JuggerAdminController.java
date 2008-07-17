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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for jugger administration functionalities.
 * 
 * @author Enrico Giurin
 * 
 */
@Controller
@RequestMapping("/adminjugger/*.html")
public class JuggerAdminController {

    private static Logger logger = Logger.getLogger(JuggerAdminController.class);
    @Autowired
    private JuggerBo juggerBo;

    /**
     * Delete jugger.
     * 
     * @param req
     * @param res
     * @return
     */
    @RequestMapping
    public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        juggerBo.delete(username);
        return new ModelAndView("redirect:/adminjugger/juggersearch.form");

    }

    /**
     * Jugger details
     * 
     * @param req
     * @param res
     * @return
     */
    @RequestMapping
    public ModelAndView viewJugger(HttpServletRequest req,
            HttpServletResponse res) {
        String username = req.getParameter("username");
        ModelAndView mv = new ModelAndView("jugger/admin/viewJugger");
        mv.addObject("jugger", juggerBo.searchByUsername(username));
        return mv;
    }

    /**
     * Enable Jugger
     * 
     * @param req
     * @param res
     * @return
     */
    @RequestMapping
    public ModelAndView enableJugger(HttpServletRequest req,
            HttpServletResponse res) {
        String username = req.getParameter("username");
        juggerBo.enableJugger(username);
        return new ModelAndView("redirect:/adminjugger/juggersearch.form");

    }

    /**
     * Disable jugger.
     * 
     * @param req
     * @param res
     * @return
     */
    @RequestMapping
    public ModelAndView disableJugger(HttpServletRequest req,
            HttpServletResponse res) {
        String username = req.getParameter("username");
        juggerBo.disableJugger(username);
        return new ModelAndView("redirect:/adminjugger/juggersearch.form");

    }
}
