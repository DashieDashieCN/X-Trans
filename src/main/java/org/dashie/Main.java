package org.dashie;

import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.dashie.common.CommandEnum;
import org.dashie.entity.TemplateObject;
import org.dashie.utils.excel.ExcelUtil;
import org.dashie.utils.text.TextUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.exe4j.Controller;

import static org.dashie.common.CommandCommon.STRATEGY_AVOID;
import static org.dashie.common.CommandCommon.STRATEGY_CONTAINS;
import static org.dashie.common.CommandEnum.*;
import static org.dashie.common.FileCommon.*;
import static org.dashie.utils.file.FileUtil.openFile;
import static org.dashie.utils.print.ScreenPrintUtil.*;

/**
 * @author DashieDasie
 * @since 2025/1/14 9:34
 */
public class Main {
    public static final String VERSION = "V1.4.2";

    public static final Scanner scanner = new Scanner(System.in, "GBK");

    /**
     * 输出标题
     */
    public static void printTitle() {
        System.out.println("\n" +
                "  _____            _     _      _____            _     _      \n" +
                " |  __ \\          | |   (_)    |  __ \\          | |   (_)     \n" +
                " | |  | | __ _ ___| |__  _  ___| |  | | __ _ ___| |__  _  ___ \n" +
                " | |  | |/ _` / __| '_ \\| |/ _ \\ |  | |/ _` / __| '_ \\| |/ _ \\\n" +
                " | |__| | (_| \\__ \\ | | | |  __/ |__| | (_| \\__ \\ | | | |  __/\n" +
                " |_____/ \\__,_|___/_| |_|_|\\___|_____/ \\__,_|___/_| |_|_|\\___|\n" +
                "                                                              ");
        System.out.println("\n" +
                "             _  __    ______                     \n" +
                "            | |/ /   /_  __/________ _____  _____\n" +
                "            |   /_____/ / / ___/ __ `/ __ \\/ ___/\n" +
                "           /   /_____/ / / /  / /_/ / / / (__  ) \n" +
                "          /_/|_|    /_/ /_/   \\__,_/_/ /_/____/  \n" +
                "                                                 " + VERSION);
    }

    /**
     * 输出指南
     */
    public static void printGuide() {
        newLine();
        System.out.println("操作指南：");
        newLine();
        System.out.println(getFixedLengthString("1. ", 5, ALIGN_RIGHT) + "xls文件需要先转换为xlsx格式");
        System.out.println(getFixedLengthString("2. ", 5, ALIGN_RIGHT) + "配置setting目录下的template.txt文件");
        System.out.println(getFixedLengthString("3. ", 5, ALIGN_RIGHT) + "输入s并按下回车开始执行转换程序");
        System.out.println(getFixedLengthString("4. ", 5, ALIGN_RIGHT) + "输入h并按下回车查看全部指令");
    }

    /**
     * 输出指令帮助
     */
    public static void printHelp() {
        cmdCls();
        newLine();
        System.out.println("指令帮助：");
        newLine();
        for (CommandEnum command : CommandEnum.values()) {
            System.out.println(getFixedLengthString(command.getCode(), 5, ALIGN_RIGHT) + " " + command.getDesc());
        }
        getCmd();
    }

