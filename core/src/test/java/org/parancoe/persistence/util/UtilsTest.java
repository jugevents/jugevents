package org.parancoe.persistence.util;

import junit.framework.TestCase;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.parancoe.util.Utils;


public class UtilsTest extends TestCase {
 public void testConvertToNameValueList(){
        Map input = new HashMap();
       
        input.put("A","B");
        input.put("C","D");
        input.put("E","F");

        List<String> expected = new ArrayList();
        //changed add order in the list
        expected.add("E=F");
        expected.add("A=B");
        expected.add("C=D");
        
        assertEquals(expected, Utils.convertToNameValueList(input));
    }
}
