package ai.engageminds.sdk.test;

import ai.engageminds.sdk.ApacheAsyncHttpSender;
import ai.engageminds.sdk.EmSdk;
import ai.engageminds.sdk.JacksonProvider;
import ai.engageminds.sdk.dto.Event;
import ai.engageminds.sdk.dto.EventRequest;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class TestEmSdk {

    @Test
    public void test() throws InterruptedException {
        EmSdk em = EmSdk.builder()
                .appk("xxxx")
                .httpSender(new ApacheAsyncHttpSender())
                .jsonProvider(new JacksonProvider())
                .build();

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
                .cdid("testCdid1")
                .eid("testE1")
                .ts(System.currentTimeMillis())
                .build()
                .setProp("prop1", "aaaa")
                .setProp("prop2", 1234));

        req.addEvent(Event.builder()
                .cdid("testCdid1")
                .eid("testE2")
                .ts(System.currentTimeMillis())
                .build()
                .setProp("prop3", "bbbb")
                .setProp("prop4", 5678.12));
        CountDownLatch cd = new CountDownLatch(1);
        em.track(req, (res, e) -> {
            if (e != null) {
                e.printStackTrace();
                cd.countDown();
                return;
            }
            System.out.println(JSON.toJSONString(res));
            cd.countDown();
        });
        cd.await();
    }
}

