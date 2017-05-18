package com.mockuai.seckillcenter.mop.api.util;

import com.mockuai.mop.common.constant.MopRespCode;
import com.mockuai.mop.common.service.action.MopResponse;
import com.mockuai.seckillcenter.mop.api.domain.SeckillUidDTO;
import com.mockuai.seckillcenter.mop.api.domain.SkuUidDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MopApiUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MopApiUtil.class.getName());

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Long parseConsigneeId(String consigneeUid) {
        if (consigneeUid == null) {
            return null;
        }
        String[] strs = consigneeUid.split("_");
        if (strs.length != 2) {
            return null;
        }
        return Long.valueOf(strs[1]);
    }

    public static SkuUidDTO parseSkuUid(String skuUid) {
        if (skuUid == null) {
            return null;
        }
        String[] strs = skuUid.split("_");
        if (strs.length != 2) {
            return null;
        }
        SkuUidDTO skuUidDTO = new SkuUidDTO();
        Long sellerId = Long.valueOf(strs[0]);
        Long skuId = Long.valueOf(strs[1]);
        skuUidDTO.setSellerId(sellerId);
        skuUidDTO.setSkuId(skuId);
        return skuUidDTO;
    }

    public static SeckillUidDTO parseSeckillUid(String seckillUid) {
        if (seckillUid == null) {
            return null;
        }
        String[] strs = seckillUid.split("_");
        if (strs.length != 2) {
            return null;
        }
        SeckillUidDTO seckillUidDTO = new SeckillUidDTO();
        Long sellerId = Long.valueOf(strs[0]);
        Long seckillId = Long.valueOf(strs[1]);
        seckillUidDTO.setSellerId(sellerId);
        seckillUidDTO.setSeckillId(seckillId);
        return seckillUidDTO;
    }

//    public static VirtualWealthUidDTO parseVirtualWealthUid(String virtualWealthUid) {
//        if (virtualWealthUid == null) {
//            return null;
//        }
//
//        String[] strs = virtualWealthUid.split("_");
//        if (strs.length != 2) {
//            return null;
//        }
//
//        VirtualWealthUidDTO virtualWealthUidDTO = new VirtualWealthUidDTO();
//
//        long creatorId = Long.parseLong(strs[0]);
//        long virtualWealthId = Long.parseLong(strs[1]);
//        virtualWealthUidDTO.setCreatorId(creatorId);
//        virtualWealthUidDTO.setVirtualWealthId(virtualWealthId);
//        return virtualWealthUidDTO;
//    }


    public static String genItemSkuUid(Long sellerId, Long skuId) {
        return "" + sellerId + "_" + skuId;
    }


    public static MopResponse getResponse(MopRespCode mopRespCode) {
        return new MopResponse(mopRespCode);
    }
}