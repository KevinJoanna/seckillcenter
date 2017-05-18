package com.mockuai.seckillcenter.core.manager;

import com.mockuai.appcenter.common.domain.AppInfoDTO;
import com.mockuai.appcenter.common.domain.BizInfoDTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;

/**
 * Created by zengzhangqiang on 6/28/15.
 */
public interface AppManager {
    /**
     * 根据bizCode获取业务信息
     *
     * @param bizCode
     * @return
     * @throws SeckillException
     */
    public BizInfoDTO getBizInfo(String bizCode) throws SeckillException;

    /**
     * 根据appKey获取应用信息
     *
     * @param appKey
     * @return
     * @throws SeckillException
     */
    public AppInfoDTO getAppInfo(String appKey) throws SeckillException;

    /**

     * 通过bizCode获得AppKey
     * @param bizCode
     * @throws SeckillException
     * */
    String getAppKeyByBizCode(String bizCode) throws SeckillException;
}