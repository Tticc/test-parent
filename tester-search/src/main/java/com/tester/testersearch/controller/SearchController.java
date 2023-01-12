package com.tester.testersearch.controller;


import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.model.request.PagerInfo;
import com.tester.testercommon.controller.BaseController;
import com.tester.testersearch.model.KnowledgePageRequest;
import com.tester.testersearch.model.KnowledgeRequest;
import com.tester.testersearch.model.KnowledgeResponse;
import com.tester.testersearch.service.SearchManager;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索
 *
 * @author 温昌营
 * @date 2022-10-10 14:58:21
 */
@Api(tags = "搜索")
@Slf4j
@RequestMapping("/api/search")
@RestController
public class SearchController extends BaseController {

    @Autowired
    private SearchManager searchManager;

    /**
     * @Date 14:57 2022/10/10
     * @Author 温昌营
     **/
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public RestResult<PagerInfo<KnowledgeResponse>> search(@Validated @RequestBody KnowledgePageRequest request) throws BusinessException {
        return success(searchManager.search(request));
    }

    /**
     * @Date 17:25 2022/10/25
     * @Author 温昌营
     **/
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RestResult<String> add(@Validated @RequestBody KnowledgeRequest model) throws BusinessException {
        return success(searchManager.add(model));
    }

    /**
     * @Date 2023-1-12 15:32:08
     * @Author 温昌营
     **/
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResult<String> update(@Validated @RequestBody KnowledgeRequest model) throws BusinessException {
        return success(searchManager.update(model));
    }


}
