# EngageMinds Java SDK

1. 添加 pom 引用

```xml
<!-- 添加 em sdk 依赖 -->
<dependency>
    <groupId>ai.engageminds</groupId>
    <artifactId>em-sdk-java</artifactId>
    <version>1.0</version>
</dependency>
```

2. 定义全局 bean

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

3. 引用 bean

```java
@Resource
private EmSdk em;
```

4. 发送事件

```java
EventRequest req = new EventRequest();
req.setTs(System.currentTimeMillis());
req.setGaid("54287c36-dbd4-4e10-8b49-30541c517113");
req.setMake("Huawei");
req.setBrand("Huawei");
req.setModel("mate60 pro");
req.setOs(1);
req.setOsv("12");
req.setBundle("com.test.game");
req.setIp("1.1.1.1");
req.addEvent(Event.builder()
        .cdid("testCdid")
        .eid("testE1")
        .ts(System.currentTimeMillis())
        .build()
        .setProp("prop1", "aaaa")
        .setProp("prop2", 1234));

req.addEvent(Event.builder()
        .cdid("testCdid")
        .eid("testE2")
        .ts(System.currentTimeMillis())
        .build()
        .setProp("prop3", "bbbb")
        .setProp("prop4", 5678.12));
em.track(req, (res, e) -> {
    if (e != null) {
        log.error("trackErr", e);
        return;
    }
    log.debug(res);
});
```
