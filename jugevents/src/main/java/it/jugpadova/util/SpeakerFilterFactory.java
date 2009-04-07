/**
 * 
 */
package it.jugpadova.util;

import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Filter;
import org.hibernate.search.annotations.Factory;

/**
 * @author egiurin
 *
 */
public class SpeakerFilterFactory 
{
	@Factory
    public Filter getFilter() { 
		return new Filter() {

			@Override
			public BitSet bits(IndexReader indexReader) throws IOException {
				System.out.println(indexReader);
				return null;
			}
			
		};
       
    }
}//end of class
