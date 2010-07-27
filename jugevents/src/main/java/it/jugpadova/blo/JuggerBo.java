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
import it.jugpadova.bean.JuggerSearch;
import it.jugpadova.dao.JUGDao;
import it.jugpadova.dao.JuggerDao;
import it.jugpadova.exception.EmailAlreadyPresentException;
import it.jugpadova.exception.UserAlreadyEnabledException;
import it.jugpadova.exception.UserAlreadyPresentsException;
import it.jugpadova.exception.UserNotEnabledException;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Jugger;
import it.jugpadova.util.SecurityUtilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.mail.internet.MimeMessage;

import org.springframework.security.providers.encoding.MessageDigestPasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.proxy.scriptaculous.Effect;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.parancoe.plugins.security.Authority;
import org.parancoe.plugins.security.AuthorityDao;
import org.parancoe.plugins.security.SecureUtility;
import org.parancoe.plugins.security.User;
import org.parancoe.plugins.security.UserDao;
import org.parancoe.plugins.world.Continent;
import org.parancoe.plugins.world.ContinentDao;
import org.parancoe.plugins.world.Country;
import org.parancoe.plugins.world.CountryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * Defines business methods for Jugger entity.
 *
 * @author Enrico Giurin, Lucio Benfante
 *
 */
@Component
@RemoteProxy(name = "juggerBo")
public class JuggerBo {

    private static final Logger logger = Logger.getLogger(JuggerBo.class);
    @Autowired
    private JuggerDao juggerDao;
    @Autowired
    private ContinentDao continentDao;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private JUGDao jugDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthorityDao authorityDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private JugBo jugBo;
    @Autowired
    private ServicesBo servicesBo;
    @Autowired
    private Conf conf;

    public Jugger retrieveJugger(Long id) {
        return juggerDao.read(id);
    }

    /**
     * Creates and persists new Jugger.
     *
     * @param jugger
     * @param baseUrl
     *            for confirmation mail
     * @throws Exception
     */
    public void newJugger(Jugger jugger, String baseUrl,
            boolean requiredReliability, String motivation)
            throws EmailAlreadyPresentException, UserAlreadyPresentsException,
            IOException {
        // check if it exists yet a jugger with the same email
        Jugger prevJugger = juggerDao.findByEmail(jugger.getEmail());
        if (prevJugger != null) {
            throw new EmailAlreadyPresentException(
                    "An user tried to register with an email that exists yet");
        }
        // creates or updated jug associated to jugger
        JUG jug = jugBo.saveJUG(jugger);
        // assign values to jugger
        jugger.setJug(jug);
        jugger.setUser(newUser(jugger.getUser().getUsername()));
        jugger.setConfirmationCode(generateConfirmationCode(jugger));
        juggerDao.store(jugger);
        if (requiredReliability) {
            servicesBo.requireReliability(jugger, motivation, baseUrl);
        }
        sendEmail(jugger, baseUrl, "Please Confirm your Jugger registration",
                jugger.getConfirmationCode(),
                "it/jugpadova/jugger-registration-confirmation.vm");
        logger.info("Jugger (" + jugger.getUser().getUsername()
                + ") has been created with success");
    }

    /**
     * Send mail to the user for changing password.
     *
     * @param jugger
     * @param baseUrl
     * @throws Exception
     */
    public void passwordRecovery(Jugger jugger, String baseUrl)
            throws Exception {
        jugger.setChangePasswordCode(generateConfirmationCode(jugger));
        juggerDao.store(jugger);
        // send mail to user
        sendEmail(jugger, baseUrl, "Password Recovery",
                jugger.getChangePasswordCode(),
                "it/jugpadova/password-recovery.vm");
    }

    private String generateConfirmationCode(Jugger jugger) {
        return new MessageDigestPasswordEncoder("MD5", true).encodePassword(
                jugger.getFirstName() + jugger.getLastName() + jugger.getEmail(),
                new Date());
    }

