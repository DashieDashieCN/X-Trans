package org.dashie.common;

import lombok.Getter;

/**
 * @author DashieDasie
 * @since 2025/1/15 15:12
 */

@Getter
public enum CommandEnum {
    H("h", "查看指令帮助", "指令帮助"),
    O("o", "打开模板文件（template.txt）所在文件夹", "打开模板文件所在文件夹"),
    F("f", "打开模板文件（template.txt）", "打开模板文件"),
    P("p", "打开输出文件夹"),
    I("i", "查看模板配置说明", "模板配置说明"),
    Q("q", "退出程序"),
    R("r", "重启程序"),
    E("e", "返回主页（成功初始化后允许使用）", "返回主页"),
    S("s", "开始执行转换（成功初始化后允许使用）", "（重新）执行转换"),
    C("c", "查看当前模板中的配置信息（成功初始化后允许使用）", "模板配置信息");

    private String code;
    private String desc;
    private String simpleDesc;

    CommandEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
        this.simpleDesc = desc;
    }

    CommandEnum(String code, String desc, String simpleDesc) {
        this.code = code;
        this.desc = desc;
        this.simpleDesc = simpleDesc;
    }

    public static CommandEnum getCommandByCode(String code) {
        for (CommandEnum command : CommandEnum.values()) {
            if (command.getCode().equals(code)) {
                return command;
            }
        }
        return null;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc;
    }



}
