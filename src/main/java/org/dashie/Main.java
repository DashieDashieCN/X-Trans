package org.dashie;

import lombok.extern.log4j.Log4j2;
import org.dashie.entity.TemplateObject;
import org.dashie.utils.excel.ExcelUtil;
import org.dashie.utils.text.TextUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static org.dashie.common.FileCommon.*;
import static org.dashie.utils.print.ScreenPrintUtil.*;

/**
 * @author DashieDasie
 * @since 2025/1/14 9:34
 */
@Log4j2
public class Main {
    public static final String VERSION = "V1.2.3";

    public static final Desktop desktop = Desktop.getDesktop();

    public static final Scanner scanner = new Scanner(System.in);

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

    public static void printGuide() {
        newLine();
        System.out.println("操作指南：");
        newLine();
        System.out.println(getFixedLengthString("1. ", 5, ALIGN_RIGHT) + "配置setting目录下的template.txt文件");
        System.out.println(getFixedLengthString("2. ", 5, ALIGN_RIGHT) + "输入s并按下回车开始执行转换程序");
        System.out.println(getFixedLengthString("3. ", 5, ALIGN_RIGHT) + "输入h并按下回车查看全部指令");
    }

    public static void printHelp() throws IOException, InterruptedException {
        cmdCls();
        newLine();
        System.out.println("指令帮助：");
        newLine();
        System.out.println(getFixedLengthString("h", 5, ALIGN_RIGHT) + " " + "查看指令帮助");
        System.out.println(getFixedLengthString("o", 5, ALIGN_RIGHT) + " " + "打开模板文件（template.txt）所在文件夹");
        System.out.println(getFixedLengthString("f", 5, ALIGN_RIGHT) + " " + "打开模板文件（template.txt）");
        System.out.println(getFixedLengthString("p", 5, ALIGN_RIGHT) + " " + "打开输出文件夹");
        System.out.println(getFixedLengthString("i", 5, ALIGN_RIGHT) + " " + "查看模板配置说明");
        System.out.println(getFixedLengthString("q", 5, ALIGN_RIGHT) + " " + "退出程序");
        System.out.println(getFixedLengthString("r", 5, ALIGN_RIGHT) + " " + "重启程序");
        System.out.println(getFixedLengthString("e", 5, ALIGN_RIGHT) + " " + "返回主页");
        getCmd();
    }

    public static void templateHelp() throws IOException, InterruptedException {
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
        System.out.println(getFixedLengthString("关键字 {} ", 16, ALIGN_RIGHT) + "复用Excel文件名（不包含后缀名）");
        System.out.println(getFixedLengthString("2. ", 5, ALIGN_RIGHT) + "上文设置");
        System.out.println(getFixedLengthString("", 5, ALIGN_RIGHT) + "类型：字符串（可多行）；选填；在读取Excel数据并转换为文本之前写入的文本");
        System.out.println(getFixedLengthString("3. ", 5, ALIGN_RIGHT) + "循环模板设置");
        System.out.println(getFixedLengthString("", 5, ALIGN_RIGHT) + "类型：字符串（可多行）；选填；读取Excel数据并转换为文本时使用的模板，可使用特殊关键字");
        System.out.println(getFixedLengthString("关键字 {列号} ", 16, ALIGN_RIGHT) + "在大括号中填写从0开始计数的列号，对应每行对应列的单元格中的数据");
        System.out.println(getFixedLengthString("   关键字 ``` ", 16, ALIGN_RIGHT) + "标识循环模板的首尾，包裹循环模板");
        System.out.println(getFixedLengthString("4. ", 5, ALIGN_RIGHT) + "下文设置");
        System.out.println(getFixedLengthString("", 5, ALIGN_RIGHT) + "类型：字符串（可多行）；选填；在读取Excel数据并转换为文本之后写入的文本");
        newLine();
        System.out.println("输入e并按下回车回到主页");
        getCmd();
    }

    public static void getCmd() throws IOException, InterruptedException {
        while (true) {
            String s = scanner.nextLine();
            analyseCode(s);
        }
    }

    public static void analyseCode(String code) throws IOException, InterruptedException {
        switch (code) {
            case "h":
                printHelp();
                break;
            case "o":
                desktop.open(new File(TEMPLATE_DIR_PATH));
                break;
            case "f":
                desktop.open(new File(TEMPLATE_PATH));
                break;
            case "p":
                desktop.open(new File(OUTPUT_PATH));
                break;
            case "i":
                templateHelp();
                break;
            case "q":
                shutdown();
                break;
            case "r":
                init();
            case "e":
                home();
                break;
        }
    }

    public static void dirCheck() throws InterruptedException {
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
                shutdown();
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
                shutdown();
            }
        }
        success("文件完整性校验完成");
    }

    public static void shutdown() throws InterruptedException {
        info("程序将在1秒后退出");
        Thread.sleep(1000);
        System.exit(0);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        try {
            init();
            home();
        } catch (Exception e) {
            error(e.toString());
        }
        getCmd();
    }

    public static void home() throws IOException, InterruptedException {
        cmdCls();
        printTitle();
        printGuide();
        while (true) {
            String s = scanner.nextLine();
            if (s.equals("s")) {
                break;
            } else {
                analyseCode(s);
            }
        }
        start();
    }

    public static void init() throws IOException, InterruptedException {
        cmdCls();
        printTitle();
        dirCheck();
    }

    public static void start() throws IOException, InterruptedException {
        String filePath = null;
        String templatePath = TEMPLATE_PATH;
//        System.out.println(templateObject);
//        System.out.println("请输入Excel文件路径后按下回车");
//        System.out.print("path=");
//        Scanner scanner = new Scanner(System.in);
//        String filePath = scanner.nextLine();
        TemplateObject templateObject;
        info("正在读取模板文件：" + templatePath);
        try {
            templateObject = TextUtil.readTemplate(templatePath);
        } catch (Exception e) {
            error("无法读取模板文件，请确认路径：" + templatePath);
            error(e.toString());
            return;
        }
        if (templateObject.isEmpty()) {
            error("模板文件正文为空，请确认！");
            shutdown();
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
        ExcelUtil.readExcel(filePath, templateObject);
        cmdCls();
        success("成功转换为：" + TextUtil.getOutputPath(templateObject));
        info("1秒后将打开输出文件夹");
        Thread.sleep(1000);
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(OUTPUT_PATH));
        System.out.println("输入e并按下回车回到主页\n输入q并按下回车退出程序\n输入s并按下回车重新执行程序");
        while (true) {
            String s = scanner.nextLine();
            if (s.equals("s")) {
                start();
            } else {
                analyseCode(s);
            }
        }
    }
}