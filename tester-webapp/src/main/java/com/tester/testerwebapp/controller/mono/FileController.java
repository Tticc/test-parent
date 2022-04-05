package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.model.request.IdAndNameRequest;
import com.tester.base.dto.model.request.TextRequest;
import com.tester.testercommon.model.response.QuotationResponse;
import com.tester.testercommon.util.ExcelUtil;
import com.tester.testerwebapp.config.MultiFileConfig;
import com.tester.testerwebapp.service.ExcelManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件服务接口<br/>
 * 查看config
 *
 * @see MultiFileConfig
 */
@Api(tags = "文件 测试模块")
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController extends BaseController {


    @Autowired
    private ExcelManager excelManager;

    @ApiOperation(value = "excel上传", notes = "", httpMethod = "POST")
    @PostMapping(value = "/uploadExcel")
    public RestResult<String> uploadExcel(@RequestPart("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        String filename = file.getOriginalFilename();
        try {
            excelManager.parseExcel(inputStream, filename);
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return success();
    }

    @ApiOperation(value = "uploadForDifSource", notes = "", httpMethod = "POST")
    @PostMapping(value = "/uploadForDifSource")
    public RestResult<Void> uploadForDifSource(@RequestPart("file") MultipartFile file, @ModelAttribute IdAndNameRequest model) throws UnsupportedEncodingException {
        String originalFileName = file.getOriginalFilename();
        System.out.println(originalFileName);
        originalFileName = URLDecoder.decode(originalFileName, "UTF-8");
        System.out.println(originalFileName);
        System.out.println(model);
        return success();
    }


    @ApiOperation(value = "导出Excel - GET", notes = "", httpMethod = "GET")
    @RequestMapping(value = "/exportExcelGet", method = RequestMethod.GET)
    public void exportExcelGet() throws Exception {
        TextRequest request = new TextRequest();
        request.setText("exportExcelGet");
        exportExcelPost(request);
    }


    @ApiOperation(value = "导出Excel - POST", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/exportExcelPost", method = RequestMethod.POST)
    public void exportExcelPost(@RequestBody @Validated TextRequest request) throws Exception {
        List<QuotationResponse> list = new ArrayList<>();
        list.add(newExcelRowData());
        list.add(newExcelRowData());
        list.add(newExcelRowData());
        list.add(newExcelRowData());
        list.add(newExcelRowData());
        list.add(newExcelRowData());
        SXSSFWorkbook workbook = excelManager.buildExcel(list);
        // 回写
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpRequest = attributes.getRequest();
        HttpServletResponse httpResponse = attributes.getResponse();
        ExcelUtil.outExcelFile(workbook, request.getText(), httpRequest, httpResponse);
    }

    private QuotationResponse newExcelRowData() {
        QuotationResponse res = new QuotationResponse();

        Field[] declaredFields = QuotationResponse.class.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                field.set(res, new BigDecimal("0.0"));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        res.setMiddleAppCost(new BigDecimal("1"));
        res.setMiddleBackCost(new BigDecimal("1.2"));
        res.setMiddleH5AppCost(new BigDecimal("1.2"));
        res.setMiddleH5WebCost(new BigDecimal("1.2"));
        res.setMiddleProductCost(new BigDecimal("1.2"));
        res.setMiddleTestCost(new BigDecimal("1.2"));
        res.setMiddleUiCost(new BigDecimal("1.2"));
        res.setSeniorAppCost(new BigDecimal("1.2"));
        res.setSeniorAppCost(new BigDecimal("1.2"));
        res.setSeniorBackCost(new BigDecimal("1.2"));
        res.setSeniorProductCost(new BigDecimal("1.2"));
        res.setSeniorProductCost(new BigDecimal("1.2"));

        res.setTotal(res.sum());
        return res;
    }

}
