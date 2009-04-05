/**
 * 
 */
package it.jugpadova.blo;

import it.jugpadova.dao.SpeakerDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.JUG;
import it.jugpadova.po.Speaker;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.proxy.scriptaculous.Effect;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.parancoe.plugins.world.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * BO for Speaker entity.
 *
 */
@Component
@RemoteProxy(name = "speakerBo")
public class SpeakerBo {
	private static final Logger logger = Logger.getLogger(SpeakerBo.class);
    
	@Autowired
	private SpeakerDao speakerDao;
	
	/**
	 * Execute a full text search on speakers. Hardly copied from the corresponding 
	 * search in EventBo.
	 * @param searchQuery
	 * @param maxResults
	 * @return
	 * @throws ParseException
	 */
	public List<Speaker> search(String searchQuery, int maxResults) throws ParseException {
        List<Speaker> result = null;
        Session session =
                this.speakerDao.getHibernateTemplate().
                getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{
                    "firstName", "lastName", "resume"},
                new StandardAnalyzer());
        org.apache.lucene.search.Query query = parser.parse(searchQuery);
        FullTextQuery hibQuery =
                fullTextSession.createFullTextQuery(query, Speaker.class);
        //  hibQuery.setSort(arg0)      
        if (maxResults > 0) {
            hibQuery.setMaxResults(maxResults);
        }
        result = hibQuery.list();
        return result;
    }
	
	
	public void regenerateLuceneIndexes() {
        Session session =
                this.speakerDao.getHibernateTemplate().
                getSessionFactory().getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        fullTextSession.setFlushMode(FlushMode.MANUAL);
        fullTextSession.setCacheMode(CacheMode.IGNORE);
        ScrollableResults results = fullTextSession.createCriteria(Speaker.class).
                scroll(ScrollMode.FORWARD_ONLY);
        
        int index = 0;
        while (results.next()) {
            index++;
            fullTextSession.index(results.get(0)); //index each element

            if (index % 50 == 0) {
                fullTextSession.clear(); //clear every batchSize since the queue is processed

            }
        }
    }
	
	 @RemoteMethod
	    public void fullTextSearch(String searchQuery, int maxResults) {
	        if (StringUtils.isNotBlank(searchQuery)) {
	            WebContext wctx = WebContextFactory.get();
	            ScriptSession session = wctx.getScriptSession();
	            Util util = new Util(session);	           
	            List<Speaker> speakers = null;
	            try {
	            	speakers = this.search(searchQuery, maxResults);
	            } catch (ParseException pex) {
	                logger.info("Error parsing query: " + searchQuery);
	            } catch (Exception ex) {
	                logger.error("Error searching speakers", ex);
	            }
	            if (speakers != null && speakers.size() > 0) {
	                StringBuilder sb = new StringBuilder();
	                for (Speaker speaker : speakers) {
	                	Event event = speaker.getEvent();
	                    sb.append("<div>\n");
	                    sb.append(speaker.getFirstName()).append(" ").append(speaker.getLastName()).append("<br/>");
	                    sb.append(event.getTitle()).append("&nbsp;(").append(event.getOwner().getJug().getName()).append(")<br/>");	                                    
	                    sb.append("&nbsp;<a href=\"javascript:populateSpeakerFields(\'").append(speaker.getId()).append("\');\">Select this speaker</a>").append("<br/>");	                   
	                    sb.append("</div>\n").append("<br/>");
	                }
	                util.setValue("content_textSearch_result", sb.toString(), false);
	            } else {
	                util.setValue("content_textSearch_result", "", false);
	            }
	        }
	    }
	 
	 
	 @RemoteMethod
	    public void populateSpeakerFields(String speakerId) {
	        Speaker speaker = speakerDao.get(new Long(speakerId));
	        if (speaker != null) {
	           
	            WebContext wctx = WebContextFactory.get();
	            ScriptSession session = wctx.getScriptSession();
	            Util util = new Util(session);

	            Effect effect = new Effect(session);

	            String cp = wctx.getHttpServletRequest().getContextPath();
	            util.setValue("firstName", speaker.getFirstName());
	            util.setValue("lastName", speaker.getLastName());
	            util.setValue("email", speaker.getEmail());
	            util.setValue("resume", speaker.getResume());
	            util.setValue("url", speaker.getUrl());
	            util.setValue("skypeId", speaker.getSkypeId());
	            //TODO to find a way to populate the picture
                /*
	            util.setValue("speakerImage",
	                    "<img style=\"float: right;\" src=\"" + cp +
	                    "/bin/pictureSpeaker.bin?id=" + speaker.getId() +
	                    "\" alt=\"Speaker Image\" width=\"100\"/>");
	            */


	        // fixJugFields(false);
	        }

	    }



}
