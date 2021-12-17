package com.tester.testerwebapp.service;

import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.response.ExcelUserResponse;
import com.tester.testercommon.model.response.QuotationResponse;
import com.tester.testercommon.util.ExcelUtil;
import com.tester.testercommon.util.MyConsumer;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author 温昌营
 * @Date
 */
@Service
public class ExcelManager {
    @SneakyThrows
    public void parseExcel(InputStream inputStream,String fileName){
        if(fileName.endsWith(".xls")){
            parse2003(inputStream,fileName);
        }else if(fileName.endsWith(".xlsx")){
            parse2007(inputStream,fileName);
        }else {
            throw new BusinessException(5000L);
        }
    }


    @SneakyThrows
    private void parse2007(InputStream inputStream,String fileName){
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet firstSheet = getSheet(wb,0);
        XSSFRow titleRow = getRow(firstSheet,0);
        // 总行数
        int totalRow = firstSheet.getLastRowNum()+1;
        // 总列数
        int totalCol = titleRow.getPhysicalNumberOfCells();
        String[] titles = new String[totalCol];
        for(int i = 0; i < totalCol; i++){
            Cell cell = titleRow.getCell(i);
            if(null == cell){
                break;
            }
            titles[i] = cell.getStringCellValue();
        }
        List<ExcelUserResponse> list = new ArrayList<>();
        for(int i = 1; i < totalRow; i++){

        }
    }
    @SneakyThrows
    private void parse2003(InputStream inputStream,String fileName){
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        HSSFSheet firstSheet = getSheet(wb,0);
        HSSFRow titleRow = getRow(firstSheet,0);
        // 总列数
        int col = titleRow.getPhysicalNumberOfCells();
//        for(int i = 0; i < col; i++){
//            HSSFCell cell = titleRow.getCell(i);
//        }
        HSSFCell cell = titleRow.getCell(400);
        System.out.println(cell);
    }

    private <T extends Sheet> T getSheet(Workbook wb, int sheetNum){
        return (T)wb.getSheetAt(sheetNum);
    }
    private <T extends Row> T getRow(Sheet sheet,int rowNum){
        return (T)sheet.getRow(rowNum);
    }



    public SXSSFWorkbook buildExcel(List<QuotationResponse> list) throws Exception {
        LinkedHashMap<String, String> titleMap = listTitleMap();
        return createExcel(titleMap, (sheet) -> {
            int rowIndex = 1;
            int startColumn = 0;
            int endColumn = 0;
            for (QuotationResponse quotationResponse : list) {
                startColumn = 0;
                endColumn = 0;
                SXSSFRow row = ExcelUtil.createRow(sheet, rowIndex++);
                Set<Map.Entry<String, String>> entries = titleMap.entrySet();
                int cellIndex = 0;
                for (Map.Entry<String, String> entry : entries) {
                    String key = entry.getKey();
                    // 设置描述信息
                    Field field = null;
                    // 设置工时数字
                    try {
                        field = quotationResponse.getClass().getDeclaredField(key);
                    } catch (NoSuchFieldException nsfe) {
                    }
                    if(field != null) {
                        ++endColumn;
                        field.setAccessible(true);
                        try {
                            String value = String.valueOf(field.get(quotationResponse));
                            SXSSFCell cell = row.createCell(cellIndex++);
                            cell.setCellValue(Double.parseDouble(value));
                        }catch (IllegalAccessException ie){
                            throw new BusinessException(500L,ie.getMessage(),ie);
                        }
                    }
                }
            }
            SXSSFRow row = ExcelUtil.createRow(sheet, rowIndex);
            printSumRow(row,1,rowIndex-1, startColumn,endColumn);
        });

    }


    private static SXSSFWorkbook createExcel(LinkedHashMap<String, String> titleMap, MyConsumer<SXSSFSheet> consumer) throws BusinessException {
        SXSSFWorkbook workbook = ExcelUtil.createWorkbook();
        SXSSFSheet sheet = ExcelUtil.createWorkSheet(workbook, "需求清单");
        SXSSFRow row = ExcelUtil.createRow(sheet, 0);
        Set<Map.Entry<String, String>> entries = titleMap.entrySet();

        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            SXSSFCell cell = row.createCell(i++);
            cell.setCellValue(entry.getValue());
        }
        sheet.createFreezePane(0, 1, 0, 1);
        consumer.accept(sheet);
        return workbook;
    }

    private void printSumRow(SXSSFRow row, int startRow, int endRow, int startCol, int endCol){
        startRow = startRow + 1;
        endRow = endRow + 1;
        for (int i = startCol; i < endCol; i++) {
            SXSSFCell cell = row.createCell(i);
            String rowLetter = CellReference.convertNumToColString(i);
            cell.setCellFormula("=sum("+rowLetter+startRow+":"+rowLetter+endRow+")");
        }
    }


    private static LinkedHashMap<String, String> listTitleMap() {
        List<QuotationResponse> list;
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "编号");
        map.put("name", "名称");
        map.put("desc", "描述");
        map.put("seniorProductCost", "产品(高级)");
        map.put("middleProductCost", "产品(中级)");
        map.put("seniorBackCost", "后端(高级)");
        map.put("middleBackCost", "后端(中级)");
        map.put("seniorH5AppCost", "H5 App(高级)");
        map.put("middleH5AppCost", "H5 App(中级)");
        map.put("seniorH5WebCost", "H5 Web(高级)");
        map.put("middleH5WebCost", "H5 Web(中级)");
        map.put("seniorAppCost", "App(高级)");
        map.put("middleAppCost", "App(中级)");
        map.put("seniorUiCost", "UI设计(高级)");
        map.put("middleUiCost", "UI设计(中级)");
        map.put("seniorTestCost", "测试(高级)");
        map.put("middleTestCost", "测试(中级)");
        map.put("total", "总计");
        return map;
    }

    /**
     * for testing
     * @Date 17:49 2021/12/9
     * @Author 温昌营
     **/
    @Deprecated
    public static void exportToSomeway(SXSSFWorkbook workbook) throws IOException {
        //获取文件名
        String fileName = "C:\\Users\\Admin\\Desktop\\projects\\test.xlsx";
        File file = new File(fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
    }

}
