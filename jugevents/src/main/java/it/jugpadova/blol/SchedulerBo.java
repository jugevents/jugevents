/**
 * 
 */
package it.jugpadova.blol;

import it.jugpadova.Conf;
import it.jugpadova.dao.ParticipantDao;
import it.jugpadova.po.Participant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author Enrico Giurin
 *
 */
@Component
public class SchedulerBo {
	 private static final Logger logger = Logger.getLogger(SchedulerBo.class);
	    @Autowired
	    private ParticipantDao participantDao;
	    @Autowired
	    private VelocityEngine velocityEngine;
	    @Autowired
	    private JavaMailSender mailSender;	    
	    @Autowired
	    private Conf conf;
	    
	     //TODO find the way to externalize this in some properties file
	    public static final String SUBJECT= "Event Reminder";
	    
	    public static final String MAIL_TEMPLATE= "it/jugpadova/participant-reminder.vm";
	    
	    
	    
	    
	    private void sendEmailAsReminder(final Participant participant, final String baseUrl,
	            final String subject, final String template, final String sender) {
	        MimeMessagePreparator preparator = new MimeMessagePreparator() {

	            @SuppressWarnings(value = "unchecked")
	            public void prepare(MimeMessage mimeMessage) throws Exception {
	                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	                message.setTo(participant.getEmail());
	                message.setFrom(sender);
	                message.setSubject(subject);
	                Map model = new HashMap();
	                model.put("participant", participant);
	                model.put("baseUrl", baseUrl);
	                String text = VelocityEngineUtils.mergeTemplateIntoString(
	                        velocityEngine, template, model);
	                message.setText(text, true);
	            }
	        };
	        this.mailSender.send(preparator);
	    }
	    
	    @Transactional(propagation = Propagation.REQUIRED)	
	    public void remindToParticipants()
	    {
	    	logger.debug("Running scheduler for reminder");
	    	List<Participant> participants = participantDao.findParticipantsToBeReminded();
	    	logger.debug("Found "+participants.size()+" to be reminded");
	    	for (Participant participant : participants) {
	    		remindToParticipant(participant);
			}
	    	
	    }
	    
	    
	    @Transactional(propagation = Propagation.REQUIRES_NEW)	    
	    private void remindToParticipant(Participant participant)
	    {
	    	participant.setReminderSentDate(new Date());
	    	participantDao.store(participant);
	    	String subject = SUBJECT+ " - "+participant.getEvent().getTitle();
	    	sendEmailAsReminder(participant, conf.getJugeventsBaseUrl(), subject,  MAIL_TEMPLATE, conf.getInternalMail());
	    	logger.info("Sent reminder to "+participant.getFirstName()+" "+participant.getLastName()+" for the event: "+
	    			participant.getEvent().getTitle());
	    	
	    }

}
