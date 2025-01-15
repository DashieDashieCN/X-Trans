package org.dashie.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.AbstractCell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dashie.entity.TemplateObject;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.dashie.common.FileCommon.OUTPUT_PATH;
import static org.dashie.utils.print.ScreenPrintUtil.*;
import static org.dashie.utils.text.TextUtil.getWriter;

/**
 * @author DashieDasie
 * @since 2025/1/14 9:37
 */
public class ExcelUtil {
    public static void readExcel(String path, TemplateObject templateObject) throws IOException {
        try (FileInputStream file = new FileInputStream(path)) {
            boolean is2003Excel= path.toLowerCase().endsWith("xls");
            Workbook workbook = is2003Excel ? new HSSFWorkbook(file) : new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getLastRowNum();
            info("共" + totalRows + 1 + "行");
            BufferedWriter bufferedWriter = getWriter(templateObject);
            info("正在写入上文");
            if (!templateObject.getPreText().isEmpty()) {
                bufferedWriter.write(templateObject.getPreText());
            }
            success("上文写入成功");
            info("正在写入循环模板");
            SheetListener sheetListener = new SheetListener(
                    templateObject,
                    bufferedWriter,
                    (totalRows - templateObject.getStartRowIndex() + 1) / templateObject.getRowStep()
            );
            EasyExcel.read(path, sheetListener).sheet().doRead();
            info("正在写入下文");
            if (!templateObject.getSufText().isEmpty()) {
                if (!templateObject.getPreText().isEmpty() || !templateObject.getLoopTemplate().isEmpty()) {
                    bufferedWriter.newLine();
                }
                bufferedWriter.write(templateObject.getSufText());
            }
            success("下文写入成功");
            bufferedWriter.close();
        }
    }

    /**
     * 监听器
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SheetListener extends AnalysisEventListener<Map<Integer, String>> {
        public static Long time = null;

        TemplateObject templateObject;
        BufferedWriter bufferedWriter;
        int steps;

        @Override
        public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
            if (templateObject.getStartRowIndex() == 0) {
                Map<Integer, String> data = headMap.values().stream().filter(v -> !v.getType().equals(CellDataTypeEnum.EMPTY)).collect(Collectors.toMap(AbstractCell::getColumnIndex, v -> {
                    try {
                        if (v.getStringValue() == null) {
                            return "";
                        }
                        return v.getStringValue();
                    } catch (Exception e) {
                        return "";
                    }
                }));
                saveData(data, 0);
            }
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            ReadRowHolder readRowHolder = context.readRowHolder();
            int rowIndex = readRowHolder.getRowIndex();
            if (rowIndex < templateObject.getStartRowIndex() || (rowIndex - templateObject.getStartRowIndex()) % templateObject.getRowStep() != 0) {
                return;
            }
            saveData(data, readRowHolder.getRowIndex());
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        /**
         * 加上存储数据库
         */
        private void saveData(Map<Integer, String> data, int rowIndex) {
            boolean updateFlag = checkTime();
            if (updateFlag) {
                try {
                    cmdCls();
                } catch (Exception ignored) {
                }

                info("读取Excel中...", PRINT_STYLE_HIDE_DATE);
                printProgressBar(25, (double) (rowIndex - templateObject.getStartRowIndex() + 1) / templateObject.getRowStep() / steps);
                BigDecimal bigDecimal = new BigDecimal((double) (rowIndex - templateObject.getStartRowIndex() + 1) / templateObject.getRowStep() / steps);
                printFixedLengthString(bigDecimal.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) + "%", 25, ALIGN_RIGHT);
            }
            if (!templateObject.getLoopTemplate().isEmpty()) {
                try {
                    if (!templateObject.getPreText().isEmpty() || rowIndex != templateObject.getStartRowIndex()) {
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.write(templateObject.getFilledLoopTemplate(data));
                    if (updateFlag) {
                        success("写入成功", PRINT_STYLE_HIDE_DATE);
                    }
                } catch (IOException e) {
                    if (updateFlag) {
                        error("写入失败！", PRINT_STYLE_HIDE_DATE);
                    } else {
                        newLine();
                        error("写入失败！");
                    }
                }
            }
        }

        private boolean checkTime() {
            Long date = System.currentTimeMillis();
            if (time == null) {
                time = date;
                return true;
            }
            if (date - time >= 50) {
                time = date;
                return true;
            }
            return false;
        }
    }
}
