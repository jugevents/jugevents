/**
 * 
 */
package it.jugpadova.blo;

import it.jugpadova.dao.SpeakerDao;
import it.jugpadova.po.Event;
import it.jugpadova.po.Speaker;

import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.directwebremoting.annotations.RemoteProxy;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * BO for Speaker entity.
 *
 */
@Component
@RemoteProxy(name = "speakerBo")
public class SpeakerBo {
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



}
