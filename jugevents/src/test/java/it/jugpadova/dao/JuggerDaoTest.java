package it.jugpadova.dao;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.po.Jugger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author lucio
 */
public class JuggerDaoTest extends JugEventsBaseTest {

    @Autowired
    private JuggerDao juggerDao;

    public void testFindByPartialJugNameAndCountryAndContinent() {
        List<Jugger> juggers = juggerDao.findByPartialJugNameAndCountryAndContinent("%J%", "%Ital%", "%Eur%");
        assertSize(3, juggers);
    }

    public void testFindAll() {
        List<Jugger> juggers = juggerDao.findAll();
        assertSize(3, juggers);
    }
}
