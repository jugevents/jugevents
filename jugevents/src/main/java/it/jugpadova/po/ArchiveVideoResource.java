// Copyright 2008 The Parancoe Team
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
package it.jugpadova.po;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A SlideShare (www.slideshare.net) resource.
 *
 * @author Lucio Benfante (<a href="lucio.benfante@jugpadova.it">lucio.benfante@jugpadova.it</a>)
 * @version $Revision: b8584eaacad9 $
 */
@Entity
@DiscriminatorValue("ArchiveVideo")
public class ArchiveVideoResource extends EventResource {
    private String flashVideoUrl;
    private String detailsUrl;

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getFlashVideoUrl() {
        return flashVideoUrl;
    }

    public void setFlashVideoUrl(String flashVideoUrl) {
        this.flashVideoUrl = flashVideoUrl;
    }

}
