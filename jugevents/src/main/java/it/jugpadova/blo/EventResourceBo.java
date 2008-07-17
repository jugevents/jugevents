// Copyright 2006-2008 The JUG Events Team
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package it.jugpadova.blo;

import com.benfante.jslideshare.SlideShareAPI;
import com.benfante.jslideshare.messages.Slideshow;
import it.jugpadova.dao.ArchiveVideoResourceDao;
import it.jugpadova.dao.EventDao;
import it.jugpadova.dao.EventLinkDao;
import it.jugpadova.dao.EventResourceDao;
import it.jugpadova.dao.FlickrResourceDao;
import it.jugpadova.dao.SlideShareResourceDao;
import it.jugpadova.dao.YouTubeResourceDao;
import it.jugpadova.po.ArchiveVideoResource;
import org.apache.log4j.Logger;
import it.jugpadova.po.EventLink;
import it.jugpadova.po.EventResource;
import it.jugpadova.po.FlickrResource;
import it.jugpadova.po.SlideShareResource;
import it.jugpadova.po.YouTubeResource;
import it.jugpadova.util.Utilities;
import java.net.URLEncoder;
import org.apache.commons.lang.StringUtils;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business logic for the event resource management.
 *
 * @author Lucio Benfante (<a href="lucio.benfante@jugpadova.it">lucio.benfante@jugpadova.it</a>)
 * @version $Revision: dfa2455c3721 $
 */
@Component
public class EventResourceBo {

    private static final Logger logger = Logger.getLogger(EventResourceBo.class);
    @Autowired
    private EventResourceDao eventResourceDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private EventLinkDao eventLinkDao;
    @Autowired
    private FlickrResourceDao flickrResourceDao;
    @Autowired
    private SlideShareResourceDao slideShareResourceDao;
    @Autowired
    private ArchiveVideoResourceDao archiveVideoResourceDao;
    @Autowired
    private YouTubeResourceDao youTubeResourceDao;
    @Autowired
    private SlideShareAPI slideShareApi;

    /**
     * Delete an event resource.
     * 
     * @param eventResourceId The id of the resource to delete
     * @return true if all went well.
     */
    public boolean deleteResource(long eventResourceId) {
        try {
            EventResource resource = eventResourceDao.read(
                    eventResourceId);
            logger.info("Deleting resource " + eventResourceId +
                    " for the event " + resource.getEvent().getId());
            eventResourceDao.delete(resource);
        } catch (RuntimeException e) {
            logger.error("Error deleting an event resource (" + eventResourceId +
                    ")", e);
            throw e;
        }
        return true;
    }

    public String manageEventLinkResource(Long eventResourceId, Long eventId,
            String url, String description, boolean canUserManageTheEvent) {
        EventLink link = null;
        String display = null;
        if (eventResourceId == null) {
            // new resource
            link = new EventLink();
            link.setEvent(eventDao.read(eventId));
            display = "none";
            logger.info("Creating new link resource for the event " + eventId +
                    " (" + url + ")");
        } else {
            // update resource
            link = eventLinkDao.read(eventResourceId);
            display = "block";
            logger.info("Updating link resource " + eventResourceId +
                    " for the event " + eventId + " (" + url + ")");
        }
        link.setDescription(description);
        link.setUrl(url);
        eventLinkDao.store(link);
        WebContext wctx = WebContextFactory.get();
        String result = null;
        try {
            result = wctx.forwardToString(
                    "/WEB-INF/jsp/event/resources/link.jsp?id=" + link.getId() +
                    "&url=" +
                    URLEncoder.encode(link.getUrl(), "UTF-8") +
                    "&abbreviatedUrl=" +
                    URLEncoder.encode(StringUtils.abbreviate(link.getUrl(), 40),
                    "UTF-8") + "&description=" +
                    URLEncoder.encode(link.getDescription(), "UTF-8") +
                    "&canUserManageTheEvent=" + canUserManageTheEvent +
                    "&display=" + display);
        } catch (Exception ex) {
            logger.error("Error calling link page fragment", ex);
        }
        return result;
    }

