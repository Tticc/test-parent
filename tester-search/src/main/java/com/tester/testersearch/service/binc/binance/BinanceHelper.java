package com.tester.testersearch.service.binc.binance;

import com.github.pagehelper.PageInfo;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import com.tester.testersearch.service.binc.strategy.MACrossStrategy;
import com.tester.testersearch.service.binc.strategy.MACrossWithTPSLStrategy;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binc.binance.CombineCandle;
import com.tester.testersearch.util.binc.tradeSign.ADXUtil;
import com.tester.testersearch.util.binc.tradeSign.BollingerBandsUtil;
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


    public List<TradeSignDTO> traceLocal(String startAt, Integer limit, Integer step, BarEnum barEnum, MyConsumer<Boolean> myConsumer, boolean tpsl) throws BusinessException {
        Map<Long, TradeSignDTO> hisData = hisDataMap.get(barEnum.getCode());
        if (null == hisData) {
            hisData = new HashMap<>();
            hisDataMap.put(barEnum.getCode(), hisData);
        }
        boolean first = false;
        if (null == limit) {
            limit = 2;
        }
        if (limit < 10) {
            if (CollectionUtils.isEmpty(hisData)) {
                log.error("异常，数据未初始化完成");
                throw new BusinessException(5000L);
            }
            // 获取最新1s数据
            Long aLong = hisData.keySet().stream().max(Comparator.naturalOrder()).orElse(0L);
            TradeSignDTO lastTradeSignDTO = hisData.get(aLong);
            if (null == lastTradeSignDTO) {
                log.error("异常，未找到数据");
                throw new BusinessException(5000L);
            }
            Long lastUpdateTimestamp = lastTradeSignDTO.getLastUpdateTimestamp();
            List<TradeSignDTO> tradeSignDTOS = this.fetchData(null, lastTradeSignDTO, step, barEnum);
            for (TradeSignDTO signDTO : tradeSignDTOS) {
                hisData.put(signDTO.getId(), signDTO);
            }
            TradeSignDTO newLastTradeSignDTO = tradeSignDTOS.get(tradeSignDTOS.size() - 1);
            // 是否已经取完所有数据
            myConsumer.accept(Objects.equals(lastUpdateTimestamp, newLastTradeSignDTO.getLastUpdateTimestamp()));
        } else {
            first = true;
            if (CollectionUtils.isEmpty(hisData)) {
                // 初始化，获取数据
                List<TradeSignDTO> res = this.fetchData(startAt, null, limit * barEnum.getInterval(), barEnum);
                for (TradeSignDTO signDTO : res) {
                    hisData.put(signDTO.getId(), signDTO);
                }
            }
        }
        List<TradeSignDTO> allTradeDatas = hisData.values().stream()
                .sorted(Comparator.comparing(TradeSignDTO::getId))
                .collect(Collectors.toList());
//        this.calculateTradeData(allTradeDatas, first, false);
        this.calculateTradeData(allTradeDatas, first, true, tpsl);
        return first ? allTradeDatas : allTradeDatas.stream().skip(Math.max(0, allTradeDatas.size() - limit)).collect(Collectors.toList());
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
    private List<TradeSignDTO> fetchData(String startAt, TradeSignDTO last, int size, BarEnum barEnum) {
        Long minId;
        if (null != last) {
            minId = last.getLastUpdateTimestamp();
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


    /**
     * 计算交易数据
     * @param allTradeDatas 所有数据
     * @param first 是否时第一次。非第一次取后半部分计算
     * @param excludeLast 是否排除最后一个蜡烛，确保计算的蜡烛都是已完成的
     * @throws BusinessException
     */
    private void calculateTradeData(List<TradeSignDTO> allTradeDatas, boolean first, boolean excludeLast, boolean tpsl) throws BusinessException {
        int branderPeriod = 20;
        int adxPeriod = 14;
        int dataSize = Math.max(branderPeriod, adxPeriod * 2 + 2);
        List<TradeSignDTO> data = allTradeDatas;
        if (!first) {
            // 如果不是第一次初始化数据，取最后 dataSize 条数据处理
            data = allTradeDatas.stream().skip(Math.max(0, allTradeDatas.size() - dataSize)).collect(Collectors.toList());
        }
//        log.info("size:{}",data.size());
        // 计算MA
        MAUtil.calculateAndSetMA(data, 5, 10, 20);
        if(tpsl) {
            MACrossWithTPSLStrategy.calculateTradeSign_excludeLast(allTradeDatas);
        }else {
            MACrossStrategy.calculateTradeSign(allTradeDatas, excludeLast);
        }
        // 计算Brander
//        BollingerBandsUtil.calculateBollingerBands(data, branderPeriod, 2);
        // 计算ADX
//        ADXUtil.calculateADX(data, adxPeriod);
    }
}
