package it.jugpadova.dao;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.po.EventResource;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author lucio
 */
public class EventResourceDaoTest extends JugEventsBaseTest {
    @Autowired
    private EventResourceDao eventResourceDao;

    public void testFindAll() {
        List<EventResource> resources = eventResourceDao.findAll();
        assertSize(7, resources);
    }
}
