package it.jugpadova.blol;

import it.jugpadova.Conf;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.dao.ReliabilityRequestDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Jugger;
import it.jugpadova.po.ReliabilityRequest;
import it.jugpadova.util.RRStatus;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.security.Authentication;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.parancoe.plugins.security.Authority;
import org.parancoe.plugins.security.User;
import org.parancoe.plugins.security.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * General porpouse BO for reliability services. The methods defined in this
 * class should be called only by the others BO.
 *
 * @author Enrico Giurin
 *
 */
@Component("servicesBo")
public class ServicesBo {

    /**
     * min value for threshold access.
     */
    public static final double MIN_THRESHOLD_ACCESS = 0d;
    /**
     * Max value for threshold access
     */
    public static final double MAX_THRESHOLD_ACCESS = 1d;
    /**
     * Template for the email on updating KML
     */
    public static final String KML_UPDATE_EMAIL_TEMPLATE =
            "it/jugpadova/kmlUpdateEmailTemplate.vm";
    private static final Logger logger = Logger.getLogger(ServicesBo.class);
    @Autowired
    private JuggerDao juggerDao;
    @Autowired
    private ReliabilityRequestDao reliabilityRequestDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Conf conf;

    /**
     * Returns true if jugger is reliable according to jugevents policies.
     *
     * @param jugger
     * @return
     */
    public boolean isJuggerReliable(double reliability) {
        // NOTE: we can change here Policy to grant reliablility, we
        // can also decide to grant a special ROLE to jugger, without using the
        // attribute reliability

        double thresholdAccess = conf.getThresholdAccess();
        if (reliability < MIN_THRESHOLD_ACCESS || reliability >
                MAX_THRESHOLD_ACCESS) {
            throw new IllegalArgumentException("reliability: " + reliability +
                    " is out of range");
        }
        if (thresholdAccess < MIN_THRESHOLD_ACCESS || thresholdAccess >
                MAX_THRESHOLD_ACCESS) {
            throw new IllegalArgumentException("thresholdAccess: " +
                    thresholdAccess + " is out of range");
        }
        if (reliability >= thresholdAccess) {
            return true;
        }

        return false;
    }

    /**
     * Business method to require Reliability.
     *
     * @param jug
     */
    // Metodo da chiamare all' interno di un contesto transazionale
    public void requireReliability(Jugger jugger, String motivation, String baseURL) {

        ReliabilityRequest rr = jugger.getReliabilityRequest();
        if (rr != null) {
            rr = reliabilityRequestDao.read(rr.getId());
        } else {
            rr = new ReliabilityRequest();
        }
        rr.setDateRequest(new Date(System.currentTimeMillis()));
        rr.setMotivation(motivation);
        rr.setStatus(RRStatus.RELIABILITY_REQUIRED.value);
        reliabilityRequestDao.store(rr);

        jugger.setReliabilityRequest(rr);
        juggerDao.store(jugger);

        // send mail to admin-jugevents
        sendEmail(jugger, baseURL, "A jugger has required reliability",
                "it/jugpadova/request-reliability2admin.vm",
                conf.getInternalMail(), conf.getAdminMailJE(), motivation);
        logger.info("Jugger " + jugger.getUser().getUsername() +
                " has completed with success request of reliability");
    }

    public String requireReliabilityOnExistingJugger(String emailJugger,
            String motivation, String baseURL) {
        Jugger jugger = juggerDao.findByEmail(emailJugger);

        try {

            requireReliability(jugger, motivation, baseURL);
            return "true";

        } catch (Exception e) {
            logger.error(e.toString(), e);
            return "false";
        }
    }

    public void updateReliability(Jugger jugger, String baseUrl) {
        juggerDao.store(jugger);
        if (jugger.getReliabilityRequest() != null) {
            sendAdminEmail(jugger, baseUrl,
                    "Response to the Request for Reliability",
                    "it/jugpadova/response-ReliabilityAdmin.vm",
                    conf.getConfirmationSenderEmailAddress(), jugger.getEmail());
        }
        logger.info("Update of the reliability for the jugger " +
                jugger.getUser().getUsername() +
                " has been processed with success");
    }