    public Jugger enableJugger(Jugger jugger, String password)
            throws UserAlreadyEnabledException {
        if (jugger.getUser().isEnabled()) {
            throw new UserAlreadyEnabledException("User " + jugger.getUser().
                    getUsername() + " already enabled");
        }
        jugger.getUser().setEnabled(true);
        String encryptedPWD = SecurityUtilities.encodePassword(password, jugger.
                getUser().
                getUsername());
        jugger.getUser().setPassword(encryptedPWD);
        //jugger.getUser().setPassword(password);
        // one way code...so regenerate it
        jugger.setConfirmationCode(generateConfirmationCode(jugger));
        juggerDao.store(jugger);
        logger.info("Username " + jugger.getUser().getUsername()
                + " enabled to jugevents");
        return jugger;
    }

    public Jugger changePassword(Jugger jugger, String password)
            throws UserNotEnabledException {
        if (!jugger.getUser().isEnabled()) {
            throw new UserNotEnabledException("User " + jugger.getUser().
                    getUsername() + " is not enabled");
        }
        String encryptedPWD = SecurityUtilities.encodePassword(password, jugger.
                getUser().
                getUsername());
        jugger.getUser().setPassword(encryptedPWD);
        // one way code...so regenerate it
        jugger.setChangePasswordCode(generateConfirmationCode(jugger));
        juggerDao.store(jugger);
        logger.info("User " + jugger.getUser().getUsername()
                + " changed its password");
        return jugger;
    }

    @RemoteMethod
    public List findPartialContinent(String partialContinent) {
        List<String> result = new ArrayList<String>();
        if (!StringUtils.isBlank(partialContinent)) {
            try {
                List<Continent> continents = continentDao.findByPartialName(
                        "%" + partialContinent + "%");
                Iterator<Continent> itContinents = continents.iterator();
                while (itContinents.hasNext()) {
                    Continent continent = itContinents.next();
                    result.add(continent.getName());
                }
            } catch (Exception e) {
                logger.error("Error completing the continent", e);
            }
        }
        return result;
    }

    @RemoteMethod
    public List findPartialCountryWithContinent(String partialCountry,
            String partialContinent) {
        List<String> result = new ArrayList<String>();
        if (!StringUtils.isBlank(partialCountry)) {
            try {
                List<Country> countries = countryDao.
                        findByPartialLocalNameAndContinent(
                        "%" + partialCountry + "%",
                        "%" + partialContinent + "%");
                Iterator<Country> itCountries = countries.iterator();
                while (itCountries.hasNext()) {
                    Country country = itCountries.next();
                    result.add(country.getLocalName());
                }
            } catch (Exception e) {
                logger.error("Error completing the country", e);
            }
        }
        return result;
    }

    @RemoteMethod
    public List findPartialCountry(String partialCountry) {
        List<String> result = new ArrayList<String>();
        if (!StringUtils.isBlank(partialCountry)) {
            try {
                List<Country> countries = countryDao.findByPartialEnglishName(
                        partialCountry + "%");
                Iterator<Country> itCountries = countries.iterator();
                while (itCountries.hasNext()) {
                    Country country = itCountries.next();
                    result.add(country.getEnglishName());
                }
            } catch (Exception e) {
                logger.error("Error completing the country", e);
            }
        }
        return result;
    }

    @RemoteMethod
    public List findPartialJugNameWithCountry(String partialJugName,
            String partialCountry) {

        List<String> result = new ArrayList<String>();
        if (!StringUtils.isBlank(partialJugName)) {
            try {
                List<JUG> jugs = jugDao.findByPartialJugNameAndCountry(
                        "%" + partialJugName + "%",
                        "%" + partialCountry + "%");

                Iterator<JUG> itJugs = jugs.iterator();
                while (itJugs.hasNext()) {
                    JUG jug = itJugs.next();
                    result.add(jug.getName());
                }

            } catch (Exception e) {
                logger.error("Error completing the JUG Name", e);
            }
        }
        return result;
    }

