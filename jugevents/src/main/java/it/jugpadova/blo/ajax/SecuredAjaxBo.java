package it.jugpadova.blo.ajax;

import java.util.List;

import it.jugpadova.bean.ParticipantBean;
import it.jugpadova.blo.EventResourceBo;
import it.jugpadova.blo.ParticipantBo;
import it.jugpadova.blol.ServicesBo;
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
@RemoteProxy(name = "AjaxMethodsJS")
public class SecuredAjaxBo implements AjaxMethods {

    @Autowired
    private ServicesBo servicesBo;
    @Autowired
    private ParticipantBo participantBo;
    @Autowired
    private EventResourceBo eventResourceBo;

    @RemoteMethod
    public String requireReliabilityOnExistingJugger(String emailJugger,
            String motivation, String baseURL) {
        return servicesBo.requireReliabilityOnExistingJugger(
                emailJugger, motivation, baseURL);
    }

    @RemoteMethod
    public List<ParticipantBean> chooseWinnerForEvent(long eventId) {
        return participantBo.chooseWinnerForEvent(eventId);
    }

    @RemoteMethod
    public void confirmParticipantOnAttendance(long participantId,
            boolean attended, boolean wasCancelled) {
        participantBo.confirmParticipantOnAttendance(participantId,
                attended, wasCancelled);
    }

    @RemoteMethod
    public List<ParticipantBean> findAllWinnersForEvent(long eventId) {
        return participantBo.findAllWinnersForEvent(eventId);
    }

    @RemoteMethod
    public void sendCertificateToAllParticipants(long eventId, String baseUrl) {
        participantBo.sendCertificateToAllParticipants(eventId,
                baseUrl);

    }

    @RemoteMethod
    public void sendCertificateToParticipant(long participantId, String baseUrl) {
        participantBo.sendCertificateToParticipant(participantId,
                baseUrl);

    }

    @RemoteMethod
    public void setAttended(long participantId, boolean value) {
        participantBo.setAttended(participantId, value);
    }

    @RemoteMethod
    public void setWinner(long participantId, boolean value) {
        participantBo.setWinner(participantId, value);
    }

    @RemoteMethod
    public void setShowFullLastName(long participantId, boolean value) {
        participantBo.setShowFullLastName(participantId, value);
    }

    @RemoteMethod
    public void updateParticipantFieldValue(Long participantId, String field,
            String value) {
        participantBo.updateParticipantFieldValue(participantId, field,
                value);

    }

    @RemoteMethod
    public boolean deleteEventResource(long eventResource) {
        return eventResourceBo.deleteResource(eventResource);
    }

    @RemoteMethod
    public String manageEventLinkResource(Long eventResourceId, Long eventId,
            String url,
            String description, boolean canUserManageTheEvent) {
        return eventResourceBo.manageEventLinkResource(eventResourceId,
                eventId, url, description, canUserManageTheEvent);
    }

    @RemoteMethod
    public String manageEventFlickrResource(Long eventResourceId, Long eventId,
            String flickrTag,
            String description, boolean canUserManageTheEvent) {
        return eventResourceBo.manageEventFlickrResource(
                eventResourceId,
                eventId, flickrTag, description, canUserManageTheEvent);
    }

    @RemoteMethod
    public String manageEventSlideShareResource(Long eventResourceId,
            Long eventId,
            String slideshareId,
            String description, boolean canUserManageTheEvent) {
        return eventResourceBo.manageEventSlideShareResource(
                eventResourceId,
                eventId, slideshareId, description, canUserManageTheEvent);
    }

    @RemoteMethod
    public String manageEventArchiveVideoResource(Long eventResourceId,
            Long eventId, String archiveFlashVideoUrl, String archiveDetailsUrl,
            String description,
            boolean canUserManageTheEvent) {
        return eventResourceBo.manageEventArchiveVideoResource(
                eventResourceId,
                eventId, archiveFlashVideoUrl, archiveDetailsUrl, description,
                canUserManageTheEvent);
    }

    @RemoteMethod
    public String manageEventYouTubeResource(Long eventResourceId, Long eventId,
            String youtubeVideoId, String description,
            boolean canUserManageTheEvent) {
        return eventResourceBo.manageEventYouTubeResource(
                eventResourceId,
                eventId, youtubeVideoId, description, canUserManageTheEvent);
    }

    @RemoteMethod
    public void fillEventResourceForm(Long eventResourceId) {
        eventResourceBo.fillEventResourceForm(eventResourceId);
    }
}
