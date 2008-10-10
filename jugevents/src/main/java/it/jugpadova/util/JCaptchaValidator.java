package it.jugpadova.util;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

/**
 * A validator for classes with jCaptchaCode and captchaService attributes
 *
 * @author Lucio Benfante (<a href="mailto:lucio.benfante@jugpadova.it">lucio.benfante@jugpadova.it</a>)
 */
public class JCaptchaValidator implements Validator {

    private static final Logger logger = Logger.getLogger(
            JCaptchaValidator.class);
    private boolean yetValidated = false;

    public JCaptchaValidator() {
    }

    @SuppressWarnings(value = "unchecked")
    public boolean supports(Class clazz) {
        return JCaptchaValidable.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        JCaptchaValidable vobj = (JCaptchaValidable) obj;
        CaptchaService captchaService = vobj.getCaptchaService();
        String captchaId = vobj.getCaptchaId();
        String response = vobj.getCaptchaResponse();
        boolean isResponseCorrect = false;
        try {
            if (response != null) {
                isResponseCorrect = captchaService.validateResponseForID(
                        captchaId, response);
                if (!isResponseCorrect) {
                    errors.rejectValue("captchaResponse", "captchaerror",
                            "Wrong control text!");
                }
            }
        } catch (CaptchaServiceException e) {
            logger.debug("Error validating captcha field", e);
            errors.rejectValue("captchaResponse", "captchaerror",
                    "Wrong control text!");
        }
    }
}
