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
import it.jugpadova.exception.JUGEventsException;
import it.jugpadova.exception.RegistrationNotOpenException;
import it.jugpadova.po.Participant;

import it.jugpadova.util.Utilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/confirm/*.html")
public class ConfirmController {

    private static final Logger logger =
            Logger.getLogger(ConfirmController.class);
    @Autowired
    private EventBo eventBo;

    /**
     * Confirm the registration of a participant.
     */
    @RequestMapping
    public ModelAndView registration(@RequestParam("email") String email,
            @RequestParam("code") String code) {
        ModelAndView result = null;
        try {
            Participant participant =
                    eventBo.confirmParticipant(email, code);            
                result = Utilities.getMessageView("participant.registration.ok",
                        participant.getFirstName(),
                        participant.getEvent().getTitle());
           
        } catch (RegistrationNotOpenException registrationNotOpenException) {
            result = Utilities.getMessageView(
                    "participant.registration.cantConfirmRegistrationClosed",
                    registrationNotOpenException.getEvent().getTitle());
        }catch (JUGEventsException e) {
        	result = Utilities.getMessageView(
            "participant.registration.failed");
		}
        return result;
    }
    
    
    @RequestMapping
    public ModelAndView cancellation(@RequestParam("email") String email,
    		@RequestParam("code") String code) {
    	ModelAndView result = null;
    	try {
    		Participant participant =
    			eventBo.cancelParticipant(email, code);
    		result = Utilities.getMessageView("participant.cancellation.ok",
    				participant.getFirstName(),
    				participant.getEvent().getTitle());          
    	} catch (RegistrationNotOpenException registrationNotOpenException) {
    		result = Utilities.getMessageView(
    				"participant.registration.cantConfirmRegistrationClosed",
    				registrationNotOpenException.getEvent().getTitle());
    	}catch (JUGEventsException e) {
    		result = Utilities.getMessageView(
    				"participant.cancellation.failed");
    	}

    	return result;
    }
}
