# X-Trans

## 基本信息

### 什么是X-Trans？

X-Trans是一款基于命令行的Windows应用，提供将Excel文件（.xlsx）转换为文本的方法。用户可通过配置模板来实现读取和填充对应单元格数据。

### 操作系统

X-Trans仅适用于Window操作系统，推荐使用Windows 10及以上操作系统。

## 使用说明

### 流程

X-Trans的使用流程基本如下：

1. 准备好要上传的`xlsx`格式Excel（`xls`格式可能不适用于该应用，请提前进行转换）
2. 配置模板文件
3. 在命令行中输入`s`并回车执行程序
4. 转换完成

### 配置模板文件

模板文件自上而下分为以下几个部分：

1. 参数设置
2. 上文设置
3. 循环模板设置
4. 下文设置

#### 参数设置

参数设置的格式为：key=value，每行仅允许配置1个参数。

可配置参数如下：

| 键               | 键中文名   | 值类型  | 取值要求                 | 必填 | 默认值   | 功能                                            | 备注                   | 样例                         |
| ---------------- | ---------- | ------- | ------------------------ | ---- | -------- | ----------------------------------------------- | ---------------------- | ---------------------------- |
| `startRowIndex`  | 起始行号   | Integer | 大于等于0的整数          | 否   | `1`      | 限制Excel读取数据时的起始行号                   | 行号自0开始计数        | startRowIndex=2              |
| `rowStep`        | 行读取步长 | Integer | 大于等于1的整数          | 否   | `1`      | Excel读取数据时下一读取行到当前读取行之间的步长 |                        | rowStep=2                    |
| `path`           | Excel路径  | String  | 文件夹路径+文件名+后缀名 | 否   | `""`     | 读取的Excel路径                                 | 缺失时需要用户手动输入 | path=D:files\表格.xlsx       |
| `outputFileName` | 输出文件名 | String  | 文件名+后缀名            | 否   | `{}.txt` | Excel转换后的输出文件名                         | 可使用特殊关键字       | outputFileName={}_转换后.txt |

##### 特殊关键字

| 关键字 | 作用域           | 功能                                              | 样例                                                         |
| ------ | ---------------- | ------------------------------------------------- | ------------------------------------------------------------ |
| `{}`   | `outputFileName` | 替换后为程序所读取的Excel的文件名（不包含后缀名） | Excel文件名为：`表格.xlsx`<br/>`outputFileName`设置为`{}_转换后.txt`<br/>最终输出文件名为：`表格_转换后.txt` |

#### 上文设置

上文、循环模板、下文组成了输出文件的全部内容。

上文是输出文件内容的第一部分，在读取Excel中的数据之前写入。

#### 循环模板设置

循环模板是输出文件内容的第二部分，被关键字` ``` `包裹，作用为循环读取Excel各行内容时将Excel对应单元格中的数据替换到模板中，再以依次入输出文件。

循环模板的配置中可使用特殊关键字。

##### 特殊关键字

| 关键字     | 作用域       | 功能                                                         | 样例                                                         |
| ---------- | ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `${列号}$` | 循环模板设置 | 定位Excel中的对应列，在循环读取时从对应单元格中获取数据并进行替换（列号从0计数） | Excel中某行列0为数据：x<br/>循环模板设置为：`获得数据：${0}$`<br/>最终输出时对应片段为：`获得数据：x` |

#### 下文设置

下文是输出文件内容的第三部分，在读取Excel中的数据之后写入。

#### 配置样例

````xml
startRowIndex=1
outputFileName={}.ctd
path=C:\Users\华硕\Desktop\file\工作簿1.xlsx
<?xml version="1.0" encoding="UTF-8"?>
<files>
	<report>
		<testNo>测试</testNo>
		<testName>测试1</testName>
		<testDate>45657.776594</testDate>
		<alarmConsistometer>100.0</alarmConsistometer>
		<experimentalist>主检人</experimentalist>
		<custom1Consistometer>40.0</custom1Consistometer>
		<custom2Consistometer>50.0</custom2Consistometer>
		<custom3Consistometer>60.0</custom3Consistometer>
		<custom4Consistometer>70.0</custom4Consistometer>
		<startTemperature>20.9</startTemperature>
		<startPressure>1.6</startPressure>
		<startConsistometer>5.3</startConsistometer>
		<endTemperature>0.0</endTemperature>
		<endPressure>0.0</endPressure>
		<scStartTime>15.0</scStartTime>
		<scEndTime>30.0</scEndTime>
		<consistometer30BcTime>0.156652</consistometer30BcTime>
		<consistometerTime>0.000000</consistometerTime>
		<custom1ConsistometerTime>0.15</custom1ConsistometerTime>
		<custom2ConsistometerTime>0.16</custom2ConsistometerTime>
		<custom3ConsistometerTime>0.16</custom3ConsistometerTime>
		<custom4ConsistometerTime>0.16</custom4ConsistometerTime>
		<testPF></testPF>
		<testMemo></testMemo>
		<alarmIsRecord>0</alarmIsRecord>
		<thirtyBcIsRecord>1</thirtyBcIsRecord>
		<custom1IsRecord>1</custom1IsRecord>
		<custom2IsRecord>1</custom2IsRecord>
		<custom3IsRecord>1</custom3IsRecord>
		<custom4IsRecord>1</custom4IsRecord>
	</report>
	<datas>
```
		<data time="${30}$">
			<t>${31}$</t>
			<p>${32}$</p>
			<c>${33}$</c>
		</data>
```
	</datas>
</files>
````

### 命令行相关指令

通常的操作为输入字符后回车执行指令。个别非通用的特殊指令操作会在命令行中有明确提示。

| 指令 | 功能                                   |
| ---- | -------------------------------------- |
| `h`  | 查看指令帮助                           |
| `o`  | 打开模板文件（template.txt）所在文件夹 |
| `f`  | 打开模板文件（template.txt）           |
| `p`  | 打开输出文件夹                         |
| `i`  | 查看模板配置说明                       |
| `q`  | 退出程序                               |
| `r`  | 重启程序                               |
| `e`  | 返回主页                               |

请注意，指令区分大小写。
