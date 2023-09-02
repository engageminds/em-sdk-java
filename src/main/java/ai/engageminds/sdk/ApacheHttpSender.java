package ai.engageminds.sdk;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.function.BiConsumer;

/**
 * default implement of HttpSender with ApacheHttpClient
 */
public class ApacheHttpSender implements HttpSender {

    private final HttpClient httpClient;
    private final RequestConfig reqCfg;

    public ApacheHttpSender() {
        this(HttpClients.custom()
                .setMaxConnPerRoute(1000)
                .setMaxConnTotal(10000)
                .build());
    }

    public ApacheHttpSender(HttpClient httpClient) {
        this(httpClient, null);
    }

    public ApacheHttpSender(HttpClient httpClient, RequestConfig reqCfg) {
        this.httpClient = httpClient;
        if (reqCfg != null) {
            this.reqCfg = reqCfg;
            return;
        }

        this.reqCfg = RequestConfig.custom()
                .setRedirectsEnabled(false)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setSocketTimeout(5000)
                .build();
    }

    @Override
    public void send(Request req, BiConsumer<Response, Exception> cb) {
        HttpPost post = new HttpPost(req.getUrl());
        post.setConfig(reqCfg);
        post.setHeader("User-Agent", UA);
        if (req.getCompress() != null) {
            post.setHeader("Content-Encoding", req.getCompress());
        }
        post.setEntity(new ByteArrayEntity(req.getBody(), ContentType.APPLICATION_JSON));

        HttpEntity resEntity = null;
        Response rv = new Response();
        try {
            HttpResponse res = httpClient.execute(post);
            StatusLine sl = res.getStatusLine();
            resEntity = res.getEntity();
            rv.setStatusCode(sl.getStatusCode());
            rv.setReasonPhrase(sl.getReasonPhrase());
            Headers hs = new Headers();
            for (Header h : res.getAllHeaders()) {
                hs.set(h.getName(), h.getValue());
            }
            rv.setHeaders(hs);
            rv.setBody(EntityUtils.toString(resEntity));
            cb.accept(rv, null);
        } catch (Exception e) {
            cb.accept(null, e);
        } finally {
            EntityUtils.consumeQuietly(resEntity);
        }
    }
}