    @RemoteMethod
    public List findPartialJugNameWithCountryAndContinent(
            String partialJugName, String partialCountry,
            String partialContinent) {
        List<String> result = new ArrayList<String>();
        if (!StringUtils.isBlank(partialJugName)) {
            try {
                List<JUG> jugs = jugDao.
                        findByPartialJugNameAndCountryAndContinent(
                        "%" + partialJugName + "%",
                        "%" + partialCountry + "%",
                        "%" + partialContinent + "%");
                for (JUG jug : jugs) {
                    result.add(jug.getName());
                }
            } catch (Exception e) {
                logger.error("Error completing the JUG Name", e);
            }
        }
        return result;
    }

    @RemoteMethod
    public List findPartialJugName(String partialJugName) {
        List<String> result = new ArrayList<String>();
        if (!StringUtils.isBlank(partialJugName)) {
            try {
                List<JUG> jugs = jugDao.findByPartialName("%" + partialJugName
                        + "%");
                for (JUG jug : jugs) {
                    result.add(jug.getName());
                }
            } catch (Exception e) {
                logger.error("Error completing the JUG Name", e);
            }
        }
        return result;
    }

    public void disableJugger(String username) {
        Jugger jugger = juggerDao.searchByUsername(username);
        jugger.getUser().setEnabled(false);
        logger.info("User: " + username + " has been disabled");
    }

    public void enableJugger(String username) {
        Jugger jugger = juggerDao.searchByUsername(username);
        jugger.getUser().setEnabled(true);
        logger.info("User: " + username + " has been enabled");
    }

    public List<Jugger> searchJugger(JuggerSearch juggerSearch) {
        List<Jugger> juggers = new LinkedList<Jugger>();
        try {
            DetachedCriteria searchCriteria =
                    DetachedCriteria.forClass(Jugger.class);
            if (StringUtils.isNotBlank(juggerSearch.getJUGName())) {
                DetachedCriteria jugCriteria =
                        searchCriteria.createCriteria("jug");
                jugCriteria.add(Restrictions.like("name",
                        juggerSearch.getJUGName(), MatchMode.ANYWHERE));
            }
            if (StringUtils.isNotBlank(juggerSearch.getUsername())) {
                DetachedCriteria usernameCriteria =
                        searchCriteria.createCriteria("user");
                usernameCriteria.add(Restrictions.like("username",
                        juggerSearch.getUsername(), MatchMode.ANYWHERE));
            }
            if (juggerSearch.getRRStatus() != JuggerSearch.INVALID_STATUS) {
                DetachedCriteria statusCriteria =
                        searchCriteria.createCriteria("reliabilityRequest");
                statusCriteria.add(Restrictions.eq("status",
                        juggerSearch.getRRStatus()));
            }



            juggers = juggerDao.searchByCriteria(searchCriteria);
        } catch (Exception e) {
            logger.error("Error searching events", e);
        }

        return juggers;
    }

