### Port Scan  端口扫描工具
-  **Author:brok1n** 
- **Email:brok1n@outlook.com** 
- **Version: 1.0.0** 
- **Date:2017/09/03** 



声明: 本工具只用于本人于工作上做检测公司项目服务器在线状态、服务器服务平台端口是否正常、编程技术学习使用。禁止除本人以外的任何个人或公司用本工具做任何其他非法用途

由于公司处于各省服务器时不时的会出现连接不上的问题  同时由不同开发人员 维护人员维护的服务器 加之各种关系  还是自己写一个工具来检测服务器状态方便快捷。

端口扫描工具 PortScan 主入口位于  portScan/PortScan/src/main/java/com/brok1n/java/portscan/PortScan.java

基本用法可以使用 PortScan ?|--help|help|man 查看工具帮助信息

```
PortScan.jar -i ip -p port [-t timeout] [-th thread number]
Example: PortScan.jar -i 192.168.1.105 -p 80:3389 -t 100 -th 500
-i
    ip  one or more ip address
    example: one ip
        -i 192.168.1.100
    example: more ip split with ','
        -i 192.168.1.100,192.168.1.135,192.168.1.188
    example: ip section split with ':'
        -i 192.168.1.2:192.168.1.254
-p
    port one or more port
    example: one port
        -p 80
    example: more port split with','
        -p 21,22,25,135,443,445,3306,3389
    example: port section split with ':'
        -p 20:30000
-t
    timeout default 300
-th
    processing thread number default 300

```

1.0.0版本共有几个参数
    -i IP地址   
        -i参数后跟IP地址  ip地址可以是一个、多个、区间、区间加单个或者多个的混合组合模式
        单个IP就直接写IP地址 如: -i 192.168.1.100 
        多个IP用逗号','分隔 如: -i 192.168.1.100,192.168.1.105,192.168.1.50
        IP区间用冒号':'分隔 如 -i 192.168.1.100:192.168.1.200  前一个是起始IP后一个是终止IP
    
        公司在湖南 湖北 天津 重庆 新疆等地的服务器 有连续的和不连续的地址 举例如下 扫描时 就可以这样配置
            PortScan.jar -i 221.196.8.6,221.196.9.144,49.112.120.18:49.112.120.22
    
    -p 端口号
        -p参数后跟要扫描的端口号 端口号是从0~65535范围的数字 
        多个端口同IP参数一样 用逗号','分隔多个ip 用冒号':'分隔端口区间

    -t 端口扫描超时时间 (可选)
        -t参数跟IP的链接超时时间。这个数字默认是300 
        链接超时时间可以自己ping目标地址得出来的平均延迟时间+100就可以作为扫描时的超时时间

    -th 线程数(可选)
        -th参数后跟扫描启动的线程数 默认是300个线程 可选

