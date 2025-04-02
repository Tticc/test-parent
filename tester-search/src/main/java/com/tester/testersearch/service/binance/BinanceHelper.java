package com.tester.testersearch.service.binance;

import com.github.pagehelper.PageInfo;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binance.CombineCandle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service
public class BinanceHelper {

    public static Map<String, Map<Long, TradeSignDTO>> hisDataMap = new HashMap();

    public static Map<Long, TradeSignDTO> getByBarEnumCode(String code){
        return hisDataMap.get(code);
    }

    @Autowired
    private TradeDataBaseService tradeDataBaseService;


    public List<TradeSignDTO> traceLocal(String startAt, Integer limit, Integer step, BarEnum barEnum) throws BusinessException {
        Map<Long, TradeSignDTO> hisData = hisDataMap.get(barEnum.getCode());
        if (null == hisData) {
            hisData = new LinkedHashMap<>();
            hisDataMap.put(barEnum.getCode(), hisData);
        }
        List<TradeSignDTO> res = new ArrayList<>();
        if (limit < 10) {
            // 获取最新1s数据
            Long aLong = hisData.keySet().stream().max(Comparator.naturalOrder()).orElse(0L);
            TradeSignDTO tradeSignDTOBefore = hisData.get(aLong - barEnum.getInterval() * 1000);
            TradeSignDTO tradeSignDTO = hisData.get(aLong);
            if (null == tradeSignDTOBefore || null == tradeSignDTO) {
                log.error("异常，未找到数据");
                throw new BusinessException(5000L);
            }
            res.add(tradeSignDTOBefore);
            List<TradeSignDTO> tradeSignDTOS = this.fetchData(null, tradeSignDTO, 1 + step, barEnum);
            for (TradeSignDTO signDTO : tradeSignDTOS) {
                hisData.put(signDTO.getId(), signDTO);
            }
            res.addAll(tradeSignDTOS);
        } else {
            if (!CollectionUtils.isEmpty(hisData)) {
                res = hisData.values().stream().sorted(Comparator.comparing(TradeSignDTO::getId))
                        .collect(Collectors.toList());
            } else {
                // 初始化，获取数据
                res = this.fetchData(startAt, null, limit * barEnum.getInterval(), barEnum);
                for (TradeSignDTO signDTO : res) {
                    hisData.put(signDTO.getId(), signDTO);
                }
            }
        }
        return res;
    }

    public List<TradeSignDTO> fetchData(String startAt, TradeSignDTO last, int size, BarEnum barEnum) {
        Long minId;
        if (null != last) {
            minId = last.getLastUpdateTimestamp();
        } else if (!StringUtils.isEmpty(startAt)) {
            LocalDateTime localDateTime = DateUtil.getLocalDateTime(startAt);
            Date startDate = DateUtil.getDateFromLocalDateTime(localDateTime);
            minId = startDate.getTime();
        } else {
            LocalDateTime localDateTime = DateUtil.getLocalDateTime("20250301000000");
            Date startDate = DateUtil.getDateFromLocalDateTime(localDateTime);
            minId = startDate.getTime();
        }

        Map<Long, TradeSignDTO> combineMap = new LinkedHashMap<>();
        int batchSize = 1000;
        long currentId = minId;

        while (size > 0) {
            int fetchSize = Math.min(batchSize, size);
            TradeDataBasePageRequest req = new TradeDataBasePageRequest();
            req.setId(currentId);
            req.setPageNum(1);
            req.setPageSize(fetchSize);

            PageInfo<TradeSignDTO> pageInfo = tradeDataBaseService.listPage(req);

            if (pageInfo == null || CollectionUtils.isEmpty(pageInfo.getList())) {
                break;
            }
            List<TradeSignDTO> combine = CombineCandle.combine(last, barEnum, pageInfo.getList());
            for (TradeSignDTO tradeSignDTO : combine) {
                last = tradeSignDTO;
                combineMap.put(tradeSignDTO.getId(), tradeSignDTO);
            }
            size -= fetchSize;
            currentId = pageInfo.getList().get(pageInfo.getList().size() - 1).getId(); // 更新当前ID
        }
        return combineMap.values().stream().sorted(Comparator.comparing(TradeSignDTO::getId)).collect(Collectors.toList());
    }
}
