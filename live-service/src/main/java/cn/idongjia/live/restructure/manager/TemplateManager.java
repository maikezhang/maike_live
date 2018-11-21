package cn.idongjia.live.restructure.manager;

import cn.idongjia.activity.lib.pojo.Activity;
import cn.idongjia.activity.lib.service.ActivityService;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.cache.pojo.TemplateCache;
import cn.idongjia.live.support.GsonUtil;
import cn.idongjia.live.support.HttpAsyncClientUtil;
import cn.idongjia.live.support.HttpUtil;
import cn.idongjia.live.support.response.TemplateResponse;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lts.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;

@Component
public class TemplateManager {

    @Resource
    private ActivityService activityService;

    @Resource
    private ConfigManager configManager;


    @Resource
    private TemplateCache templateCache;

    private static final Log LOGGER = LogFactory.getLog(TemplateManager.class);

    /**
     * 创建超级模版
     *
     * @param data  超级模版序列化字段
     * @param title 活动标题
     * @return 超级模版ID
     */
    public Long addTemplate(String data, String title) {

        String templateUrl = configManager.getSaveTemplateUrl();
        HttpPost httpPost = new HttpPost(templateUrl);
        Long templateId;
        try {
//            Activity activity = new Activity();
//            activity.setTitle(title);
//            activity.setType(6);
//            activity.setParentId(0L);
//            activity.setData(data);


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("type", 6);
            jsonObject.put("data", data);


            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HttpUtil.UTF_8);
            stringEntity.setContentType(HttpUtil.JSON_TYPE);
            httpPost.setEntity(stringEntity);
            LOGGER.info("匠人推流端调用前端保存模板参数:param:{},stringEntity:{}", cn.idongjia.util.GsonUtil.GSON.toJson(jsonObject),stringEntity.toString());
            String result = HttpUtil.sendPost(httpPost);

            Gson gson = new Gson();

            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> reMap = gson.fromJson(result, mapType);
            Map<String, Object> reData = gson.fromJson(reMap.get("data").toString(), mapType);
            if (Objects.isNull(reData)) {
                throw new LiveException(-12138, "调用前端保存模板服务失败");
            }
            templateId = new Double(reData.get("atid").toString()).longValue();


//            templateId = activityService.persist(activity);
        } catch (Exception e) {
            LOGGER.warn("调用前端保存模板服务失败,{}",e);
            throw new LiveException(-12138, "调用前端保存模板服务失败");
        }
        if (templateId == null) {
            LOGGER.warn("调用前端保存模板服务失败,返回的页面id为空");
            throw new LiveException(-12138, "调用前端保存模板服务失败");
        }
        return templateId;
    }


    /**
     * 修改超级模版
     *
     * @param templateId 超级模版ID
     * @param data       超级模版序列化数据
     * @return 是否成功
     */
    public void modifyTemplate(Long templateId, String data) {
        String templateUrl = configManager.getSaveTemplateUrl();
        HttpPost httpPost = new HttpPost(templateUrl);
        try {

            Activity activity = new Activity();
            activity.setId(templateId);
            activity.setData(data);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atid", templateId);
            jsonObject.put("type", 6);
            jsonObject.put("data", data);
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HttpUtil.UTF_8);
            stringEntity.setContentType(HttpUtil.JSON_TYPE);
            httpPost.setEntity(stringEntity);
            LOGGER.info("匠人推流端调用前端保存模板参数:{}", cn.idongjia.util.GsonUtil.GSON.toJson(jsonObject));
            String result = HttpUtil.sendPost(httpPost);
            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Gson gson = new Gson();
            Map<String, Object> reMap = gson.fromJson(result, mapType);
            String re = reMap.get("msg").toString();
            if (!"成功".equals(re)) {
                LOGGER.warn("调用前端保存模板服务失败");
                throw new LiveException(-12138, "调用前端保存模板服务失败");
            }
            Boolean result1 = templateCache.vanishRedis(templateId);
            if (result1) {
                LOGGER.info("超级模板删除缓存成功,templateId:{}", templateId);
            }

//            activityService.update(templateId, activity);
        } catch (Exception e) {
            LOGGER.warn("调用前端保存模板服务失败" + e);
            throw new LiveException(-12138, "调用前端保存模板服务失败");
        }
    }

    /**
     * 根据模版ID查询模版序列化内容
     *
     * @param templateId 超级模版ID
     * @return 超级模版序列化内容
     */
    public String acquireTemplate(Long templateId) {

        Optional<String> datacache = templateCache.takeRedis(templateId);
        if (datacache.isPresent()) {
            return datacache.get();
        }

        String getTemplateDataUrl = configManager.getGetTemplateDataUrl();
        getTemplateDataUrl = getTemplateDataUrl + "?id=" + templateId;
        String data = null;
//        Activity activity;
        try {
            Gson gson = new Gson();
            String result = (String) HttpAsyncClientUtil.getInstance().get(getTemplateDataUrl).getData();
//            String result = HttpUtil.sendGet(getTemplateDataUrl);
//            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
//            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            TemplateResponse response = gson.fromJson(result, TemplateResponse.class);
            if (response.getCode() == 200) {
                data = response.getRes().get(0).getData();
            }

//            activity = activityService.get(templateId);
        } catch (Exception e) {
            LOGGER.warn("调用前端获取模板服务失败" + e);
            throw new LiveException(-12138, "调用前端获取模板服务失败");
        }
        if (data == null) {
            throw new LiveException(-12138, "调用前端获取模板数据为空");
        }
        Boolean result = templateCache.putRedis(templateId, data);
        if (result) {
            LOGGER.info("超级模办添加缓存成功，templateId:{}", templateId);
        }
        return data;
    }

    /**
     * 创建推流端超级模版
     *
     * @param templateJsonStr 超级模版json字符串
     * @param title           直播标题
     * @return 超级模版ID
     */
    public Long addAppTemplate(String templateJsonStr, String title) {
        String templateUrl = configManager.getAppInvokeTemplateUrl();
        HttpPost httpPost = new HttpPost(templateUrl);
        Long templateId;
        try {


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", templateJsonStr);
            jsonObject.put("title", title);

            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HttpUtil.UTF_8);
            stringEntity.setContentType(HttpUtil.JSON_TYPE);
            httpPost.setEntity(stringEntity);
            LOGGER.info("匠人推流端调用前端保存模板参数:{},", cn.idongjia.util.GsonUtil.GSON.toJson(jsonObject));
            String result = HttpUtil.sendPost(httpPost);


            Gson gson = new Gson();

            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> reMap = gson.fromJson(result, mapType);
            Map<String, Object> reData = gson.fromJson(reMap.get("res").toString(), mapType);
            if (Objects.isNull(reData)) {
                throw new LiveException(-12138, "匠人推流端调用前端保存模板服务失败");
            }
            templateId = new Double(reData.get("atid").toString()).longValue();

        } catch (Exception e) {
            LOGGER.warn("匠人推流端调用前端保存模板服务失败:{}", e);
            throw new LiveException(-12138, "调用前端保存模板服务失败");
        }
        if (templateId == null) {
            LOGGER.warn("匠人推流端调用前端保存模板服务失败,返回的页面id为空");
            throw new LiveException(-12138, "匠人推流端调用前端保存模板服务失败");
        }
        return templateId;
    }

    /**
     * 匠人推流端根据模版ID查询模版序列化内容
     * 获取详情的方法麻烦你仔细看下
     *
     * @param templateId 超级模版ID
     * @return 图文详情列表
     */
    public String appQueryTemplate(Long templateId) {
        String getTemplateDataUrl = configManager.getGetAppTemplateUrl();
        getTemplateDataUrl = getTemplateDataUrl + "?atid=" + templateId;
        String templateJsonStr;
        try {
            Gson gson = new Gson();
            String result= (String) HttpAsyncClientUtil.getInstance().get(getTemplateDataUrl).getData();
//            String result = HttpUtil.sendGet(getTemplateDataUrl);
            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> reMap=new HashMap<>();
            try {
                reMap = gson.fromJson(result, mapType);
            }catch (JsonSyntaxException e){
                LOGGER.info("data:{} exception:{}",reMap.toString(),e);
                if(e.toString().contains("Unterminated object at line")){
                    reMap=gson.fromJson(gson.toJson(result),mapType);
                }
            }


            templateJsonStr = (String) reMap.get("res");



        } catch (Exception e) {
            LOGGER.warn("匠人推流端调用前端获取模板服务失败:{}",e);
            throw new LiveException(-12138, "匠人推流端调用前端获取模板服务失败");
        }
        if (templateJsonStr == null) {
            throw new LiveException(-12138, "匠人推流端调用前端获取模板数据为空");
        }
        return templateJsonStr;
    }

    /**
     * 匠人推流端，更新超级模板内容
     *
     * @param templateId      超级模版ID
     * @param templateJsonStr 文图详情列表
     */
    public void modifyAppTemplate(Long templateId, String templateJsonStr) {
        String templateUrl = configManager.getAppInvokeTemplateUrl();
        HttpPost httpPost = new HttpPost(templateUrl);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atid", templateId);
            jsonObject.put("data", templateJsonStr);
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HttpUtil.UTF_8);
            stringEntity.setContentType(HttpUtil.JSON_TYPE);
            httpPost.setEntity(stringEntity);
            LOGGER.info("匠人推流端调用前端保存模板参数:{}", stringEntity.toString());
            String result = HttpUtil.sendPost(httpPost);
            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Gson gson = new Gson();
            Map<String, Object> reMap = gson.fromJson(result, mapType);
            String re = reMap.get("msg").toString();
            if (!"成功".equals(re)) {
                LOGGER.warn("匠人推流端调用前端保存模板服务失败");
                throw new LiveException(-12138, "匠人推流端调用前端保存模板服务失败");
            }
            Boolean result1 = templateCache.vanishRedis(templateId);
            if (result1) {
                LOGGER.info("超级模办删除缓存成功，templateId:{}", templateId);
            }

        } catch (Exception e) {
            LOGGER.warn("匠人推流端调用前端保存模板服务失败:{}",e);
            throw new LiveException(-12138, "匠人推流端调用前端保存模板服务失败");
        }
    }


}
