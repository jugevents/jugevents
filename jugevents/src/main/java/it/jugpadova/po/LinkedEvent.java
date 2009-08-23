/*
 *  Copyright 2009 JUG Events Team.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package it.jugpadova.po;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import org.parancoe.persistence.po.hibernate.EntityBase;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expression;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expressions;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * An event linked from another source, usually from a sponsor.
 *
 * @author Lucio Benfante
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "LinkedEvent.findExposedEvents", query =
    "from LinkedEvent e where e.expositionStartDate <= current_date() and e.expositionEndDate >= current_date() order by e.startDate")
})
@Expressions({
    @Expression(errorCode = "startDateAfterEndDate",
    message = "?startDateAfterEndDate?",
    applyIf = "startDate is not null and endDate is not null",
    value = "startDate <= endDate"),
    @Expression(errorCode = "expositionStartDateAfterExpositionEndDate",
    message = "?expositionStartDateAfterExpositionEndDate?",
    applyIf =
    "expositionStartDate is not null and expositionEndDate is not null",
    value = "expositionStartDate <= expositionEndDate")
})
public class LinkedEvent extends EntityBase {

    @NotBlank
    protected String title;
    @NotBlank
    protected String url;
    @NotBlank
    protected String urlLabel;
    protected byte[] background;
    @NotNull
    protected Date startDate;
    @NotNull
    protected Date endDate;
    @NotNull
    protected Date expositionStartDate;
    @NotNull
    protected Date expositionEndDate;

    @Lob
    public byte[] getBackground() {
        return background;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlLabel() {
        return urlLabel;
    }

    public void setUrlLabel(String urlLabel) {
        this.urlLabel = urlLabel;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getExpositionEndDate() {
        return expositionEndDate;
    }

    public void setExpositionEndDate(Date expositionEndDate) {
        this.expositionEndDate = expositionEndDate;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getExpositionStartDate() {
        return expositionStartDate;
    }

    public void setExpositionStartDate(Date expositionStartDate) {
        this.expositionStartDate = expositionStartDate;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
