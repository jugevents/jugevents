/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.jugpadova.util;

import it.jugpadova.po.JUG;
import it.jugpadova.util.mime.MimeUtil;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Complex validation on the JUG data.
 * 
 * @author lucio
 */
public class JUGValidator implements Validator {
    private static final Logger logger = Logger.getLogger(JUGValidator.class);
    public static final String[] VALID_IMAGE_TYPES = {"gif", "jpeg", "png"};
    public static final String[] VALID_CERTIFICATE_TEMPLATE_TYPES = {"pdf"};

    public boolean supports(Class clazz) {
        return JUG.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        JUG jug = (JUG) obj;
        if (jug.getLogo() != null && jug.getLogo().length > 0) {
            String mimeType =
                    MimeUtil.getMimeType(jug.getLogo());
            String imgType = MimeUtil.getMinorComponent(mimeType);
            if (Arrays.binarySearch(VALID_IMAGE_TYPES, imgType) < 0) {
                logger.debug("An user tried to upload a logo of the wrong type: "+imgType);
                errors.rejectValue("logo", "JUG.logo.wrongContentType", new Object[] {Arrays.toString(VALID_IMAGE_TYPES)}, "The type of the content of the logo is wrong");
            }
        }
        if (jug.getCertificateTemplate() != null && jug.getCertificateTemplate().length > 0) {
            String mimeType =
                    MimeUtil.getMimeType(jug.getCertificateTemplate());
            String tplType = MimeUtil.getMinorComponent(mimeType);
            if (Arrays.binarySearch(VALID_CERTIFICATE_TEMPLATE_TYPES, tplType) < 0) {
                logger.debug("An user tried to upload a certificate template of the wrong type: "+tplType);
                errors.rejectValue("certificateTemplate", "JUG.certificate.wrongContentType", new Object[] {Arrays.toString(VALID_CERTIFICATE_TEMPLATE_TYPES)}, "The type of the content of the certificate template is wrong");
            }
        }
    }

}
