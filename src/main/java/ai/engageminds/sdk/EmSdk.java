package ai.engageminds.sdk;

import ai.engageminds.sdk.dto.EventRequest;
import ai.engageminds.sdk.dto.EventResponse;
import lombok.Builder;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.zip.GZIPOutputStream;

@Getter
@Builder
public class EmSdk {
    public static final String VERSION = "1.0";
    private static final String defaultServerUrl = "https://a.engageminds.ai";

    private String serverUrl;
    private String appk; // s2s上报app key
    private HttpSender httpSender;
    private JsonProvider jsonProvider;

    /**
     * 异步单条上报
     *
     * @param event 事件对象
     */
    public void track(EventRequest event) {
        track(event, (r, e) -> {
        });
    }

    /**
     * 异步单条上报
     *
     * @param event 事件对象
     */
    public void track(EventRequest event, BiConsumer<EventResponse, Exception> cb) {
        if (event.getAppk() == null) {
            event.setAppk(appk);
        }
        trackBatch(Collections.singletonList(event), cb);
    }

    /**
     * 异步批量上报
     *
     * @param events 事件对象集合
     */
    public void trackBatch(List<EventRequest> events) {
        trackBatch(events, (r, e) -> {
        });
    }

    /**
     * 异步批量上报
     *
     * @param events 事件对象集合
     */
    public void trackBatch(List<EventRequest> events, BiConsumer<EventResponse, Exception> cb) {
        if (events == null || events.isEmpty()) {
            cb.accept(null, null);
            return;
        }
        for (EventRequest req : events) {
            if (req.getAppk() == null) {
                req.setAppk(appk);
            }
        }

        if (serverUrl == null || serverUrl.isEmpty()) {
            serverUrl = defaultServerUrl;
        }

        HttpSender.Request req = new HttpSender.Request();
        req.setUrl(serverUrl + "/s2s/es");
        try {
            byte[] ori = jsonProvider.format(events);
            ByteArrayOutputStream buf = new ByteArrayOutputStream((int) (ori.length * 0.8));
            GZIPOutputStream out = new GZIPOutputStream(buf);
            out.write(ori);
            out.close();
            byte[] data = buf.toByteArray();
            req.setBody(data);
            req.setCompress("gzip");
        } catch (Exception e) {
            cb.accept(null, e);
            return;
        }
        httpSender.send(req, (res, e) -> {
            if (e != null) {
                cb.accept(null, e);
                return;
            }
            String ct = res.getHeaders().get("content-type");
            if (res.getStatusCode() != 200 || (ct == null || !ct.startsWith("application/json"))) {
                EventResponse rv = new EventResponse();
                rv.setCode(res.getStatusCode());
                rv.setMsg(res.getBody());
                cb.accept(rv, null);
                return;
            }
            try {
                cb.accept(jsonProvider.parse(res.getBody().getBytes(StandardCharsets.UTF_8), EventResponse.class), null);
            } catch (Exception ex) {
                cb.accept(null, ex);
            }
        });
    }
}
