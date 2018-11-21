package cn.idongjia.live.support;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * httpClient相关静态方法
 */
public final class HttpUtil {

    private HttpUtil(){}

    private static final Log LOGGER = LogFactory.getLog(HttpUtil.class);

    private static PoolingHttpClientConnectionManager ccm = new PoolingHttpClientConnectionManager();
    private static RequestConfig defaultRequestConfig=RequestConfig.custom()
            .setConnectTimeout(1000)
            .setSocketTimeout(1000)
            .setConnectionRequestTimeout(1000)
            .build();
    private static CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(ccm)
            .build();

    public static final String JSON_TYPE = "application/json;charset=utf-8";
    public static final String UTF_8 = "UTF-8";
    private static final int SUCCESS_CODE = 200;

    /**
     * 发送POST请求
     * @param httpPost post http请求
     * @return 请求的结果
     */
    public static String sendPost(HttpPost httpPost) {
        LOGGER.info("[发送POST请求] httPost: " + httpPost);
        HttpClientContext context = HttpClientContext.create();
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpPost, context);
            int statusCode = response.getStatusLine().getStatusCode();
            String httpResult = EntityUtils.toString(response.getEntity(), UTF_8);
            if (statusCode != SUCCESS_CODE){
                LOGGER.warn("[发送POST请求失败] statusCode: " + statusCode + " 失败内容: " + httpResult);
                throw new LiveException(-12138, "POST请求失败!");
            }
            LOGGER.info("[发送POST请求] httpPost: " + httpPost + "[接收结果] httpResult：" + httpResult);
            return httpResult;

        } catch (IOException e) {
            LOGGER.warn("发送post请求失败{}",e);
            return null;
        }
    }

    /**
     * 发送GET请求
     * @param url get 请求链接
     * @return 请求结果
     */
    public static String sendGet(String url) {
        LOGGER.info("[发送GET请求] url: " + url);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response;
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(ccm)
                .build();
        try{
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != SUCCESS_CODE){
                LOGGER.warn("[发送GET请求失败] url :{},statusCode: {}",url , statusCode);
                throw new LiveException(-12138, "GET请求失败");
            }
            String httpResult = EntityUtils.toString(response.getEntity(), UTF_8);
            LOGGER.info("[发送GET请求] url: {},[接收结果] :{}" ,url  ,httpResult);
            return httpResult;
        }catch (Exception e){
            LOGGER.warn("发送get请求失败 url:{}, Exception: {}",url,e);
            return "";
        }
    }

    /**
     * 发送PUT请求
     * @param httpPut put http请求
     * @return put请求结果
     */
    public static String sendPut(HttpPut httpPut){
        LOGGER.info("[发送PUT请求] url: " + httpPut.getURI());
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != SUCCESS_CODE){
                LOGGER.warn("[发送PUT请求] statusCode: " + statusCode);
                throw new LiveException(-12138, "PUT请求失败");
            }
            String httpResult = EntityUtils.toString(response.getEntity(), UTF_8);
            LOGGER.info("[发送PUT请求] url: " + httpPut.getURI() + " [接收结果]" + httpResult);
            return httpResult;
        } catch (IOException e) {
            LOGGER.warn("发送put请求失败{}",e);
            return "";
        }
    }
}
