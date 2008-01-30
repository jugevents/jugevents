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
import it.jugpadova.po.Jugger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.parancoe.web.BaseMultiActionController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Admin
 *
 */
public abstract class JuggerController extends BaseMultiActionController {

    private static Logger logger =
            Logger.getLogger(JuggerController.class);

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

    public ModelAndView confirmUpdateJugger(HttpServletRequest req,
            HttpServletResponse res) {
        Long Id = new Long(req.getParameter("id"));
        Jugger jugger = dao().getJuggerDao().read(Id);
        ModelAndView mv =
                new ModelAndView("jugger/confirmUpdateJugger");
        mv.addObject("jugger", jugger);
        return mv;
    }

    protected abstract Daos dao();

    protected abstract Blos blo();
}
