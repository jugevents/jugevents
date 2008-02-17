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
import it.jugpadova.bean.NewJugger;
import it.jugpadova.bean.RequireReliability;
import it.jugpadova.exception.EmailAlreadyPresentException;
import it.jugpadova.exception.UserAlreadyPresentsException;
import it.jugpadova.util.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.parancoe.plugins.world.Country;
import org.parancoe.web.BaseFormController;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.octo.captcha.service.CaptchaService;

/**
 * 
 * @author Enrico Giurin
 * 
 */
public abstract class JuggerRegistrationController extends BaseFormController {

	private static final Logger logger = Logger
			.getLogger(JuggerRegistrationController.class);

	// captcha
	private CaptchaService captchaService;

	@Override
	protected void initBinder(HttpServletRequest req,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(byte[].class,
                new ByteArrayMultipartFileEditor());
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command)
			throws Exception {
		NewJugger jc = (NewJugger) command;
		jc.getJugger().getUser().setPassword("xxx");
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest req,
			HttpServletResponse res, Object command, BindException errors)
			throws Exception {
		NewJugger jc = (NewJugger) command;
		try {
			blo().getJuggerBO().newJugger(jc.getJugger(),
					Utilities.getBaseUrl(req), jc.getRequireReliability().isRequireReliability(),
					jc.getRequireReliability().getComment());
		} catch (EmailAlreadyPresentException e) {
			errors.rejectValue("jugger.email", "emailalreadypresent", e
					.getMessage());
			logger.error(e);
			return showForm(req, res, errors);
		} catch (UserAlreadyPresentsException e) {
			errors.rejectValue("jugger.user.username", "useralreadypresents", e
					.getMessage());
			logger.error(e);
			return showForm(req, res, errors);
		} finally {
			if (jc.getJugger().getJug().getCountry() == null) {
				jc.getJugger().getJug().setCountry(new Country());
			}
		}
		ModelAndView mv = onSubmit(command, errors);
		Utilities.addMessageArguments(mv, jc.getJugger().getEmail());
		return mv;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest req) throws Exception {
		NewJugger jc = Utilities.newJuggerCaptcha();
		jc.setCaptchaId(req.getSession().getId());
		jc.setCaptchaService(captchaService);
		jc.setRequireReliability(new RequireReliability());
		return jc;
	}

	public Logger getLogger() {
		return logger;
	}

	protected abstract Daos dao();

	protected abstract Blos blo();

	public CaptchaService getCaptchaService() {
		return captchaService;
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}
}
