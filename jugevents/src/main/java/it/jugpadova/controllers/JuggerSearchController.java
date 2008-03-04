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

import it.jugpadova.Blos;
import it.jugpadova.Daos;
import it.jugpadova.bean.JuggerSearch;
import it.jugpadova.po.Jugger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import org.parancoe.web.BaseFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to manage search actions on Jugger
 * @author Enrico Giurin
 *
 */
public abstract class JuggerSearchController extends BaseFormController {

	/* (non-Javadoc)
	 * @see org.parancoe.web.BaseFormController#getLogger()
	 */
	

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		JuggerSearch js = (JuggerSearch)command;
		//really sorry Lucio...but I couldn't find better
		request.getSession(false).setAttribute("Parancoe.JS", js);
		ModelAndView mv = onSubmit(command, errors);
		mv.addObject("juggers", blo().getJuggerBO().searchJugger(js));
		return mv;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		//request.setAttribute("juggers", blo().getJuggerBO().searchAllOrderByUsername());
		JuggerSearch js = (JuggerSearch)request.getSession(false).getAttribute("Parancoe.JS");
		List<Jugger> list = null;
		if(js == null)
		{
			list = blo().getJuggerBO().searchAllOrderByUsername();
		}
		else
		{
			list = blo().getJuggerBO().searchJugger(js);
		}
		request.setAttribute("juggers", list);		
		return new JuggerSearch();
	}
	
	protected abstract Daos dao();

    protected abstract Blos blo();

}
