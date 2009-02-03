/*
 *  Copyright 2009 JUG Events Team.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package it.jugpadova.urlrewrite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;
import org.tuckey.web.filters.urlrewrite.extend.RewriteRule;

/**
 *
 * @author Lucio Benfante
 */
public class CheckPreconditions extends RewriteRule {

    private static final Logger logger = Logger.getLogger(
            CheckPreconditions.class);

    @Override
    public RewriteMatch matches(HttpServletRequest request,
            HttpServletResponse response) {
        if (request.getRequestURI().matches(".*/jugger/registration.form")) {
            if (request.getMethod().equals("POST")) {
                if (request.getSession(false) == null || request.getSession().
                        getAttribute("jugger") == null) {
                    logger.debug(
                            "Unsatisfied precondition for /jugger/registration.form");
                    return new ErrorMatch(
                            HttpServletResponse.SC_PRECONDITION_FAILED);
                }
            }
        } else if (request.getRequestURI().matches(".*/event/show.html")) {
            boolean satisfied = checkLongParameter(request, "id");
            if (!satisfied) {
                logger.debug(
                        "Unsatisfied precondition for /event/show.html");
                return new ErrorMatch(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        } else if (request.getRequestURI().matches(".*/event/([0-9]+)$")) {
            boolean satisfied = checkNoParameter(request, "id");
            if (!satisfied) {
                logger.debug(
                        "Unsatisfied precondition for /event/([0-9]+)$");
                return new ErrorMatch(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        }
        return null;
    }

    private boolean checkLongParameter(HttpServletRequest request,
            String parameter) {
        String[] parameterValues = request.getParameterValues(parameter);
        boolean satisfied = false;
        if (parameterValues != null && parameterValues.length == 1) {
            try {
                Long.parseLong(parameterValues[0]);
                satisfied = true;
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return satisfied;
    }

    private boolean checkNoParameter(HttpServletRequest request,
            String parameter) {
        String[] parameterValues = request.getParameterValues(parameter);
        boolean satisfied = false;
        if (parameterValues == null && parameterValues.length == 0) {
            satisfied = true;
        }
        return satisfied;
    }
}