    public String manageEventFlickrResource(Long eventResourceId, Long eventId,
            String flickrTag, String description, boolean canUserManageTheEvent) {
        FlickrResource flickr = null;
        String display = null;
        if (eventResourceId == null) {
            // new resource
            flickr = new FlickrResource();
            flickr.setEvent(eventDao.read(eventId));
            display = "none";
            logger.info("Creating new flickr resource for the event " +
                    eventId + " (" + flickrTag + ")");
        } else {
            // update resource
            flickr = flickrResourceDao.read(eventResourceId);
            display = "block";
            logger.info("Updating flickr resource " + eventResourceId +
                    " for the event " + eventId + " (" + flickrTag + ")");
        }
        flickr.setTag(flickrTag);
        flickr.setDescription(description);
        String result = null;
        flickrResourceDao.store(flickr);
        WebContext wctx = WebContextFactory.get();
        try {
            StringBuilder fragUrl = new StringBuilder();
            fragUrl.append("/WEB-INF/jsp/event/resources/flickr.jsp");
            Utilities.appendUrlParameter(fragUrl, "id",
                    flickr.getId().toString(), true);
            Utilities.appendUrlParameter(fragUrl, "tag", flickr.getTag(), true);
            Utilities.appendUrlParameter(fragUrl, "description",
                    flickr.getDescription(), true);
            Utilities.appendUrlParameter(fragUrl, "canUserManageTheEvent",
                    Boolean.toString(canUserManageTheEvent), true);
            Utilities.appendUrlParameter(fragUrl, "display", display, true);
            result = wctx.forwardToString(fragUrl.toString());
        } catch (Exception ex) {
            logger.error("Error calling flickr page fragment", ex);
        }
        return result;
    }

    public String manageEventSlideShareResource(Long eventResourceId,
            Long eventId,
            String slideshareId, String description,
            boolean canUserManageTheEvent) {
        SlideShareResource slideshare = null;
        String display = null;
        if (eventResourceId == null) {
            // new resource
            slideshare = new SlideShareResource();
            slideshare.setEvent(eventDao.read(eventId));
            display = "none";
            logger.info("Creating new slideshare resource for the event " +
                    eventId + " (" + slideshareId + ")");
        } else {
            // update resource
            slideshare = slideShareResourceDao.read(eventResourceId);
            display = "block";
            logger.info("Updating slideshare resource " + eventResourceId +
                    " for the event " + eventId + " (" + slideshareId + ")");
        }
        slideshare.setResourceId(slideshareId);
        slideshare.setDescription(description);
        String result = null;
        Slideshow slideshow = null;
        try {
            slideshow = this.slideShareApi.getSlideshow(slideshareId);
        } catch (Exception ex) {
            logger.error("Error retrieving slideshare slideshow", ex);
        }
        if (slideshow != null && slideshow.getStatus() == 2) {
            slideshare.setEmbedCode(slideshow.getEmbedCode());
            slideshare.setUrl(slideshow.getPermalink());
        } else {
            slideshare.setEmbedCode(null);
            slideshare.setUrl(null);
        }
        slideShareResourceDao.store(slideshare);
        WebContext wctx = WebContextFactory.get();
        try {
            StringBuilder fragUrl = new StringBuilder();
            fragUrl.append("/WEB-INF/jsp/event/resources/slideshare.jsp");
            Utilities.appendUrlParameter(fragUrl, "id",
                    slideshare.getId().toString(), true);
            Utilities.appendUrlParameter(fragUrl, "embedCode",
                    slideshare.getEmbedCode(), true);
            Utilities.appendUrlParameter(fragUrl, "url", slideshare.getUrl(),
                    true);
            Utilities.appendUrlParameter(fragUrl, "abbreviatedUrl",
                    slideshare.getAbbreviatedUrl(), true);
            Utilities.appendUrlParameter(fragUrl, "description",
                    slideshare.getDescription(), true);
            Utilities.appendUrlParameter(fragUrl, "canUserManageTheEvent",
                    Boolean.toString(canUserManageTheEvent), true);
            Utilities.appendUrlParameter(fragUrl, "display", display, true);
            result = wctx.forwardToString(fragUrl.toString());
        } catch (Exception ex) {
            logger.error("Error calling slideshare page fragment", ex);
        }
        return result;
    }

    public String manageEventArchiveVideoResource(Long eventResourceId,
            Long eventId, String archiveFlashVideoUrl, String archiveDetailsUrl,
            String description, boolean canUserManageTheEvent) {
        ArchiveVideoResource archivevideo = null;
        String display = null;
        if (eventResourceId == null) {
            // new resource
            archivevideo = new ArchiveVideoResource();
            archivevideo.setEvent(eventDao.read(eventId));
            display = "none";
            logger.info("Creating new archive video resource for the event " +
                    eventId + " (" + archiveDetailsUrl + ")");
        } else {
            // update resource
            archivevideo = archiveVideoResourceDao.read(
                    eventResourceId);
            display = "block";
            logger.info("Updating archive video resource " + eventResourceId +
                    " for the event " + eventId + " (" + archiveFlashVideoUrl +
                    ")");
        }
        archivevideo.setFlashVideoUrl(archiveFlashVideoUrl);
        archivevideo.setDetailsUrl(archiveDetailsUrl);
        archivevideo.setDescription(description);
        String result = null;
        archiveVideoResourceDao.store(archivevideo);
        WebContext wctx = WebContextFactory.get();
        try {
            StringBuilder fragUrl = new StringBuilder();
            fragUrl.append("/WEB-INF/jsp/event/resources/archivevideo.jsp");
            Utilities.appendUrlParameter(fragUrl, "id",
                    archivevideo.getId().toString(), true);
            Utilities.appendUrlParameter(fragUrl, "flashVideoUrl",
                    archivevideo.getFlashVideoUrl(), true);
            Utilities.appendUrlParameter(fragUrl, "detailsUrl",
                    archivevideo.getDetailsUrl(), true);
            Utilities.appendUrlParameter(fragUrl, "description",
                    archivevideo.getDescription(), true);
            Utilities.appendUrlParameter(fragUrl, "canUserManageTheEvent",
                    Boolean.toString(canUserManageTheEvent), true);
            Utilities.appendUrlParameter(fragUrl, "display", display, true);
            result = wctx.forwardToString(fragUrl.toString());
        } catch (Exception ex) {
            logger.error("Error calling archive video page fragment", ex);
        }
        return result;
    }

