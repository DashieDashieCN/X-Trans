package org.dashie.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.dashie.common.TemplateObjectCommon.*;
import static org.dashie.utils.print.ScreenPrintUtil.newLine;

/**
 * @author DashieDasie
 * @since 2025/1/14 9:39
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TemplateObject {
    // 起始行号，默认1
    private Integer startRowIndex;
    // 步长，默认1
    private Integer rowStep;
    // 上文
    private String preText;
    // 下文
    private String sufText;
    // 循环部分模板
    private String loopTemplate;
    // 文件名
    private String fileName;
    // 输出文件名
    private String outputFileName;
    // 输入文件路径
    private String inputFilePath;

    TemplateObject(Integer startRowIndex, Integer rowStep, String preText, String sufText, String loopTemplate, String fileName, String outputFileName, String inputFilePath) {
        this.startRowIndex = (startRowIndex == null || startRowIndex < 0) ? DEFAULT_START_ROW_INDEX : startRowIndex;
        this.rowStep = (rowStep == null || rowStep < 1) ? DEFAULT_ROW_STEP : rowStep;
        this.preText = preText == null ? DEFAULT_PRE_TEXT : preText;
        this.sufText = sufText == null ? DEFAULT_SUF_TEXT : sufText;
        this.loopTemplate = loopTemplate == null ? DEFAULT_LOOP_TEMPLATE : loopTemplate;
        this.fileName = (fileName == null || fileName.isEmpty()) ? DEFAULT_FILE_NAME : fileName;
        this.outputFileName = (outputFileName == null || outputFileName.isEmpty()) ? DEFAULT_OUTPUT_FILE_NAME : outputFileName;
        this.inputFilePath = inputFilePath;
    }

    public TemplateObject checkAndFix() {
        if (startRowIndex == null || startRowIndex < 0) {
            startRowIndex = DEFAULT_START_ROW_INDEX;
        }
        if (rowStep == null || this.rowStep < 1) {
            rowStep = DEFAULT_ROW_STEP;
        }
        if (preText == null) {
            preText = DEFAULT_PRE_TEXT;
        }
        if (sufText == null) {
            sufText = DEFAULT_SUF_TEXT;
        }
        if (loopTemplate == null) {
            loopTemplate = DEFAULT_LOOP_TEMPLATE;
        }
        if (fileName == null || fileName.isEmpty()) {
            fileName = DEFAULT_FILE_NAME;
        }
        if (outputFileName == null || outputFileName.isEmpty()) {
            outputFileName = DEFAULT_OUTPUT_FILE_NAME;
        }
        return this;
    }

    public String getFilledOutputFileName() {
        return outputFileName.replaceAll("\\{}", fileName);
    }

    public String getFilledLoopTemplate(Map<Integer, String> excelData) {
        String loopString = loopTemplate;
        Set<Integer> colIndexSet = getDistinctColIndexKeywordSet();
        for (Integer index : colIndexSet) {
            try {
                loopString = loopString.replaceAll("\\$\\{" + index + "}\\$", excelData.get(index));
            } catch (Exception ignore) {
                loopString = loopString.replaceAll("\\$\\{" + index + "}\\$", "");
            }
        }
        return loopString;
    }

    public List<String> getColIndexKeywords() {
        List<String> colIndexKeywords = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{[0-9]+}\\$");
        Matcher matcher = pattern.matcher(loopTemplate);
        while (matcher.find()) {
            colIndexKeywords.add(matcher.group());
        }
        return colIndexKeywords;
    }

    public Set<Integer> getDistinctColIndexKeywordSet() {
        return getColIndexKeywords().stream()
                .map(s -> s.replaceAll("(\\$\\{)|(}\\$)", ""))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    public boolean isEmpty() {
        return loopTemplate.isEmpty() && preText.isEmpty() && sufText.isEmpty();
    }

    public void printInfo() {
        newLine();
        boolean noInputFileName = inputFilePath == null || inputFilePath.isEmpty();
        System.out.println("            起始行号  " + startRowIndex);
        System.out.println("          行读取步长  " + rowStep);
        System.out.println("        输入文件路径  " + (noInputFileName ? "*未配置" : inputFilePath));
        System.out.println("  （解析前）输出文件名  " + outputFileName);
        if (!noInputFileName) {
            System.out.println("  （解析后）输出文件名  " + getFilledOutputFileName());
        }
    }
}
