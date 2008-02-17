package it.jugpadova.blo;

import it.jugpadova.Daos;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.dao.ReliabilityRequestDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Jugger;
import it.jugpadova.po.ReliabilityRequest;
import it.jugpadova.util.RRStatus;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.acegisecurity.Authentication;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.parancoe.plugins.security.User;
import org.parancoe.plugins.security.UserAuthority;
import org.parancoe.plugins.security.UserDao;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * General porpouse BO for reliability services. The methods defined in this 
 * class should be called only by the others BO.
 * 
 * @author Enrico Giurin
 * 
 */
public class ServicesBo {

    /**
     * min value for threshold access.
     */
    public static final double MIN_THRESHOLD_ACCESS = 0d;
    /**
     * Max value for threshold access
     */
    public static final double MAX_THRESHOLD_ACCESS = 1d;
    private static final Logger logger = Logger.getLogger(ServicesBo.class);
    private double thresholdAccess;
    private VelocityEngine velocityEngine;
    private JavaMailSender mailSender;
    private String adminMailJE;
    private String internalMail;
    private String confirmationSenderEmailAddress;
    private Daos daos;

    public double getThresholdAccess() {
        return thresholdAccess;
    }

    public void setThresholdAccess(double thresholdAccess) {
        this.thresholdAccess = thresholdAccess;
    }

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
    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    void requireReliability( Jugger jugger,  String motivation) {
        JuggerDao jdao = daos.getJuggerDao();
        ReliabilityRequestDao rrdao = daos.getReliabilityRequestDao();

        ReliabilityRequest rr = jugger.getReliabilityRequest();
        if (rr != null) {
            rr = rrdao.read(rr.getId());
        } else {
            rr = new ReliabilityRequest();
        }



        rr.setDateRequest(new Date(System.currentTimeMillis()));
        rr.setMotivation(motivation);
        rr.setStatus(RRStatus.RELIABILITY_REQUIRED.value);
        rrdao.create(rr);

        jugger.setReliabilityRequest(rr);
        jdao.update(jugger);

        // send mail to admin-jugevents
        sendEmail(jugger, "", "A jugger has required reliability",
                "it/jugpadova/request-reliability2admin.vm", internalMail,
                adminMailJE, motivation);
        logger.info("Jugger " + jugger.getUser().getUsername() +
                " has completed wth success request of reliability");
    }

    @Transactional
    public String requireReliabilityOnExistingJugger(String emailJugger,
            String motivation) {
        Jugger jugger = daos.getJuggerDao().findByEmail(emailJugger);

        try {

            requireReliability(jugger, motivation);
            return "true";

        } catch (Exception e) {
            logger.error(e.toString(), e);
            return "false";
        }
       }
    
    @Transactional
    public void updateReliability(Jugger jugger, String baseUrl)
    {
    	Jugger ej = daos.getJuggerDao().read(jugger.getId());
    	ej.setReliability(jugger.getReliability());
    	ej.getReliabilityRequest().setAdminResponse(jugger.getReliabilityRequest().getAdminResponse());
    	ej.getReliabilityRequest().setDateAdminResponse(new Date(System.currentTimeMillis()));
    	ej.getReliabilityRequest().setStatus(jugger.getReliabilityRequest().getStatus());
    	daos.getReliabilityRequestDao().update(ej.getReliabilityRequest());
    	daos.getJuggerDao().update(ej);
    	sendAdminEmail(jugger, baseUrl, "Response to the Request for Reliability",  "it/jugpadova/response-ReliabilityAdmin.vm", confirmationSenderEmailAddress, jugger.getEmail());
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
                model.put("motivation", jugger.getReliabilityRequest().getMotivation());
                model.put("adminResponse", jugger.getReliabilityRequest().getAdminResponse());
                model.put("dateRequest", jugger.getReliabilityRequest().getDateRequest());
               
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
    User getCurrentUser() {
        User result = null;
        String name = authenticatedUsername();
        if (name != null) {
            UserDao userDao = getDaos().getUserDao();
            List<User> users = userDao.findByUsername(name);
            if (users.size() > 0) {
                result = users.get(0);
                if (users.size() > 1) {
                    logger.warn("More than an user with the '" + name +
                            "' username");
                }
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
    Jugger getCurrentJugger() {
        User currenUser = getCurrentUser();
        Jugger result = null;
        String username = currenUser.getUsername();
        if (username != null) {
            JuggerDao juggerDao = getDaos().getJuggerDao();
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
    @Transactional
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

    boolean isCurrentUserAuthorized(User user) {
        return checkAuthorization(user.getUsername());
    }

    /**
     * Returns true if the user is in the role of ROLE_AMIN.
     * @param user
     * @return
     */
    private boolean isAdmin(User user) {
        List<UserAuthority> userAuthorities = user.getUserAuthority();
        for (UserAuthority userAuthority : userAuthorities) {
            if ("ROLE_ADMIN".equals(userAuthority.getAuthority().getRole())) {
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
                org.acegisecurity.context.SecurityContextHolder.getContext().
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
    @Transactional(readOnly = true)
    public boolean canCurrentUserManageEvent(Event event) {
        boolean result = false;
        User user = getCurrentUser();
        if (user != null) {
            if (isAdmin(user)) {
                // admins, of course
                return true;
            }
            if (event.getOwner() != null) {
                if (event.getOwner().getUser().getUsername().equals(user.getUsername())) {
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

    public String getAdminMailJE() {
        return adminMailJE;
    }

    public void setAdminMailJE(String adminMailJE) {
        this.adminMailJE = adminMailJE;
    }

    public String getInternalMail() {
        return internalMail;
    }

    public void setInternalMail(String internalMail) {
        this.internalMail = internalMail;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public Daos getDaos() {
        return daos;
    }

    public void setDaos(Daos daos) {
        this.daos = daos;
    }

	public String getConfirmationSenderEmailAddress() {
		return confirmationSenderEmailAddress;
	}

	public void setConfirmationSenderEmailAddress(
			String confirmationSenderEmailAddress) {
		this.confirmationSenderEmailAddress = confirmationSenderEmailAddress;
	}
}
