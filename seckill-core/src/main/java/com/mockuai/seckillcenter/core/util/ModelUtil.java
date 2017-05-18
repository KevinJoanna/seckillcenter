package com.mockuai.seckillcenter.core.util;

import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.dto.SeckillForMopDTO;
import com.mockuai.seckillcenter.common.domain.dto.SeckillHistoryDTO;
import com.mockuai.seckillcenter.core.domain.SeckillDO;
import com.mockuai.seckillcenter.core.domain.SeckillHistoryDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ModelUtil {

    public static SeckillDO genSeckillDO(SeckillDTO seckillDTO) {
        if (seckillDTO == null) return null;

        SeckillDO seckillDO = new SeckillDO();
        BeanUtils.copyProperties(seckillDTO, seckillDO);
        return seckillDO;
    }

    public static SeckillDTO genSeckillDTO(SeckillDO seckillDO) {
        if (seckillDO == null) return null;

        SeckillDTO seckillDTO = new SeckillDTO();
        BeanUtils.copyProperties(seckillDO, seckillDTO);
        return seckillDTO;
    }

    public static SeckillForMopDTO genSeckillForMopDTO(SeckillDTO seckillDTO) {
        if (seckillDTO == null) return null;

        SeckillForMopDTO seckillForMopDTO = new SeckillForMopDTO();
        seckillForMopDTO.setItemUid(seckillDTO.getSellerId() + "_" + seckillDTO.getSeckillItem().getItemId());
        seckillForMopDTO.setLifecycle(seckillDTO.getLifecycle());
        seckillForMopDTO.setStartTime(seckillDTO.getStartTime().getTime());
        seckillForMopDTO.setEndTime(seckillDTO.getEndTime().getTime());
        seckillForMopDTO.setSales(seckillDTO.getSales());
        seckillForMopDTO.setSeckillUid(seckillDTO.getSellerId() + "_" + seckillDTO.getId());
        seckillForMopDTO.setStockNum(seckillDTO.getSeckillItem().getStockNum());

        return seckillForMopDTO;
    }

    public static List<SeckillForMopDTO> genSeckillForMopDTOList(List<SeckillDTO> seckillDTOs) {
        List<SeckillForMopDTO> seckillForMopDTOs = new ArrayList<SeckillForMopDTO>();
        if (seckillDTOs == null || seckillDTOs.isEmpty()) return seckillForMopDTOs;

        for (SeckillDTO seckillDTO : seckillDTOs) {
            seckillForMopDTOs.add(ModelUtil.genSeckillForMopDTO(seckillDTO));
        }
        return seckillForMopDTOs;
    }

    public static List<SeckillDTO> genSeckillDTOList(List<SeckillDO> seckillDOs) {
        List<SeckillDTO> seckillDTOs = new ArrayList<SeckillDTO>();
        if (seckillDOs == null || seckillDOs.isEmpty()) return seckillDTOs;

        SeckillDTO seckillDTO;
        for (SeckillDO seckillDO : seckillDOs) {
            seckillDTOs.add(ModelUtil.genSeckillDTO(seckillDO));
        }
        return seckillDTOs;
    }

    public static List<SeckillHistoryDTO> genSeckillHistoryDTOList(List<SeckillHistoryDO> seckillHistoryDOs) {
        List<SeckillHistoryDTO> seckillHistoryDTOs = new ArrayList<SeckillHistoryDTO>();
        if (seckillHistoryDOs == null || seckillHistoryDOs.isEmpty()) return seckillHistoryDTOs;

        SeckillHistoryDTO seckillHistoryDTO;
        for (SeckillHistoryDO seckillHistoryDO : seckillHistoryDOs) {
            seckillHistoryDTO = new SeckillHistoryDTO();
            BeanUtils.copyProperties(seckillHistoryDO, seckillHistoryDTO);
            seckillHistoryDTOs.add(seckillHistoryDTO);
        }
        return seckillHistoryDTOs;
    }
}