/*
 *  Copyright 2009 lucio.
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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;

/**
 *
 * @author lucio
 */
public class ErrorMatch extends RewriteMatch {

    private int error;

    public ErrorMatch(int error) {
        this.error = error;
    }

    @Override
    public boolean execute(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.sendError(this.error);
        return true;
    }

}
