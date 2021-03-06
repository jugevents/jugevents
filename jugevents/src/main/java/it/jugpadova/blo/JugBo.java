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
package it.jugpadova.blo;

import it.jugpadova.blol.ServicesBo;
import it.jugpadova.Conf;
import it.jugpadova.dao.JUGDao;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Jugger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import nu.xom.Serializer;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.parancoe.plugins.world.Continent;
import org.parancoe.plugins.world.ContinentDao;
import org.parancoe.plugins.world.Country;
import org.parancoe.plugins.world.CountryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author lucio
 */
@Component
public class JugBo {

    private static final Logger logger = Logger.getLogger(JugBo.class);
    private static final String EARTH_NAMESPACE =
            "http://earth.google.com/kml/2.1";
    @Autowired
    private ContinentDao continentDao;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private JUGDao jugDao;
    @Autowired
    private ServicesBo servicesBo;
    @Autowired
    private Conf conf;

    /**
     * Update the list of JUGs taking data from a KML file.
     * 
     * @param kmlUrl
     *            The URL of the KML file. If null, it's used the defaultKmlUrl.
     * @return Log messages
     */
    public void updateJugList(String kmlUrl) throws Exception {
        logger.info("Update JUG List started...");
        logger.info("kmlUrl = " + kmlUrl);
        if (kmlUrl == null) {
            kmlUrl = conf.getDefaultKmlUrl();
            logger.info("Using defaultKmlUrl: " + conf.getDefaultKmlUrl());
        }
        Builder parser = new Builder();
        Document doc = parser.build(kmlUrl);
        Element kml = doc.getRootElement();
        Element document = kml.getFirstChildElement("Document", EARTH_NAMESPACE);
        Elements continents = document.getChildElements("NetworkLink",
                EARTH_NAMESPACE);
        for (int i = 0; i < continents.size(); i++) {
            Element continent = continents.get(i);
            String continentName = continent.getFirstChildElement("name",
                    EARTH_NAMESPACE).getValue();
            if ("Oceania".equals(continentName)) {
                continentName = "Australia";
                logger.info("Substituted continent name: Oceania with Australia");
            }
            Continent continentPo = continentDao.findByName(continentName);
            if (continentPo != null) {
                String continentUrl = continent.getFirstChildElement("Link",
                        EARTH_NAMESPACE).getFirstChildElement("href",
                        EARTH_NAMESPACE).getValue();
                logger.info("Loading " + continentName + " countries from " +
                        continentUrl + ".");
                Builder continentParser = new Builder();
                continentUrl = continentUrl.replaceFirst("http:", "https:");
                Document continentDoc = continentParser.build(continentUrl);
                Element continentKml = continentDoc.getRootElement();
                Elements countries = continentKml.getChildElements("Folder",
                        EARTH_NAMESPACE);
                for (int j = 0; j < countries.size(); j++) {
                    Element country = countries.get(j);
                    String countryName = country.getFirstChildElement("name",
                            EARTH_NAMESPACE).getValue();
                    Country countryPo =
                            countryDao.findByEnglishName(countryName);
                    if (countryPo != null) {
                        logger.info("Loading " + countryName + " JUGs.");
                        Elements placemarks =
                                country.getChildElements("Placemark",
                                EARTH_NAMESPACE);
                        for (int k = 0; k < placemarks.size(); k++) {
                            Element placemark = placemarks.get(k);
                            String jugName =
                                    placemark.getFirstChildElement("name",
                                    EARTH_NAMESPACE).getValue();
                            String description = placemark.getFirstChildElement(
                                    "description",
                                    EARTH_NAMESPACE).getValue();
                            Element point =
                                    placemark.getFirstChildElement("Point",
                                    EARTH_NAMESPACE);
                            String coordinatesStr =
                                    point.getFirstChildElement("coordinates",
                                    EARTH_NAMESPACE).getValue();
                            String[] coordinatesArr = coordinatesStr.split(",");
                            Double longitude =
                                    Double.parseDouble(coordinatesArr[0]);
                            Double latitude =
                                    Double.parseDouble(coordinatesArr[1]);
                            JUG jug = jugDao.findByName(jugName);
                            if (jug != null && jug.isModifiedKmlData() != null &&
                                    jug.isModifiedKmlData().booleanValue()) {
                                logger.info(
                                        "Skipping updating of " + jugName +
                                        " kml data, because yet modified through JUG Events.");
                            } else {
                                if (jug == null) {
                                    logger.info("Creating a new JUG: " + jugName);
                                    jug = new JUG();
                                    jug.setName(jugName);
                                } else {
                                    logger.info("Updating a JUG: " + jugName);
                                }
                                jug.setCountry(countryPo);
                                jug.setLongitude(longitude);
                                jug.setLatitude(latitude);
                                jug.setInfos(description);
                                jug.setModifiedKmlData(Boolean.FALSE);
                                jugDao.store(jug);
                            }
                        }
                    } else {
                        logger.warn(
                                "Country " + countryName +
                                " not found in the database. Not loading its JUGs.");
                    }
                }
            } else {
                logger.warn("Continent " + continentName +
                        " not found in the database. Not loading its JUGs.");
            }
        }
        logger.info("Update JUG List completed...");
    }

