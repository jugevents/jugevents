package it.jugpadova.util;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.java.textilej.parser.MarkupParser;
import net.java.textilej.parser.builder.HtmlDocumentBuilder;
import net.java.textilej.parser.markup.textile.TextileDialect;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * Utility for text filtering
 *
 * @author Lucio Benfante
 */
public class TextFilterUtils {

    private static final Pattern replaceTypoCodePattern = Pattern.compile(
            "<typo:code\\b([^>]*)>(.*?)</typo:code>", Pattern.DOTALL);
    private static final Pattern replaceLangPattern = Pattern.compile(
            "lang(?:\\s)?=(?:\\s)?([\"']+.*?[\"']+)");
    private static final Pattern replaceIncompleteTypoCodePattern = Pattern.compile(
            "<typo:code>(.*?)</typo:code>", Pattern.DOTALL);
    public static final String TEXTILE_FILTER = "textile";

    public static String formatText(String txt, String fltr) {
        String result = null;
        if (txt != null) {
            txt = filterTypoCode(txt);
            if (TEXTILE_FILTER.equals(fltr)) {
                StringWriter writer = new StringWriter();
                HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
                // avoid the <html> and <body> tags
                builder.setEmitAsDocument(false);
                MarkupParser parser = new MarkupParser(new TextileDialect());
                parser.setBuilder(builder);
                parser.parse(txt);
                result = writer.toString();
            } else {
                result = escapeHtml(txt);
            }
        }
        return result;
    }

    public static String filterTypoCode(String txt) {
        String result = null;
        Matcher matcher = replaceIncompleteTypoCodePattern.matcher(txt);
        result = matcher.replaceAll("<pre name=\"code\" class=\"xml\">$1</pre>");
        matcher = replaceTypoCodePattern.matcher(result);
        result = matcher.replaceAll("<pre name=\"code\"$1>$2</pre>");
        matcher = replaceLangPattern.matcher(result);
        result = matcher.replaceAll("class=$1");
        return result;
    }

    public static String escapeHtml(String txt) {
        return StringEscapeUtils.escapeHtml(txt);
    }
}