    @RemoteMethod
    public void populateJugFields(String jugName) {
        JUG jug = jugDao.findByName(jugName);
        if (jug != null) {
            jugFieldsEnable(true);
            WebContext wctx = WebContextFactory.get();
            ScriptSession session = wctx.getScriptSession();
            Util util = new Util(session);

            Effect effect = new Effect(session);

            String cp = wctx.getHttpServletRequest().getContextPath();

            Country country = jug.getCountry();

            if (country != null) {
                util.setValue("jugger.jug.country.englishName",
                        country.getEnglishName());
            } else {
                util.setValue("jugger.jug.country.englishName", null);
            }
            // effect.highlight("jugger.jug.country.englishName");            

            util.setValue("jugger.jug.internalFriendlyName", jug.
                    getInternalFriendlyName());

            util.setValue("jugger.jug.webSite", jug.getWebSite());
            // effect.highlight("jugger.jug.webSite");

            util.setValue("jugLogo",
                    "<img style=\"float: right;\" src=\"" + cp
                    + "/bin/jugLogo.bin?id=" + jug.getId()
                    + "\" alt=\"JUG Logo\" width=\"100\"/>");

            if (jug.getLongitude() != null) {
                util.setValue("jugger.jug.longitude", jug.getLongitude().
                        toString());
            } else {
                util.setValue("jugger.jug.longitude", null);
            }
            // effect.highlight("jugger.jug.longitude");
            if (jug.getLatitude() != null) {
                util.setValue("jugger.jug.latitude",
                        jug.getLatitude().toString());
            } else {
                util.setValue("jugger.jug.latitude", null);
            }
            // effect.highlight("jugger.jug.latitude");

            // effect.highlight("jugger.jug.infos");

            util.setValue("jugger.jug.timeZoneId", jug.getTimeZoneId());
            util.setValue("jugger.jug.contactName", jug.getContactName());
            util.setValue("jugger.jug.contactEmail", jug.getContactEmail());

            util.setValue("jugger.jug.infos", jug.getInfos());

            // fixJugFields(false);
        }

    }

    /**
     * Read only/not read only all jug fields.
     *
     * @param jugName
     */
    @RemoteMethod
    public void readOnlyJugFields(String jugName, boolean reliability) {
        if ((jugDao.findByICName(jugName) != null) && (!reliability)) {
            jugFieldsEnable(false);
        } else {
            jugFieldsEnable(true);
        }
    }

    /**
     * Enable or disable the jug fields in page
     *
     * @param enable enable the fields if true. Disable, if false.
     */
    private void jugFieldsEnable(boolean enable) {
        WebContext wctx = WebContextFactory.get();
        ScriptSession session = wctx.getScriptSession();
        Util util = new Util(session);
        String jsFunction = null;
        if (enable) {
            jsFunction = "parancoe.util.fullEnableFormElement";
        } else {
            jsFunction = "parancoe.util.fullDisableFormElement";
        }
        util.addFunctionCall(jsFunction, "jugger.jug.country.englishName");
        util.addFunctionCall(jsFunction, "jugger.jug.internalFriendlyName");
        util.addFunctionCall(jsFunction, "jugger.jug.webSite");
        util.addFunctionCall(jsFunction, "jugger.jug.logo");
        util.addFunctionCall(jsFunction, "jugger.jug.longitude");
        util.addFunctionCall(jsFunction, "jugger.jug.latitude");
        util.addFunctionCall(jsFunction, "jugger.jug.infos");
    }

