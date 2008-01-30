// Copyright 2006-2007 The JUG Events Team
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

import it.jugpadova.Blos;
import it.jugpadova.Daos;
import it.jugpadova.po.JUG;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.parancoe.web.BaseMultiActionController;
import org.parancoe.web.controller.annotation.DefaultUrlMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controlle for managing binary contents
 * 
 * @author Lucio Benfante
 *
 */
@DefaultUrlMapping
public abstract class BinController extends BaseMultiActionController {

    private static Logger logger =
            Logger.getLogger(BinController.class);

    /*
     * (non-Javadoc)
     *
     * @see org.parancoe.web.BaseMultiActionController#getLogger()
     */
    @Override
    public Logger getLogger() {
        // TODO Auto-generated method stub
        return logger;
    }

    public ModelAndView jugLogo(HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        Long id = new Long(req.getParameter("id"));
        byte[] jugLogo = blo().getJugBo().retrieveJugLogo(id);
        res.setContentType("image/jpeg");
        res.setContentLength(jugLogo.length);
        OutputStream out = new BufferedOutputStream(res.getOutputStream());
        out.write(jugLogo);
        out.flush();
        out.close();
        return null;
    }

    protected abstract Daos dao();

    protected abstract Blos blo();
}