    public String manageEventYouTubeResource(Long eventResourceId, Long eventId,
            String youtubeVideoId, String description,
            boolean canUserManageTheEvent) {
        YouTubeResource youtube = null;
        String display = null;
        if (eventResourceId == null) {
            // new resource
            youtube = new YouTubeResource();
            youtube.setEvent(eventDao.read(eventId));
            display = "none";
            logger.info("Creating new YouTube video resource for the event " +
                    eventId + " (" + youtubeVideoId + ")");
        } else {
            // update resource
            youtube = youTubeResourceDao.read(
                    eventResourceId);
            display = "block";
            logger.info("Updating YouTube video resource " + eventResourceId +
                    " for the event " + eventId + " (" + youtubeVideoId + ")");
        }
        youtube.setVideoId(youtubeVideoId);
        youtube.setDescription(description);
        String result = null;
        youTubeResourceDao.store(youtube);
        WebContext wctx = WebContextFactory.get();
        try {
            StringBuilder fragUrl = new StringBuilder();
            fragUrl.append("/WEB-INF/jsp/event/resources/youtube.jsp");
            Utilities.appendUrlParameter(fragUrl, "id",
                    youtube.getId().toString(), true);
            Utilities.appendUrlParameter(fragUrl, "videoId",
                    youtube.getVideoId(), true);
            Utilities.appendUrlParameter(fragUrl, "description",
                    youtube.getDescription(), true);
            Utilities.appendUrlParameter(fragUrl, "canUserManageTheEvent",
                    Boolean.toString(canUserManageTheEvent), true);
            Utilities.appendUrlParameter(fragUrl, "display", display, true);
            result = wctx.forwardToString(fragUrl.toString());
        } catch (Exception ex) {
            logger.error("Error calling YouTube video page fragment", ex);
        }
        return result;
    }

    public void fillEventResourceForm(Long eventResourceId) {
        try {
            WebContext wctx = WebContextFactory.get();
            ScriptSession session = wctx.getScriptSession();
            Util util = new Util(session);
            EventResource eventResource = eventResourceDao.get(eventResourceId);
            util.setValue("resourceId", eventResource.getId().toString());
            if (eventResource instanceof EventLink) {
                EventLink link = (EventLink) eventResource;
                util.setValue("resourceType", "link");
                util.setValue("linkUrl", link.getUrl(), false);
                util.setValue("linkDescription", link.getDescription(), false);
            } else if (eventResource instanceof FlickrResource) {
                FlickrResource flickrResource =
                        (FlickrResource) eventResource;
                util.setValue("resourceType", "flickr");
                util.setValue("flickrTag", flickrResource.getTag());
                util.setValue("flickrDescription",
                        flickrResource.getDescription());
            } else if (eventResource instanceof SlideShareResource) {
                SlideShareResource slideShareResource =
                        (SlideShareResource) eventResource;
                util.setValue("resourceType", "slideshare");
                util.setValue("slideshareId", slideShareResource.getResourceId());
                util.setValue("slideshareDescription",
                        slideShareResource.getDescription());
            } else if (eventResource instanceof ArchiveVideoResource) {
                ArchiveVideoResource archiveVideoResource =
                        (ArchiveVideoResource) eventResource;
                util.setValue("resourceType", "archive");
                util.setValue("archiveFlashVideoUrl",
                        archiveVideoResource.getFlashVideoUrl());
                util.setValue("archiveDetailsUrl",
                        archiveVideoResource.getDetailsUrl());
                util.setValue("archiveDescription",
                        archiveVideoResource.getDescription());
            } else if (eventResource instanceof YouTubeResource) {
                YouTubeResource youTubeResource =
                        (YouTubeResource) eventResource;
                util.setValue("resourceType", "youtube");
                util.setValue("youtubeId", youTubeResource.getVideoId());
                util.setValue("youtubeDescription",
                        youTubeResource.getDescription());
            }
            util.addFunctionCall("showResourceAddForm");
        } catch (RuntimeException e) {
            logger.error("Error filling the event resoutce form", e);
            throw e;
        }
    }
}
