/**
 *
 */
package it.jugpadova.util;

import it.jugpadova.exception.ParancoeAccessDeniedException;

import it.jugpadova.exception.RegistrationNotOpenException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.parancoe.web.ExceptionResolver;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * Ovverride ExceptionResolver to define custom web application Exception.
 * Should be mapped in parancoe-servlet.xml
 * @author Enrico Giurin
 *
 */
public class JUGEventsExceptionResolver extends ExceptionResolver {
    private static final Logger logger =
            Logger.getLogger(JUGEventsExceptionResolver.class);

    private CommonsMultipartResolver multipartResolver;

    public CommonsMultipartResolver getMultipartResolver() {
        return multipartResolver;
    }

    public void setMultipartResolver(CommonsMultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object object, Exception e) {
        if (e instanceof ParancoeAccessDeniedException) {
            return new ModelAndView("accessDenied", null);
        }
        if (e instanceof MaxUploadSizeExceededException) {
            return Utilities.getMessageView("upload.maximumSizeExceeded", Long.toString(multipartResolver.getFileUpload().getSizeMax()));
        }
        if (e instanceof RegistrationNotOpenException) {
            return Utilities.getMessageView("participant.registration.notOpen", ((RegistrationNotOpenException)e).getEvent().getTitle());
        }
        logger.error("Unexpected exception", e);
        return super.resolveException(req, res, object, e);
    }
}
