/**
 * 
 */
package it.jugpadova.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Mock Quartz job used in the maven test.
 * The job does nothing.
 * @author eg
 *
 */
public class MockQuartzJob  extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// DO NOTHING
		
	}

}
