package com.tester.testersearch.controller.binc;


import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.controller.BaseController;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.model.TradeDataRequest;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.service.binc.okx.OkxHelper;
import com.tester.testersearch.util.BarEnum;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


}