    public Document buildKml() {
        Element kml = new Element("kml", EARTH_NAMESPACE);
        Element document = new Element("Document", EARTH_NAMESPACE);
        kml.appendChild(document);
        Element documentName = new Element("name", EARTH_NAMESPACE);
        documentName.appendChild("Java User Group International");
        Element documentDescription = new Element("description",
                EARTH_NAMESPACE);
        documentDescription.appendChild(
                "\nGeographic location, leaders and web site information for JUGs from\n" +
                "around the world. For convenience, they are grouped by continent." +
                "Instructions for submitting new JUG entries can be found" +
                "<a href=\"http://wiki.java.net/bin/view/JUGs/JUG-MAP\">here</a>." +
                "<br/>&nbsp;<br/><img src=\"http://sv-web-jug.dev.java.net/images/jug_leaders_large.gif\"><br/>&nbsp;");
        document.appendChild(documentName);
        document.appendChild(documentDescription);
        List<Continent> continents =
                continentDao.findByPartialName("%");
        for (Continent continent : continents) {
            Element continentFolder = null;
            List<Country> countries = continent.getCountries();
            for (Country country : countries) {
                Element countryFolder = null;
                List<JUG> jugs = jugDao.findByPartialJugNameAndCountry("%",
                        country.getEnglishName());
                for (JUG jug : jugs) {
                    if (jug.getLongitude() != null && jug.getLatitude() != null) {
                        if (continentFolder == null) {
                            continentFolder = new Element("Folder",
                                    EARTH_NAMESPACE);
                            Element continentName = new Element("name",
                                    EARTH_NAMESPACE);
                            String continentNameText = continent.getName();
                            if ("Australia".equals(continentNameText)) {
                                continentNameText = "Oceania";
                            }
                            continentName.appendChild(continentNameText);
                            Element continentOpen = new Element("open",
                                    EARTH_NAMESPACE);
                            continentOpen.appendChild("1");
                            continentFolder.appendChild(continentName);
                            continentFolder.appendChild(continentOpen);
                            document.appendChild(continentFolder);
                        }
                        if (countryFolder == null) {
                            countryFolder = new Element("Folder",
                                    EARTH_NAMESPACE);
                            Element countryName = new Element("name",
                                    EARTH_NAMESPACE);
                            countryName.appendChild(country.getEnglishName());
                            Element countryOpen = new Element("open",
                                    EARTH_NAMESPACE);
                            countryOpen.appendChild("0");
                            countryFolder.appendChild(countryName);
                            countryFolder.appendChild(countryOpen);
                            continentFolder.appendChild(countryFolder);
                        }
                        Element placemark = buildKmlPlacemark(jug);
                        countryFolder.appendChild(placemark);
                    }
                }
            }
        }
        return new Document(kml);
    }

