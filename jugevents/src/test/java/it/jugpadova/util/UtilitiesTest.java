// Copyright 2006-2008 The JUG Events Team
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
package it.jugpadova.util;

import junit.framework.TestCase;

/**
 * Tests of the Utilities methods.
 *
 * @author Lucio Benfante (<a href="lucio.benfante@jugpadova.it">lucio.benfante@jugpadova.it</a>)
 * @version $Revision: 4cbe92b5cdfe $
 */
public class UtilitiesTest extends TestCase {
    
    public UtilitiesTest(String testName) {
        super(testName);
    }            

    /**
     * Test of appendUrlParameter method, of class Utilities.
     */
    public void testAppendUrlParameterAllNotNull() throws Exception {
        StringBuilder sb = new StringBuilder("http://www.jugevents.org/jugevents/prova.html");
        Utilities.appendUrlParameter(sb, "p1", "v1", true);
        Utilities.appendUrlParameter(sb, "p2", "v2", true);
        Utilities.appendUrlParameter(sb, "p3", "v3", true);
        String expResult = "http://www.jugevents.org/jugevents/prova.html?p1=v1&p2=v2&p3=v3";
        assertEquals(expResult, sb.toString());
    }

}
