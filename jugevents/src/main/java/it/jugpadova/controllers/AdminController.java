// Copyright 2006-2008 The Parancoe Team
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
import it.jugpadova.blo.SpeakerBo;
import it.jugpadova.dao.SpeakerDao;
import it.jugpadova.po.Speaker;
import it.jugpadova.util.Utilities;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.parancoe.util.MemoryAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/*.html")
public class AdminController {

    @Autowired
    private JugBo jugBo;
    @Autowired
    private SpeakerDao speakerDao;
    @Autowired
    private EventBo eventBo;
    @Autowired
    private SpeakerBo speakerBo;
    private static final Logger logger =
            Logger.getLogger(AdminController.class);

    @RequestMapping
    public ModelAndView index(HttpServletRequest req,
            HttpServletResponse res) {
        return new ModelAndView("admin/index", null);
    }

    @RequestMapping
    public ModelAndView logs(HttpServletRequest req,
            HttpServletResponse res) {
        if ("true".equals(req.getParameter("clean"))) {
            MemoryAppender.clean();
        }

        if ("error".equals(req.getParameter("test"))) {
            logger.error("sample error message");
        }
        if ("warn".equals(req.getParameter("test"))) {
            logger.warn("sample warn message");
        }
        String log = MemoryAppender.getFullLog();
        log = colourLog(log);

        Map params = new HashMap();
        params.put("log", log);
        return new ModelAndView("admin/logs", params);
    }

    @RequestMapping
    public ModelAndView conf(HttpServletRequest req,
            HttpServletResponse res) {
        return new ModelAndView("admin/conf", null);
    }

    @RequestMapping
    public ModelAndView spring(HttpServletRequest req,
            HttpServletResponse res) {
        return new ModelAndView("admin/spring", null);
    }

    private String colourLog(String log) {
        String[] lines;
        if (log == null) {
            lines = new String[]{""};
        } else {
            lines = log.split("[\\n\\r]");
        }
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].indexOf("[ERROR]") != -1) {
                lines[i] = "<span class=\"log_error\">" + lines[i] + "</span>";
            }
            if (lines[i].indexOf("[WARN]") != -1) {
                lines[i] = "<span class=\"log_warn\">" + lines[i] + "</span>";
            }
            if (StringUtils.isNotBlank(lines[i])) {
                lines[i] += "<br/>";
            }
        }
        return StringUtils.join(lines);
    }

    @RequestMapping
    public ModelAndView updateJugList(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        jugBo.updateJugList(null);
        return new ModelAndView("redirect:/admin/logs.html");
    }

    @RequestMapping
    public ModelAndView regenerateLuceneIndexes(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        eventBo.regenerateLuceneIndexes();
        speakerBo.regenerateLuceneIndexes();
        logger.info("Regenerated the Lucene indexes");
        return new ModelAndView("redirect:/admin/logs.html");
    }
    
    /**
     * This a temporary method just to allow to insert images (the enrico.jpg, lucio.jpg) into db to 
     * see how the event page looks like. After completed the insert speaker functionalities
     * this method will be removed.
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping
    public void insertImage(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
    	byte[] image = Utilities.resourceToBytes("/images/enrico.jpg");    	
    	Speaker speaker = speakerDao.findByResume("%electronic engineer%").get(0);    	   	
    	speaker.setPicture(image);
    	speakerDao.store(speaker);
    	
    	image = Utilities.resourceToBytes("/images/lucio.jpg");
    	speaker = speakerDao.findByResume("%is the president%").get(0); 
    	speaker.setPicture(image);
    	speakerDao.store(speaker);
    }
}
