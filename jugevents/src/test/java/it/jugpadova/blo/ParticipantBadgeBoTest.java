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
package it.jugpadova.blo;

import com.lowagie.text.DocumentException;
import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.dao.EventDao;
import it.jugpadova.po.Event;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.Resource;

/**
 *
 * @author lucio
 */
public class ParticipantBadgeBoTest extends JugEventsBaseTest {

    @Resource
    private ParticipantBadgeBo participantBadgeBo;
    @Resource
    private EventDao eventDao;

    /**
     * Test of buildPDFBadges method, of class ParticipantBo.
     */
    public void testBuildPDFBadges() throws IOException, DocumentException {
        List<Event> events = eventDao.findByTitle("Future Meeting");
        assertEquals(1, events.size());
        Event event = events.get(0);
        byte[] pdfBytes = participantBadgeBo.buildPDFBadges(event);
        assertNotNull(pdfBytes);
        assertTrue("The PDF is empty", pdfBytes.length > 0);
        writeTestFile("testBuildPDFBadges", ".pdf", pdfBytes);
    }

    /**
     * Test of getDefaultBadgePageTemplateInputStream method, of class ParticipantBo.
     */
    public void testGetDefaultBadgePageTemplateInputStream() throws IOException {
        InputStream is = participantBadgeBo.getDefaultBadgePageTemplateInputStream();
        assertNotNull(is);
        is.close();
    }

    public void testGetParticipantPerPage() throws IOException {
        int count = participantBadgeBo.getParticipantsPerPage(null);
        assertEquals(6, count);
    }
}
