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
package it.jugpadova.dao;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.po.JUG;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Enrico Giurin
 */
public class JUGDaoTest extends JugEventsBaseTest {

    @Autowired
    private JUGDao jugDao;

    public void testFindByPartialName() {
        List<JUG> jugs = jugDao.findByPartialName("%J%");
        assertSize(3, jugs);
    }
    
    public void testFindByPartialJugNameAndCountry() {
        List<JUG> jugs = jugDao.findByPartialJugNameAndCountry("J%", "It%");
        assertSize(2, jugs);
    }
    
    
    public void testfindByNameAndCountryEN() {
        List<JUG> jugs = jugDao.findByNameAndCountryEN("JUG Padova", "Italy");
        assertSize(1, jugs);
    }
    
    public void testFindByICName() {
        JUG jug = jugDao.findByICName("jug padova");
        assertNotNull(jug);
        assertEquals(jug.getName(), "JUG Padova");
    }

   
}
