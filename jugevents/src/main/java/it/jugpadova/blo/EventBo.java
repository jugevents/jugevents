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
package it.jugpadova.blo;

import it.jugpadova.Conf;
import it.jugpadova.bean.EventSearch;
import it.jugpadova.bean.NewsMessage;
import it.jugpadova.blol.ServicesBo;
import it.jugpadova.dao.EventDao;
import it.jugpadova.dao.ParticipantDao;
import it.jugpadova.exception.ParancoeAccessDeniedException;
import it.jugpadova.exception.RegistrationNotOpenException;
import it.jugpadova.po.Event;
import it.jugpadova.po.Jugger;
import it.jugpadova.po.Participant;
import it.jugpadova.util.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.providers.encoding.MessageDigestPasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.velocity.app.VelocityEngine;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.proxy.scriptaculous.Effect;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * Business logic for the event management.
 *
 * @author Lucio Benfante (<a href="lucio.benfante@jugpadova.it">lucio.benfante@jugpadova.it</a>)
 * @version $Revision: d4da88c905ea $
 */
@Component
@RemoteProxy(name = "eventBo")
public class EventBo {

    private static final Logger logger = Logger.getLogger(EventBo.class);
    @Autowired
    private ParticipantDao participantDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private ServicesBo servicesBo;
    
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Conf conf;

    public List<Participant> searchConfirmedParticipantsByEventId(Long id) {
        return participantDao.findConfirmedParticipantsByEventId(id);
    }

    public List<Participant> searchNonwinningParticipantsByEventId(Long id) {
        return participantDao.findNonwinningParticipantsByEventId(id);
    }

    public List<Participant> searchNotConfirmedParticipantsByEventId(Long id) {
        return participantDao.findNotConfirmedParticipantsByEventId(id);
    }

    public Participant searchParticipantById(Long participantId) {
        return participantDao.read(participantId);
    }

    public List<Participant> searchWinningParticipantsByEventId(Long id) {
        return participantDao.findWinningParticipantsByEventId(id);
    }

    public List<Participant> searchParticipantByEmailAndEventId(String email,
            Long id) {
        return participantDao.findParticipantByEmailAndEventId(email,
                id);
    }

    public List<Event> retrieveEvents() {
        List<Event> events = eventDao.findCurrentEvents();
        for (Event event : events) {
            event.getParticipants().size();
        }
        return events;
    }

    public List<Event> search(EventSearch eventSearch) {
        List<Event> events = new LinkedList<Event>();
        try {
            DetachedCriteria eventCriteria =
                    DetachedCriteria.forClass(Event.class);
            if (StringUtils.isNotBlank(eventSearch.getJugName()) ||
                    StringUtils.isNotBlank(eventSearch.getCountry()) ||
                    StringUtils.isNotBlank(eventSearch.getContinent())) {
                DetachedCriteria ownerCriteria =
                        eventCriteria.createCriteria("owner.jug");
                if (StringUtils.isNotBlank(eventSearch.getJugName())) {
                    ownerCriteria.add(Restrictions.ilike("name",
                            eventSearch.getJugName(), MatchMode.ANYWHERE));
                }
                if (StringUtils.isNotBlank(eventSearch.getCountry()) ||
                        StringUtils.isNotBlank(eventSearch.getContinent())) {
                    DetachedCriteria countryCriteria =
                            ownerCriteria.createCriteria("country");
                    if (StringUtils.isNotBlank(eventSearch.getCountry())) {
                        countryCriteria.add(Restrictions.or(Restrictions.ilike(
                                "englishName", eventSearch.getCountry(),
                                MatchMode.ANYWHERE), Restrictions.ilike(
                                "localName", eventSearch.getCountry(),
                                MatchMode.ANYWHERE)));
                    }
                    if (StringUtils.isNotBlank(eventSearch.getContinent())) {
                        DetachedCriteria continentCriteria =
                                countryCriteria.createCriteria("continent");
                        continentCriteria.add(Restrictions.ilike("name",
                                eventSearch.getContinent(), MatchMode.ANYWHERE));
                    }
                }
            }
            if (!eventSearch.isPastEvents()) {
                eventCriteria.add(Restrictions.ge("startDate", new Date()));
            }
            if ("desc".equals(eventSearch.getOrderByDate())) {
                eventCriteria.addOrder(Order.desc("startDate"));
                eventCriteria.addOrder(Order.desc("creationDate"));
            } else {
                eventCriteria.addOrder(Order.asc("startDate"));
                eventCriteria.addOrder(Order.asc("creationDate"));
            }
            if (eventSearch.getMaxResults() == null) {
                events = eventDao.searchByCriteria(eventCriteria);
            } else {
                events = eventDao.searchByCriteria(eventCriteria, 0,
                        eventSearch.getMaxResults().intValue());
            }
            for (Event event : events) {
                event.getParticipants().size();
            }
        } catch (Exception e) {
            logger.error("Error searching events", e);
        }

        return events;
    }