    /**
     * Send an email with the upadted kml data and fragment.
     *
     * @param jugger The jugger that did the update
     * @param jug The updated JUG
     * @param isNewJug True if it's a new JUG
     */
    public void sendUpdatedKmlDataEmail(final Jugger jugger, final JUG jug,
            final boolean isNewJug, final String kmlPlacemark) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(conf.getKmlUpdateToAddress());
                message.setFrom(conf.getKmlUpdateFromAddress());
                message.setReplyTo(conf.getKmlUpdateReplyAddress());
                if (isNewJug) {
                    message.setSubject(conf.getKmlUpdateSubjectPrefix() +
                            "The \"" + jug.getName() + "\" has been added");
                } else {
                    message.setSubject(conf.getKmlUpdateSubjectPrefix() +
                            "The \"" + jug.getName() + "\" has been updated");
                }
                Map model = new HashMap();
                model.put("jug", jug);
                model.put("jugeventsBaseUrl", conf.getJugeventsBaseUrl());
                model.put("jugEventsEmailLogoUrl", conf.getJugeventsBaseUrl() +
                        "/images/jugeventsLogoReflectedWhite.jpg");
                model.put("jugger", jugger);
                model.put("kmlPlacemark", StringEscapeUtils.escapeHtml(
                        kmlPlacemark));
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, KML_UPDATE_EMAIL_TEMPLATE, model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
        logger.info("Sent updated kml email for \"" + jug.getName() +
                "\" to " + conf.getKmlUpdateToAddress() + " from " +
                conf.getKmlUpdateFromAddress());
    }

    private void sendEmail(final Jugger jugger, final String baseUrl,
            final String subject, final String template, final String sender,
            final String mailTo, final String motivation) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @SuppressWarnings(value = "unchecked")
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(mailTo);
                message.setFrom(sender);
                message.setSubject(subject);
                Map model = new HashMap();
                model.put("jugger", jugger);
                model.put("baseUrl", baseUrl);
                model.put("motivation", motivation);

                model.put("username", URLEncoder.encode(jugger.getUser().
                        getUsername(), "UTF-8"));
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, template, model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
    }

    private void sendAdminEmail(final Jugger jugger, final String baseUrl,
            final String subject, final String template, final String sender,
            final String mailTo) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @SuppressWarnings(value = "unchecked")
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(mailTo);
                message.setFrom(sender);
                message.setSubject(subject);
                Map model = new HashMap();
                model.put("jugger", jugger);
                model.put("baseUrl", baseUrl);
                model.put("motivation",
                        jugger.getReliabilityRequest().getMotivation());
                model.put("adminResponse",
                        jugger.getReliabilityRequest().getAdminResponse());
                model.put("dateRequest",
                        jugger.getReliabilityRequest().getDateRequest());

                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, template, model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
    }

    /**
     * Retrieves the current authenticated user.
     * @return
     */
    public User getCurrentUser() {
        User result = null;
        String name = authenticatedUsername();
        if (name != null) {
        	result = userDao.findByUsername(name);
            if (result!=null) {
            	/*
                result = users.get(0);
                if (users.size() > 1) {
                    logger.warn("More than an user with the '" + name +
                            "' username");                           
                }
                 */
            } else {
                logger.error("No user with the '" + name + "' username");
            }
        }
        return result;
    }

    /**
     * Returns the current jugger, that is, the jugger, if exists, corrisponding to
     * the autherized user.
     * @return
     */
    public Jugger getCurrentJugger() {
        User currenUser = getCurrentUser();
        Jugger result = null;
        String username = currenUser.getUsername();
        if (username != null) {
            result = juggerDao.searchByUsername(currenUser.getUsername());
            if (result == null) {
                logger.error("No jugger with the '" + username + "' username");
            }
        }
        return result;
    }

    /**
     * This method return true in one of these two cases:
     * <ol>
     *  <li>The user identified by username is the authentified user</li>
     *  <li>The authentified user is in the role of ROLE_ADMIN</li>
     *  </ol>
     * @param username
     */
    public boolean checkAuthorization(String username) {
        String name = authenticatedUsername();
        if (name != null) {
            if (username.equals(name)) {
                return true;
            }
        } // end of if
        // is the authenticated user in the role_admin?

        User currentUser = getCurrentUser();
        return isAdmin(currentUser);

    } // end of method

    public boolean isCurrentUserAuthorized(User user) {
        return checkAuthorization(user.getUsername());
    }

    /**
     * Returns true if the user is in the role of ROLE_AMIN.
     * @param user
     * @return
     */
    private boolean isAdmin(User user) {
        List<Authority> userAuthorities = user.getAuthorities();
        for (Authority userAuthority : userAuthorities) {
            if ("ROLE_ADMIN".equals(userAuthority.getRole())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the authenticated username.
     * @return
     */
    public String authenticatedUsername() {
        Authentication authentication =
        	org.springframework.security.context.SecurityContextHolder.getContext().
                getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null; //not so good...

    }

    /**
     * Check if the current user can manage an event.
     *
     * @param event The event to manage
     * @return true if the user can manage the event
     */
    public boolean canCurrentUserManageEvent(Event event) {
        boolean result = false;
        User user = getCurrentUser();
        if (user != null) {
            if (isAdmin(user)) {
                // admins, of course
                return true;
            }
            if (event.getOwner() != null) {
                if (event.getOwner().getUser().getUsername().equals(
                        user.getUsername())) {
                    // the event owner
                    return true;
                }
                Jugger currentJugger = getCurrentJugger();
                if (isJuggerReliable(currentJugger.getReliability()) && event.getOwner().
                        getJug().equals(currentJugger.getJug())) {
                    // the jugger is reliable and is from the same jug of the event owner
                    return true;
                }
            }
        }
        return result;
    }
}
