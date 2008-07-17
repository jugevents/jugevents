package it.jugpadova;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.java.dev.footprint.exporter.Exporter;
import net.java.dev.footprint.exporter.pdf.PdfExporterFactory;
import net.java.dev.footprint.model.generated.FootprintProperties;

import org.parancoe.web.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author lucio
 */
public class FootprintTest extends BaseTest {
    @Autowired
    private FootprintProperties footprintProperties;
    
    public void testConfiguration() {
        assertNotNull(footprintProperties);
    }

    public void testExporter() {
        try {
            Exporter exporter =
                    PdfExporterFactory.getPdfExporter(Exporter.DEFAULT_SIGNED_PDF_EXPORTER,
                    new Object[]{footprintProperties});
            assertNotNull(exporter);
            Map map = new HashMap();
            map.put("jug", "JUG Padova JUG Padova JUG Padova JUG Padova JUG Padova JUG Padova JUG Padova JUG Padova");
            map.put("name", "Nome");
            map.put("title", "Titolo");
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
            map.put("date", df.format(new Date()));
            map.put("certdate", df.format(new Date()));
            exporter.export(new File("/tmp"), map);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}
