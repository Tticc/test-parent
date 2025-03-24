package com.tester.testersearch.controller.trade;


import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.controller.BaseController;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.model.TradeDataRequest;
import com.tester.testersearch.service.okx.OkxHelper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private OkxHelper okxHelper;

    /**
     * @Date 14:57 2022/10/10
     * @Author 温昌营
     **/
    @RequestMapping(value = "/trade", method = RequestMethod.POST)
    public RestResult<List<TradeDataBaseDomain>> trace(@Validated @RequestBody TradeDataRequest request) throws BusinessException {
        return success(OkxHelper.getOKXKlineData(request.getLimit() + "", "15m"));
    }


}
