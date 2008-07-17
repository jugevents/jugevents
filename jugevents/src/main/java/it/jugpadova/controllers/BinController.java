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

import it.jugpadova.blo.EventBo;
import it.jugpadova.blo.JugBo;
import it.jugpadova.blo.ParticipantBo;
import it.jugpadova.po.Event;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Participant;
import it.jugpadova.util.mime.MimeUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controlle for managing binary contents
 *
 * @author Lucio Benfante
 *
 */
@Controller
@RequestMapping("/bin/*.bin")
public class BinController {

    private static final Logger logger =
            Logger.getLogger(BinController.class);
    @Autowired
    private JugBo jugBo;
    @Autowired
    private EventBo eventBo;
    @Autowired
    private ParticipantBo participantBo;

    /**
     * Produce the JUG Logo from the database, or the default no logo image.
     */
    @RequestMapping
    public ModelAndView jugLogo(HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        Long id = new Long(req.getParameter("id"));
        byte[] jugLogo = jugBo.retrieveJugLogo(id);
        if (jugLogo != null && jugLogo.length > 0) {
            String contentType = MimeUtil.getMimeType(jugLogo);
            if (contentType == null) {
                contentType = "image/jpeg";
            }
            String fileExtension = MimeUtil.getMinorComponent(contentType);
            res.setContentType(contentType);
            res.setContentLength(jugLogo.length);
            res.setHeader("Content-Disposition", "filename=JugLogo." +
                    fileExtension);
            OutputStream out = new BufferedOutputStream(res.getOutputStream());
            out.write(jugLogo);
            out.flush();
            out.close();
        } else {
            // no logo image
            InputStream in =
                    new BufferedInputStream(this.getClass().getClassLoader().
                    getResourceAsStream("images/noJugLogo.jpg"));
            res.setContentType("image/jpeg");
            res.setHeader("Content-Disposition", "filename=noJugLogo.jpg");
            OutputStream out = new BufferedOutputStream(res.getOutputStream());
            int b = 0;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
            out.flush();
            out.close();
        }
        return null;
    }

    /**
     * Produce a preview of the current certificate for a JUG.
     */
    @RequestMapping
    public ModelAndView jugCertificatePreview(HttpServletRequest req,
            HttpServletResponse res) {
        try {
            Long id = new Long(req.getParameter("id"));
            JUG jug = jugBo.retrieveJug(id);
            InputStream jugCertificateTemplate =
                    jugBo.retrieveJugCertificateTemplate(id);
            byte[] jugCertificate = participantBo.buildCertificate(
                    jugCertificateTemplate, "James Duke", jug.getName() +
                    " Meeting",
                    new Date(),
                    jug.getName());
            res.setContentType("application/pdf");
            res.setContentLength(jugCertificate.length);
            res.setHeader("Content-Disposition",
                    "attachment; filename=JugCertificatePreview.pdf");
            OutputStream out =
                    new BufferedOutputStream(res.getOutputStream());
            out.write(jugCertificate);
            out.flush();
            out.close();
        } catch (Exception ex) {
            logger.error("Error producing the certificate preview", ex);
        }
        return null;
    }

    /**
     * Produce a certificate for a participant of an event.
     */
    @RequestMapping
    public ModelAndView participantCertificate(HttpServletRequest req,
            HttpServletResponse res) {
        try {
            Long id = new Long(req.getParameter("id"));
            Participant participant = participantBo.retrieveParticipant(id);
            Event event = participant.getEvent();
            eventBo.checkUserAuthorization(event);
            InputStream jugCertificateTemplate = null;
            String jugName = null;
            if (participant.getEvent().getOwner() != null) {
                JUG jug = participant.getEvent().getOwner().getJug();
                jugName = jug.getName();
                jugCertificateTemplate = jugBo.retrieveJugCertificateTemplate(
                        jug.getId());
            } else {
                jugName = "";
            }
            byte[] jugCertificate = participantBo.buildCertificate(
                    jugCertificateTemplate, participant.getFirstName() + " " +
                    participant.getLastName(), participant.getEvent().getTitle(),
                    event.getStartDate(),
                    jugName);
            res.setContentType("application/pdf");
            res.setContentLength(jugCertificate.length);
            res.setHeader("Content-Disposition",
                    "attachment; filename=" + StringUtils.deleteWhitespace(
                    participant.getLastName() + participant.getFirstName()) +
                    "Certificate.pdf");
            OutputStream out =
                    new BufferedOutputStream(res.getOutputStream());
            out.write(jugCertificate);
            out.flush();
            out.close();
        } catch (Exception ex) {
            logger.error("Error producing the certificate", ex);
        }
        return null;
    }
}