    /**
     * Update the frienly URLs div.
     *
     * @param jugName
     */
    @RemoteMethod
    public void updateFriendlyUrls(String jugName, String friendlyName) throws
            UnsupportedEncodingException {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setCalendar(cal);
        WebContext wctx = WebContextFactory.get();
        ScriptSession session = wctx.getScriptSession();
        Util util = new Util(session);
        String friendly = jugName;
        if (StringUtils.isNotBlank(friendlyName)) {
            friendly = friendlyName;
        }
        friendly = URLEncoder.encode(friendly, "UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"").append(conf.getJugeventsBaseUrl()).append(
                "/ical/").append(friendly).append("\">").append(conf.
                getJugeventsBaseUrl()).append("/ical/").append(friendly).append(
                "</a><br/>");
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        Date startDate = cal.getTime();
        sb.append("<a href=\"").append(conf.getJugeventsBaseUrl()).append(
                "/ical/").append(friendly).append(
                "/").append(df.format(startDate)).append("\">").append(conf.
                getJugeventsBaseUrl()).append("/ical/").append(friendly).append(
                "/").append(df.format(startDate)).append(
                "</a><br/>");
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.MONTH, 11);
        Date endDate = cal.getTime();
        sb.append("<a href=\"").append(conf.getJugeventsBaseUrl()).append(
                "/ical/").append(friendly).append(
                "/").append(df.format(startDate)).append(
                "/").append(df.format(endDate)).append("\">").append(conf.
                getJugeventsBaseUrl()).append("/ical/").append(friendly).append(
                "/").append(df.format(startDate)).append(
                "/").append(df.format(endDate)).append(
                "</a>");
        util.setValue("friendlyUrls", sb.toString());
    }

    /**
     * Updates Jugger and its childs.
     *
     * @param jugger
     */
    public void update(Jugger jugger, boolean requiredReliability,
            String motivation, String baseURL) throws IOException {

        if (requiredReliability) {
            servicesBo.requireReliability(jugger, motivation, baseURL);
        }

        User newUser = updateUser(jugger.getUser());

        JUG newJUG = jugBo.saveJUG(jugger);
        jugger.setJug(newJUG);
        jugger.setUser(newUser);
        juggerDao.store(jugger);
        logger.info("Updated Jugger with id " + jugger.getId());
    }

    public User newUser(String username) throws UserAlreadyPresentsException {
        Authority authority = authorityDao.findByRole("ROLE_JUGGER");
        User userToValidate = null;

        // check if username is already presents
        if (userDao.findByUsername(username) != null) {

            throw new UserAlreadyPresentsException("User with username: "
                    + username + " already presents in the database!");
        }

        // set authority to jugger
        userToValidate = SecureUtility.newUserToValidate(username);
        userToValidate.getAuthorities().add(authority);
        // create the user
        userDao.store(userToValidate);

        return userToValidate;
    }

    public User updateUser(User newUser) {
        User user = userDao.findByUsername(newUser.getUsername());
        if (user.getPassword().equals(newUser.getPassword())) {
            // we only
            // update the
            // password
            return user;
        }
        user.setPassword(newUser.getPassword());
        userDao.store(user);
        logger.info("User " + user.getUsername() + " has been updated");
        return user;
    } // end of method

    public void delete(String username) {
        Jugger jugger = juggerDao.searchByUsername(username);

        if (jugger == null) {
            throw new IllegalArgumentException("No jugger with username "
                    + username);
        }
        User user = jugger.getUser();
        userDao.delete(user);
        juggerDao.delete(jugger);
        // verify if jugger has been deleted
        jugger = juggerDao.searchByUsername(username);
        if (jugger == null) {
            logger.info("Jugger with username: " + username
                    + " has been deleted");
            return;
        }
        throw new RuntimeException("There were some problems deleting jugger: "
                + username);
    }

    /**
     * General jugger mail sender
     *
     * @param jugger
     * @param baseUrl
     * @param subject
     * @param oneWayCode
     * @param template
     */
    private void sendEmail(final Jugger jugger, final String baseUrl,
            final String subject, final String oneWayCode,
            final String template) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @SuppressWarnings(value = "unchecked")
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(jugger.getEmail());
                message.setFrom(conf.getConfirmationSenderEmailAddress());
                message.setSubject(subject);
                Map model = new HashMap();
                model.put("jugger", jugger);
                model.put("baseUrl", baseUrl);
                model.put("oneWayCode", URLEncoder.encode(oneWayCode, "UTF-8"));
                model.put("username", URLEncoder.encode(jugger.getUser().
                        getUsername(), "UTF-8"));
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, template, model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
    }

    public Jugger searchByUsername(String username) {
        return juggerDao.searchByUsername(username);
    }

    public Jugger searchByUsernameAndChangePasswordCode(String username,
            String changePasswordCode) {
        return juggerDao.findByUsernameAndChangePasswordCode(username,
                changePasswordCode);
    }

    public Jugger searchByUsernameAndConfirmationCode(String username,
            String confirmationCode) {
        return juggerDao.findByUsernameAndConfirmationCode(username,
                confirmationCode);
    }

    public Jugger searchByEmail(String email) {
        return juggerDao.findByEmail(email);
    }

    public List<Jugger> searchAllOrderByUsername() {
        return juggerDao.findAllOrderByUsername();
    }
} // end of class

