package com.tester.testersearch.controller.binc;


import com.github.pagehelper.PageInfo;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.dao.domain.CandleTradeSignalDomain;
import com.tester.testersearch.dao.domain.TradeCandleDataDomain;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.model.CandleTradeSignalPageRequest;
import com.tester.testersearch.dao.model.TradeCandleDataPageRequest;
import com.tester.testersearch.dao.service.CandleTradeSignalService;
import com.tester.testersearch.dao.service.TradeCandleDataService;
import com.tester.testersearch.model.TradeDataRequest;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.service.binc.okx.OkxHelper;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import com.tester.testersearch.util.BKeyEnum;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.StrategyEnum;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 搜索
 *
 * @author 温昌营
 * @date 2022-10-10 14:58:21
 */
@Api(tags = "搜索")
@Slf4j
@RequestMapping("/api/trade")
@RestController
public class TradeController extends BaseController {

    @Autowired
    private BinanceHelper binanceHelper;
    @Autowired
    private CandleTradeSignalService candleTradeSignalService;

    @Autowired
    private TradeCandleDataService tradeCandleDataService;

    /**
     * @Date 14:57 2022/10/10
     * @Author 温昌营
     **/
    @RequestMapping(value = "/tradeOkx", method = RequestMethod.POST)
    public RestResult<List<TradeDataBaseDomain>> traceOkx(@Validated @RequestBody TradeDataRequest request) throws BusinessException {
        return success(OkxHelper.getOKXKlineData(request.getLimit() + "", "15m"));
    }

    /**
     * @Date 14:57 2022/10/10
     * @Author 温昌营
     **/
    @RequestMapping(value = "/tradeLocal", method = RequestMethod.POST)
    public RestResult<List<TradeSignDTO>> traceLocal(@Validated @RequestBody TradeDataRequest request) throws BusinessException {
        // 步长。默认1s
        int step = 20;
        BarEnum barEnum = BarEnum._30m;
//        return success(binanceHelper.traceLocal("20250201000000",request.getLimit(), step, barEnum, (ifLast) -> {}, true));
        Map<Long, TradeSignDTO> hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
        List<TradeSignDTO> allTradeDatas = hisDataMap.values().stream().collect(Collectors.toList());
        return success(allTradeDatas);
    }

    /**
     * @Date 14:57 2022/10/10
     * @Author 温昌营
     **/
    @RequestMapping(value = "/tradeLocal5", method = RequestMethod.POST)
    public RestResult<List<TradeSignDTO>> tradeLocal5(@Validated @RequestBody TradeDataRequest request1) throws BusinessException {
        TradeParam tradeParam = StrategyEnum._1000150.getParam();
        String bKey = BKeyEnum.BTCUSDT.getCode();
        int pageNum = 1;
        int pageSize = 300;

        TradeCandleDataPageRequest candleReq = new TradeCandleDataPageRequest();
        candleReq.setBKey(bKey)
                .setBar(tradeParam.getBarEnum().getCode());
        candleReq.setDeleted(0);
        candleReq.setPageNum(pageNum);
        candleReq.setPageSize(pageSize);
        PageInfo<TradeCandleDataDomain> tradeCandleDataDomainPageInfo = tradeCandleDataService.listPage(candleReq);
        List<TradeCandleDataDomain> candleList = tradeCandleDataDomainPageInfo.getList();


        CandleTradeSignalPageRequest request = new CandleTradeSignalPageRequest();
        request.setBKey(bKey)
                .setStrategyCode(tradeParam.getStrategyCode())
                .setOpenTimestampList(candleList.stream().map(e -> e.getOpenTimestamp()).collect(Collectors.toList()));
        request.setPageNum(1);
        request.setPageSize(pageSize);
        PageInfo<CandleTradeSignalDomain> candleTradeSignalDomainPageInfo = candleTradeSignalService.listPage(request);
        List<CandleTradeSignalDomain> tradeSignalList = candleTradeSignalDomainPageInfo.getList();
        Map<Long, CandleTradeSignalDomain> collect = tradeSignalList.stream().collect(Collectors.toMap(e -> e.getOpenTimestamp(), e -> e, (a, b) -> a));

        List<TradeSignDTO> collect1 = candleList.stream().map(e -> {
            TradeSignDTO tradeSignDTO = new TradeSignDTO();
            BeanUtils.copyProperties(e, tradeSignDTO);
            tradeSignDTO.setId(e.getOpenTimestamp());
            tradeSignDTO.setTimestamp(e.getOpenTimestamp());
            CandleTradeSignalDomain candleTradeSignalDomain = collect.get(e.getOpenTimestamp());
            if (null != candleTradeSignalDomain) {
                TradeSignDTO.TradeInfo tradeInfo = new TradeSignDTO.TradeInfo();
                BeanUtils.copyProperties(candleTradeSignalDomain, tradeInfo);
                tradeSignDTO.setTradeInfo(tradeInfo);
            }
            return tradeSignDTO;
        }).collect(Collectors.toList());
        return success(collect1);
    }
}
