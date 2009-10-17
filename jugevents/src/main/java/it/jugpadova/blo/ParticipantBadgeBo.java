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
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import it.jugpadova.po.Event;
import it.jugpadova.po.Participant;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author lucio
 */
@Service
public class ParticipantBadgeBo {

    private static final Logger logger =
            Logger.getLogger(ParticipantBadgeBo.class);
    private static final String BADGE_PAGE_TEMPLATE_PATH =
            "it/jugpadova/BadgePageTemplate.pdf";
    @Resource
    private EventBo eventBo;

    /**
     * Build a PDF with the badges of confirmed participants.
     */
    public byte[] buildPDFBadges(Event event) throws IOException,
            DocumentException {
        List<Participant> participants =
                eventBo.searchConfirmedParticipantsByEventId(event.getId());
        int participantsPerPage = getParticipantsPerPage(event);
        int pages = (participants.size() / participantsPerPage) + 1;
        ByteArrayOutputStream pdfMergedBaos = new ByteArrayOutputStream();
        PdfCopyFields pdfMerged = new PdfCopyFields(pdfMergedBaos);
        int newIndex = 1;
        for (int i = 0; i < pages; i++) {
            InputStream templateIs = getBadgePageTemplateInputStream(event);
            RandomAccessFileOrArray rafa = new RandomAccessFileOrArray(
                    templateIs);
            PdfReader template = new PdfReader(rafa, null);
            ByteArrayOutputStream pdfPageBaos = new ByteArrayOutputStream();
            PdfStamper pdfPage = new PdfStamper(template, pdfPageBaos);
            AcroFields pdfPageForm = pdfPage.getAcroFields();
            for (int j = 1; j <= participantsPerPage; j++) {
                pdfPageForm.renameField("title" + j, "title" + newIndex);
                pdfPageForm.renameField("firstName" + j, "firstName" + newIndex);
                pdfPageForm.renameField("lastName" + j, "lastName" + newIndex);
                pdfPageForm.renameField("firm" + j, "firm" + newIndex);
                pdfPageForm.renameField("role" + j, "role" + newIndex);
                newIndex++;
            }
            pdfPage.close();
            template.close();
            pdfMerged.addDocument(new PdfReader(pdfPageBaos.toByteArray()));
        }
        pdfMerged.close();
        ByteArrayOutputStream resultBaos = new ByteArrayOutputStream();
        PdfReader mergedReader = new PdfReader(pdfMergedBaos.toByteArray());
        PdfStamper mergedStamper = new PdfStamper(mergedReader, resultBaos);
        AcroFields mergedForm = mergedStamper.getAcroFields();
        int i = 1;
        for (Participant participant : participants) {
            mergedForm.setField("title" + i, event.getTitle());
            mergedForm.setField("firstName" + i, participant.getFirstName());
            mergedForm.setField("lastName" + i, participant.getLastName());
            i++;
        }
        mergedStamper.setFormFlattening(true);
        mergedStamper.close();
        return resultBaos.toByteArray();
    }

    protected InputStream getBadgePageTemplateInputStream(Event event) {
        InputStream is = null;
        if (event != null) {
            // TODO check and retrieve the customized template
            is = getDefaultBadgePageTemplateInputStream();
        } else {
            is = getDefaultBadgePageTemplateInputStream();
        }
        return is;
    }

    protected InputStream getDefaultBadgePageTemplateInputStream() {
        InputStream is =
                this.getClass().getClassLoader().
                getResourceAsStream(BADGE_PAGE_TEMPLATE_PATH);
        return is;
    }

    protected int getParticipantsPerPage(Event event) throws IOException {
        int count = 0;
        InputStream templateIs = getBadgePageTemplateInputStream(event);
        RandomAccessFileOrArray rafa = new RandomAccessFileOrArray(templateIs);
        PdfReader template = new PdfReader(rafa, null);
        AcroFields form = template.getAcroFields();
        HashMap fields = form.getFields();
        while (true) {
            if (fields.containsKey("firstName" + (count + 1))) {
                count++;
            } else {
                break;
            }
        }
        template.close();
        return count;
    }
}
