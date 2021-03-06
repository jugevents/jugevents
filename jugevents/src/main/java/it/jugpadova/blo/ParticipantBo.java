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
package it.jugpadova.blo;

import it.jugpadova.Conf;
import it.jugpadova.bean.ParticipantBean;
import it.jugpadova.dao.ParticipantDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Participant;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import net.java.dev.footprint.exporter.Exporter;
import net.java.dev.footprint.exporter.pdf.PdfExporterFactory;
import net.java.dev.footprint.model.generated.FootprintProperties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * Business logic for the participant management.
 *
 * @author Lucio Benfante (<a href="lucio.benfante@jugpadova.it">lucio.benfante@jugpadova.it</a>)
 * @version $Revision: 447b44fd4f78 $
 */
@Component
public class ParticipantBo {

    private static final Logger logger =
            Logger.getLogger(ParticipantBo.class);
    private static final String BADGE_PAGE_TEMPLATE_PATH =
            "it/jugpadova/BadgePageTemplate.pdf";
    @Autowired
    private ParticipantDao participantDao;
    @Autowired
    private FootprintProperties footprintSettings;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private JugBo jugBo;
    @Autowired
    private Conf conf;
    private DateFormat df = DateFormat.getDateInstance(DateFormat.LONG,
            Locale.US);

    /**
     * Set the attended flag of a participant.
     *
     * @param participantId The id of the participa
     * @param value true if attended
     */
    public void setAttended(long participantId, boolean value) {
        Participant participant =
                participantDao.read(Long.valueOf(participantId));
        participant.setAttended(new Boolean(value));
    }

    /**
     * Confirm a participant when he is present to the event.
     *
     * @param participantId The id of the participa
     * @param value true if he's present
     */
    public void confirmParticipantOnAttendance(long participantId,
            boolean attended, boolean wasCancelled) {
        Participant participant =
                participantDao.read(Long.valueOf(participantId));
        if (attended) {
            participant.setAttended(Boolean.TRUE);
            participant.setConfirmed(Boolean.TRUE);
            participant.setConfirmationDate(new Date());
            participant.setCancelled(Boolean.FALSE);
        } else {
            participant.setAttended(Boolean.FALSE);
            participant.setConfirmed(Boolean.FALSE);
            participant.setConfirmationDate(null);
            if (wasCancelled) {
                participant.setCancelled(Boolean.TRUE);
            }
        }
    }

    public void setWinner(long participantId, boolean value) {
        Participant participant =
                participantDao.read(Long.valueOf(participantId));
        participant.setWinner(value);
    }

    public void setShowFullLastName(long participantId, boolean value) {
        Participant participant =
                participantDao.read(Long.valueOf(participantId));
        participant.setShowFullLastName(value);
    }


    public void sendCertificateToParticipant(long participantId, String baseUrl) {
        try {
            WebContext wctx = WebContextFactory.get();
            ScriptSession session = wctx.getScriptSession();
            Util util = new Util(session);
            Participant participant =
                    participantDao.read(Long.valueOf(participantId));
            Event event = participant.getEvent();
            InputStream jugCertificateTemplate =
                    jugBo.retrieveJugCertificateTemplate(
                    event.getOwner().getJug().getId());
            byte[] certificate =
                    buildCertificate(jugCertificateTemplate, participant.
                    getFirstName() + " " +
                    participant.getLastName(), event.getTitle(),
                    event.getStartDate(), event.getOwner().getJug().
                    getName());
            ByteArrayResource attachment = new ByteArrayResource(certificate);
            sendEmail(participant, baseUrl,
                    "Your certificate is here",
                    "it/jugpadova/participant-certificate.vm", attachment,
                    "certificate" + event.getId() + ".pdf");
            participant.setLastCertificateSentDate(new Date());
            util.setValue("certificateMsg" + participantId, "Sent");
            logger.info("Sent certificate for the participant " + participantId);
        } catch (Exception ex) {
            logger.error("Error generating a certificate", ex);
            throw new RuntimeException(ex);
        }
    }

    public void sendCertificateToAllParticipants(long eventId, String baseUrl) {
        WebContext wctx = WebContextFactory.get();
        ScriptSession session = wctx.getScriptSession();
        Util util = new Util(session);
        List<Participant> participantList =
                participantDao.findPresentParticipantsByEventId(eventId);
        int total = participantList.size();
        int count = 0;
        for (Participant participant : participantList) {
            try {
                Event event = participant.getEvent();
                InputStream jugCertificateTemplate =
                        jugBo.retrieveJugCertificateTemplate(
                        event.getOwner().getJug().getId());
                byte[] certificate =
                        buildCertificate(jugCertificateTemplate, participant.
                        getFirstName() + " " +
                        participant.getLastName(), event.getTitle(),
                        event.getStartDate(), event.getOwner().getJug().
                        getName());
                ByteArrayResource attachment =
                        new ByteArrayResource(certificate);
                sendEmail(participant, baseUrl,
                        "Your certificate is here",
                        "it/jugpadova/participant-certificate.vm", attachment,
                        "certificate" + event.getId() + ".pdf");
                participant.setLastCertificateSentDate(new Date());
                count++;
                util.setValue("sentCertificatesMessage", "Sent " + count +
                        " of " + total + " certificates");
            } catch (Exception ex) {
                logger.error("Error generating certificates", ex);
            }
        }
        logger.info("Sent " + count + " certificates for the event " + eventId);
    }

