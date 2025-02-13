package org.dashie.utils.text;

import org.dashie.entity.TemplateObject;

import java.io.*;

import static org.dashie.common.FileCommon.*;
import static org.dashie.entity.TemplateObject.string2Boolean;
import static org.dashie.utils.print.ScreenPrintUtil.*;

/**
 * @author DashieDasie
 * @since 2025/1/14 9:39
 */
public class TextUtil {
    public static TemplateObject readTemplate(String path) throws IOException {
        TemplateObject templateObject = new TemplateObject().checkAndFix();
        File file = new File(path);
        boolean settingEnd = false;
        int textLocation = 0;
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (!settingEnd && line.matches("[a-zA-Z]+=.*")) {
                String[] splitString =  line.split("=", 2);
                String key = splitString[0];
                String value = splitString[1];
                switch (key) {
                    case KEY_START_ROW_INDEX:
                        try {
                            info("读取模板配置：起始行号=" + value);
                            templateObject.setStartRowIndex(Integer.parseInt(value));
                        } catch (Exception ignored) {
                        }
                        break;
                    case KEY_ROW_STEP:
                        try {
                            info("读取模板配置：行读取步长=" + value);
                            templateObject.setRowStep(Integer.parseInt(value));
                        } catch (Exception ignored) {
                        }
                        break;
                    case KEY_OUTPUT_FILE_NAME:
                        info("读取模板配置：输出文件名=" + value);
                        templateObject.setOutputFileName(value);
                        break;
                    case KEY_INPUT_FILE_PATH:
                        info("读取模板配置：Excel路径=" + value);
                        templateObject.setInputFilePath(value);
                        try {
                            File excel = new File(value);
                            if (!excel.exists()) {
                                throw new Exception();
                            }
                            templateObject.setFileName(excel.getName().substring(0, excel.getName().lastIndexOf('.')));
                        } catch (Exception e) {
                            error("无法根据路径查找到对应Excel文件，需用户手动输入");
                            templateObject.setInputFilePath(null);
                        }
                        break;
                    case KEY_PRE_TEXT_LINE_BREAK:
                        info("读取模板配置：上文末尾换行=" + value);
                        Boolean flag = string2Boolean(value);
                        if (flag == null) {
                            error("上文末尾换行对应值非法，使用默认值");
                        } else {
                            templateObject.setPreTextLineBreak(flag);
                        }
                        break;
                    case KEY_SUF_TEXT_LINE_BREAK:
                        info("读取模板配置：下文开头换行=" + value);
                        Boolean flag1 = string2Boolean(value);
                        if (flag1 == null) {
                            error("下文开头换行对应值非法，使用默认值");
                        } else {
                            templateObject.setSufTextLineBreak(flag1);
                        }
                        break;
                    case KEY_LOOP_LINE_BREAK:
                        info("读取模板配置：循环部分换行=" + value);
                        Boolean flag2 = string2Boolean(value);
                        if (flag2 == null) {
                            error("循环部分换行对应值非法，使用默认值");
                        } else {
                            templateObject.setLoopLineBreak(flag2);
                        }
                        break;
                    case KEY_SEPARATOR:
                        info("读取模板配置：循环部分分隔符=" + value);
                        templateObject.setSeparator(value);
                        break;
                    case KEY_PREFIX_OVERRIDES:
                        info("读取模板配置：循环部分去除多余前缀=" + value);
                        templateObject.setPrefixOverrides(value);
                        break;
                    case KEY_SUFFIX_OVERRIDES:
                        info("读取模板配置：循环部分去除多余后缀=" + value);
                        templateObject.setSuffixOverrides(value);
                        break;
                    default:
                        warn("未知配置，结束读取模板配置：" + line);
                        settingEnd = true;
                }
            } else if (!settingEnd) {
                settingEnd = true;
            }
            if (settingEnd) {
                if (textLocation == 0 && line.equals(KEY_LOOP)) {
                    textLocation = 1;
                } else if (textLocation == 1 && line.equals(KEY_LOOP)){
                    textLocation = 2;
                }
                if (!line.equals(KEY_LOOP)) {
                    switch (textLocation) {
                        // 上文读取
                        case 0:
                            info("上文读取：" + line);
                            String text0 = templateObject.getPreText();
                            templateObject.setPreText(text0.isEmpty() ? line : text0 + "\n" + line);
                            break;
                        // 循环模板读取
                        case 1:
                            info("循环模板读取：" + line);
                            String text1 = templateObject.getLoopTemplate();
                            templateObject.setLoopTemplate(text1.isEmpty() ? line : text1 + "\n" + line);
                            break;
                        // 下文读取
                        case 2:
                            info("下文读取：" + line);
                            String text2 = templateObject.getSufText();
                            templateObject.setSufText(text2.isEmpty() ? line : text2 + "\n" + line);
                            break;
                    }
                }
            }
        }
        return templateObject;
    }

    public static String getOutputPath(TemplateObject templateObject) {
        return OUTPUT_PATH + File.separator + templateObject.getFilledOutputFileName();
    }

    public static BufferedWriter getWriter(TemplateObject templateObject) throws FileNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(getOutputPath(templateObject));
        return new BufferedWriter(new OutputStreamWriter(fileOutputStream));
    }
}
