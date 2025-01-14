package org.dashie;

import lombok.extern.log4j.Log4j2;
import org.dashie.entity.TemplateObject;
import org.dashie.utils.excel.ExcelUtil;
import org.dashie.utils.text.TextUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.dashie.common.FileCommon.OUTPUT_PATH;
import static org.dashie.common.FileCommon.TEMPLATE_PATH;
import static org.dashie.utils.print.ScreenPrintUtil.*;

/**
 * @author DashieDasie
 * @since 2025/1/14 9:34
 */
@Log4j2
public class Main {
    public static final String VERSION = "V1.0.0";

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

    public static void main(String[] args) throws InterruptedException, IOException {
        printTitle();
        Thread.sleep(1000);
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
        } else {
            success("模板文件读取成功！");
        }
        if (templateObject.getInputFilePath() == null || templateObject.getInputFilePath().isEmpty()) {
            Scanner scanner = new Scanner(System.in);
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
        info("正在读取Excel文件");
        ExcelUtil.readExcel(filePath, templateObject);
        cmdCls();
        success("成功转换为：" + TextUtil.getOutputPath(templateObject));
        info("1秒后程序将关闭并打开输出文件夹");
        Thread.sleep(1000);
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(OUTPUT_PATH));
    }
}