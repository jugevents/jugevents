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

package it.jugpadova.util;

import java.io.IOException;
import java.io.StringReader;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * A class for converting HTML to plain text.
 *
 * @author Lucio Benfante
 */
public class HtmlTextConverter {

    public String convert(String htmlString) {
        MyParserCallback callback = new HtmlTextConverter.MyParserCallback();
        StringReader reader = new StringReader(htmlString);
        try {
            (new ParserDelegator()).parse(reader, callback, false);
        } catch (IOException ioe) {}
        return callback.getResult();
    }

    public class MyParserCallback extends HTMLEditorKit.ParserCallback {

        private StringBuffer result = new StringBuffer();

        public String getResult() {
            return result.toString();
        }

        @Override
        public void handleText(char[] data, int pos) {
            String s = new String(data);
            s = s.replaceAll("&amp;", "&");
            s = s.replaceAll("&lt;", "<");
            s = s.replaceAll("&gt;", ">");
            s = s.replaceAll("&quot;", "\"");
            result.append(s);
        }

        @Override
        public void handleComment(char[] data, int pos) {
        }

        @Override
        public void handleStartTag(HTML.Tag t,
                                   MutableAttributeSet a, int pos) {
            if (t == HTML.Tag.B) {
                result.append("*");
            } else if (t == HTML.Tag.I) {
                result.append("_");
            } else if (t == HTML.Tag.P) {
                result.append("\n");
            } else if (t == HTML.Tag.DIV) {
                result.append("\n");
            } else if (t == HTML.Tag.H1) {
                result.append("\n");
            } else if (t == HTML.Tag.H2) {
                result.append("\n");
            } else if (t == HTML.Tag.H3) {
                result.append("\n");
            } else if (t == HTML.Tag.H4) {
                result.append("\n");
            } else if (t == HTML.Tag.H5) {
                result.append("\n");
            } else if (t == HTML.Tag.H6) {
                result.append("\n");
            } else if (t == HTML.Tag.LI) {
                result.append("  * ");
            }
        }

        @Override
        public void handleEndTag(HTML.Tag t, int pos) {
            if (t == HTML.Tag.B) {
                result.append("*");
            } else if (t == HTML.Tag.I) {
                result.append("_");
            } else if (t == HTML.Tag.P) {
                result.append("");
            }
        }

        @Override
        public void handleSimpleTag(HTML.Tag t,
                                    MutableAttributeSet a, int pos) {
            if (t == HTML.Tag.BR) {
                result.append("\n");
            }
        }
    }

}
