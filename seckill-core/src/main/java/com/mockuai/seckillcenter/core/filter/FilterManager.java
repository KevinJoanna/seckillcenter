package com.mockuai.seckillcenter.core.filter;

import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.filter.conf.FilterConfElementEnum;
import com.mockuai.seckillcenter.core.filter.conf.FilterConfBean;
import com.mockuai.seckillcenter.core.filter.conf.FilterConfParser;
import com.mockuai.seckillcenter.core.filter.conf.FilterDescBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * com.mockuai.marketingcenter.core.filter.filter 的管理 采用的单例模式
 */
public class FilterManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterManager.class);

    /**
     * 默认的filter配置
     */
    private static final String DEFAULT_FILTER_CONF = "default";
    /**
     * 默认filter的配置文件
     */
    private static final String DEFAULT_FILTER_CONF_PATH = "filters_default_support.xml";
    /**
     * appCode和过滤器配置文件映射关系配置
     */
    private static final String APPCODE_FILTER_MAPPING_CONF_PATH = "/filters/filter_conf_mapping.xml";
    private static Map<String, FilterChainHolder> chains = new HashMap<String, FilterChainHolder>();
    private static Map<String, FilterChain> filterChainCacheMap = new HashMap<String, FilterChain>();
    private final static FilterManager instance = new FilterManager();

    private FilterManager() {
        try {
            //解析默认的filter配置
            parseDefaultFilterConf();
            //解析各应用自己的filter配置
            Map<String, String> map = parseFilterMap(APPCODE_FILTER_MAPPING_CONF_PATH);
            for (String appCode : map.keySet()) {
                if (appCode.equals(DEFAULT_FILTER_CONF) == false) {
                    parseAppFilterConf(appCode, map.get(appCode));
                }
            }
        } catch (SeckillException e) {
            LOGGER.error("load defalut com.mockuai.marketingcenter.core.filter.filter config occur Exception", e);
        }
    }

    public static FilterManager getInstance() {
        return instance;
    }

    /**
     * 根据appCode和actionName来取得对应的action操作的filterChain，如果bizId没有对应的配置文件，取默认的配置文件中的配置信息.
     * 如果默认配置文件中有对应的action操作的filterChain则返回对应的FilterChain
     * 如没有则返回一个必须的filterChain。
     * 如果配置了对应的文件取相应的action配置信息
     */
    public FilterChain getFilterChain(String appCode, String actionName) throws SeckillException {
        //TODO 为了调试方便，先注释掉这里的代码，上线前需去掉注释. RdsException 是 啥?
//		if(StringUtils.isBlank(appCode)==true || StringUtils.isBlank(actionName)){
//			throw new RdsException("invalid param, appCode="+appCode+", actionName="+actionName);
//		}
        String filterChainCacheKey = "" + appCode + "_" + actionName;
//		if(filterChainCacheMap.get(filterChainCacheKey) != null){//如果命中缓存则直接return
//			return filterChainCacheMap.get(filterChainCacheKey);
//		}
        //根据appCode取得对应的APP配置的filterChain集合
        FilterChainHolder filterChainHolder = chains.get(appCode);
        FilterChain appActionChain = null;
        FilterChain appRequiredChain = null;
        //取得应用配置的action关联filterChain以及必选filterChain
        if (filterChainHolder != null) {
            appActionChain = filterChainHolder.getChain(actionName);
            appRequiredChain = filterChainHolder.getChain(FilterConfElementEnum.ELEMENT_REQUIRED_CHAIN.getValue());
        }

        //取得默认配置的filterChain集合
        FilterChainHolder defaultFilterChains = chains.get(DEFAULT_FILTER_CONF);
        //取得默认配置的action关联filterChain以及必选filterChain
        FilterChain defaultActionChain = null;
        FilterChain defaultRequiredChain = null;
        if (defaultFilterChains != null) {
            defaultActionChain = defaultFilterChains.getChain(actionName);
            defaultRequiredChain = defaultFilterChains.getChain(FilterConfElementEnum.ELEMENT_REQUIRED_CHAIN.getValue());
        }

        List<Filter> filters = new ArrayList<Filter>();
        try {
            //指定appCode没有配置filterChain，或者配置了filterChain的可继承属性，则取默认filter配置中的filterChain
            if (filterChainHolder == null || filterChainHolder.isExtendsChain() == true) {
                if (defaultActionChain != null && defaultActionChain.getFilters().isEmpty() == false) {
                    filters.addAll(defaultActionChain.getFilters());
                }

                if (defaultRequiredChain != null && defaultRequiredChain.getFilters().isEmpty() == false) {
                    filters.addAll(defaultRequiredChain.getFilters());
                }
            }

            //如果应用配置了filterChain，则添加之
            if (filterChainHolder != null) {
                if (appActionChain != null && appActionChain.getFilters().isEmpty() == false) {
                    filters.addAll(appActionChain.getFilters());
                } else if (filterChainHolder.isExtendsChain() == false) {//应用未配置必选filterChain，并且之前未继承过，则使用系统配置的必选filterChain
                    if (defaultActionChain != null && defaultActionChain.getFilters().isEmpty() == false) {
                        filters.addAll(defaultActionChain.getFilters());
                    }
                }

                if (appRequiredChain != null && appRequiredChain.getFilters().isEmpty() == false) {
                    filters.addAll(appRequiredChain.getFilters());
                } else if (filterChainHolder.isExtendsChain() == false) {//应用未配置必选filterChain，并且之前未继承过，则使用系统配置的必选filterChain
                    //默认配置的必选filterChain
                    if (defaultRequiredChain != null && defaultRequiredChain.getFilters().isEmpty() == false) {
                        filters.addAll(defaultRequiredChain.getFilters());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("error in getFilterChain, the appCode : {}, the actionName : {} ", appCode, actionName, e);
        }
        filterChainCacheMap.put(filterChainCacheKey, new FilterChainImpl(new ArrayList<Filter>(filters)));
        return filterChainCacheMap.get(filterChainCacheKey);
    }

    /**
     * 解析各app自己的filter配置文件
     *
     * @param appCode
     * @param confPath
     * @throws SeckillException
     */
    private void parseAppFilterConf(String appCode, String confPath) throws SeckillException {
        if (chains.get(appCode) != null) {//filter配置已完成
            return;
        }
        FilterConfBean filterConfBean = null;
        try {
            FilterConfParser confParser = new FilterConfParser();
            filterConfBean = confParser.parseConfig(confPath);
        } catch (Exception e) {
            throw new SeckillException(e);
        }
        //组装app配置的filterChain
        FilterChainHolder filterChainHolder = genFilterChainHolder(filterConfBean.getChainMap());
        filterChainHolder.setExtendsChain(filterConfBean.isExtension());
        chains.put(appCode, filterChainHolder);
    }

    /**
     * 解析默认的filter配置文件
     *
     * @throws SeckillException
     */
    private void parseDefaultFilterConf() throws SeckillException {
        try {
            FilterConfParser confParser = new FilterConfParser();
            FilterConfBean filterConfBean = confParser.parseConfig(DEFAULT_FILTER_CONF_PATH);
            if (filterConfBean != null) {
                FilterChainHolder filterChainHolder = genFilterChainHolder(filterConfBean.getChainMap());
                chains.put(DEFAULT_FILTER_CONF, filterChainHolder);
            } else {//正常情况下不会出现这种情况，所以这里需要进行错误处理
                LOGGER.error("cannot parse FilterConfBean : {}", DEFAULT_FILTER_CONF_PATH);
            }
        } catch (Exception e) {
            throw new SeckillException(e);
        }
    }

    /**
     * 组装FilterChainHolder
     *
     * @param FilterChainMap
     * @return
     * @throws SeckillException
     */
    private FilterChainHolder genFilterChainHolder(Map<String, List<FilterDescBean>> FilterChainMap) throws SeckillException {
        FilterChainHolder filterChainHolder = new FilterChainHolder();
        String requiredChainName = "required";
        //先将必选filterChain加入到filterChainHolder中
        FilterChain requiredfilterChain = buildChain(FilterChainMap.get(requiredChainName));
        filterChainHolder.putChain(requiredChainName, requiredfilterChain);

        //组装action filterChain
        for (String chainName : FilterChainMap.keySet()) {
            if (StringUtils.isBlank(chainName) || chainName.equals(requiredChainName)) {
                continue;
            }

            FilterChain filterChain = buildChain(FilterChainMap.get(chainName));
            filterChainHolder.putChain(chainName, filterChain);
        }
        return filterChainHolder;
    }

    /**
     * 创建filter的实例并组装成filterChain
     *
     * @param list
     * @return
     * @throws SeckillException
     */
    private FilterChain buildChain(List<FilterDescBean> list) throws SeckillException {
        List<Filter> filters = new ArrayList<Filter>();
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (FilterDescBean bean : list) {
            if (bean.getFactoryClass() == null) {
                filters.add(FilterFactory.createFilter(bean));
            }
        }
        return new FilterChainImpl(filters);
    }

    private Map<String, String> parseFilterMap(String filePath) {
        InputStream is = loadFile(filePath);
        if (is == null) {
            return Collections.emptyMap();
        }
        Map<String, String> filtersMap = new HashMap<String, String>();
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = fac.newDocumentBuilder();
            Document doc = builder.parse(is);
            NodeList l = doc.getElementsByTagName("mapping");
            for (int i = 0; i < l.getLength(); i++) {
                Element element = (Element) l.item(i);
                String bizCode = element.getAttribute("appCode");
                String xmlPath = element.getAttribute("filterConfPath");
                filtersMap.put(bizCode, xmlPath);
            }
        } catch (ParserConfigurationException e) {
            LOGGER.error("", e);
        } catch (SAXException e) {
            LOGGER.error("", e);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return filtersMap;
    }

    private InputStream loadFile(String filePath) {
        return this.getClass().getResourceAsStream(filePath);
    }
}