/**
 * 
 */
package it.jugpadova.blo.ajax;

import it.jugpadova.bean.ParticipantBean;

import java.util.List;

import org.acegisecurity.annotation.Secured;

/**
 * Defines all the methods available for accessing using ajax-dwr protocol.
 * The methods have been secured using acegi annotations.
 * @author Enrico Giurin
 *
 */
public interface AjaxMethods {

    @Secured({"ROLE_JUGGER"})
    String requireReliabilityOnExistingJugger( String emailJugger,
            String motivation, String baseURL);

    @Secured({"ROLE_JUGGER"})
    void setAttended( long participantId, boolean value);

    @Secured({"ROLE_JUGGER"})
    void setWinner( long participantId, boolean value);

    @Secured({"ROLE_JUGGER"})
    void confirmParticipantOnAttendance( long participantId, boolean value);

    @Secured({"ROLE_JUGGER"})
    void sendCertificateToParticipant( long participantId, String baseUrl);

    @Secured({"ROLE_JUGGER"})
    void sendCertificateToAllParticipants( long eventId, String baseUrl);

    @Secured({"ROLE_JUGGER"})
    List<ParticipantBean> chooseWinnerForEvent( long eventId);

    @Secured({"ROLE_JUGGER"})
    List<ParticipantBean> findAllWinnersForEvent( long eventId);

    @Secured({"ROLE_JUGGER"})
    void updateParticipantFieldValue( Long participantId, String field,
            String value);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    boolean deleteEventResource( long eventResource);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    String manageEventLinkResource( Long eventResourceId, Long eventId,
            String url, String description, boolean canUserManageTheEvent);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    public void fillEventResourceForm(Long eventResourceId);
}