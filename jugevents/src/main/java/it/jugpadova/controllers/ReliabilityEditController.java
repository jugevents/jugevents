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
package it.jugpadova.controllers;

import it.jugpadova.Blos;
import it.jugpadova.Daos;
import it.jugpadova.po.Jugger;
import it.jugpadova.util.RRStatus;
import it.jugpadova.util.Utilities;

import java.util.EnumSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.parancoe.web.BaseFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for managing reliability of the jugger. 
 * @author Enrico Giurin
 *
 */
public abstract class ReliabilityEditController extends BaseFormController {
	
	

	

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		Jugger jugger = (Jugger)command;
		//ReliabilityRequest rr = jugger.getReliabilityRequest();
		blo().getServicesBo().updateReliability(jugger, Utilities.getBaseUrl(request));
		return onSubmit(command, errors);
			
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		String username = request.getParameter("jugger.user.username");        
        Jugger jugger = blo().getJuggerBO().searchByUsername(username);        
        request.setAttribute("statusList", EnumSet.allOf(RRStatus.class));
        return jugger;
	}   
	
	protected abstract Daos dao();

    protected abstract Blos blo();

}
