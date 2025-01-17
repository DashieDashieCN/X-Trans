package org.dashie.common;

import java.io.File;

/**
 * @author DashieDasie
 * @since 2025/1/14 10:48
 */
public class FileCommon {
    public static final String TEMPLATE_DIR_PATH = System.getProperty("user.dir") + File.separator + "setting";
    public static final String TEMPLATE_PATH = System.getProperty("user.dir") + File.separator + "setting" + File.separator + "template.txt";
    public static final String OUTPUT_PATH = System.getProperty("user.dir") + File.separator + "output";

    public static final String KEY_START_ROW_INDEX = "startRowIndex";
    public static final String KEY_ROW_STEP = "rowStep";
    public static final String KEY_OUTPUT_FILE_NAME = "outputFileName";
    public static final String KEY_PRE_TEXT_LINE_BREAK = "lineBreakPre";
    public static final String KEY_SUF_TEXT_LINE_BREAK = "lineBreakSuf";
    public static final String KEY_LOOP_LINE_BREAK = "lineBreakLoop";
    public static final String KEY_SEPARATOR = "separator";
    public static final String KEY_PREFIX_OVERRIDES = "prefixOverrides";
    public static final String KEY_SUFFIX_OVERRIDES = "suffixOverrides";
    public static final String KEY_INPUT_FILE_PATH = "path";
    public static final String KEY_LOOP = "```";

}
