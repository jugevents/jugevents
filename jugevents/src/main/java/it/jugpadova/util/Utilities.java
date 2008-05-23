/**
 *
 */
package it.jugpadova.util;

import it.jugpadova.bean.NewJugger;
import it.jugpadova.po.JUG;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.parancoe.plugins.security.User;
import org.parancoe.plugins.world.Country;
import org.springframework.web.servlet.ModelAndView;

/**
 * Defines useful functions.
 * @author Enrico Giurin
 *
 */
public class Utilities {

    /**
     * Returns an instance of NewJugger with beans attributes set.
     * @return
     */
    public static NewJugger newJuggerCaptcha() {
        NewJugger jc = new NewJugger();
        jc.getJugger().setUser(new User());
        jc.getJugger().setJug(new JUG());
        jc.getJugger().getJug().
                setCountry(new Country());
        return jc;
    }

    /**
     * Compose the base URL from an HttpRequest.
     */
    public static String getBaseUrl(HttpServletRequest req) {
        return "http://" + req.getServerName() + ":" + req.getServerPort() +
                req.getContextPath();
    }

    /**
     * Build a ModelAndView for the message page.
     * 
     * @param messageCode The code of the localized message
     * @param messageArguments The arguments to insert in the message
     * @return The message view
     */
    public static ModelAndView getMessageView(String messageCode,
            String... messageArguments) {
        ModelAndView mv =
                new ModelAndView("redirect:/home/message.html");
        addMessageCode(mv, messageCode);
        addMessageArguments(mv, messageArguments);
        return mv;
    }

    /**
     * Add the messageCode parameter, used by spring:message in the message page.
     * 
     * @param mv The view
     * @param messageCode The message code
     */
    public static void addMessageCode(ModelAndView mv, String messageCode) {
        mv.addObject("messageCode", messageCode);
    }

    /**
     * Add the messageArguments parameter, used by spring:message in the message page.
     * 
     * @param mv The view
     * @param messageArguments The arguments to insert in the message
     */
    public static void addMessageArguments(ModelAndView mv,
            String... messageArguments) {
        if (messageArguments.length > 0) {
            StringBuilder arguments = new StringBuilder();
            boolean first = true;
            for (String arg : messageArguments) {
                if (!first) {
                    arguments.append(',');
                } else {
                    first = false;
                }
                arguments.append(StringEscapeUtils.escapeHtml(arg));
            }
            mv.addObject("messageArguments", arguments.toString());
        }
    }

    public static String javascriptize(String s) {
        return s.replaceAll("\'", Matcher.quoteReplacement("\\'")).replaceAll(
                "\\r\\n",
                Matcher.quoteReplacement("\\n")).replaceAll("\\r",
                Matcher.quoteReplacement("\\n")).replaceAll("\\n",
                Matcher.quoteReplacement("\\n"));
    }

    /**
     * Append a parameter to an URL.
     * 
     * @param sb The StringBuilder containing the URL where to append.
     * @param parameterName The name of the parameter
     * @param parameterValue The value of the parameter. It will be url encoded.
     * @param ifNotNull if true the parameter is appended only if its vale is not null.
     * @return The passed StringBuilder
     */
    public static StringBuilder appendUrlParameter(StringBuilder sb,
            String parameterName, String parameterValue, boolean ifNotNull)
            throws UnsupportedEncodingException {
        if (ifNotNull && parameterValue == null) {
            return sb;
        }
        if (sb.indexOf("?") == -1) {
            sb.append('?');
        }
        if (sb.charAt(sb.length() - 1) != '?') {
            sb.append('&');
        }
        sb.append(parameterName).append('=');
        if (parameterValue != null) {
            sb.append(URLEncoder.encode(parameterValue, "UTF-8"));
        }
        return sb;
    }
}
