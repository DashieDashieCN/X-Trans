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
    // 上文末尾是否换行，默认true
    private Boolean preTextLineBreak;
    // 下文开头是否换行，默认true
    private Boolean sufTextLineBreak;
    // 循环部分是否换行，默认true
    private Boolean loopLineBreak;
    // 循环部分分隔符
    private String separator;
    // 循环部分去除首行前缀
    private String prefixOverrides;
    // 循环部分去除最后一行后缀
    private String suffixOverrides;

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
        if (preTextLineBreak == null) {
            preTextLineBreak = true;
        }
        if (sufTextLineBreak == null) {
            sufTextLineBreak = true;
        }
        if (loopLineBreak == null) {
            loopLineBreak = true;
        }
        if (separator == null) {
            separator = "";
        }
        if (prefixOverrides == null) {
            prefixOverrides = "";
        }
        if (suffixOverrides == null) {
            suffixOverrides = "";
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

    private String getBooleanCnValue(boolean flag) {
        return flag ? "是" : "否";
    }

    public static Boolean string2Boolean(String s) {
        switch (s) {
            case "1":
                return true;
            case "0":
                return false;
        }
        if (s.equalsIgnoreCase("true")) {
            return true;
        }
        if (s.equalsIgnoreCase("false")) {
            return false;
        }
        return null;
    }

    public void printInfo() {
        newLine();
        boolean noInputFileName = inputFilePath == null || inputFilePath.isEmpty();
        System.out.println("              起始行号 | " + startRowIndex);
        System.out.println("            行读取步长 | " + rowStep);
        System.out.println("          输入文件路径 | " + (noInputFileName ? "*未配置或配置有误" : inputFilePath));
        System.out.println("  （解析前）输出文件名 | " + outputFileName);
        if (!noInputFileName) {
            System.out.println("  （解析后）输出文件名 | " + getFilledOutputFileName());
        }
        System.out.println("          上文末尾换行 | " + getBooleanCnValue(preTextLineBreak));
        System.out.println("          下文开头换行 | " + getBooleanCnValue(sufTextLineBreak));
        System.out.println("          循环部分换行 | " + getBooleanCnValue(loopLineBreak));
        System.out.println("        循环部分分隔符 | " + (separator.isEmpty() ? "*未配置" : separator));
        System.out.println("  循环部分去除多余前缀 | " + (prefixOverrides.isEmpty() ? "*未配置" : prefixOverrides));
        System.out.println("  循环部分去除多余后缀 | " + (suffixOverrides.isEmpty() ? "*未配置" : suffixOverrides));
    }
    
    public static String removePrefix(String s, TemplateObject templateObject) {
        String prefixOverrides = templateObject.getPrefixOverrides();
        if (!prefixOverrides.isEmpty() && s.length() >= prefixOverrides.length()) {
            String prefix = s.substring(0, prefixOverrides.length());
            if (prefix.equals(prefixOverrides)) {
                return s.substring(prefixOverrides.length());
            }
        }
        return s;
    }
    
    public static String removeSuffix(String s, TemplateObject templateObject) {
        String suffixOverrides = templateObject.getSuffixOverrides();
        if (!suffixOverrides.isEmpty() && s.length() >= suffixOverrides.length()) {
            String suffix = s.substring(s.length() - suffixOverrides.length());
            if (suffix.equals(suffixOverrides)) {
                return s.substring(0, s.length() - suffixOverrides.length());
            }
        }
        return s;
    }

    public static String removeAffix(String s, TemplateObject templateObject) {
        return removeSuffix(removePrefix(s, templateObject), templateObject);
    }
}