    /**
     * Execute a full text search on events.
     *
     * @param searchQuery The text to search
     * @param maxResults The max number of results. No limit if maxResults <= 0.
     * @return The list of events matching the query
     */
    public List<Event> search(String searchQuery, boolean pastEvents,
            int maxResults) throws ParseException {
        List<Event> result = null;
        Session session =
                this.eventDao.getHibernateTemplate().
                getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{
                    "title", "location", "directions", "description",
                    "startDate"
                },
                new StandardAnalyzer());
        org.apache.lucene.search.Query query = parser.parse(searchQuery);
        FullTextQuery hibQuery =
                fullTextSession.createFullTextQuery(query, Event.class);
      //  hibQuery.setSort(arg0)
        if (!pastEvents) {
            hibQuery.enableFullTextFilter("notPassedEvents");
        }
        if (maxResults > 0) {
            hibQuery.setMaxResults(maxResults);
        }
        result = hibQuery.list();
        return result;
    }

    /**
     * Ajax method for a full text search on events.
     *
     * @param searchQuery The text to search
     * @param maxResults The max number of results. No limit if maxResults <= 0.
     */
    @RemoteMethod
    public void fullTextSearch(String searchQuery, boolean pastEvents,
            String locale, int maxResults) {
        if (StringUtils.isNotBlank(searchQuery)) {
            WebContext wctx = WebContextFactory.get();
            HttpServletRequest req = wctx.getHttpServletRequest();
            ScriptSession session = wctx.getScriptSession();
            Util util = new Util(session);
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(
                    java.text.DateFormat.SHORT, new Locale(locale));
            java.lang.String baseUrl =
                    "http://" + req.getServerName() + ":" + req.getServerPort() +
                    req.getContextPath();
            List<Event> events = null;
            try {
                events = this.search(searchQuery, pastEvents, maxResults);
            } catch (ParseException pex) {
                logger.info("Error parsing query: " + searchQuery);
            } catch (Exception ex) {
                logger.error("Error searching events", ex);
            }
            if (events != null && events.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Event event : events) {
                    sb.append("<div>\n");
                    sb.append("<div class=\"eventDate\">").append(dateFormat.format(
                            event.getStartDate())).
                            append("</div>");
                    if (event.getOwner() != null) {
                        sb.append("<div class=\"eventSignature\"><a href=\"").
                                append(event.getOwner().getJug().getWebSiteUrl()).
                                append("\">").append(event.getOwner().getJug().
                                getName()).append("</a></div>");
                    }
                    sb.append("<div class=\"eventContent\"><a href=\"").
                            append(baseUrl).append("/event/").
                            append(event.getId()).append("\">").
                            append(event.getTitle()).append("</a></div>");
                    sb.append("</div>\n");
                }
                util.setValue("content_textSearch_result", sb.toString(), false);
            } else {
                util.setValue("content_textSearch_result", "", false);
            }
        }
    }

    public void save(Event event) {
        boolean isNew = false;
        if (event.getId() == null) {
            isNew = true;
        }
        String loggedUser = "Unknown user";
        if (event.getOwner() == null) {
            Jugger jugger = servicesBo.getCurrentJugger();
            event.setOwner(jugger);
            if (jugger != null) {
                loggedUser = jugger.getUser().getUsername();
            }
        } else {
            loggedUser = event.getOwner().getUser().getUsername();
        }
        if (isNew) {
            event.setCreationDate(new Date());
        }
        //easy thanks to Lucio :)
        eventDao.store(event);
        if (isNew) {
            logger.info(loggedUser + " created a new event with id=" +
                    event.getId());
        } else {
            logger.info(loggedUser + " updated the event with id=" +
                    event.getId());
        }
    }

    public void register(Event event, Participant participant, String baseUrl) {
        participant.setConfirmed(Boolean.FALSE);
        participant.setConfirmationCode(generateConfirmationCode(event,
                participant));
        participant.setCreationDate(new Date());
        sendConfirmationEmail(event, participant, baseUrl);
        event.addParticipant(participant);
        eventDao.store(event);
        logger.info(participant.getEmail() + " (" + participant.getId() +
                ") registered to the event with id=" + event.getId());
    }

    public void addParticipant(Event event, Participant participant) {
        event = eventDao.read(event.getId());
        participant.setConfirmed(Boolean.TRUE);
        participant.setEvent(event);
        participant.setCreationDate(new Date());
        participantDao.store(participant);
        event.addParticipant(participant);
        eventDao.store(event);
        logger.info(participant.getEmail() + " (" + participant.getId() +
                ") added to the event with id=" + event.getId());
    }

    public void refreshRegistration(Event event, Participant participant,
            String baseUrl) {
        participant.setConfirmed(Boolean.FALSE);
        participant.setConfirmationCode(generateConfirmationCode(event,
                participant));
        participant.setEvent(event);
        participant.setCreationDate(new Date());
        sendConfirmationEmail(event, participant, baseUrl);
        participantDao.store(participant);
    }

    private String generateConfirmationCode(Event event,
            Participant participant) {
        return new MessageDigestPasswordEncoder("MD5", true).encodePassword(
                event.getTitle() + participant.getFirstName() +
                participant.getLastName() + participant.getEmail(),
                new Date());
    }

    private void sendConfirmationEmail(final Event event,
            final Participant participant, final String baseUrl) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @SuppressWarnings(value = "unchecked")
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(participant.getEmail());
                message.setFrom(conf.getConfirmationSenderEmailAddress());
                message.setSubject("Please confirm event registration");
                Map model = new HashMap();
                model.put("participant", participant);
                model.put("event", event);
                model.put("baseUrl", baseUrl);
                model.put("confirmationCode",
                        URLEncoder.encode(participant.getConfirmationCode(),
                        "UTF-8"));
                model.put("email", URLEncoder.encode(participant.getEmail(),
                        "UTF-8"));
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine,
                        "it/jugpadova/registration-confirmation.vm", model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
    }

    public Event retrieveEvent(Long id) {
        Event event = eventDao.read(id);
        if (event != null) {
            event.getParticipants().size();
            event.getEventResources().size();
            event.getSpeakers().size();
            logger.debug("Found " + event.getSpeakers().size() + " speakers for event id: " + id);
        }
        return event;
    }

    @RemoteMethod
    public List findPartialLocation(String partialLocation, String username) {
        List<String> result = new ArrayList<String>();
        if (!StringUtils.isBlank(partialLocation) &&
                !StringUtils.isBlank(username)) {
            try {
                Map<String, Event> yetAdded = new HashMap<String, Event>();
                List<Event> events = eventDao.findEventByPartialLocationAndOwner(
                        "%" + partialLocation + "%", username);
                Iterator<Event> itEvents = events.iterator();
                while (itEvents.hasNext()) {
                    Event event = itEvents.next();
                    final String listItem =
                            event.getLocation() + "<div class=\"informal\">" +
                            event.getDirections() + "</div>" +
                            "<div class=\"informal hidden\">" + event.getId() +
                            "</div>";
                    final String listItemSignature = event.getLocation() +
                            event.getDirections();
                    if (!yetAdded.containsKey(listItemSignature)) {
                        result.add(listItem);
                        yetAdded.put(listItemSignature, event);
                    }
                }
            } catch (Exception e) {
                logger.error("Error completing the location", e);
            }
        }
        return result;
    }

    @RemoteMethod
    public void copyDirectionsFromEvent(long eventId) {
        WebContext wctx = WebContextFactory.get();
        ScriptSession session = wctx.getScriptSession();
        Util util = new Util(session);
        Event event = eventDao.read(Long.valueOf(eventId));
        if (event != null) {
            util.setValue("location", event.getLocation());
            util.setValue("directions", event.getDirections());
            util.setValue("directionsPreview",
                    FilterBo.filterText(event.getDirections(), event.getFilter(),
                    false));
            Effect effect = new Effect(session);
            effect.highlight("location");
            effect.highlight("directions");
            effect.highlight("directionsPreview");
        }
    }

    /**
     * Confirm the registration of a participant to an event.
     *
     * @param email The email of the participant
     * @param confirmationCode The confirmation code of the registration
     * @return The confirmed participant, if all went well
     * @throws it.jugpadova.exception.RegistrationNotOpenException When the participant can't be confirmed because the registration is yet closed
     */
    public Participant confirmParticipant(String email, String confirmationCode)
            throws RegistrationNotOpenException {
        List<Participant> participants = participantDao.findByEmailAndConfirmationCodeAndConfirmed(email,
                confirmationCode, Boolean.FALSE);
        if (participants != null && participants.size() > 0) {
            Participant p = participants.get(0);
            if (p.getEvent().getRegistrationOpen()) {
                p.setConfirmed(Boolean.TRUE);
                p.setConfirmationDate(new Date());
            } else {
                throw new RegistrationNotOpenException(p.getEvent());
            }
            return p;
        }
        return null;
    }

    @RemoteMethod
    public void updateBadgePanel(String continent, String country,
            String jugName, String pastEvents, String orderByDate,
            String jebShowJUGName, String jebShowCountry,
            String jebShowDescription, String jebShowParticipants,
            String badgeStyle, String locale, String maxResults) {
        WebContext wctx = WebContextFactory.get();
        HttpServletRequest req = wctx.getHttpServletRequest();
        ScriptSession session = wctx.getScriptSession();
        Util util = new Util(session);
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(
                java.text.DateFormat.DEFAULT, new Locale(locale));
        java.lang.String baseUrl =
                "http://" + req.getServerName() + ":" + req.getServerPort() +
                req.getContextPath();
        it.jugpadova.bean.EventSearch eventSearch =
                new it.jugpadova.bean.EventSearch();
        eventSearch.setContinent(continent);
        eventSearch.setCountry(country);
        eventSearch.setJugName(jugName);
        eventSearch.setPastEvents(java.lang.Boolean.parseBoolean(pastEvents));
        eventSearch.setOrderByDate(orderByDate);
        if (StringUtils.isNotBlank(maxResults)) {
            try {
                eventSearch.setMaxResults(new Integer(maxResults));
            } catch (NumberFormatException numberFormatException) {
                /* ignore it */
            }
        }
        java.util.List<it.jugpadova.po.Event> events = this.search(eventSearch);
        boolean showJUGName =
                java.lang.Boolean.parseBoolean(jebShowJUGName);
        boolean showCountry =
                java.lang.Boolean.parseBoolean(jebShowCountry);
        boolean showDescription =
                java.lang.Boolean.parseBoolean(jebShowDescription);
        boolean showParticipants =
                java.lang.Boolean.parseBoolean(jebShowParticipants);
        util.setValue("badgeCode", this.getBadgePageCode(baseUrl, continent,
                country, jugName, pastEvents, orderByDate, jebShowJUGName,
                jebShowCountry, jebShowDescription, jebShowParticipants,
                badgeStyle, locale, maxResults));
        util.setValue("badgePreview", this.getBadgeHtmlCode(events, dateFormat,
                baseUrl, showJUGName, showCountry, showDescription,
                showParticipants, badgeStyle, locale));
    }

    public String getBadgeCode(String badgeHtmlCode) {
        java.lang.StringBuffer result = new java.lang.StringBuffer();
        result.append("var badge=\'\';\n");
        result.append("badge += '").append(
                Utilities.javascriptize(badgeHtmlCode)).append(
                "';\n");
        result.append("document.write(badge);");
        return result.toString();
    }

    public String getBadgePageCode(String baseUrl, String continent,
            String country, String jugName, String pastEvents,
            String orderByDate, String jebShowJUGName, String jebShowCountry,
            String jebShowDescription, String jebShowParticipants,
            String badgeStyle, String lang, String maxResults) {
        StringBuffer result = new StringBuffer();
        result.append("<script type=\"text/javascript\" src=\"").append(baseUrl).
                append("/event/badge.html");
        try {
            if (StringUtils.isNotBlank(continent) ||
                    StringUtils.isNotBlank(country) ||
                    StringUtils.isNotBlank(jugName) ||
                    StringUtils.isNotBlank(pastEvents) ||
                    StringUtils.isNotBlank(orderByDate) ||
                    StringUtils.isNotBlank(jebShowJUGName) ||
                    StringUtils.isNotBlank(jebShowCountry) ||
                    StringUtils.isNotBlank(jebShowDescription) ||
                    StringUtils.isNotBlank(jebShowParticipants) ||
                    StringUtils.isNotBlank(badgeStyle) ||
                    StringUtils.isNotBlank(lang)) {
                result.append('?');
                boolean first = true;
                if (StringUtils.isNotBlank(continent)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("continent=").append(
                            URLEncoder.encode(continent, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(country)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("country=").append(
                            URLEncoder.encode(country, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(jugName)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("jugName=").append(
                            URLEncoder.encode(jugName, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(pastEvents)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("pastEvents=").append(
                            URLEncoder.encode(pastEvents, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(orderByDate)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("order=").append(
                            URLEncoder.encode(orderByDate, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(jebShowJUGName)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("jeb_showJUGName=").append(
                            URLEncoder.encode(jebShowJUGName, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(jebShowCountry)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("jeb_showCountry=").append(
                            URLEncoder.encode(jebShowCountry, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(jebShowDescription)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("jeb_showDescription=").append(
                            URLEncoder.encode(jebShowDescription, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(jebShowParticipants)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("jeb_showParticipants=").append(
                            URLEncoder.encode(jebShowParticipants, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(badgeStyle)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("jeb_style=").append(
                            URLEncoder.encode(badgeStyle, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(lang)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("lang=").append(
                            URLEncoder.encode(lang, "UTF-8"));
                    first = false;
                }
                if (StringUtils.isNotBlank(maxResults)) {
                    if (!first) {
                        result.append('&');
                    }
                    result.append("maxResults=").append(
                            URLEncoder.encode(maxResults, "UTF-8"));
                    first = false;
                }
            }
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            logger.error("Error encoding URL parameters",
                    unsupportedEncodingException);
        }
        result.append("\"><!--\n");
        result.append("// --></script>");
        return result.toString();
    }

    public String getBadgeHtmlCode(List<Event> events, DateFormat dateFormat,
            String baseUrl, boolean showJUGName, boolean showCountry,
            boolean showDescription, boolean showParticipants, String badgeStyle,
            String lang) {
        StringBuffer result = new StringBuffer();
        if ("simple".equals(badgeStyle)) {
            result.append("<style type=\"text/css\"><!--\n");
            result.append(
                    ".jeb_event_odd {background-color: rgb(90%, 90%, 90%); padding: 4px;}\n");
            result.append(
                    ".jeb_event_even {background-color: white; padding: 4px;}\n");
            result.append(".jeb_date {font-weight: bold;}\n");
            result.append(".jeb_title {margin-left: 15px;}\n");
            result.append(".jeb_jug_name {margin-left: 15px;}\n");
            result.append(".jeb_country {margin-left: 15px;}\n");
            result.append(".jeb_participants {margin-left: 15px;}\n");
            result.append(".jeb_description {margin-left: 15px;}\n");
            result.append("--></style>\n");
        }
        boolean isOdd = false;
        for (it.jugpadova.po.Event event : events) {
            result.append("<div class=\"jeb_event_").append(
                    isOdd ? "odd" : "even").append("\">");
            result.append("<div class=\"jeb_date\"><span class=\"jeb_text\">").
                    append(dateFormat.format(event.getStartDate())).append(
                    "</span></div>");
            result.append(
                    "<div class=\"jeb_title\"><span class=\"jeb_text\"><a href=\"").
                    append(baseUrl).append("/event/").append(
                    event.getId()).append("\">").append(
                    event.getTitle()).append("</a></span></div>");
            if (showJUGName) {
                result.append(
                        "<div class=\"jeb_jug_name\"><span class=\"jeb_text\">");
                if (event.getOwner() != null) {
                    result.append("<a href=\"").append(event.getOwner().getJug().
                            getWebSiteUrl()).append("\">").append(event.getOwner().
                            getJug().getName()).append("</a>");
                }
                result.append("</span></div>");
            }
            if (showCountry) {
                result.append(
                        "<div class=\"jeb_country\"><span class=\"jeb_text\">");
                if (event.getOwner() != null && event.getOwner().getJug().
                        getCountry() != null) {
                    result.append(event.getOwner().getJug().getCountry().
                            getLocalName());
                }
                result.append("</span></div>");
            }
            if (showParticipants) {
                result.append(
                        "<div class=\"jeb_participants\"><span id=\"jeb_participants_label\" class=\"jeb_text\">").
                        append(messageSource.getMessage("Participants", null,
                        "Participants", StringUtils.isNotBlank(lang) ? new Locale(lang)
                        : Locale.ENGLISH)).
                        append(": </span><span class=\"jeb_text\">").append(
                        event.getNumberOfParticipants()).
                        append("</span></div>");
            }
            if (showDescription) {
                result.append(
                        "<div class=\"jeb_description\"><span class=\"jeb_text\">").
                        append(event.getFilteredDescription()).append(
                        "</span></div>");
            }
            result.append("</div>");
            isOdd = !isOdd;
        }
        return result.toString();
    }

    public void checkUserAuthorization(Event event)
            throws ParancoeAccessDeniedException {
        if (!servicesBo.canCurrentUserManageEvent(event)) {
            throw new ParancoeAccessDeniedException(
                    "You are not authorized on this event.");
        }
    }

    public List<NewsMessage> buildNewsMessages(String baseUrl) {
        List<NewsMessage> messages = new ArrayList<NewsMessage>();
        DateTime dt = new DateTime();
        List<Event> upcomings = eventDao.findUpcomingEvents(
                dt.plusDays(conf.getUpcomingEventDays()).toDate());
        for (Event event : upcomings) {
            messages.add(new NewsMessage(NewsMessage.TYPE_UPCOMING_EVENT,
                    event.getStartDate(), event, baseUrl));
        }
        List<Event> newEvents = eventDao.findNewEvents(
                dt.minusDays(conf.getNewEventDays()).toDate());
        for (Event event : newEvents) {
            if (!upcomings.contains(event)) {
                messages.add(new NewsMessage(NewsMessage.TYPE_NEW_EVENT,
                        event.getStartDate(), event, baseUrl));
            }
        }
        return messages;
    }

    public void regenerateLuceneIndexes() {
        Session session =
                this.eventDao.getHibernateTemplate().
                getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        fullTextSession.setFlushMode(FlushMode.MANUAL);
        fullTextSession.setCacheMode(CacheMode.IGNORE);
        ScrollableResults results = fullTextSession.createCriteria(Event.class).
                scroll(ScrollMode.FORWARD_ONLY);
        
        int index = 0;
        while (results.next()) {
            index++;
            fullTextSession.index(results.get(0)); //index each element

            if (index % 50 == 0) {
                fullTextSession.clear(); //clear every batchSize since the queue is processed

            }
        }
    }

    public Event buildEventFromTemplate(Event templateEvent, String lang) {
        Event result = new Event();
        result.setOwner(getServicesBo().getCurrentJugger());
        result.setTitle("(" + messageSource.getMessage("copy", null,
                "?copy?", StringUtils.isNotBlank(lang) ? new Locale(lang)
                : Locale.ENGLISH) + ") " + templateEvent.getTitle());
        result.setFilter(templateEvent.getFilter());
        result.setDescription(templateEvent.getDescription());
        result.setDirections(templateEvent.getDirections());
        result.setLocation(templateEvent.getLocation());
        result.setStartTime(templateEvent.getStartTime());
        result.setEndTime(templateEvent.getEndTime());
        return result;
    }

    public ServicesBo getServicesBo() {
        return servicesBo;
    }

    public void setServicesBo(ServicesBo servicesBo) {
        this.servicesBo = servicesBo;
    }
}
