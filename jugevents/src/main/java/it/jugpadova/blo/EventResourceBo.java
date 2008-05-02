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

import it.jugpadova.Daos;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import it.jugpadova.po.EventLink;
import it.jugpadova.po.EventResource;
import java.net.URLEncoder;
import org.apache.commons.lang.StringUtils;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

/**
 * Business logic for the event resource management.
 *
 * @author Lucio Benfante (<a href="lucio.benfante@jugpadova.it">lucio.benfante@jugpadova.it</a>)
 * @version $Revision: 52a8d7d0c3ba $
 */
public class EventResourceBo {

    private static final Logger logger = Logger.getLogger(EventResourceBo.class);
    private Daos daos;

    public Daos getDaos() {
        return daos;
    }

    public void setDaos(Daos daos) {
        this.daos = daos;
    }

    /**
     * Delete an event resource.
     * 
     * @param eventResourceId The id of the resource to delete
     * @return true if all went well.
     */
    @Transactional
    public boolean deleteResource(long eventResourceId) {
        try {
            EventResource resource = daos.getEventResourceDao().read(
                    eventResourceId);
            logger.info("Deleting resource "+eventResourceId+" for the event "+resource.getEvent().getId());
            daos.getEventResourceDao().delete(resource);
        } catch (RuntimeException e) {
            logger.error("Error deleting an event resource (" + eventResourceId +
                    ")", e);
            throw e;
        }
        return true;
    }

    @Transactional
    public String manageEventLinkResource(Long eventResourceId, Long eventId,
            String url, String description, boolean canUserManageTheEvent) {
        EventLink link = null;
        String display = null;
        if (eventResourceId == null) {
            // new resource
            link = new EventLink();
            link.setEvent(daos.getEventDao().read(eventId));
            display = "none";
            logger.info("Creating new link resource for the event "+eventId+" ("+url+")");
        } else {
            // update resource
            link = daos.getEventLinkDao().read(eventResourceId);
            display = "block";
            logger.info("Updating link resource "+eventResourceId+" for the event "+eventId+" ("+url+")");
        }
        link.setDescription(description);
        link.setUrl(url);
        daos.getEventLinkDao().createOrUpdate(link);
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

    @Transactional(readOnly=true)
    public void fillEventResourceForm(Long eventResourceId) {
        try {
            WebContext wctx = WebContextFactory.get();
            ScriptSession session = wctx.getScriptSession();
            Util util = new Util(session);
            EventResource eventResource = daos.getEventResourceDao().read(
                    eventResourceId);
            util.setValue("resourceId", eventResource.getId().toString());
            if (eventResource instanceof EventLink) {
                EventLink link = (EventLink) eventResource;
                util.setValue("linkUrl", link.getUrl(), false);
                util.setValue("linkDescription", link.getDescription(), false);
            }
            util.addFunctionCall("showResourceAddForm");
        } catch (RuntimeException e) {
            logger.error("Error filling the event resoutce form", e);
            throw e;
        }
    }
}
