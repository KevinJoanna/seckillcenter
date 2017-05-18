package com.mockuai.seckillcenter.core.filter.conf;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujin.zzq
 */
public class FilterConfParser {
    private String rootpath = "/filters/";

    public static void main(String[] args) {
        FilterConfParser conf = new FilterConfParser();
        try {
            System.out.println(conf.parseConfig("model_biz_support.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FilterConfBean parseConfig(String filterConfFileName) throws Exception {
        //载入filter配置文件
        InputStream is = loadFile(filterConfFileName);
        if (is == null) {
            return null;
        }

//		byte[] data = new byte[1024];
//		int index = is.read(data);
//		System.out.println(new String(data));

        FilterConfBean filterConfBean = new FilterConfBean();
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = fac.newDocumentBuilder();
        Document doc = builder.parse(is);
        NodeList nodeList = doc.getElementsByTagName(FilterConfElementEnum.ELEMENT_CHAIN.getValue());
        Map<String, List<FilterDescBean>> chainMap = new HashMap<String, List<FilterDescBean>>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element chainElement = (Element) nodeList.item(i);
            List<FilterDescBean> chain = parseChain(chainElement);
            chainMap.put(chainElement.getAttribute(FilterConfElementEnum.ELEMENT_ATTRBUTE_NAME.getValue()), chain);
        }

        nodeList = doc.getElementsByTagName(FilterConfElementEnum.ELEMENT_EXTENTION.getValue());
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) instanceof Element
                    && ((Element) nodeList.item(i)).getAttribute("value") != null
                    && ((Element) nodeList.item(i)).getAttribute("value").equalsIgnoreCase("true")) {
                filterConfBean.setExtension(true);
            }
        }
        filterConfBean.setChainMap(chainMap);
        return filterConfBean;
    }

    private List<FilterDescBean> parseChain(Element chainElement) {
        List<FilterDescBean> chain = new ArrayList<FilterDescBean>();
        NodeList cmds = chainElement.getChildNodes();
        for (int i = 0; i < cmds.getLength(); i++) {
            Node cmd = cmds.item(i);
            if (cmd instanceof Element) {
                chain.add(parseFilter((Element) cmd));
            }
        }
        return chain;

    }

    private FilterDescBean parseFilter(Element filterElement) {
        FilterDescBean filterDesc = new FilterDescBean();
        String v = filterElement.getAttribute(FilterConfElementEnum.ELEMENT_ATTR_FACTORY.getValue());
        if (v != null && v.trim().length() > 0)
            filterDesc.setFactoryClass(v.trim());
        v = filterElement.getAttribute(FilterConfElementEnum.ELEMENT_ATTR_IMPL_CLASS.getValue());
        if (v != null && v.trim().length() > 0)
            filterDesc.setImplClass(v.trim());
        v = filterElement.getAttribute(FilterConfElementEnum.ELEMENT_ATTR_INTERFACE.getValue());
        if (v != null && v.trim().length() > 0)
            filterDesc.setIntfClass(v);
        v = filterElement.getAttribute(FilterConfElementEnum.ELEMENT_ATTR_METHOD.getValue());
        if (v != null && v.trim().length() > 0)
            filterDesc.setMethodName(v);
        v = filterElement.getAttribute(FilterConfElementEnum.ELEMENT_ATTR_EXCLUSION.getValue());
        if (v != null && v.trim().length() > 0)
            filterDesc.setExclusion(v);
        NodeList params = filterElement.getChildNodes();

        for (int i = 0; i < params.getLength(); i++) {
            Node param = params.item(i);
            if (param instanceof Element) {
                filterDesc.setAttribute(((Element) param)
                                .getAttribute(FilterConfElementEnum.ELEMENT_ATTRBUTE_NAME.getValue()),
                        ((Element) param).getTextContent().trim());
            }
        }
        return filterDesc;
    }

    private InputStream loadFile(String fileName) {
        return this.getClass().getResourceAsStream(rootpath + fileName);
    }
}