    public JUG saveJUG(Jugger jugger) throws IOException {
        JUG newJUG = jugger.getJug();
        // create or find JUG
        JUG jug = jugDao.findByName(newJUG.getName());
        if (jug == null) {
            // create the JUG instance
            jug = new JUG();
        } else {
            // check if this jugger could update the JUG attribute
            if (!servicesBo.isJuggerReliable(jugger.getReliability())) {
                logger.warn("Jugger " + jugger.getUser().getUsername() +
                        " is not reliable!");
                return jug;
            }// end of if

        }// end of if

        final Boolean modifiedKmlData = evaluateModifiedKmlData(newJUG, jug);
        if (modifiedKmlData) {
            jug.setModifiedKmlData(modifiedKmlData);
        } else {
            jug.setModifiedKmlData(newJUG.isModifiedKmlData());
        }
        jug.setName(newJUG.getName());
        jug.setInternalFriendlyName(newJUG.getInternalFriendlyName());
        jug.setCountry(countryDao.findByEnglishName(newJUG.getCountry().
                getEnglishName()));
        if (newJUG.getLogo() != null && newJUG.getLogo().length > 0) {
            jug.setLogo(newJUG.getLogo());
        }
        jug.setWebSite(newJUG.getWebSite());
        jug.setLongitude(newJUG.getLongitude());
        jug.setLatitude(newJUG.getLatitude());
        jug.setTimeZoneId(newJUG.getTimeZoneId());
        jug.setContactName(newJUG.getContactName());
        jug.setContactEmail(newJUG.getContactEmail());
        if (newJUG.getCertificateTemplate() != null &&
                newJUG.getCertificateTemplate().length > 0) {
            jug.setCertificateTemplate(newJUG.getCertificateTemplate());
        }
        jug.setInfos(newJUG.getInfos());
        Long id = jug.getId();
        if (id == null) {
            jugDao.store(jug);
            logger.info("JUG with name " + jug.getName() + " has been created");
        } else {
            jugDao.store(jug);
            logger.info("JUG with name " + jug.getName() + " has been updated");
        }
        if (modifiedKmlData) {
            this.servicesBo.sendUpdatedKmlDataEmail(jugger, jug, id == null,
                    buildKmlPlacemarkText(jug, jugger.getFirstName() + " " +
                    jugger.getLastName(), jugger.getEmail()));
        }
        return jug;
    }

    public byte[] retrieveJugLogo(Long jugId) {
        JUG jug = jugDao.read(jugId);
        return jug.getLogo();
    }

    /**
     * Retrieve the personalized certificate template for a JUG.
     * 
     * @param jugId The ID of the JUG
     * @return The personalized certificate template for the JUG. null if the JUG don't have a personalized certificate template.
     */
    public InputStream retrieveJugCertificateTemplate(Long jugId) {
        InputStream result = null;
        JUG jug = jugDao.read(jugId);
        byte[] certificateTemplate = jug.getCertificateTemplate();
        if (certificateTemplate != null && certificateTemplate.length > 0) {
            result = new ByteArrayInputStream(certificateTemplate);
        }
        return result;
    }

    /**
     * Retrieve a JUG by ID.
     * 
     * @param jugId The ID of the JUG
     * @return The plate for the JUG. null if the JUG don't have a personalized certificate template.
     */
    public JUG retrieveJug(Long jugId) {
        return jugDao.read(jugId);
    }

    private Element buildKmlPlacemark(JUG jug) {
        Element placemark = new Element("Placemark", EARTH_NAMESPACE);
        Element jugName = new Element("name", EARTH_NAMESPACE);
        jugName.appendChild(jug.getName());
        Element jugDescription =
                new Element("description", EARTH_NAMESPACE);
        jugDescription.appendChild(jug.getInfos());
        Element point = new Element("Point", EARTH_NAMESPACE);
        Element coordinates =
                new Element("coordinates", EARTH_NAMESPACE);
        coordinates.appendChild(jug.getLongitude() + "," + jug.getLatitude() +
                ",0");
        Element style =
                new Element("styleUrl", EARTH_NAMESPACE);
        style.appendChild("#jugStyle");
        point.appendChild(coordinates);
        placemark.appendChild(jugName);
        placemark.appendChild(jugDescription);
        placemark.appendChild(point);
        placemark.appendChild(style);
        return placemark;
    }

