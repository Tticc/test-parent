package com.tester.testersearch.service.binc.binance;

import com.github.pagehelper.PageInfo;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import com.tester.testersearch.service.binc.strategy.MACrossWithTPSLStrategy;
import com.tester.testersearch.service.binc.strategy.MACrossWithTPSLStrategy_reverse;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binc.binance.CombineCandle;
import com.tester.testersearch.util.binc.tradeSign.MAUtil;
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

    public static Map<Long, TradeSignDTO> getByBarEnumCode(String code) {
        return hisDataMap.get(code);
    }

    @Autowired
    private TradeDataBaseService tradeDataBaseService;


    public List<TradeSignDTO> traceLocal(String startAt, Integer limit, MyConsumer<Boolean> myConsumer, boolean tpsl, TradeParam tradeParam) throws BusinessException {
        Map<Long, TradeSignDTO> hisData = hisDataMap.get(tradeParam.getBarEnum().getCode());
        if (null == hisData) {
            hisData = new LinkedHashMap<>();
            hisDataMap.put(tradeParam.getBarEnum().getCode(), hisData);
        }
        boolean first = false;
        if (null == limit) {
            limit = 2;
        }
        int tempNum = 240;
        if (limit < 10) {
            if (CollectionUtils.isEmpty(hisData)) {
                log.error("异常，数据未初始化完成");
                throw new BusinessException(5000L);
            }
            Collection<TradeSignDTO> values = hisData.values();
            if(values.size() > 1000){
                hisData.remove(values.stream().findFirst().get().getId());
            }
            List<TradeSignDTO> collect = values.stream().skip(Math.max(0, values.size() - tempNum)).collect(Collectors.toList());
            // 获取最新蜡烛数据
            TradeSignDTO lastTradeSignDTO = collect.get(collect.size()-1);
            if (null == lastTradeSignDTO) {
                log.error("异常，未找到数据");
                throw new BusinessException(5000L);
            }
            Long lastUpdateTimestamp = lastTradeSignDTO.getLastUpdateTimestamp();
            List<TradeSignDTO> tradeSignDTOS = this.fetchData(null, lastTradeSignDTO, tradeParam.getStep(), tradeParam.getBarEnum(),tradeParam.getBKey());
            for (TradeSignDTO signDTO : tradeSignDTOS) {
                hisData.put(signDTO.getId(), signDTO);
            }
            // 是否已经取完所有数据
            myConsumer.accept(CollectionUtils.isEmpty(tradeSignDTOS) ||
                    Objects.equals(lastUpdateTimestamp, tradeSignDTOS.get(tradeSignDTOS.size() - 1).getLastUpdateTimestamp()));
        } else {
            if (CollectionUtils.isEmpty(hisData)) {
                first = true;
                // 初始化，获取数据
                List<TradeSignDTO> res = this.fetchData(startAt, null, limit * tradeParam.getBarEnum().getInterval(), tradeParam.getBarEnum(),tradeParam.getBKey());
                for (TradeSignDTO signDTO : res) {
                    hisData.put(signDTO.getId(), signDTO);
                }
                if(CollectionUtils.isEmpty(res)){
                    return Collections.emptyList();
                }
            }
        }
        List<TradeSignDTO> allTradeDatas = hisData.values().stream().skip(Math.max(0, hisData.size() - tempNum)).collect(Collectors.toList());
        this.calculateTradeData(allTradeDatas, first, true, tpsl, tradeParam);
        return first ? allTradeDatas : allTradeDatas.stream().skip(Math.max(0, allTradeDatas.size() - 2)).collect(Collectors.toList());
    }

    /**
     * 获取交易数据
     *
     * @param startAt
     * @param last
     * @param size
     * @param barEnum
     * @return
     */
    public List<TradeSignDTO> fetchData(String startAt, TradeSignDTO last, int size, BarEnum barEnum, String bKey) {
        Long minId;
        if (null != last) {
            minId = last.getLastUpdateTimestamp()+1000;
        } else if (!StringUtils.isEmpty(startAt)) {
            LocalDateTime localDateTime = DateUtil.getLocalDateTime(startAt);
            Date startDate = DateUtil.getDateFromLocalDateTime(localDateTime);
            minId = startDate.getTime();
        } else {
            LocalDateTime localDateTime = DateUtil.getLocalDateTime("20250401000000");
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
            req.setBKey(bKey);
            req.setPageNum(1);
            req.setPageSize(fetchSize);

            PageInfo<TradeSignDTO> pageInfo = tradeDataBaseService.listPageWithCache(req);
//            PageInfo<TradeSignDTO> pageInfo = tradeDataBaseService.listPage(req);

            if (pageInfo == null || CollectionUtils.isEmpty(pageInfo.getList())) {
                break;
            }
            List<TradeSignDTO> combine = CombineCandle.combine(last, barEnum, pageInfo.getList());
            for (TradeSignDTO tradeSignDTO : combine) {
                last = tradeSignDTO;
                combineMap.put(tradeSignDTO.getId(), tradeSignDTO);
            }
            size -= fetchSize;
            currentId = pageInfo.getList().get(pageInfo.getList().size() - 1).getId()+1000; // 更新当前最大ID+1000
        }
        return combineMap.values().stream().sorted(Comparator.comparing(TradeSignDTO::getId)).collect(Collectors.toList());
    }


    /**
     * 计算交易数据
     * @param allTradeDatas 所有数据
     * @param first 是否时第一次。非第一次取后半部分计算
     * @param excludeLast 是否排除最后一个蜡烛，确保计算的蜡烛都是已完成的
     * @throws BusinessException
     */
    private void calculateTradeData(List<TradeSignDTO> allTradeDatas, boolean first, boolean excludeLast, boolean tpsl, TradeParam tradeParam) throws BusinessException {
        int branderPeriod = 20;
        int adxPeriod = 14;
        int dataSize = Math.max(branderPeriod, adxPeriod * 2 + 2);
        List<TradeSignDTO> data = allTradeDatas;
        if (!first) {
            // 如果不是第一次初始化数据，取最后 dataSize 条数据处理
            data = allTradeDatas.stream().skip(Math.max(0, allTradeDatas.size() - dataSize)).collect(Collectors.toList());
        }
        // 计算MA
        MAUtil.calculateAndSetMA(data, 5, 10, 20);
        if(tpsl) {
            MACrossWithTPSLStrategy.calculateTradeSign_excludeLast(allTradeDatas, tradeParam, first);
//            MACrossWithTPSLStrategy_reverse.calculateTradeSign_excludeLast_reverse(allTradeDatas, tradeParam, first);
        }
        // 计算Brander
//        BollingerBandsUtil.calculateBollingerBands(data, branderPeriod, 2);
        // 计算ADX
//        ADXUtil.calculateADX(data, adxPeriod);
    }
}
