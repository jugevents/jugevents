/*
 *  Copyright 2008 lucio.
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

package it.jugpadova.mock;

import it.jugpadova.blo.ServicesBo;

/**
 * A mock for the ServicesBo class. For example, for simulating login.
 * 
 * @author Lucio Benfante
 */
public class MockServicesBo extends ServicesBo {
    private String authenticatedUsername;

    public void setAuthenticatedUsername(String authenticatedUsername) {
        this.authenticatedUsername = authenticatedUsername;
    }        

    @Override
    public String authenticatedUsername() {
        return this.authenticatedUsername;
    }
    
}
