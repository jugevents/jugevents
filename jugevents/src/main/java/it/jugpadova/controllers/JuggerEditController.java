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

import it.jugpadova.bean.EditJugger;
import it.jugpadova.bean.RequireReliability;
import it.jugpadova.bean.TimeZoneBean;
import it.jugpadova.blo.JuggerBo;
import it.jugpadova.blo.ServicesBo;
import it.jugpadova.exception.ParancoeAccessDeniedException;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Jugger;

import it.jugpadova.util.SecurityUtilities;
import it.jugpadova.util.Utilities;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.FixedDateTimeZone;
import org.parancoe.plugins.security.User;
import org.parancoe.plugins.world.Country;
import org.parancoe.web.validation.Validation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Controller for editing Jugger informations.
 *
 * @author Enrico Giurin
 *
 */
@Controller
@RequestMapping("/jugger/edit.form")
@SessionAttributes(JuggerEditController.JUGGER_ATTRIBUTE)
public class JuggerEditController {

    private static final Logger logger =
            Logger.getLogger(JuggerEditController.class);
    public static final String FORM_VIEW = "jugger/editJugger";
    public static final String JUGGER_ATTRIBUTE = "jugger";
    @Autowired
    private JuggerBo juggerBo;
    @Autowired
    private ServicesBo servicesBo;

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(byte[].class,
                new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(method = RequestMethod.POST)
    @Validation(view=FORM_VIEW)
    protected ModelAndView save(HttpServletRequest req,
            @ModelAttribute(JUGGER_ATTRIBUTE) EditJugger ej,
            BindingResult result, SessionStatus status) throws IOException {

        if (StringUtils.isNotBlank(ej.getPassword())) {
            ej.getJugger().getUser().setPassword(SecurityUtilities.encodePassword(
                    ej.getPassword(), ej.getJugger().getUser().getUsername()));
        }
        juggerBo.update(ej.getJugger(), ej.getRequireReliability().
                isRequireReliability(), ej.getRequireReliability().getComment(),
                Utilities.getBaseUrl(req));
        ModelAndView mv = new ModelAndView("redirect:/jugger/edit.form");
        mv.addObject("jugger.user.username",
                ej.getJugger().getUser().getUsername());
        Utilities.addMessageCode(mv, "juggerUpdateSuccessful");
        status.setComplete();
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected String form(@RequestParam("jugger.user.username") String username, Model model) {
        if (!servicesBo.checkAuthorization(username)) {
            throw new ParancoeAccessDeniedException("Forbidden access to user identified by " +
                    username);
        }
        EditJugger ej = new EditJugger();
        Jugger jugger = juggerBo.searchByUsername(username);
        Jugger newJugger = new Jugger();
        BeanUtils.copyProperties(jugger, newJugger, new String[]{"jug", "user"});
        User newUser = new User();
        BeanUtils.copyProperties(jugger.getUser(), newUser);
        newJugger.setUser(newUser);
        JUG newJug = new JUG();
        BeanUtils.copyProperties(jugger.getJug(), newJug,
                new String[]{"country"});
        newJugger.setJug(newJug);
        Country newCountry = new Country();
        if (jugger.getJug().getCountry() != null) {
            BeanUtils.copyProperties(jugger.getJug().getCountry(), newCountry);
        }
        newJug.setCountry(newCountry);
        ej.setJugger(newJugger);
        ej.setRequireReliability(new RequireReliability());
        ej.setReliable(servicesBo.isJuggerReliable(
                jugger.getReliability()));
        model.addAttribute(JUGGER_ATTRIBUTE, ej);
        return FORM_VIEW;
    }

    @ModelAttribute("timezones")
    protected List<TimeZoneBean> getTimezones(HttpServletRequest req) throws
            Exception {
        RequestContext rc = (RequestContext) req.getAttribute("requestContext");
        List<TimeZoneBean> timezones = new ArrayList();
        Date now = new Date();
        for (int i = -12; i <= 12; i++) {
            DateTimeZone fdtz = FixedDateTimeZone.forOffsetHours(i);
            timezones.add(new TimeZoneBean(fdtz.getID(), fdtz.getShortName(
                    now.getTime(), rc.getLocale())));
        }
        return timezones;
    }
} // end of class
