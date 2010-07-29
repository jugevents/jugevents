/*
 *  Copyright 2009 JUG Events Team.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package it.jugpadova.blol;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.dao.EventDao;
import it.jugpadova.po.Event;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.Resource;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import org.apache.commons.lang.StringUtils;

/**
 * Test of methos of the FeedBo.
 *
 * @author Lucio Benfante
 */
public class FeedsBoTest extends JugEventsBaseTest {

    private static final String TEST_BASE_URL = "http://test.base.url";
    @Resource
    private FeedsBo feedsBo;
    @Resource
    private EventDao eventDao;

    /**
     * Test of buildCalendar method, of class FeedsBo.
     */
    public void testBuildCalendar() throws URISyntaxException, ParseException {
        Calendar calendar =
                feedsBo.buildCalendar(eventDao.findAll(), TEST_BASE_URL);
        try {
            calendar.validate(true);
        } catch (ValidationException ex) {
            fail(ex.getLocalizedMessage());
        }
    }

    public void testConvertDateAndTime() throws ParseException {
        SimpleDateFormat df =
                new SimpleDateFormat("dd/MM/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().
                createRegistry();
        net.fortuna.ical4j.model.TimeZone tz = registry.getTimeZone("GMT");
        Date d = df.parse("18/09/2009");
        net.fortuna.ical4j.model.Date cd1 =
                feedsBo.convertDateAndTime(d, "12:00 AM", tz);
        assertEquals("20090918T120000", cd1.toString());
        net.fortuna.ical4j.model.Date cd2 =
                feedsBo.convertDateAndTime(d, "05:00 PM", tz);
        assertEquals("20090918T170000", cd2.toString());
    }

    public void testConvertDateAndTimeBrazil() throws ParseException {
        SimpleDateFormat df =
                new SimpleDateFormat("dd/MM/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().
                createRegistry();
        net.fortuna.ical4j.model.TimeZone tz = registry.getTimeZone("America/Sao_Paulo");
        Date d = df.parse("18/09/2009");
        net.fortuna.ical4j.model.Date cd1 =
                feedsBo.convertDateAndTime(d, "12:00 AM", tz);
        assertEquals("20090918T120000", cd1.toString());
        net.fortuna.ical4j.model.Date cd2 =
                feedsBo.convertDateAndTime(d, "05:00 PM", tz);
        assertEquals("20090918T170000", cd2.toString());
    }

    /**
     * Test of buildJson method, of class FeedsBo.
     */
    public void testBuildJson() {
        List<Event> events = eventDao.findAll();
        String json = feedsBo.buildJson(events, TEST_BASE_URL, true);
        assertNotNull(json);
        assertTrue("The json resul is blank", StringUtils.isNotBlank(json));
    }

    /**
     * Test of buildJson method, of class FeedsBo.
     */
    public void testBuildJsonSimple() {
        List<Event> events = new LinkedList<Event>();
        events.add(createTestEvent("Meeting 1", "JBoss", 2010, 0, 9, "05:30 PM",
                "07:30 PM"));
        events.add(createTestEvent("Meeting 2", "JQuery", 2010, 0, 10,
                "05:30 PM",
                "07:30 PM"));
        String json = feedsBo.buildJson(events, TEST_BASE_URL, false);
        assertNotNull(json);
        assertTrue("The json resul is blank", StringUtils.isNotBlank(json));
        System.out.println(json);
        assertEquals(
                "[{\"title\":\"Meeting 1\",\"description\":\"\\u003cp\\u003eJBoss\\u003c/p\\u003e\",\"start\":\"2010-01-09 17:30:00\",\"end\":\"2010-01-09 19:30:00\",\"allDay\":false},{\"title\":\"Meeting 2\",\"description\":\"\\u003cp\\u003eJQuery\\u003c/p\\u003e\",\"start\":\"2010-01-10 17:30:00\",\"end\":\"2010-01-10 19:30:00\",\"allDay\":false}]",
                json);
    }

    private Event createTestEvent(String title, String description, int year,
            int month, int day, String startTime, String endTime) {
        java.util.Calendar cal =
                java.util.Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        cal.set(year, month, day);
        event.setStartDate(cal.getTime());
        event.setStartTime(startTime);
        event.setEndDate(cal.getTime());
        event.setEndTime(endTime);
        return event;
    }
}
