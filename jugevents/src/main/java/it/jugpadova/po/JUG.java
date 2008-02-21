// Copyright 2006-2007 The JUG Events Team
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

import it.jugpadova.util.JUGValidator;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.parancoe.persistence.po.hibernate.EntityBase;
import org.parancoe.plugins.world.Country;
import org.springmodules.validation.bean.conf.loader.annotation.handler.CascadeValidation;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Validator;

/**
 * Represents Java User Group informations.
 * @author Enrico Giurin
 *
 */
@Entity
@NamedQueries(value = {@NamedQuery(name = "JUG.findByPartialName", query =
        "from JUG j where upper(j.name) like upper(?) order by j.name asc"), @NamedQuery(name =
        "JUG.findByPartialJugNameAndCountry", query =
        "from JUG jug where upper(jug.name) like upper(?) and upper(jug.country.englishName) like upper(?) order by jug.name asc"), @NamedQuery(name =
        "JUG.findByNameAndCountryEN", query =
        "from JUG jug where upper(jug.name) = upper(?) and upper(jug.country.englishName) = upper(?) order by jug.name asc"),
        @NamedQuery(name =
            "JUG.findByICName", query =
            "from JUG jug where upper(jug.name) = upper(?) order by jug.name asc")
        , @NamedQuery(name =
        "JUG.findByPartialJugNameAndCountryAndContinent", query =
        "from JUG j where upper(j.name) like upper(?) and upper(j.country.localName) like upper(?) and upper(j.country.continent.name) like upper(?) order by j.name asc")})
@Validator(value=JUGValidator.class)
public class JUG extends EntityBase {

    private static final long serialVersionUID = -40063909128565029L;

    /**
     * JUG name
     */
    @NotBlank
    private String name;

    @CascadeValidation
    private Country country;

    private String webSite;

    private Double latitude;

    private Double longitude;
    
    private String infos;

    private Boolean modifiedKmlData;

    private byte[] logo;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
    
    @Column(length = 1024)
    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public Boolean isModifiedKmlData() {
        return modifiedKmlData;
    }

    public void setModifiedKmlData(Boolean modifiedKmlData) {
        this.modifiedKmlData = modifiedKmlData;
    }

    @Lob @Basic(fetch = FetchType.LAZY)
    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Transient
    public String getWebSiteUrl() {
        String result = this.getWebSite();
        if (this.getWebSite() != null && !this.getWebSite().contains("://")) {
            result = "http://"+result;
        }
        return result;
    }
}
