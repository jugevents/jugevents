package it.jugpadova.blo.ajax;

import java.util.List;

import it.jugpadova.Blos;
import it.jugpadova.bean.ParticipantBean;
import it.jugpadova.blo.ServicesBo;

/**
 * This class manages security concerns for all the dwr methods.
 * @author Enrico Giurin
 *
 */
public class SecuredAjaxBo implements AjaxMethods {
	private Blos blos = null;

	

	public Blos getBlos() {
		return blos;
	}



	public void setBlos(Blos blos) {
		this.blos = blos;
	}



	public String requireReliabilityOnExistingJugger(String emailJugger,
			String motivation, String baseURL) {		
		return blos.getServicesBo().requireReliabilityOnExistingJugger(emailJugger, motivation, baseURL);
	}



	public List<ParticipantBean> chooseWinnerForEvent(long eventId) {		
		return blos.getParticipantBo().chooseWinnerForEvent(eventId);
	}



	public void confirmParticipantOnAttendance(long participantId, boolean value) {
		blos.getParticipantBo().confirmParticipantOnAttendance(participantId, value);
		
	}



	public List<ParticipantBean> findAllWinnersForEvent(long eventId) {
		return blos.getParticipantBo().findAllWinnersForEvent(eventId);
	}



	public void sendCertificateToAllParticipants(long eventId, String baseUrl) {
		blos.getParticipantBo().sendCertificateToAllParticipants(eventId, baseUrl);
		
	}



	public void sendCertificateToParticipant(long participantId, String baseUrl) {
		blos.getParticipantBo().sendCertificateToParticipant(participantId, baseUrl);
		
	}



	public void setAttended(long participantId, boolean value) {
		blos.getParticipantBo().setAttended(participantId, value);
		
	}



	public void setWinner(long participantId, boolean value) {
		blos.getParticipantBo().setWinner(participantId, value);
		
	}



	public void updateParticipantFieldValue(Long participantId, String field,
			String value) {
		blos.getParticipantBo().updateParticipantFieldValue(participantId, field, value);
		
	}

}
