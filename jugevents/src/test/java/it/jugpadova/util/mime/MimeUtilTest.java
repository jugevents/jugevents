/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.jugpadova.util.mime;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import junit.framework.TestCase;

/**
 * Tests on MimeUtil
 * 
 * @author lucio
 */
public class MimeUtilTest extends TestCase {

    public MimeUtilTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getMimeType method, of class MimeUtil.
     */
    public void testGetMimeTypeForJpeg() throws IOException {
        byte[] content = loadToByteArray("it/jugpadova/util/mime/noJugLogo.jpg");
        String expResult = "image/jpeg";
        String result = MimeUtil.getMimeType(content);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMimeType method, of class MimeUtil.
     */
    public void testGetMimeTypeForPng() throws IOException {
        byte[] content = loadToByteArray("it/jugpadova/util/mime/noJugLogo.png");
        String expResult = "image/png";
        String result = MimeUtil.getMimeType(content);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMimeType method, of class MimeUtil.
     */
    public void testGetMimeTypeForGif() throws IOException {
        byte[] content = loadToByteArray("it/jugpadova/util/mime/noJugLogo.gif");
        String expResult = "image/gif";
        String result = MimeUtil.getMimeType(content);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMimeType method, of class MimeUtil.
     */
    public void testGetMimeTypeForPdf() throws IOException {
        byte[] content = loadToByteArray("it/jugpadova/util/mime/template.pdf");
        String expResult = "application/pdf";
        String result = MimeUtil.getMimeType(content);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getMajorComponent method, of class MimeUtil.
     */
    public void testGetMajorComponent() {
        String mimeType = "image/jpeg";
        String expResult = "image";
        String result = MimeUtil.getMajorComponent(mimeType);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMinorComponent method, of class MimeUtil.
     */
    public void testGetMinorComponent() {
        String mimeType = "image/jpeg";
        String expResult = "jpeg";
        String result = MimeUtil.getMinorComponent(mimeType);
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileExtension method, of class MimeUtil.
     */
    public void testGetFileExtension() {
        File file = new File("jugevents.jpg");
        String expResult = "jpg";
        String result = MimeUtil.getFileExtension(file);
        assertEquals(expResult, result);
    }

    private byte[] loadToByteArray(String resource) throws IOException {
        InputStream in = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(resource));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream out = new BufferedOutputStream(bos);
        int b = 0;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        in.close();
        out.flush();
        out.close();
        return bos.toByteArray();
    }
}
