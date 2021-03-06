/**
 * 
 */
package it.jugpadova.blo.ajax;

import it.jugpadova.bean.ParticipantBean;

import java.util.List;

//import org.springframework.security.annotation.Secured;

/**
 * Defines all the methods available for accessing using ajax-dwr protocol.
 * The methods have been secured using spring-security annotations.
 * @author Enrico Giurin
 *
 */
public interface AjaxMethods {

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    String requireReliabilityOnExistingJugger(String emailJugger,
            String motivation, String baseURL);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    void setAttended(long participantId, boolean value);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    void setWinner(long participantId, boolean value);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    void setShowFullLastName(long participantId, boolean value);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    void confirmParticipantOnAttendance(long participantId, boolean attended, boolean wasCancelled);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    void sendCertificateToParticipant(long participantId, String baseUrl);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    void sendCertificateToAllParticipants(long eventId, String baseUrl);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    List<ParticipantBean> chooseWinnerForEvent(long eventId);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    List<ParticipantBean> findAllWinnersForEvent(long eventId);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    void updateParticipantFieldValue(Long participantId, String field,
            String value);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    boolean deleteEventResource(long eventResource);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    String manageEventLinkResource(Long eventResourceId, Long eventId,
            String url, String description, boolean canUserManageTheEvent);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    public String manageEventFlickrResource(Long eventResourceId, Long eventId,
            String flickrTag, String description, boolean canUserManageTheEvent);
    
    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    String manageEventSlideShareResource(Long eventResourceId, Long eventId,
            String slideshareId, String description,
            boolean canUserManageTheEvent);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    String manageEventArchiveVideoResource(Long eventResourceId, Long eventId,
            String archiveFlashVideoUrl, String archiveDetailsUrl, String description,
            boolean canUserManageTheEvent);

    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    String manageEventYouTubeResource(Long eventResourceId, Long eventId,
            String youtubeVideoId, String description,
            boolean canUserManageTheEvent);
    
    // TODO: Unsecured waiting to solve the problems with the proxy
    // @Secured({"ROLE_JUGGER"})
    public void fillEventResourceForm(Long eventResourceId);
}