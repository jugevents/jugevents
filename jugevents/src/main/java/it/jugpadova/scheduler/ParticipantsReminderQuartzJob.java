/**
 * 
 */
package it.jugpadova.scheduler;

import it.jugpadova.blol.SchedulerBo;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author eg
 *
 */
public class ParticipantsReminderQuartzJob extends QuartzJobBean {
	private static final Logger logger = Logger.getLogger(ParticipantsReminderQuartzJob.class);
	

    private SchedulerBo schedulerBo;
	

	public SchedulerBo getSchedulerBo() {
		return schedulerBo;
	}


	public void setSchedulerBo(SchedulerBo schedulerBo) {
		this.schedulerBo = schedulerBo;
	}


	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
	
		schedulerBo.remindToParticipants();
		logger.debug("quartz process executed");

	}

}
