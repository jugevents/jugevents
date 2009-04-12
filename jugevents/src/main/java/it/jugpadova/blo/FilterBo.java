package it.jugpadova.blo;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

import it.jugpadova.util.TextFilterUtils;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Component;

/**
 *
 * @author lucio
 */
@Component
@RemoteProxy(name="filterBo")
public class FilterBo {

    public FilterBo() {
    }

    @RemoteMethod
    public void populatePreview(String text, String filter,
            String previewDivId) {
        String result =
                FilterBo.filterText(text, filter, false);
        WebContext wctx = WebContextFactory.get();
        ScriptSession session = wctx.getScriptSession();
        Util util = new Util(session);
        util.setValue(previewDivId, result, false);
    }

    public static String filterText(String text, String filter,
            boolean escapeXml) {
        // Only Textile for now
        String result = TextFilterUtils.formatText(text, TextFilterUtils.TEXTILE_FILTER);
        return result;
    }
}
