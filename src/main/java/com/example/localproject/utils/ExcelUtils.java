package com.example.localproject.utils;

import org.apache.poi.hssf.usermodel.*;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author xiazhengtao
 * @date 2020-02-14 15:27
 */
public class ExcelUtils {
    public static void createExcel(List<String> list,String exportPath) {

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultColumnWidth(20);// 默认列宽
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
//        HSSFRow rows = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFRow rows;
        // 添加excel title
        HSSFCell cells = null;
        for (int i = 0; i < list.size(); i++) {
            // 在这个sheet页里创建一行
            rows = sheet.createRow(i + 1);
            // 该行创建一个单元格,在该单元格里设置值
            cells = rows.createCell(0);
            cells.setCellValue(list.get(i));

        }
        // 第六步，将文件存到指定位置
        try {
            FileOutputStream fout = new FileOutputStream(exportPath);
            wb.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
