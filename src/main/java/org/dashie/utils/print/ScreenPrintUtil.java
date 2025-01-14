package org.dashie.utils.print;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author DashieDasie
 * @since 2025/1/14 13:30
 */
public class ScreenPrintUtil {
    public static final String SUCCESS = "[SUCCESS] ";
    public static final String INFO = "[INFO]    ";
    public static final String WARN = "[WARN]    ";
    public static final String ERROR = "[ERROR]   ";

    public static final int PRINT_STYLE_NORMAL = 0;
    public static final int PRINT_STYLE_BREAK = 1;
    public static final int PRINT_STYLE_HIDE_DATE = 2;

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    // 颜色
    public static final int COLOR_WHITE = 0;
    public static final int COLOR_RED = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_YELLOW = 3;
    public static final int COLOR_BLUE = 4;
    public static final int COLOR_PURPLE = 5;
    public static final int COLOR_CYAN = 6;
    public static final int COLOR_GRAY = 7;
    // 字体格式
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 3;
    public static final int STYLE_UNDERLINE = 4;
    public static final int STYLE_INVERSE = 7;
    // 进度条字符
    public static final String PROGRESS_DONE = "■";
    public static final String PROGRESS_NOT_DONE = "□";
    // 进度条默认长度
    public static final int PROGRESS_BAR_DEFAULT_LENGTH = 25;
    // 文字对齐方式
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_MIDDLE = 3;
    // 加载动画字符
    public static final String[] LOADING = {"-", "\\", "|", "/"};

    /**
     * 打印带颜色文字
     * @param line 打印内容
     * @param backgroundColor 背景颜色
     * @param fontColor 文字颜色
     * @param style 文字风格
     */
    public static void printWithColor(String line, int backgroundColor, int fontColor, int style) {
        String back = "4" + backgroundColor;
        String font = "3" + fontColor;
        System.out.format("\33[%s;%s;%dm%s\33[0m %n", back, font, style, line);
    }

    /**
     * 打印有字体颜色的文字
     * @param line 打印内容
     * @param color 字体颜色
     * @param style 文字风格
     */
    public static void printWithFontColor(String line, int color, int style) {
        String font = "3" + color;
        System.out.format("\33[%s;%dm%s\33[0m %n", font, style, line);
    }

    /**
     * 打印有背景颜色的文字
     * @param line 打印内容
     * @param color 背景颜色
     * @param style 文字风格
     */
    public static void printWithBackgroundColor(String line, int color, int style) {
        String back = "4" + color;
        System.out.format("\33[%s;%dm%s\33[0m %n", back, style, line);
    }

    /**
     * 打印有风格的文字
     * @param line 打印内容
     * @param style 文字风格
     */
    public static void printWithStyle(String line, int style) {
        System.out.format("\33[%dm%s\33[0m %n", style, line);
    }

    /**
     * 打印带颜色且字体颜色更亮的文字（不适用于cmd）
     * @param line 打印内容
     * @param backgroundColor 背景颜色
     * @param fontColor 文字颜色
     * @param style 文字风格
     */
    public static void printWithBrightColor(String line, int backgroundColor, int fontColor, int style) {
        String back = "4" + backgroundColor;
        String font = "9" + fontColor;
        System.out.format("\33[%s;%s;%dm%s\33[0m %n", back, font, style, line);
    }

    /**
     * 打印字体颜色更亮的文字（不适用于cmd）
     * @param line 打印内容
     * @param color 文字颜色
     * @param style 文字风格
     */
    public static void printWithBrightFontColor(String line, int color, int style) {
        String font = "9" + color;
        System.out.format("\33[%s;%dm%s\33[0m %n", font, style, line);
    }

    /**
     * cmd清空命令
     * @throws IOException start()导致的异常
     * @throws InterruptedException waitFor()导致的异常
     */
    public static void cmdCls() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    /**
     * 打印进度条
     * @param length 进度条总长
     * @param done 已完成进度长度
     */
    public static void printProgressBar(int length, int done) {
        System.out.println(getProgressBar(length, done));
    }

