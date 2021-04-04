package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testerwebapp.config.MultiFileConfig;
import com.tester.testercommon.model.request.IdAndNameRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;


/**
 * 文件服务接口<br/>
 * 查看config
 * @see MultiFileConfig
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController extends BaseController {
    /**
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/uploadFile")
    public RestResult<Void> uploadForDifSource(@RequestBody @Valid IdAndNameRequest model){
        System.out.println(model);
        return success();
    }
}
