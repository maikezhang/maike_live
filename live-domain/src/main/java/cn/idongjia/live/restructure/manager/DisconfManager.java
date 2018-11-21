package cn.idongjia.live.restructure.manager;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.support.HttpUtil;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DisconfManager {

    @Resource
    private ConfigManager configManager;

    private static final Log LOGGER = LogFactory.getLog(DisconfManager.class);

    private static final HashMap<String, Long> CONFIG_ID = new HashMap<>();

    /**
     * 向Disconf中增加配置
     * @param uidStr uid字符串，以逗号分隔
     * @param configName 配置名字
     * @return 是否成功
     */
    public boolean addConfig(String uidStr, String keyName, String configName){
        String signUrl = "http://" + configManager.getDisconfAddr() + "/api/account/signin";
        //首先连接到disconf
        if (logOnDisconf(signUrl, configManager.getDisconfUser(), configManager.getDisconfPassWord())){
            //修改disconf文件
            String value = configName + "=" + uidStr;
            //表单
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("value", value));
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HttpUtil.UTF_8);
                String putUrl = "http://" + configManager.getDisconfAddr() + "/api/web/config/item/"
                        + getConfigIdByName(keyName);
                HttpPut httpPut = new HttpPut(putUrl);
                httpPut.setEntity(entity);
                String result = HttpUtil.sendPut(httpPut);
                Gson gson = new Gson();
                Map reMap = gson.fromJson(result, Map.class);
                String re = reMap.get("success").toString();
                return re.equals("true");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("不支持字符编码异常" + e.getMessage());
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 登录到disconf服务器
     * @param url disconf链接
     * @param username 用户名
     * @param password 用户密码
     * @return 是否成功
     */
    private boolean logOnDisconf(String url, String username, String password){
        HttpPost httpPost = new HttpPost(url);
        //使用表单参数
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("remember", "1"));
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HttpUtil.UTF_8);
            httpPost.setEntity(entity);
            String result = HttpUtil.sendPost(httpPost);
            Gson gson = new Gson();
            Map reMap = gson.fromJson(result, Map.class);
            String re = reMap.get("success").toString();
            return re.equals("true");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }


    Long getConfigIdByName(String configName) {
        if (CONFIG_ID.containsKey(configName)){
            return CONFIG_ID.get(configName);
        }else {
            String signUrl = "http://" + configManager.getDisconfAddr() + "/api/account/signin";
            //首先连接到disconf
            if (logOnDisconf(signUrl, configManager.getDisconfUser(), configManager.getDisconfPassWord())) {
                Long appId = null;
                Long envId = null;
                Long configId = null;
                Gson gson = new Gson();
                //首先获取appId
                String appUrl = "http://" + configManager.getDisconfAddr() + "/api/app/list";
                String appRes = HttpUtil.sendGet(appUrl);
                Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
                Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                Map<String, Object> appReMap = gson.fromJson(appRes, mapType);
                appReMap = gson.fromJson(appReMap.get("page").toString(), mapType);
                List<Map<String, Object>> appList = gson.fromJson(appReMap.get("result").toString(), listType);
                for (Map<String, Object> app : appList) {
                    if (app.get("name").equals(configManager.getDisconfApp())) {
                        appId = new Double(app.get("id").toString()).longValue();
                        break;
                    }
                }
                //再获取envId
                String envUrl = "http://" + configManager.getDisconfAddr() + "/api/env/list";
                String envRes = HttpUtil.sendGet(envUrl);
                Map<String, Object> envMap = gson.fromJson(envRes, mapType);
                envMap = gson.fromJson(envMap.get("page").toString(), mapType);
                List<Map<String, Object>> envList = gson.fromJson(envMap.get("result").toString(), listType);
                for (Map<String, Object> env : envList) {
                    if (env.get("name").equals(configManager.getDisconfEnv())) {
                        envId = new Double(env.get("id").toString()).longValue();
                        break;
                    }
                }
                //再再获取configId
                String configUrl = "http://" + configManager.getDisconfAddr() + "/api/web/config/simple/list";
                configUrl = configUrl + "?appId=" + appId + "&envId=" + envId + "&version=" + configManager.getDisconfVersion();
                String configRes = HttpUtil.sendGet(configUrl);
                Map<String, Object> configMap = gson.fromJson(configRes, mapType);
                configMap = gson.fromJson(configMap.get("page").toString()
                        .replace("value=,", ""), mapType);
                List<Map<String, Object>> configList = gson.fromJson(configMap.get("result")
                        .toString(), listType);
                for (Map<String, Object> config : configList) {
                    if (config.get("key").equals(configName)) {
                        configId = new Double(config.get("configId").toString()).longValue();
                    }
                }
                CONFIG_ID.put(configName, configId);
                return configId;
            }else{
                throw new LiveException(-12138, "无法登录disconf");
            }
        }
    }
}
