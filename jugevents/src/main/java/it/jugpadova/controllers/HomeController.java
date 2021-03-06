// Copyright 2006-2007 The Parancoe Team
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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.parancoe.web.util.FlashHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home/*.html")
public class HomeController {

    private static final Logger logger = Logger.getLogger(HomeController.class);

    @RequestMapping
    public ModelAndView welcome(HttpServletRequest req, HttpServletResponse res) {
        Map params = new HashMap();
        params.put("something", new Object());
        FlashHelper.setRedirectError(req, "flash.TestError");
        FlashHelper.setRedirectNotice(req, "flash.TestNotice");
        return new ModelAndView("welcome", params);
    }

    /**
     * Login action
     */
    @RequestMapping
    public ModelAndView login(HttpServletRequest req,
            HttpServletResponse res) {
        Map params = new HashMap();
        return new ModelAndView("login", params);
    }

    /**
     * Access denied
     */
    @RequestMapping
    public ModelAndView accessDenied(HttpServletRequest req,
            HttpServletResponse res) {
        Map params = new HashMap();
        return new ModelAndView("accessDenied", params);
    }

    @RequestMapping
    public ModelAndView message(HttpServletRequest req,
            HttpServletResponse res) {
        ModelAndView mv = new ModelAndView("message");
        mv.addObject("messageCode", req.getParameter("messageCode"));
        mv.addObject("messageArguments", req.getParameter("messageArguments"));
        return mv;
    }
    
    @RequestMapping
    public String page404() {
        return "404";
    }

    @RequestMapping
    public String page500() {
        return "500";
    }

}
