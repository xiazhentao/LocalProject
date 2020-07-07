package com.example.localproject.test;

import com.example.localproject.utils.ExcelUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiazhengtao
 * @date 2020-02-14 15:11
 */
public class ReadTXTExportExcel {
    public static void main(String[] args) {
        String path = "/Users/xiazhengtao/文件/微信昵称.txt";
        String exportPath = "/Users/xiazhengtao/文件/昵称表格xls";
        List<String> list = readFile(path);
        ExcelUtils.createExcel(list,exportPath);
    }

    private static List<String> readFile(String path) {
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            File file = new File(path);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                arrayList.add(str.split(",")[0]);
            }
            bf.close();
            inputReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