    private byte[] buildCertificate(String name, String title, Date date,
            String jug) throws Exception {
        Exporter jdbcExporter =
                PdfExporterFactory.getPdfExporter(
                Exporter.DEFAULT_SIGNED_PDF_EXPORTER,
                new Object[]{this.footprintSettings});
        Map map = new HashMap();
        map.put("jug", jug);
        map.put("name", name);
        map.put("title", title);
        map.put("date", df.format(date));
        map.put("certdate", df.format(new Date()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        jdbcExporter.export(outputStream, map);
        return outputStream.toByteArray();
    }

    public byte[] buildCertificate(InputStream certificateTemplate, String name,
            String title, Date date,
            String jug) throws Exception {
        if (certificateTemplate != null) { // use a personalized certificate template

            Exporter jdbcExporter =
                    PdfExporterFactory.getPdfExporter(
                    Exporter.DEFAULT_SIGNED_PDF_EXPORTER, new Class[]{
                        FootprintProperties.class, InputStream.class
                    },
                    new Object[]{this.footprintSettings, certificateTemplate});
            Map map = new HashMap();
            map.put("jug", jug);
            map.put("name", name);
            map.put("title", title);
            map.put("date", df.format(date));
            map.put("certdate", df.format(new Date()));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            jdbcExporter.export(outputStream, map);
            return outputStream.toByteArray();
        } else { // use the default certificate template

            return buildCertificate(name, title, date, jug);
        }
    }

    /**
     * General participant mail sender
     *
     * @param participant
     * @param baseUrl
     * @param subject
     * @param oneWayCode
     * @param template
     */
    private void sendEmail(final Participant participant,
            final String baseUrl,
            final String subject, final String template,
            final Resource attachment, final String attachmentName) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @SuppressWarnings(value = "unchecked")
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                        true);
                message.setTo(participant.getEmail());
                message.setFrom(conf.getConfirmationSenderEmailAddress());
                message.setSubject(subject);
                Map model = new HashMap();
                model.put("participant", participant);
                model.put("baseUrl", baseUrl);
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, template, model);
                message.setText(text, true);
                if (attachment != null) {
                    message.addAttachment(attachmentName, attachment);
                }
            }
        };
        this.mailSender.send(preparator);
    }

    public List<ParticipantBean> chooseWinnerForEvent(long eventId) {
        List<Participant> nonwinningParticipants =
                participantDao.findNonwinningParticipantsByEventId(eventId);

        int totalParticipants = nonwinningParticipants.size();

        int winner = (int) Math.round(Math.random() * totalParticipants);
        nonwinningParticipants.get(winner).setWinner(true);
        participantDao.store(nonwinningParticipants.get(
                winner));

        List<ParticipantBean> nonwinningParticipantBeans =
                convertParticipantList(nonwinningParticipants);

        return nonwinningParticipantBeans;
    }

    public List<ParticipantBean> findAllWinnersForEvent(long eventId) {
        List<Participant> winningParticipants =
                participantDao.findWinningParticipantsByEventId(eventId);
        List<ParticipantBean> winningParticipantBeans =
                convertParticipantList(winningParticipants);
        return winningParticipantBeans;
    }

    /**
     * Convert a list o Participant to a list of ParticipantBean.
     *
     * @param participants The list of Participant
     * @return The list of ParticipantBean
     */
    private List<ParticipantBean> convertParticipantList(
            List<Participant> participants) {
        List<ParticipantBean> participantBeans =
                new ArrayList<ParticipantBean>(participants.size());
        for (Participant p : participants) {
            try {
                ParticipantBean pb = new ParticipantBean();
                BeanUtils.copyProperties(pb, p);
                participantBeans.add(pb);
            } catch (Exception ex) {
                // it shouldn't happen
                logger.error("Unexpected exception copying participant bean", ex);
            }
        }
        return participantBeans;
    }

    /**
     * Update the value of a field of a participant
     * 
     * @param participantId The id of the participant
     * @param field The field to update
     * @param value The new value
     */
    public void updateParticipantFieldValue(Long participantId, String field,
            String value) {
        try {
            WebContext wctx = WebContextFactory.get();
            ScriptSession session = wctx.getScriptSession();
            Util util = new Util(session);
            Participant participant = participantDao.read(
                    participantId);
            // TODO: maybe using reflection for updating fields?
            if ("firstName".equals(field)) {
                participant.setFirstName(value);
            }
            if ("lastName".equals(field)) {
                participant.setLastName(value);
            }
            if ("email".equals(field)) {
                participant.setEmail(value);
            }
            if ("note".equals(field)) { // TODO: maybe using reflection?

                participant.setNote(value);
            }
            util.setValue(field + "_v_" + participantId, value);
            util.setValue(field + "_f_" + participantId, value);
        } catch (Exception e) {
            logger.error("Error updating lot rejected",
                    e);
        }

    }

    /**
     * Retrieve of participant by id
     * 
     * @param id The id of the participant
     * @return The participant
     */
    public Participant retrieveParticipant(Long id) {
        return participantDao.read(id);
    }

}
