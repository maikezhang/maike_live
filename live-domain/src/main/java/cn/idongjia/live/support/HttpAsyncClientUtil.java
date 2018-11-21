package cn.idongjia.live.support;

import cn.idongjia.live.exception.LiveException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

/**
 * @Author: junlou.liu
 * @Description: http请求工厂类
 * @Date: Created in 2017/10/7 下午5:09
 * @Modified: 异步的HTTP请求对象，可设置代理
 */
public class HttpAsyncClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpAsyncClientUtil.class);
    // 请求连接超时时间
    private final static int CONNECT_TIME_OUT = 1000;
    private final static int CONNECT_REQUEST_OUT = 1000;
    // 请求读取超时时间
    private final static int READ_OUT_TIME = 1000;

    private final static int DEFAULT_POOL_SIZE = 500;

    // 异步httpclient
    private CloseableHttpAsyncClient asyncHttpClient;
    private RequestConfig requestConfig;

    private HttpAsyncClientUtil(){ this.initHttpAsyncClient(); }

    private static class HttpAsyncClientHolder{
        private static HttpAsyncClientUtil instance = new HttpAsyncClientUtil();
    }
    public static HttpAsyncClientUtil getInstance(){
        return HttpAsyncClientHolder.instance;
    }

    // 初始化异步请求工厂类
    private void initHttpAsyncClient(){
        try {
            // http 代理相关参数
//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
//                    username, password);
//            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
            // 配置请求设置
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECT_TIME_OUT)                // 设置连接超时
                    .setConnectionRequestTimeout(CONNECT_REQUEST_OUT)   // 设置请求等待超时
                    .setSocketTimeout(READ_OUT_TIME)                    // 请求返回超时
                    .setCircularRedirectsAllowed(false)
                    .build();
            SSLContext sslContext = SSLContexts.createDefault();
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE)
                    .register("https", new SSLIOSessionStrategy(sslContext))
                    .build();
            // 配置io线程
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                    .setIoThreadCount(Runtime.getRuntime().availableProcessors()).build();
            // 设置连接池大小
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            PoolingNHttpClientConnectionManager nConManager = new PoolingNHttpClientConnectionManager(ioReactor, sessionStrategyRegistry);
            nConManager.setMaxTotal(DEFAULT_POOL_SIZE);
            nConManager.setDefaultMaxPerRoute(50);

            //
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    .setCharset(Charset.defaultCharset())
                    .build();

            Lookup<AuthSchemeProvider> authSchemeProvider = RegistryBuilder.<AuthSchemeProvider>create()
                    .register(AuthSchemes.BASIC, new BasicSchemeFactory())
                    .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
                    .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
                    .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
                    .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
                    .build();
            nConManager.setDefaultConnectionConfig(connectionConfig);

            asyncHttpClient = HttpAsyncClients.custom()
                    .setConnectionManager(nConManager)
//                    .setDefaultCredentialsProvider()  // 代理设置
//                    .setProxy(new HttpHost(host, port))
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setDefaultAuthSchemeRegistry(authSchemeProvider)
                    .build();
            asyncHttpClient.start();
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
    }

    /**
     * get请求
     * @param url
     * @return
     */
    public HttpResult get(String url) throws ExecutionException, InterruptedException, IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        Future<HttpResponse> responseFuture = asyncHttpClient.execute(httpGet, null);
        HttpResponse response = responseFuture.get();

        String content;
        Header header = response.getEntity().getContentEncoding();
        if (header != null && StringUtils.equalsIgnoreCase(response.getEntity().getContentEncoding().getValue(), "gzip")) {
            content = IOUtils.toString(new GZIPInputStream(response.getEntity().getContent()), "UTF-8");
        } else {
            content = EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        HttpResult result = null;
        int code = response.getStatusLine().getStatusCode();
        if (code == 200){
            result = new HttpResult(content);
        } else {
            result = new HttpResult(code, content);
        }
        return result;
    }

    /**
     * 异步请求第三方服务方法
     * @param url
     * @param headers
     * @param body
     * @param entityType
     * @return
     */
    public HttpResult post(String url, Map<String, String> headers, Object body, EntityType entityType) throws ExecutionException, InterruptedException, IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (headers != null) {
            for (Map.Entry entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        switch (entityType){
            case URL_ENCODED_FORM_ENTITY:
                List<NameValuePair> pairs = new ArrayList<>();
                if (body != null){
                    if (body instanceof HashMap){
                        for (Map.Entry entry : ((HashMap<String,String>) body).entrySet()){
                            pairs.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(pairs, Charset.defaultCharset()));
                    }
                }
                break;
            case STRING_ENTITY:
                if (body != null) {
                    HttpEntity stringEntity = new StringEntity(body.toString(), Charset.defaultCharset());
                    httpPost.setEntity(stringEntity);
                }
                break;
            case BYTE_ARRAY_ENTITY:
                ByteArrayEntity entity = new ByteArrayEntity(body.toString().getBytes());
                httpPost.setEntity(entity);
                break;
            default:
                logger.error("暂不支持");
                throw new LiveException("the type you request not support");
        }
        Future<HttpResponse> responseFuture = asyncHttpClient.execute(httpPost, null);
        HttpResponse response = responseFuture.get();

        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
        int code = response.getStatusLine().getStatusCode();
        HttpResult result = null;
        if (code == 200){
            result = new HttpResult(content);
        } else {
            result = new HttpResult(code, content);
        }
        return result;
    }

    /**
     * 请求体类型
     */
    public enum  EntityType {
        BASIC_HTTP_ENTITY,BUFFERED_HTTP_ENTITY,BYTE_ARRAY_ENTITY,ENTITY_TEMPLATE,FILE_ENTITY,
        HTTP_ENTITY_WRAPPER,INPUT_STREAM_ENTITY,SERIALIZABLE_ENTITY,STRING_ENTITY,
        URL_ENCODED_FORM_ENTITY
    }

    public class HttpResult implements Serializable{
        private int code;
        private String msg;
        private Object data;

        public HttpResult(){super();}
        public HttpResult(int code, Object data){
            this.code = code;
            this.data = data;
        }
        public HttpResult(Object data){
            this.code = 200;
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
