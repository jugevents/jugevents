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

import it.jugpadova.blo.JugBo;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.Document;
import nu.xom.Serializer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for providing services to external.
 * @author lucio
 */
@Controller
@RequestMapping("/service/*.html")
public class ServiceController {

    private static Logger logger =
            Logger.getLogger(ServiceController.class);
    @Autowired
    private JugBo jugBo;

    @RequestMapping
    public ModelAndView kml(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        logger.info("Requested kml from " + req.getRemoteAddr());
        Document doc = jugBo.buildKml();
        res.setHeader("Cache-Control", "no-store");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setContentType("text/xml");
        ServletOutputStream resOutputStream = res.getOutputStream();
        Serializer serializer = new Serializer(resOutputStream);
        serializer.setIndent(4);
        serializer.setMaxLength(64);
        serializer.setLineSeparator("\n");
        serializer.write(doc);
        resOutputStream.flush();
        resOutputStream.close();
        return null;
    }

    @RequestMapping
    public ModelAndView services(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        return new ModelAndView("service/services");
    }
}