    /**
     * Build the text of a KML placemark for a JUG using the XOM library.
     * 
     * @param jug The JUG for wich producing the placemark
     * @return The text of the placemark
     */
    private String buildKmlPlacemarkTextByXOM(JUG jug) throws IOException {
        final Element kmlPlacemark = buildKmlPlacemark(jug);
        final ByteArrayOutputStream kmlText = new ByteArrayOutputStream();
        Serializer serializer = new Serializer(kmlText);
        serializer.setIndent(4);
        serializer.setMaxLength(64);
        serializer.setLineSeparator("\n");
        serializer.write(new Document(kmlPlacemark));
        return kmlText.toString("UTF-8");
    }

    /**
     * Build the text of a KML placemark for a JUG.
     * 
     * @param jug The JUG for wich producing the placemark
     * @return The text of the placemark
     */
    public String buildKmlPlacemarkText(JUG jug, String leaderName,
            String leaderEmail) throws IOException {
        final String SPACER = "    ";
        final String EOL = "\n";
        StringBuilder sb = new StringBuilder();
        sb.append("<Placemark>").append(EOL);
        sb.append(SPACER).append("<name>").append(StringEscapeUtils.escapeXml(
                jug.getName())).
                append("</name>").append(EOL);
        sb.append(SPACER).append("<description>").append(EOL);
        sb.append(SPACER).append(" <![CDATA[").append(EOL);
        sb.append(SPACER).append(SPACER).append(StringEscapeUtils.escapeXml(
                jug.getInfos())).
                append("<br/>").append(EOL);
        if (StringUtils.isNotBlank(leaderName) &&
                StringUtils.isNotBlank(leaderEmail)) {
            sb.append(SPACER).append(SPACER).append("<b>Leader:</b> ").append(
                    "<a href=\"mailto:").
                    append(leaderEmail).append("\">").append(StringEscapeUtils.escapeXml(
                    leaderName)).
                    append("</a>").append("<br/>").append(EOL);
        }
        if (StringUtils.isNotBlank(jug.getWebSite())) {
            sb.append(SPACER).append(SPACER).append("<b>Site:</b> ").
                    append(jug.getWebSiteUrl()).append(EOL);
        }
        sb.append(SPACER).append(" ]]>").append(EOL);
        sb.append(SPACER).append("</description>").append(EOL);
        sb.append(SPACER).append("<Point>").append(EOL);
        sb.append(SPACER).append(SPACER).append("<coordinates>").append(
                jug.getLongitude()).
                append(",").append(jug.getLatitude()).append(",0").append(
                "</coordinates>").
                append(EOL);
        sb.append(SPACER).append("</Point>").append(EOL);
        sb.append(SPACER).append("<styleUrl>#jugStyle</styleUrl>").append(EOL);
        sb.append("</Placemark>");
        return sb.toString();
    }

    Boolean evaluateModifiedKmlData(JUG newJUG, JUG oldJUG) {
        return ((newJUG.getLongitude() != null && newJUG.getLatitude() != null &&
                StringUtils.isNotBlank(newJUG.getInfos()) &&
                StringUtils.isNotBlank(newJUG.getWebSite())) &&
                (!newJUG.getLongitude().equals(oldJUG.getLongitude()) ||
                !newJUG.getLatitude().equals(oldJUG.getLatitude()) ||
                !newJUG.getInfos().equals(oldJUG.getInfos()) ||
                !newJUG.getWebSite().equals(oldJUG.getWebSite())));
    }

    public ServicesBo getServicesBo() {
        return servicesBo;
    }

    public void setServicesBo(ServicesBo servicesBo) {
        this.servicesBo = servicesBo;
    }
}
