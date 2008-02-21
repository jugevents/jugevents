/**
 * 
 */
package it.jugpadova.util;

import it.jugpadova.exception.ParancoeAccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.parancoe.web.ExceptionResolver;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object object, Exception e) {
        if (e instanceof ParancoeAccessDeniedException) {
            return new ModelAndView("accessDenied", null);
        }
        if (e instanceof MaxUploadSizeExceededException) {
            return Utilities.getMessageView("upload.maximumSizeExceeded", "100000");
        }
        logger.error("Unexpected exception", e);
        return super.resolveException(req, res, object, e);
    }
}
