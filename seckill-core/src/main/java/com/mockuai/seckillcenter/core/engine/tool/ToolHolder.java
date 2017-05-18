//package com.mockuai.groupbuy.core.engine.tool;
//
//import com.mockuai.groupbuy.core.manager.MarketToolManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ToolHolder implements ApplicationContextAware {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ToolHolder.class);
//    private ApplicationContext applicationContext;
//    private Map<String, Tool> toolMap;
//
//    @Resource
//    private MarketToolManager marketToolManager;
//
//    public ToolHolder() {
//        this.toolMap = new HashMap();
//    }
//
//    @PostConstruct
//    public void loadTool() {
//        try {
//            //TODO 当工具量超过100时，这里需要分页查找，加载所有工具, 目前的方案还不会达到这个极限
//            MarketToolQTO marketToolQTO = new MarketToolQTO();
//            marketToolQTO.setOffset(Integer.valueOf(0));
//            marketToolQTO.setCount(Integer.valueOf(100));
//            List<MarketToolDO> marketToolDOs = this.marketToolManager.queryTool(marketToolQTO);
//
//            // 工具类没有层级关系
//            for (MarketToolDO marketToolDO : marketToolDOs) {
//
//                //如果该工具已加载，则直接跳过
//                if (toolMap.containsKey(marketToolDO.getToolCode())) {
//                    break;
//                }
//
//                String beanName = marketToolDO.getImplContent();
//                Tool tool = (Tool) this.applicationContext.getBean(beanName);
//                toolMap.put(marketToolDO.getToolCode(), tool);
//            }
//
//        } catch (Exception e) {
//            LOGGER.error("", e);
//        }
//    }
//
//    public Tool getTool(String toolCode) {
//        return this.toolMap.get(toolCode);
//    }
//
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//}