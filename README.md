# Pachila IoT Inbound

<a href="http://www.pachila.cn"><img src="https://github.com/pachila-org/pachila-iot-mobile/blob/master/www/images/icon.png" align="left" hspace="4" vspace="4"></a>

Pachila-iot-Inbound是基于mosquito(mqtt的broker)开发的处理mqtt报文的应用程序。该程序能够将设备收集到的温度湿度以及其他各种信息上传到pachila-iot-paas平台，也能把平台转发过来的控制指令以及其他信息发送到设备。该程序支持基于ESP8266wifi模组的灯泡，按钮，插座与温度传感器的演示设备。使用ESP8266wifi模组与inbound应用程序，再配合pachila mobile/PAAS/fireware的demo环境可以在很短的时间里面(甚至不到1个小时)搭建出手机APP通过云平台远程监视控制家电设备的演示环境。

pachila-iot-inbound应用程序require MQTT3.1,Mosquito1.4, jDK1.7+, Tomcat7, centos6.5.

##Quick Start
* 在centos上安装JDK
* 安装Tomcat7
* 安装mosquito
* 发布pachila-iot-inbound到tomcat服务器上
* 启动mosquito 服务器
* 启动pachila-iot-inbound应用程序

## Supports

* [Homepage](http://www.pachila.cn)
* [Community](http://www.pachila.cn/)
* [Mailing List](sicon@pachila.cn)
* [Issues](https://github.com/pachila-org/pachila-iot-fireware/issues)

##Contributors

* [@Pachilatopgun](https://github.com/pachilatopgun)
* [@sicon](https://github.com/sicon)
* [@boboking](https://github.com/boboking)
* [@howard](https://github.com/howard)
* [@microlyu](https://github.com/microlyu)
* [@ryle](https://github.com/ryle)

##Author

[Pachila org](https://github.com/pachila-org)

## License

Apache License Version 2.0
