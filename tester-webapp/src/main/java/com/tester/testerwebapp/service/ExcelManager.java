package com.tester.testerwebapp.service;

import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.response.ExcelUserVO;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        List<ExcelUserVO> list = new ArrayList<>();
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

    private void cellType(Cell cell){
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_STRING:
            case Cell.CELL_TYPE_FORMULA:
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_BOOLEAN:
            case Cell.CELL_TYPE_ERROR:
            default:
                break;
        }
    }



}