    /**
     * 打印进度条
     * @param length 进度条总长
     * @param rate 进度比例
     */
    public static void printProgressBar(int length, double rate) {
        System.out.println(getProgressBar(length, rate));
    }

    /**
     * 打印默认长度的进度条
     * @param rate 进度比例
     */
    public static void printProgressBar(double rate) {
        System.out.println(getProgressBar(rate));
    }

    /**
     * 获取进度条字符串
     * @param length 进度条总长
     * @param done 已完成进度长度
     * @return 进度条字符串
     */
    public static String getProgressBar(int length, int done) {
        return StringUtils.repeat(PROGRESS_DONE, done) + StringUtils.repeat(PROGRESS_NOT_DONE, length - done);
    }

    /**
     * 获取进度条字符串
     * @param length 进度条总长
     * @param rate 进度比例
     * @return 进度条字符串
     */
    public static String getProgressBar(int length, double rate) {
        return getProgressBar(length, (int) (length * rate));
    }

    /**
     * 获取默认长度的进度条字符串
     * @param rate 进度比例
     * @return 默认长度的进度条字符串
     */
    public static String getProgressBar(double rate) {
        return getProgressBar(PROGRESS_BAR_DEFAULT_LENGTH, rate);
    }

    /**
     * 打印固定长度的字符串
     * @param text 打印内容
     * @param length 长度
     * @param align 文字对齐方式
     */
    public static void printFixedLengthString(String text, int length, int align) {
        System.out.println(getFixedLengthString(text, length, align));
    }

    /**
     * 获取固定长度的字符串
     * @param text 打印内容
     * @param length 长度
     * @param align 文字对齐方式
     * @return 固定长度的字符串
     */
    public static String getFixedLengthString(String text, int length, int align) {
        if (text.length() >= length) {
            return text;
        }
        switch (align) {
            case ALIGN_LEFT:
                return String.format("%-" + length + "s", text);
            case ALIGN_RIGHT:
                return String.format("%" + length + "s", text);
            case ALIGN_MIDDLE:
                int newLength = length / 2;
                String s = String.format("%-" + newLength + "s", text);
                return String.format("%" + (length - newLength) + "s", s);
        }
        return text;
    }

    /**
     * 打印加载中符号
     * @param index 符号索引
     * @return 下一个符号索引
     */
    public static int printLoadingString(int index) {
        System.out.println(getLoadingString(index));
        return ++index;
    }

    /**
     * 获取加载中符号（索引需自行递增）
     * @param index 符号索引
     * @return 加载中符号
     */
    public static String getLoadingString(int index) {
        return LOADING[index % 4];
    }

    public static void success(String info) {
        success(info, STYLE_NORMAL);
    }

    public static void success(String info, int style) {
        printSystemInfo(SUCCESS, info, style);
    }

    public static void info(String info) {
        info(info, STYLE_NORMAL);
    }

    public static void info(String info, int style) {
        printSystemInfo(INFO, info, style);
    }

    public static void warn(String info) {
        printSystemInfo(SUCCESS, info, PRINT_STYLE_NORMAL);
    }

    public static void warn(String info, int style) {
        printSystemInfo(SUCCESS, info, style);
    }

    public static void error(String info) {
        printSystemInfo(SUCCESS, info, STYLE_NORMAL);
    }

    public static void error(String info, int style) {
        printSystemInfo(SUCCESS, info, style);
    }

    public static void printSystemInfo(String prefix, String info, int style) {
        switch (style) {
            case PRINT_STYLE_NORMAL:
                System.out.println(format.format(new Date(System.currentTimeMillis())) + " " + prefix + info);
                break;
            case PRINT_STYLE_BREAK:
                System.out.println(format.format(new Date(System.currentTimeMillis())));
                System.out.println(prefix + info);
                break;
            case PRINT_STYLE_HIDE_DATE:
                System.out.println(prefix + info);
                break;
        }
    }

    public static void newLine() {
        System.out.println();
    }
}
