package it.jugpadova.blo;

import it.jugpadova.JugEventsBaseTest;
import it.jugpadova.dao.JUGDao;
import it.jugpadova.po.JUG;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test of the JugBo methods.
 *
 * @author lucio
 */
public class JugBoTest extends JugEventsBaseTest {

    @Autowired
    private JUGDao jugDao;
    @Autowired
    private JugBo jugBo;

    public void testBuildKmlPlacemarkText() throws IOException {
        String result = "<Placemark>\n" +
                "    <name>GOJAVA</name>\n" +
                "    <description>\n" +
                "     <![CDATA[\n" +
                "        Grupo de Usu&#225;rios JAVA Estado de Goi&#225;s<br/>\n" +
                "        <b>Leader:</b> <a href=\"mailto:raphael.adrien@gmail.com\">Raphael Adrien</a><br/>\n" +
                "        <b>Site:</b> http://www.gojava.org\n" +
                "     ]]>\n" +
                "    </description>\n" +
                "    <Point>\n" +
                "        <coordinates>-49.269009502375,-16.666921744733,0</coordinates>\n" +
                "    </Point>\n" +
                "    <styleUrl>#jugStyle</styleUrl>\n" +
                "</Placemark>";
        JUG jug = getGojavaJug();
        jug.setInfos("Grupo de Usu\u00E1rios JAVA Estado de Goi\u00E1s");
        String placemark = this.jugBo.buildKmlPlacemarkText(jug,
                "Raphael Adrien", "raphael.adrien@gmail.com");
        assertEquals(result, placemark);
    }

    public void testEvaluateModifiedKmlDataNoModifications() throws IllegalAccessException,
            InvocationTargetException {
        JUG jug = getGojavaJug();
        JUG modifiedJug = cloneJug(jug);
        Boolean b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertFalse(b);
    }

    public void testEvaluateModifiedKmlDataInfos() throws IllegalAccessException,
            InvocationTargetException {
        JUG jug = getGojavaJug();
        JUG modifiedJug = cloneJug(jug);
        modifiedJug.setInfos("Modified");
        Boolean b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertTrue(b);
        modifiedJug.setInfos(null);
        b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertFalse(b);
    }

    public void testEvaluateModifiedKmlDataLongitude() throws IllegalAccessException,
            InvocationTargetException {
        JUG jug = getGojavaJug();
        JUG modifiedJug = cloneJug(jug);
        modifiedJug.setLongitude(0.1);
        Boolean b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertTrue(b);
        modifiedJug.setLongitude(null);
        b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertFalse(b);
    }

    public void testEvaluateModifiedKmlDataLatitude() throws IllegalAccessException,
            InvocationTargetException {
        JUG jug = getGojavaJug();
        JUG modifiedJug = cloneJug(jug);
        modifiedJug.setLatitude(0.1);
        Boolean b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertTrue(b);
        modifiedJug.setLatitude(null);
        b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertFalse(b);
    }

    public void testEvaluateModifiedKmlDataWebSite() throws IllegalAccessException,
            InvocationTargetException {
        JUG jug = getGojavaJug();
        JUG modifiedJug = cloneJug(jug);
        modifiedJug.setWebSite("www.google.com");
        Boolean b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertTrue(b);
        modifiedJug.setWebSite(null);
        b = jugBo.evaluateModifiedKmlData(modifiedJug, jug);
        assertFalse(b);
    }
    
    private JUG cloneJug(JUG jug) throws IllegalAccessException,
            InvocationTargetException {
        JUG modifiedJug = new JUG();
        BeanUtils.copyProperties(modifiedJug, jug);
        return modifiedJug;
    }

    private JUG getGojavaJug() {
        JUG jug = jugDao.findByName("GOJAVA");
        assertNotNull(jug);
        return jug;
    }
    
    public void testEscapeXml() {
        String result = "Grupo de Usu&#225;rios JAVA Estado de Goi&#225;s";
        String  escaped = StringEscapeUtils.escapeXml("Grupo de Usu\u00E1rios JAVA Estado de Goi\u00E1s");
        assertEquals(result, escaped);
    }
}
