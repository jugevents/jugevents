package it.jugpadova.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.RangeFilter;
import org.hibernate.search.annotations.Factory;

/**
 * A factory for the Lucene filter producing only the not passed events.
 * 
 * @author lucio
 */
public class NotPassedEventsFilterFactory {
    static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
    
    @Factory
    public Filter getFilter() {        
        return new RangeFilter("startDate", df.format(new Date()), null, true, false);
    }
}
