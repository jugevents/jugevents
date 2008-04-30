package it.jugpadova.dao;

import it.jugpadova.Daos;
import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.po.EventResource;

import java.util.List;

/**
 *
 * @author lucio
 */
public class EventResourceDaoTest extends JugEventsBaseTest {

    private EventResourceDao eventResourceDao;

    public EventResourceDaoTest() {
        Daos daos = (Daos) ctx.getBean("daos");
        eventResourceDao = daos.getEventResourceDao();
    }


    public void testFindAll() {
        List<EventResource> resources = eventResourceDao.findAll();
        assertSize(7, resources);
    }
}
