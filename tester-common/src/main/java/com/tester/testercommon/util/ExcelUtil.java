package com.tester.testercommon.util;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.constant.ExcelConstant;
import com.tester.base.dto.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @Author 温昌营
 * @Date 2021-12-9 16:26:42
 */
@Slf4j
public class ExcelUtil {


    public static SXSSFWorkbook createWorkbook() {
        return new SXSSFWorkbook(200);
    }

    public static SXSSFSheet createWorkSheet(SXSSFWorkbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    public static SXSSFRow createRow(SXSSFSheet worksheet, int rowIndex) {
        return worksheet.createRow(rowIndex);
    }


    /**
     * 输出excel文件。用于web应用下载excel
     * @Date 15:52 2021/12/17
     * @Author 温昌营
     **/
    public static void outExcelFile(Workbook workbook, String fileName, HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try (OutputStream os = response.getOutputStream()) {
            setResponseFileNameHeader(fileName, request, response);
            workbook.write(os);
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
    /**
     * 输出excel文件。用于web应用下载excel
     * @Date 15:52 2021/12/17
     * @Author 温昌营
     **/
    public static void outExcelFile(byte[] bytes, String fileName, HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        try (OutputStream os = response.getOutputStream()) {
            setResponseFileNameHeader(fileName, request, response);
            os.write(bytes);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String encodeFileName(String fileName, HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        try {
            if (userAgent != null && (userAgent.toLowerCase().contains("msie")
                    || userAgent.toLowerCase().contains("trident"))) {
                return new String(fileName.getBytes(ConstantList.UTF_8), "ISO-8859-1");
            } else {
                return URLEncoder.encode(fileName, ConstantList.UTF_8).replaceAll("\\+", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return fileName;
    }

    public static void setResponseFileNameHeader(String fileName, HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        if (fileName == null || "".equals(fileName.trim())) {
            fileName = UUID.randomUUID().toString() + "." + ExcelConstant.EXCEL_2007_SUFFIX;
        } else {
            fileName = fileName.trim();
            String fileSuffix = CommonUtil.getFileSuffix(fileName);
            if (fileSuffix == null || ExcelConstant.EXCEL_CONTENT_TYPE_MAP.get(fileSuffix.toLowerCase()) == null) {
                fileName = fileName + "." + ExcelConstant.EXCEL_2007_SUFFIX;
            }
        }
        String encodeFileName = encodeFileName(fileName, request);
        response.setHeader("Content-Disposition", "attachment; filename=" + encodeFileName);// 设置头信息
        response.setContentType(getContentType(CommonUtil.getFileSuffix(fileName)));
    }

    private static String getContentType(String fileSuffix) {
        String contentType = ExcelConstant.EXCEL_2007_CONTENT_TYPE;
        if (ExcelConstant.EXCEL_CONTENT_TYPE_MAP.get(fileSuffix) != null) {
            contentType = ExcelConstant.EXCEL_CONTENT_TYPE_MAP.get(fileSuffix);
        }
        return contentType;
    }

    public static void setColSelect(Sheet sheet, String[] enumRange, int titleRow, int colIndex, int lastRow) {
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();

        DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(enumRange);
        CellRangeAddressList addressList = new CellRangeAddressList(titleRow + 1, lastRow, colIndex, colIndex);
        DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
        validation.setShowErrorBox(true);
        validation.setShowPromptBox(true);
        //validation.createErrorBox("", languageUtil.getText(ExcelConstant.ENUM_CELL_ERROR_VALUE_BOX_MESSAG, ExcelConstant.ENUM_CELL_ERROR_VALUE_BOX_DEFAULT_MESSAG_TEXT));
        validation.createErrorBox("", null);
        validation.createPromptBox("", null);
        sheet.addValidationData(validation);
    }
}
