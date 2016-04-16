1、运行
java -jar Collector.jar

2、config.txt
配置文件，包括：
type=                   采集类型
url=			urls，可配多个，用|分隔
filter=			过滤条件，可配多个，用|分隔
interval=		设置间隔时间

这四个可配多个，根据间隔时间的不同，用名字+数字即可（如type0/url0/filter0/interval0, type1/url1/filter1/interval1），可参考config.txt现在的配置。

3、log目录下是日志文件，debug.log给开发人员用，error.log给使用人看

4、type0-0-2015-05-22-13-52-37.csv是测试的采集文件，根据配置会有多个

在127.0.0.1配置的测试网页可以工作，你先看看，我下午有课，后面细聊。