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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.jugpadova.po.Event;
import it.jugpadova.po.JUG;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Created;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Item;
import it.jugpadova.util.HtmlTextConverter;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Url;
import org.springframework.stereotype.Component;

/**
 * Business logic for the feeds (RSS, iCal, etc.)
 *
 * @author Lucio Benfante (lucio dot benfante at jugpadova dot it)
 * @author Federico Fissore
 */
@Component
public class FeedsBo {

    /**
     * Build an iCal calendar from the list of events.
     *
     * @param events The list of events
     * @param baseUrl The application base URL
     * @return The calendar
     */
    public Calendar buildCalendar(List<Event> events, String baseUrl) throws
            URISyntaxException, ParseException {
        HtmlTextConverter htmlTextConverter = new HtmlTextConverter();
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId(
                "-//Jug Event news//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        for (Event event : events) {
            // Create a TimeZone
            String timezoneId =
                    (event.getOwner() != null
                    && event.getOwner().getJug() != null
                    && event.getOwner().getJug().getTimeZoneId() != null)
                    ? event.getOwner().getJug().getTimeZoneId() : "GMT";
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().
                    createRegistry();
            net.fortuna.ical4j.model.TimeZone timezone = registry.getTimeZone(
                    timezoneId);
            if (timezone == null) {
                timezone = registry.getTimeZone("GMT");
            }
            VTimeZone tz = timezone.getVTimeZone();
            VEvent vEvent = new VEvent();
            vEvent.getProperties().add(new Uid(event.getId().toString()));
            vEvent.getProperties().add(tz.getTimeZoneId());
            vEvent.getProperties().add(new Summary(event.getTitle()));
            vEvent.getProperties().add(new Created(new net.fortuna.ical4j.model.DateTime(event.
                    getCreationDate())));
            vEvent.getProperties().add(new DtStart(convertDateAndTime(
                    event.getStartDate(), event.getStartTime(), timezone)));
            if (event.getEndDate() != null) {
                vEvent.getProperties().add(new DtEnd(convertDateAndTime(
                        event.getEndDate(), event.getEndTime(), timezone)));
            }
            vEvent.getProperties().add(new Location(event.getLocation()));
            vEvent.getProperties().add(new Url(new URI(baseUrl
                    + "/event/show.html?id=" + event.getId())));
            vEvent.getProperties().add(new net.fortuna.ical4j.model.property.Description(htmlTextConverter.
                    convert(event.getFilteredDescription())));
            calendar.getComponents().add(vEvent);
        }
        return calendar;
    }

    /**
     * Convert separated date and time to a single Data(with time) object.
     *
     * @param date The date
     * @param time The time (in the "KK:mm aa" format)
     * @return The full date&time object
     * @throws ParseException If the time is in the wrong format.
     */
    public net.fortuna.ical4j.model.Date convertDateAndTime(Date date,
            String time, net.fortuna.ical4j.model.TimeZone timezone) throws ParseException {
        DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
        //dfDate.setTimeZone(timezone);
        if (time == null) {
            time = "08:00 AM";
        }
        String dateWithTime = dfDate.format(date) + " " + time;
        return new DateTime(dateWithTime, "dd/MM/yyyy KK:mm aa", timezone);
    }

    /**
     * Build an RSS channel from the list of events.
     *
     * @param events The list of events
     * @param baseUrl The application base URL
     * @param channelLink The URL from where this channel can be retrieved.
     * @return The channel.
     */
    public Channel buildChannel(List<Event> events, String baseUrl,
            String channelLink) {
        Channel channel = new Channel("rss_2.0");
        channel.setTitle("JUG Event news");
        channel.setDescription("JUG Events news always updated");
        channel.setLink(channelLink);
        channel.setEncoding("UTF-8");
        Date now = new Date();
        channel.setLastBuildDate(now);
        channel.setPubDate(now);
        List<Item> items = new LinkedList<Item>();
        for (Event event : events) {
            Item item = new Item();
            Guid guid = new Guid();
            guid.setValue(baseUrl + "/event/show.html?id=" + event.getId());
            guid.setPermaLink(true);
            item.setGuid(guid);
            item.setAuthor(event.getHostingOrganizationName());
            item.setTitle(event.getTitle());
            item.setExpirationDate(event.getEndDate() != null
                    ? event.getEndDate() : event.getStartDate());
            item.setPubDate(event.getStartDate());
            Description description =
                    new Description();
            description.setValue(event.getFilteredDescription());
            description.setType("text/html");
            item.setDescription(description);
            items.add(item);
        }
        channel.setItems(items);
        return channel;
    }

    /**
     * Build an JSON serialization from the list of events.
     *
     * @param events The list of events
     * @param baseUrl The application base URL
     * @return The JSON serialization
     */
    public String buildJson(List<Event> events, String baseUrl,
            boolean prettyPrinting) {
        String result = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Event.class, new EventJsonSerializer());
        if (prettyPrinting) {
            gsonBuilder.setPrettyPrinting();
        }
        Gson gson = gsonBuilder.create();
        result = gson.toJson(events);
        return result;
    }

    private class EventJsonSerializer implements JsonSerializer<Event> {

        @Override
        public JsonElement serialize(Event event, Type type,
                JsonSerializationContext jsc) {
            final JsonObject result = new JsonObject();
            result.addProperty("title", event.getTitle());
            result.addProperty("description", event.getFilteredDescription());
            String timezone =
                    (event.getOwner() != null
                    && event.getOwner().getJug() != null)
                    ? event.getOwner().getJug().getTimeZoneId() : null;
            try {
                result.addProperty("start",
                        formatDateAndTime(event.getStartDate(),
                        event.getStartTime(),
                        timezone));
            } catch (ParseException ex) {
                throw new RuntimeException("Can't convert start date and time",
                        ex);
            }
            try {
                result.addProperty("end",
                        formatDateAndTime(event.getEndDate(),
                        event.getEndTime(),
                        timezone));
            } catch (ParseException ex) {
                throw new RuntimeException("Can't convert end date and time", ex);
            }
            result.addProperty("allDay", Boolean.FALSE);
            if (event.getOwner() != null && event.getOwner().getJug() != null) {
                JUG jug = event.getOwner().getJug();
                result.addProperty("jugName", jug.getName());
                result.addProperty("jugFriendlyName", jug.
                        getInternalFriendlyName());
                result.addProperty("jugWebSite", jug.getWebSiteUrl());
            }
            return result;
        }

        /**
         * Format separated date and time to a single string object.
         *
         * @param date The date
         * @param time The time (in the "KK:mm aa" format)
         * @return The full date&time string
         * @throws ParseException If the time is in the wrong format.
         */
        public String formatDateAndTime(Date date,
                String time, String timezone) throws ParseException {
            DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat dfDateAndTime = new SimpleDateFormat(
                    "dd/MM/yyyy KK:mm aa");
            DateFormat dfDateAndTimeResult = new SimpleDateFormat(
                    "yyyy-MM-dd kk:mm:ss");
            if (timezone == null) {
                timezone = "GMT";
            }
            dfDate.setTimeZone(TimeZone.getTimeZone(timezone));
            dfDateAndTime.setTimeZone(TimeZone.getTimeZone(timezone));
            dfDateAndTimeResult.setTimeZone(TimeZone.getTimeZone(timezone));
            if (time == null) {
                time = "08:00 AM";
            }
            Date dateWithTime =
                    dfDateAndTime.parse(dfDate.format(date) + " " + time);
            return dfDateAndTimeResult.format(dateWithTime);
        }
    }
}
