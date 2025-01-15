package org.dashie.utils.file;

import java.awt.*;
import java.io.File;

import static org.dashie.utils.print.ScreenPrintUtil.error;

/**
 * @author DashieDasie
 * @since 2025/1/15 13:54
 */
public class FileUtil {
    public static final Desktop desktop = Desktop.getDesktop();

    public static void openFile(String path) {
        try {
            desktop.open(new File(path));
        } catch (Exception e) {
            error("打开" + path + "失败");
        }
    }
}