    /**
     * 输出模板帮助
     */
    public static void templateHelp() {
        cmdCls();
        newLine();
        System.out.println("模板配置帮助：");
        newLine();
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "模板配置分为四部分，由上到下依次为参数设置、上文设置、循环模板设置、下文设置");
        System.out.println(getFixedLengthString("1. ", 5, ALIGN_RIGHT) + "参数设置");
        System.out.println(getFixedLengthString("", 5, ALIGN_RIGHT) + "每行仅允许设置1个参数，格式为：key=value");
        System.out.println(getFixedLengthString("1.1 ", 5, ALIGN_RIGHT) + "起始行号 startRowIndex=XXX");
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "类型：大于等于0的正整数；非必填，默认值：1");
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "行号自0开始计数；限制Excel读取数据时的起始行号");
        System.out.println(getFixedLengthString("1.2 ", 5, ALIGN_RIGHT) + "行读取步长 rowStep=XXX");
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "类型：大于等于1的正整数；非必填，默认值：1");
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "Excel读取数据时下一读取行到当前读取行之间的步长");
        System.out.println(getFixedLengthString("1.3 ", 5, ALIGN_RIGHT) + "Excel路径 path=XXX");
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "类型：字符串；非必填，缺失时需要用户手动输入");
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "读取的Excel路径，所在文件夹路径+文件名+后缀名");
        System.out.println(getFixedLengthString("1.4 ", 5, ALIGN_RIGHT) + "输出文件名 outputFileName=XXX");
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "类型：字符串；非必填，默认值：{}.txt");
        System.out.println(getFixedLengthString(" ", 5, ALIGN_RIGHT) + "Excel转换后的输出文件名，文件名+后缀名，可使用特殊关键字");
        System.out.println("         关键字 {} 复用Excel文件名（不包含后缀名）");
        System.out.println(getFixedLengthString("2. ", 5, ALIGN_RIGHT) + "上文设置");
        System.out.println(getFixedLengthString("", 5, ALIGN_RIGHT) + "类型：字符串（可多行）；选填；在读取Excel数据并转换为文本之前写入的文本");
        System.out.println(getFixedLengthString("3. ", 5, ALIGN_RIGHT) + "循环模板设置");
        System.out.println(getFixedLengthString("", 5, ALIGN_RIGHT) + "类型：字符串（可多行）；选填；读取Excel数据并转换为文本时使用的模板，可使用特殊关键字");
        System.out.println("         关键字 ${列号}$ 在大括号中填写从0开始计数的列号，对应每行对应列的单元格中的数据");
        System.out.println("         关键字 ``` 标识循环模板的首尾，包裹循环模板");
        System.out.println(getFixedLengthString("4. ", 5, ALIGN_RIGHT) + "下文设置");
        System.out.println(getFixedLengthString("", 5, ALIGN_RIGHT) + "类型：字符串（可多行）；选填；在读取Excel数据并转换为文本之后写入的文本");
        newLine();
        printCommandTip(E);
        getCmd();
    }

    /**
     * 循环等待用户输入指令
     */
    public static void getCmd() {
        getCmd(new String[]{});
    }

    /**
     * 循环等待用户输入指令
     * @param dict 限定生效指令
     */
    public static void getCmd(CommandEnum[] dict) {
        getCmd(dict, STRATEGY_CONTAINS);
    }

    /**
     * 循环等待用户输入指令
     * @param dict 限定生效指令
     */
    public static void getCmd(String[] dict) {
        getCmd(dict, STRATEGY_CONTAINS);
    }

    /**
     * 循环等待用户输入指令
     * @param dict 限定生效指令
     * @param strategy 字典策略
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void getCmd(CommandEnum[] dict, int strategy) {
        newLine();
        while (true) {
            System.out.print("> ");
            String s = scanner.nextLine();
            analyseCode(s, (String[]) Arrays.stream(dict).map(CommandEnum::getCode).toArray(), strategy);
        }
    }

    /**
     * 循环等待用户输入指令
     * @param dict 限定生效指令
     * @param strategy 字典策略
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void getCmd(String[] dict, int strategy) {
        newLine();
        while (true) {
            System.out.print("> ");
            String s = scanner.nextLine();
            analyseCode(s, dict, strategy);
        }
    }

    /**
     * 分析指令代码
     * @param code 指令代码
     */
    public static void analyseCode(String code) {
        analyseCode(code, new String[]{}, STRATEGY_CONTAINS);
    }

    /**
     * 分析指令代码
     * @param code 指令代码
     * @param dict 限定生效指令
     * @param strategy 字典策略
     */
    public static void analyseCode(String code, String[] dict, int strategy) {
        boolean legalCommand = false;
        Set<String> set = new HashSet<>(Arrays.asList(dict));
        if (strategy == STRATEGY_CONTAINS) {
            legalCommand = dict.length == 0 || set.isEmpty() || set.contains(code);
        } else if (strategy == STRATEGY_AVOID) {
            legalCommand = dict.length == 0 || set.isEmpty() || !set.contains(code);
        }
        if (legalCommand) {
            CommandEnum command = getCommandByCode(code);
            if (command == null) {
                System.out.println("- 指令不存在");
            } else {
                switch (command) {
                    case H:
                        printHelp();
                        break;
                    case O:
                        openFile(TEMPLATE_DIR_PATH);
                        break;
                    case F:
                        openFile(TEMPLATE_PATH);
                        break;
                    case P:
                        openFile(OUTPUT_PATH);
                        break;
                    case I:
                        templateHelp();
                        break;
                    case Q:
                        shutdown();
                        break;
                    case R:
                        init();
                    case E:
                        home();
                        break;
                    case S:
                        start();
                        break;
                    case C:
                        getTemplateSettings();
                        break;
                    default:
                        System.out.println("- 指令不存在");
                }
            }
        } else {
            System.out.println("- 指令不存在或此处不适用");
        }
    }

    /**
     * 检查文件完整性
     * @throws IOException createNewFile()异常
     */
    public static void fileCheck() throws IOException {
        info("正在检查文件完整性");
        File file = new File(TEMPLATE_DIR_PATH);
        if (file.exists()) {
            info("模板目录存在");
        } else {
            info("模板目录缺失，自动创建");
            if (file.mkdirs()) {
                success("模板目录创建成功");
            } else {
                error("模板目录创建失败！");
                throw new IOException("模板目录 " + TEMPLATE_DIR_PATH + " 创建失败");
            }
        }
        file = new File(TEMPLATE_PATH);
        if (file.exists()) {
            info("模板文件存在");
        } else {
            info("模板文件缺失，自动创建");
            if (file.createNewFile()) {
                success("模板文件创建成功");
            } else {
                error("模板文件创建失败！");
                throw new IOException("模板文件 " + TEMPLATE_PATH + " 创建失败");
            }
        }
        file = new File(OUTPUT_PATH);
        if (file.exists()) {
            info("输出目录存在");
        } else {
            info("输出目录缺失，自动创建");
            if (file.mkdirs()) {
                success("输出目录创建成功");
            } else {
                error("输出目录创建失败！");
                throw new IOException("输出目录 " + OUTPUT_PATH + " 创建失败");
            }
        }
        success("文件完整性校验完成");
    }

    /**
     * 中止程序
     */
    public static void shutdown() {
        try {
            info("程序将在1秒后退出");
            Thread.sleep(1000);
            System.exit(0);
        } catch (Exception ignored) {}
    }

    /**
     * 程序主页
     */
    public static void home() {
        cmdCls();
        printTitle();
        printGuide();
        getCmd();
    }

    /**
     * 程序初始化
     */
    public static void init() {
        cmdCls();
        printTitle();
        try {
            fileCheck();
        } catch (Exception e) {
            error("初始化出错，文件完整性校验过程中创建缺失文件失败：" + e);
            info("请手动创建后按下r并回车重启程序，或下载最新版本的应用");
            getCmd();
        }
    }

    /**
     * 打开程序前初始化
     */
    public static String beforeOpen() {
        String result = "";
        cmdCls();
        try {
            Controller.writeMessage("正在检查文件完整性");
            File file = new File(TEMPLATE_DIR_PATH);
            if (file.exists()) {
                Controller.writeMessage("模板目录存在");
            } else {
                Controller.writeMessage("模板目录缺失，自动创建");
                if (file.mkdirs()) {
                    Controller.writeMessage("模板目录创建成功");
                } else {
                    Controller.writeMessage("模板目录创建失败！");
                    result += "模板目录 " + TEMPLATE_DIR_PATH + " 缺失\n";
                }
            }
            file = new File(TEMPLATE_PATH);
            if (file.exists()) {
                Controller.writeMessage("模板文件存在");
            } else {
                Controller.writeMessage("模板文件缺失，自动创建");
                if (file.createNewFile()) {
                    Controller.writeMessage("模板文件创建成功");
                } else {
                    Controller.writeMessage("模板文件创建失败！");
                    result += "模板文件 " + TEMPLATE_PATH + " 缺失\n";
                }
            }
            file = new File(OUTPUT_PATH);
            if (file.exists()) {
                Controller.writeMessage("输出目录存在");
            } else {
                Controller.writeMessage("输出目录缺失，自动创建");
                if (file.mkdirs()) {
                    Controller.writeMessage("输出目录创建成功");
                } else {
                    Controller.writeMessage("输出目录创建失败！");
                    result += "输出目录 " + OUTPUT_PATH + " 缺失\n";
                }
            }
            Controller.writeMessage("文件完整性校验完成");
            Thread.sleep(500);
            Controller.hide();
        } catch (Controller.ConnectionException ignored) {
            try {
                Controller.hide();
            } catch (Controller.ConnectionException ignored2) {
            }
        } catch (Exception e) {
            error("初始化出错，文件完整性校验过程中创建缺失文件失败：" + e);
            printCommandTips(new CommandEnum[]{R, H});
            try {
                Controller.hide();
            } catch (Controller.ConnectionException ignored) {
            }
            getCmd(new CommandEnum[]{E, S, C}, STRATEGY_AVOID);
        }
        return result;
    }

    public static void getTemplateSettings() {
        try {
            cmdCls();
            TemplateObject templateObject = TextUtil.readTemplate(TEMPLATE_PATH);
            templateObject.printInfo();
            printCommandTips(new CommandEnum[]{E, H});
            getCmd();
        } catch (Exception e) {
            error("无法读取模板文件，请确认路径：" + TEMPLATE_PATH);
            error(e.toString());
            printCommandTip(H);
            getCmd();
        }
    }

    /**
     * 开始执行
     */
    public static void start() {
        String filePath;
        String templatePath = TEMPLATE_PATH;
        TemplateObject templateObject = new TemplateObject();
        info("正在读取模板文件：" + templatePath);
        try {
            templateObject = TextUtil.readTemplate(templatePath);
        } catch (Exception e) {
            error("无法读取模板文件，请确认路径：" + templatePath);
            error(e.toString());
            printCommandTip(H);
            getCmd();
            return;
        }
        if (templateObject.isEmpty()) {
            error("模板文件正文为空，请确认！");
            info("程序执行结束");
            getCmd();
        } else {
            success("模板文件读取成功！");
        }
        if (templateObject.getInputFilePath() == null || templateObject.getInputFilePath().isEmpty()) {
            newLine();
            System.out.println("模板中未配置Excel路径，请输入Excel文件路径");
            System.out.print("path=");
            filePath = scanner.nextLine();
        } else {
            filePath = templateObject.getInputFilePath();
        }
        File file = new File(filePath);
        templateObject.setFileName(file.getName().substring(0, file.getName().lastIndexOf('.')));
        cmdCls();
        info("正在读取Excel文件：" + filePath);
        try {
            ExcelUtil.readExcel(filePath, templateObject);
            cmdCls();
            success("成功转换为：" + TextUtil.getOutputPath(templateObject));
            info("1秒后将打开输出文件夹");
            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File(OUTPUT_PATH));
        } catch (NotOLE2FileException e) {
            error("文件格式错误，系统无法读取 Office 2003 XML (xls) 等格式的文件，请将文件转化为xlsx后重试");
        } catch (IOException e) {
            error("Excel处理出错：" + e);
        }
        printCommandTips(new CommandEnum[]{E, Q, H, S});
        getCmd();
    }

    public static void main(String[] args) {
        try {
            String result = beforeOpen();
            if (result.isEmpty()) {
                home();
            } else  {
                cmdCls();
                printTitle();
                System.out.println("\n文件完整性校验不通过，系统创建文件失败，请手动创建以下文件或下载最新版本应用：\n");
                System.out.println(result);
                printCommandTips(new CommandEnum[]{R, Q, H});
                getCmd(new CommandEnum[]{E, S, C}, STRATEGY_AVOID);
            }
        } catch (Exception e) {
            error(e.toString());
        }
        getCmd();
    }
}