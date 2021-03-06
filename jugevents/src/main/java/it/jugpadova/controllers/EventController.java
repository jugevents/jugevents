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

import it.jugpadova.bean.EventSearch;
import it.jugpadova.bean.Registration;
import it.jugpadova.blo.EventBo;
import it.jugpadova.exception.ParancoeAccessDeniedException;
import it.jugpadova.po.Event;
import it.jugpadova.po.Participant;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.io.WireFeedOutput;
import it.jugpadova.blo.ParticipantBadgeBo;
import it.jugpadova.blol.FeedsBo;
import it.jugpadova.blol.ServicesBo;
import it.jugpadova.dao.EventDao;
import it.jugpadova.dao.ParticipantDao;
import it.jugpadova.util.Utilities;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/event/*.html")
public class EventController {

    private static Logger logger =
            Logger.getLogger(EventController.class);
    @Autowired
    private EventDao eventDao;
    @Autowired
    private ParticipantDao participantDao;
    @Autowired
    private EventBo eventBo;
    @Autowired
    private ServicesBo servicesBo;
    @Resource
    private FeedsBo feedsBo;
    @Resource
    private ParticipantBadgeBo participantBadgeBo;

    @RequestMapping
    public ModelAndView list(HttpServletRequest req,
            HttpServletResponse res) {
        ModelAndView mv = new ModelAndView("event/list");
        mv.addObject("news",
                eventBo.buildNewsMessages(Utilities.getBaseUrl(req)));
        return mv;
    }

    @RequestMapping
    public ModelAndView delete(HttpServletRequest req,
            HttpServletResponse res) {
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Event event = eventBo.retrieveEvent(id);
            if (event == null) {
                throw new IllegalArgumentException("No event with id " + id);
            }
            eventBo.checkUserAuthorization(event);
            eventDao.delete(event);
            logger.info("User " + servicesBo.authenticatedUsername()
                    + " deleted event with id=" + id);
        } catch (ParancoeAccessDeniedException pade) {
            throw pade;
        }
        return new ModelAndView("redirect:search.form");
    }

    @RequestMapping
    public ModelAndView show(
            @RequestParam(value = "id", required = true) Long id) {
        ModelAndView mv =
                new ModelAndView("event/show");
        Event event = eventBo.retrieveEvent(id);
        if (event == null) {
            throw new IllegalArgumentException("No event with id " + id);
        }
        mv.addObject("event", event);
        mv.addObject("canCurrentUserManageEvent",
                servicesBo.canCurrentUserManageEvent(event));
        mv.addObject("pageTitle", event.getTitle());
        return mv;
    }

    @RequestMapping
    public ModelAndView participants(HttpServletRequest req,
            HttpServletResponse res) {
        ModelAndView mv =
                new ModelAndView("event/participants");
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Event event = eventBo.retrieveEvent(id);
            if (event == null) {
                throw new IllegalArgumentException("No event with id " + id);
            }
            eventBo.checkUserAuthorization(event);
            List<Participant> participants =
                    eventBo.searchConfirmedParticipantsByEventId(event.getId());
            List<Participant> participantsNotConfirmed =
                    eventBo.searchNotConfirmedParticipantsByEventId(
                    event.getId());
            List<Participant> participantsCancelled =
                    eventBo.searchCancelledParticipantsByEventId(event.getId());

            mv.addObject("event", event);
            mv.addObject("participants", participants);
            mv.addObject("participantsNotConfirmed", participantsNotConfirmed);
            mv.addObject("participantsCancelled", participantsCancelled);
            Registration registration = new Registration();
            registration.setEvent(event);
            registration.setParticipant(new Participant());
            mv.addObject("registration", registration);
        } catch (ParancoeAccessDeniedException pade) {
            throw pade;
        }
        return mv;
    }

    @RequestMapping
    public String showParticipants(@RequestParam("id") Long id,
            HttpServletRequest req,
            HttpServletResponse res, Model model) {
        Event event = eventBo.retrieveEvent(id);
        if (event == null) {
            throw new IllegalArgumentException("No event with id " + id);
        }
        eventBo.checkShowParticipants(event);
        List<Participant> participants =
                eventBo.searchConfirmedParticipantsByEventId(event.getId());
        model.addAttribute("event", event);
        model.addAttribute("participants", participants);
        return "event/showParticipants";
    }

    @RequestMapping
    public void printBadges(
            @RequestParam(value = "id", required = false) Long id,
            HttpServletResponse res) {
        Event event = null;
        if (id != null) {
            event = eventBo.retrieveEvent(id);
            if (event == null) {
                throw new IllegalArgumentException("No event with id " + id);
            }
            eventBo.checkUserAuthorization(event);
        } else {
            // it was requested a simple empty preview
            event = new Event();
            event.setId(-1L);
            event.setTitle("Sample Event");
        }
        OutputStream out = null;
        try {
            byte[] pdfBytes = participantBadgeBo.buildPDFBadges(event);
            out = res.getOutputStream();
            res.setContentType("application/pdf");
            res.setContentLength(pdfBytes.length);
            res.setHeader("Content-Disposition",
                    " attachment; filename=\"" + event.getTitle()
                    + "_badges.pdf\"");
            res.setHeader("Expires", "0");
            res.setHeader("Cache-Control",
                    "must-revalidate, post-check=0, pre-check=0");
            res.setHeader("Pragma", "public");
            out.write(pdfBytes);
            out.flush();
        } catch (Exception ex) {
            logger.error("Can't build PDF badges for " + event.getTitle(), ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    /**
     * Delete a participant
     * 
     * @param id The id of the particpant to delete.
     * @return With success, back to the list of participants.
     */
    @RequestMapping
    public String deleteParticipant(@RequestParam("id") Long id) {
        Participant participant = participantDao.read(id);
        if (participant == null) {
            throw new IllegalArgumentException("No participant with id " + id);
        }
        final Event event = participant.getEvent();
        eventBo.checkUserAuthorization(event);
        participantDao.delete(participant);
        logger.info("Deleted participant " + participant.getFirstName() + " " + participant.
                getLastName() + " (" + participant.getEmail() + ", " + (participant.
                getConfirmed()
                ? "confirmed" : "not confirmed") + ") from the event " + event.
                getId());
        return "redirect:/event/participants.html?id=" + event.getId();
    }

    @RequestMapping
    public ModelAndView winners(HttpServletRequest req,
            HttpServletResponse res) {
        ModelAndView mv =
                new ModelAndView("event/winners");
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Event event = eventBo.retrieveEvent(id);
            if (event == null) {
                throw new IllegalArgumentException("No event with id " + id);
            }
            eventBo.checkUserAuthorization(event);
            mv.addObject("event", event);
            List<Participant> nonWinningParticipants =
                    eventBo.searchNonwinningParticipantsByEventId(id);
            mv.addObject("nonWinningParticipants", nonWinningParticipants);
            List<Participant> winningParticipants =
                    eventBo.searchWinningParticipantsByEventId(id);
            mv.addObject("winningParticipants", winningParticipants);
        } catch (ParancoeAccessDeniedException pade) {
            throw pade;
        }
        return mv;
    }

    @RequestMapping
    public ModelAndView rss(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        try {
            EventSearch eventSearch = buildEventSearch(req);
            List<Event> events = eventBo.search(eventSearch);
            Channel channel = feedsBo.buildChannel(events,
                    Utilities.getBaseUrl(req), buildChannelLink(req));
            // flush it in the res
            WireFeedOutput wfo = new WireFeedOutput();
            res.setHeader("Cache-Control", "no-store");
            res.setHeader("Pragma", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setContentType("text/xml");
            res.setCharacterEncoding("UTF-8");
            ServletOutputStream resOutputStream = res.getOutputStream();
            wfo.output(channel,
                    new OutputStreamWriter(resOutputStream, "UTF-8"));
            resOutputStream.flush();
            resOutputStream.close();
        } catch (Exception exception) {
            logger.error("Error producing RSS", exception);
            throw exception;
        }
        return null;
    }

    private String buildChannelLink(HttpServletRequest req) {
        StringBuffer channelLink = req.getRequestURL();
        if (req.getQueryString() != null) {
            channelLink.append('?').append(req.getQueryString());
        }
        return channelLink.toString();
    }

    @RequestMapping
    public ModelAndView badge(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        try {
            String locale = req.getParameter("lang");
            if (StringUtils.isBlank(locale)) {
                locale = (String) req.getAttribute("lang");
            }
            if (StringUtils.isBlank(locale)) {
                locale = "en";
            }
            java.text.DateFormat dateFormat =
                    java.text.DateFormat.getDateInstance(
                    java.text.DateFormat.DEFAULT,
                    new Locale(locale));
            String baseUrl = Utilities.getBaseUrl(req);
            EventSearch eventSearch = buildEventSearch(req);
            List<Event> events = eventBo.search(eventSearch);
            boolean showJUGName =
                    Boolean.parseBoolean(req.getParameter("jeb_showJUGName"));
            boolean showCountry =
                    Boolean.parseBoolean(req.getParameter("jeb_showCountry"));
            boolean showDescription =
                    Boolean.parseBoolean(req.getParameter("jeb_showDescription"));
            boolean showParticipants =
                    Boolean.parseBoolean(
                    req.getParameter("jeb_showParticipants"));
            String badgeStyle = req.getParameter("jeb_style");
            String result =
                    eventBo.getBadgeCode(eventBo.getBadgeHtmlCode(events,
                    dateFormat, baseUrl, showJUGName, showCountry,
                    showDescription, showParticipants, badgeStyle, locale));
            // flush it in the res
            res.setHeader("Cache-Control", "no-store");
            res.setHeader("Pragma", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setContentType("text/javascript");
            ServletOutputStream resOutputStream = res.getOutputStream();
            Writer writer =
                    new OutputStreamWriter(resOutputStream, "UTF-8");
            writer.write(result);
            writer.flush();
            writer.close();
        } catch (Exception exception) {
            logger.error("Error producing badge", exception);
            throw exception;
        }
        return null;
    }

    @RequestMapping
    public ModelAndView resources(HttpServletRequest req,
            HttpServletResponse res) {
        ModelAndView mv =
                new ModelAndView("event/resources");
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Event event = eventBo.retrieveEvent(id);
            if (event == null) {
                throw new IllegalArgumentException("No event with id " + id);
            }
            eventBo.checkUserAuthorization(event);
            mv.addObject("event", event);
        } catch (ParancoeAccessDeniedException pade) {
            throw pade;
        }
        return mv;
    }

    public Logger getLogger() {
        return logger;
    }

    @RequestMapping
    public ModelAndView ics(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        try {
            EventSearch eventSearch = buildEventSearch(req);
            List<Event> events = eventBo.search(eventSearch);

            Calendar calendar = feedsBo.buildCalendar(events, Utilities.
                    getBaseUrl(req));

            // flush it in the res
            res.setContentType("text/calendar");
            res.setHeader("Content-Disposition",
                    " attachment; filename=\"JUGEventsCalendar.ics\"");
            res.setHeader("Expires", "0");
            res.setHeader("Cache-Control",
                    "must-revalidate, post-check=0, pre-check=0, no-store");
            res.setHeader("Pragma", "public, no-cache");
            res.setCharacterEncoding("UTF-8");

            CalendarOutputter outputter = new CalendarOutputter(true);
            ServletOutputStream resOutputStream = res.getOutputStream();
            if (calendar.getComponents().isEmpty()) {
                outputter.setValidating(false);
            }
            outputter.output(calendar, resOutputStream);
            resOutputStream.flush();
            resOutputStream.close();
        } catch (Exception exception) {
            logger.error("Error producing ICS", exception);
            throw exception;
        }
        return null;
    }

    @RequestMapping
    public ModelAndView json(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        try {
            EventSearch eventSearch = buildEventSearch(req);
            List<Event> events = eventBo.search(eventSearch);
            String json =
                    feedsBo.buildJson(events,
                    Utilities.getBaseUrl(req), true);
            // flush it in the res
            res.setHeader("Cache-Control",
                    "must-revalidate, post-check=0, pre-check=0, no-store");
            res.setHeader("Pragma", "public, no-cache");
            res.setDateHeader("Expires", 0);
            res.setContentType("application/json");
            res.setContentLength(json.getBytes().length);
            res.setCharacterEncoding("UTF-8");
            ServletOutputStream resOutputStream = res.getOutputStream();
            Writer writer =
                    new OutputStreamWriter(resOutputStream, "UTF-8");
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (Exception exception) {
            logger.error("Error producing JSON", exception);
            throw exception;
        }
        return null;
    }

    private EventSearch buildEventSearch(HttpServletRequest req) throws
            ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        EventSearch eventSearch = new EventSearch();
        eventSearch.setContinent(req.getParameter("continent"));
        eventSearch.setCountry(req.getParameter("country"));
        eventSearch.setJugName(req.getParameter("jugName"));
        eventSearch.setPastEvents(java.lang.Boolean.parseBoolean(req.
                getParameter(
                "pastEvents")));
        eventSearch.setOrderByDate(req.getParameter("order"));
        String maxResults = req.getParameter("maxResults");
        if (StringUtils.isNotBlank(maxResults)) {
            try {
                eventSearch.setMaxResults(new Integer(maxResults));
            } catch (NumberFormatException numberFormatException) {
                /* ignore it */
            }
        }
        eventSearch.setFriendlyName(req.getParameter("friendlyName"));
        String startDate = req.getParameter("startDate");
        if (StringUtils.isNotBlank(startDate)) {
            Date date = df.parse(startDate);
            eventSearch.setStartDate(date);
        }
        String endDate = req.getParameter("endDate");
        if (StringUtils.isNotBlank(endDate)) {
            Date date = df.parse(endDate);
            eventSearch.setEndDate(date);
        }
        String startTimestamp = req.getParameter("start");
        if (StringUtils.isNotBlank(startTimestamp)) {
            Date date = new Date(Long.parseLong(startTimestamp) * 1000);
            eventSearch.setStartDate(date);
        }
        String endTimestamp = req.getParameter("end");
        if (StringUtils.isNotBlank(endTimestamp)) {
            Date date = new Date(Long.parseLong(endTimestamp) * 1000);
            eventSearch.setEndDate(date);
        }
        return eventSearch;
    }
}
