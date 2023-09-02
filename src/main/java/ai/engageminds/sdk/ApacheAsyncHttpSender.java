package ai.engageminds.sdk;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.util.EntityUtils;

import java.util.function.BiConsumer;

/**
 * default implement of HttpSender with ApacheAsyncHttpClient
 */
public class ApacheAsyncHttpSender implements HttpSender {

    private final HttpAsyncClient httpClient;
    private final RequestConfig reqCfg;

    public ApacheAsyncHttpSender() {
        this(HttpAsyncClients.custom()
                .setMaxConnPerRoute(1000)
                .setMaxConnTotal(10000)
                .build());
    }

    public ApacheAsyncHttpSender(CloseableHttpAsyncClient httpClient) {
        this(httpClient, null);
    }

    public ApacheAsyncHttpSender(CloseableHttpAsyncClient httpClient, RequestConfig reqCfg) {
        this.httpClient = httpClient;
        if (!httpClient.isRunning()) {
            httpClient.start();
        }
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

        httpClient.execute(post, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse res) {
                StatusLine sl = res.getStatusLine();
                HttpEntity resEntity = res.getEntity();
                try {
                    Response rv = new Response();
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

            @Override
            public void failed(Exception e) {
                cb.accept(null, e);
            }

            @Override
            public void cancelled() {
                cb.accept(null, new RuntimeException("cancelled"));
            }
        });

    }
}
