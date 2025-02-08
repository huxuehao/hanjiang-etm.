package com.hanjiang.etm.question.excelImport.parser;

import com.hanjiang.etm.common.dto.ImportQuestion;
import com.hanjiang.etm.common.util.Func;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 描述：试题Excel解析器
 *
 * @author huxuehao
 **/
public abstract class QuestionParser {
    final Map<Integer, String> china = new HashMap<Integer, String>() {{
        put(1, "一");
        put(2, "二");
        put(3, "三");
        put(4, "四");
        put(5, "五");
        put(6, "六");
        put(7, "七");
    }};
    public abstract Optional<ImportQuestion> doParser(MultipartFile file) throws IOException;

    /**
     * 获取目标sheet
     * @param workbook      文件
     * @param sheetName sheet名称
     */
    protected Sheet getTargetSheet(Workbook workbook, String sheetName) {
        if (sheetName == null) {
            throw new RuntimeException("sheetName不为为空");
        }
        Sheet sheet = null;
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            if (sheetName.equals(workbook.getSheetName(i))) {
                sheet = workbook.getSheetAt(i);
                break;
            }
        }
        return sheet;
    }

    /**
     * 获取Workbook
     */
    protected Workbook getWorkbook(MultipartFile file) {
        String filename = file.getOriginalFilename();
        assert filename != null;
        boolean isXls = filename.endsWith(".xls");
        boolean isXlsx = filename.endsWith(".xlsx");
        // 判断文件后缀的合法性
        if (!(isXls || isXlsx)) {
            throw new RuntimeException("文件格式不正确");
        }
        Workbook workbook;
        try {
            if (isXls) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败", e);
        }
        return workbook;
    }

    /**
     * 获取Row中的指定单元格的字符串值
     * @param row      Row对象
     * @param index    单元格索引
     * @param notEmpty 是否可为空
     */
    protected String getStringCellValue(Row row, int index, boolean notEmpty) {
        Cell cell = row.getCell(index);
        // 获取非空单元格
        if (cell != null && !Func.isEmpty(cell.getStringCellValue())) {
            String stringCellValue = cell.getStringCellValue();
            if (!Func.isEmpty(stringCellValue.trim())) {
                return stringCellValue;
            } else if (notEmpty) {
                throw new RuntimeException("单元格不可为空，第" + (index + 1) + "列存在空白单元格");
            } else {
                return null;
            }
        } else if (notEmpty) {
            throw new RuntimeException("单元格不可为空，第"+(index + 1)+"列存在空白单元格");
        } else {
            return null;
        }
    }

    /**
     * 获取Row中的指定单元格的数值值
     * @param row      Row对象
     * @param index    单元格索引
     * @param notEmpty 是否可为空
     */
    protected Integer getIntCellValue(Row row, int index, boolean notEmpty) {
        Cell cell = row.getCell(index);
        // 获取非空单元格
        if (cell != null) {
            double numericCellValue = cell.getNumericCellValue();
            if (!Func.isEmpty(numericCellValue)) {
                return (int) numericCellValue;
            } else if (notEmpty) {
                throw new RuntimeException("单元格不可为空，第" + (index + 1) + "列存在空白单元格");
            } else {
                return null;
            }
        } else if (notEmpty) {
            throw new RuntimeException("单元格不可为空，第"+(index + 1)+"列存在空白单元格");
        } else {
            return null;
        }
    }

    /**
     * 数值转ABC..。如 1 -> A, 2 -> B
     */
    protected String numberToLetter(int number) {
        if (number < 1 || number > 26) {
            throw new IllegalArgumentException("Number must be between 1 and 26");
        }
        // 'A' 是 65，减去 1 使得 1 对应 A
        return String.valueOf((char) ('A' + number - 1));
    }

    /**
     * 数值转中文。如 1 -> 一, 2 -> 二
     */
    protected String numberToChina(int number) {
        String num = china.get(number);
        if (num == null) {
            throw new RuntimeException("填空位匹配失败");
        }
        return "第" +num+ "个填空";
    }
}
