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
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;

/**
 * Test of methos of the FeedBo.
 *
 * @author Lucio Benfante
 */
public class FeedsBoTest extends JugEventsBaseTest {
    @Resource
    private FeedsBo feedsBo;
    @Resource
    private EventDao eventDao;

    /**
     * Test of buildCalendar method, of class FeedsBo.
     */
    public void testBuildCalendar() throws URISyntaxException, ParseException {
        Calendar calendar =
                feedsBo.buildCalendar(eventDao.findAll(), "http://test.base.url");
        try {
            calendar.validate(true);
        } catch (ValidationException ex) {
            fail(ex.getLocalizedMessage());
        }
    }

    public void testConvertDateAndTime() throws ParseException {
        SimpleDateFormat df =
                new SimpleDateFormat("dd/MM/yyyy");
        Date d = df.parse("18/09/2009");
        net.fortuna.ical4j.model.Date cd1 =
                feedsBo.convertDateAndTime(d, "12:00 AM");
        assertEquals("20090918T120000", cd1.toString());
        net.fortuna.ical4j.model.Date cd2 =
                feedsBo.convertDateAndTime(d, "05:00 PM");
        assertEquals("20090918T170000", cd2.toString());
    }
}
