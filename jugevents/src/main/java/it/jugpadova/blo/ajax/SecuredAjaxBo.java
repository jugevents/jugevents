package it.jugpadova.blo.ajax;

import java.util.List;

import it.jugpadova.Blos;
import it.jugpadova.bean.ParticipantBean;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class manages security concerns for all the dwr methods.
 * @author Enrico Giurin
 *
 */
@Component("ajaxMethods")
@RemoteProxy(name="AjaxMethodsJS")
public class SecuredAjaxBo implements AjaxMethods {

    @Autowired
    private Blos blos;
    
    @RemoteMethod
    public String requireReliabilityOnExistingJugger(String emailJugger,
            String motivation, String baseURL) {
        return blos.getServicesBo().requireReliabilityOnExistingJugger(
                emailJugger, motivation, baseURL);
    }

    @RemoteMethod
    public List<ParticipantBean> chooseWinnerForEvent(long eventId) {
        return blos.getParticipantBo().chooseWinnerForEvent(eventId);
    }

    @RemoteMethod
    public void confirmParticipantOnAttendance(long participantId, boolean value) {
        blos.getParticipantBo().confirmParticipantOnAttendance(participantId,
                value);

    }

    @RemoteMethod
    public List<ParticipantBean> findAllWinnersForEvent(long eventId) {
        return blos.getParticipantBo().findAllWinnersForEvent(eventId);
    }

    @RemoteMethod
    public void sendCertificateToAllParticipants(long eventId, String baseUrl) {
        blos.getParticipantBo().sendCertificateToAllParticipants(eventId,
                baseUrl);

    }

    @RemoteMethod
    public void sendCertificateToParticipant(long participantId, String baseUrl) {
        blos.getParticipantBo().sendCertificateToParticipant(participantId,
                baseUrl);

    }

    @RemoteMethod
    public void setAttended(long participantId, boolean value) {
        blos.getParticipantBo().setAttended(participantId, value);

    }

    @RemoteMethod
    public void setWinner(long participantId, boolean value) {
        blos.getParticipantBo().setWinner(participantId, value);

    }

    @RemoteMethod
    public void updateParticipantFieldValue(Long participantId, String field,
            String value) {
        blos.getParticipantBo().updateParticipantFieldValue(participantId, field,
                value);

    }

    @RemoteMethod
    public boolean deleteEventResource(long eventResource) {
        return blos.getEventResourceBo().deleteResource(eventResource);
    }

    @RemoteMethod
    public String manageEventLinkResource(Long eventResourceId, Long eventId,
            String url,
            String description, boolean canUserManageTheEvent) {
        return blos.getEventResourceBo().manageEventLinkResource(eventResourceId,
                eventId, url, description, canUserManageTheEvent);
    }

    @RemoteMethod
    public String manageEventFlickrResource(Long eventResourceId, Long eventId,
            String flickrTag,
            String description, boolean canUserManageTheEvent) {
        return blos.getEventResourceBo().manageEventFlickrResource(
                eventResourceId,
                eventId, flickrTag, description, canUserManageTheEvent);
    }

    @RemoteMethod
    public String manageEventSlideShareResource(Long eventResourceId,
            Long eventId,
            String slideshareId,
            String description, boolean canUserManageTheEvent) {
        return blos.getEventResourceBo().manageEventSlideShareResource(
                eventResourceId,
                eventId, slideshareId, description, canUserManageTheEvent);
    }

    @RemoteMethod
    public String manageEventArchiveVideoResource(Long eventResourceId,
            Long eventId, String archiveFlashVideoUrl, String archiveDetailsUrl,
            String description,
            boolean canUserManageTheEvent) {
        return blos.getEventResourceBo().manageEventArchiveVideoResource(
                eventResourceId,
                eventId, archiveFlashVideoUrl, archiveDetailsUrl, description,
                canUserManageTheEvent);
    }

    @RemoteMethod
    public String manageEventYouTubeResource(Long eventResourceId, Long eventId,
            String youtubeVideoId, String description,
            boolean canUserManageTheEvent) {
        return blos.getEventResourceBo().manageEventYouTubeResource(
                eventResourceId,
                eventId, youtubeVideoId, description, canUserManageTheEvent);
    }

    @RemoteMethod
    public void fillEventResourceForm(Long eventResourceId) {
        blos.getEventResourceBo().fillEventResourceForm(eventResourceId);
    }
}
