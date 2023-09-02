# EngageMinds Java SDK 接入概述

## 1. 接入准备
### 获取 AppKey

在 [EngageMinds 平台](https://dash.engageminds.ai) 页面，创建开发者账号，添加应用并获取 AppKey。

## 2. 集成 Java SDK
- ### 添加 pom 引用

```xml
<!-- 添加 em sdk 依赖 -->
<dependency>
    <groupId>ai.engageminds</groupId>
    <artifactId>em-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

- ### 添加 gradle 引用


```gradle
dependencies {
    implementation 'ai.engageminds:em-sdk-java:1.0.0'
}
```

## 3. 定义全局 EmSdk

```java
@Bean
public EmSdk em() {
    return EmSdk.builder()
            .appk("{ReplaceWithYourAppk}")
            .httpSender(new ApacheAsyncHttpSender())
            .jsonProvider(new JacksonProvider())
            .build();
}
```
**注：**

- EmSdk 默认提供了 `ApacheAsyncHttpSender` 和 `ApacheHttpSender` 两种网络请求方式。以及 `FastJsonProvider` 、`FastJson2Provider` 和 `JacksonProvider` 三种 Json 解析方式。您可以选择其中一种来使用，需要同时添加对应的引用。

- 您也可以自行实现 `ai.engageminds.sdk.HttpSender` 和 `ai.engageminds.sdk.JsonProvider` 接口来分别实现其它的网络请求和 Json 解析方式。

#### pom 引用

```xml
<dependencies>
    <!-- 添加 ApacheAsyncHttpSender 依赖 -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpasyncclient</artifactId>
        <version>4.1.5</version>
    </dependency>
    <!-- 添加 ApacheHttpSender 依赖 -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.14</version>
    </dependency>
    <!-- 添加 FastJsonProvider 依赖 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>2.0.39</version>
    </dependency>
    <!-- 添加 FastJson2Provider 依赖 -->
    <dependency>
        <groupId>com.alibaba.fastjson2</groupId>
        <artifactId>fastjson2</artifactId>
        <version>2.0.39</version>
    </dependency>
    <!-- 添加 JacksonProvider 依赖 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
</dependencies>
```

#### gradle 引用

```gradle
dependencies {
    // 添加 ApacheAsyncHttpSender 依赖
    implementation "org.apache.httpcomponents:httpasyncclient:4.1.5"
    // 添加 ApacheHttpSender 依赖
    implementation "org.apache.httpcomponents:httpclient:4.5.14"
    // 添加 FastJsonProvider 依赖
    implementation 'com.alibaba:fastjson:2.0.39'
    // 添加 FastJson2Provider 依赖
    implementation 'com.alibaba.fastjson2:fastjson2:2.0.39'
    // 添加 JacksonProvider 依赖
    implementation "com.fasterxml.jackson.core:jackson-databind:2.15.2"
}
```

## 4. 引用 EmSdk

```java
@Resource
private EmSdk em;
```

## 5. 发送事件

### 5.1 创建事件

```java
Event event = Event.builder()
        // 用户唯一标识
        .cdid("{Your User ID}")
        // 事件 ID
        .eid("track")
        // 设备事件触发时间 ms
        .ts(ts)
        .build()
        // 添加事件属性
        .setProp("prop_str", "str")
        .setProp("prop_num", 1234);
```

### 5.2 定义事件请求参数

```java
EventRequest req = new EventRequest();
// 上报时间
req.setTs(System.currentTimeMillis());
// Android 设备广告标识符
req.setGaid("f5bb825f-a1b8-44f8-a24d-10ca1ad1abe1");
// 设备硬件制造商
req.setMake("Xiaomi");
// 设备型号
req.setModel("MI 9");
// 设备操作系统, 0:iOS,1:Android,2:HarmonyOS,3:Mac,4:Windows,5:Linux
req.setOs(1);
// 设备系统版本
req.setOsv("11");
```

#### EventRequest 属性说明

| 属性名                | 类型              | 说明                                                                                                                                                                                                                                   | 示例                                 | 必选 |
|--------------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| ------------------------------------ | ------- |
| ts                 | long       | 当前时间 ms                                                                                                                                                                                                                              | 1693468388006                | ✔ |
| zo                 | int | 时区偏移量（分钟），如北京时区 time zone = 480                                                                                                                                                                                                      | 480 | ✖︎ |
| tz                 | String | 设备时区                                                                                                                                                                                                                                 | Asia/Shanghai | ✖︎ |
| rid                | String          | 唯一请求 ID                                                                                                                                                                                                                              | 50e24b75-eb4a-486d-9a93-a1fd814e1e96 | ✖︎       |
| atdk               | int            | 当前设备是否开启广告追踪，0 关闭，1开启                                                                                                                                                                                                                | 1                                    | ✖︎       |
| appv               | String      | APP 版本                                                                                                                                                                                                                               | 1.1.0                              | ✖︎       |
| bundle             | String          | APP 包名                                                                                                                                                                                                                               | com.xxx                     | ✖︎       |
| bps | Map | 公共事件属性，如何和事件属性名重复，则以事件属性为准 | "prop" : "value" | ✖︎ |
| brand              | String          | 当前设备品牌                                                                                                                                                                                                                               | Xiaomi | ✖︎       |
| carrier            | String          | 当前设备网络运营商                                                                                                                                                                                                                            | 中国移动    | ✖︎       |
| contype            | int        | 当前设备网络类型<br>0 Unknown <br>1 Ethernet <br>2 WIFI <br>3 Cellular Network – Unknown Generation<br>4 Cellular Network – 2G<br>5 Cellular Network – 3G<br>6 Cellular Network – 4G <br>7 Cellular Network – 5G <br>8 Cellular Network – 6G | 2                  | ✖︎       |
| dtype              | int        | 设备类型<br>0:unknown,1:phone,2:tablet,3:tv                                                                                                                                                                                              | 1                            | ✖︎       |
| fit                | long         | 当前设备 APP 首次安装时间，ms                                                                                                                                                                                                                   | 1693300890                         | ✖︎       |
| flt                | long          | 当前设备 APP 首次打开时间，ms                                                                                                                                                                                                                   | 1693300890                               | ✖︎       |
| gcy         | String        | 设备国家编码                                                                                                                                                                                                                    | cn                                  | ✖︎       |
| gp               | int         | 是否安装 Google Play，0 未安装，1 安装                                                                                                                                                                                  | 1                              | ✖︎       |
| width           | int         | 设备屏幕宽度 px                                                                                                                                                                                                           | 1080                           | ✖︎       |
| heigth        | int       | 设备屏幕高度 px                                                                                                                                                                                                            | 2040 | ✖︎       |
| gaid               | String          | Android 设备广告标识符                                                                                                                                                                                        | f5bb825f-a1b8-44f8-a24d-10ca1ad1abe1 | ✖︎   |
| idfa        | String          | iOS 广告标识符                                                                                                                                                                                          |                                      | ✖︎       |
| idfv          | String          | iOS 供应商标识符                                                                                                                                                                 |                                      | ✖︎       |
| ip      | String          | 设备 IP 地址                                                                                                                                                                         | 1.1.1.1 | ✖︎       |
| jb       | int        | 设备是否越狱，0 正常，1 越狱                                                                                                                                                                 | 0 | ✖︎       |
| lang | String          | 设备语言                                                                                                                                                                                  | zh | ✖︎       |
| make | String          | 设备硬件制造商                                                                                                                                                   | Xiaomi | ✖︎       |
| mode          | String          | 设备型号                                                                                                                                                                     | MI 11 | ✖︎       |
| mccmnc      | String          | "移动国家代码" + "移动网络代码"                                                                                                                                          | 4601 | ✖︎       |
| ntf            | int        | 当前设备 APP 是否开启通知权限，0 未开启，1 开启                                                                                                                                          |                                      | ✖︎       |
| oaid   | String          | 设备匿名标识符                                                                                                                                                                        |                                      | ✖︎       |
| os    | int        | 设备操作系统<br>0:iOS,1:Android,2:HarmonyOS,<br>3:Mac,4:Windows,5:Linux                                                                                                          |                                      | ✖︎       |
| sco | int        | 设备屏幕方向<br> 0:unknown,1:portrait,2:landscape                                                                                                                                      |                                      | ✖︎       |


### 5.3 发送事件

```java
// 可同时发送多个事件
req.addEvent(event);
em.track(req, (res, e) -> {
    if (e != null) {
        e.printStackTrace();
        return;
    }
    System.out.println(JSON.toJSONString(res));
});
```
