package cn.idongjia.live.restructure.impl.craft;

import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.v2.pojo.CraftsLive;
import cn.idongjia.live.v2.pojo.CraftsLive4List;
import cn.idongjia.live.v2.pojo.CraftsLiveDetail;
import cn.idongjia.live.v2.pojo.ItemResource;
import cn.idongjia.live.v2.pojo.ItemResourcePackage;
import cn.idongjia.live.v2.pojo.LiveResource;
import cn.idongjia.live.v2.pojo.query.CraftsLivePPUrl;
import cn.idongjia.live.v2.pojo.query.ResourceSearch;
import cn.idongjia.live.v2.service.CraftsLiveServiceV2;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/6/22.
 */
public class CraftsLiveServiceV2ImplTest  extends SpringJUnitNoRollbackTest{

    @Resource
    private CraftsLiveServiceV2 craftsLiveServiceV2;

    private CraftsLive craftsLive;

    private static final Log LOGGER = LogFactory.getLog(CraftsLiveServiceV2ImplTest.class);

    @Before
    public void initData(){
        craftsLive=new CraftsLive();
        //{{
        // "anchorId":642358,
        // "pic":"T3I9xTBgdT1R49Ip6K",
        // "title":"杜掌柜博物馆课堂之香文化史",
        // "showDesc":"“世事有过现，熏性无变迁”数千年风雨相伴，袅袅香烟氤氲，寄托的是我们对天地的敬畏之心，对先贤往圣的敬佩之情。炉热香暖外，静对一炉香，古人更把香视其为怡情启思的良伴，祛疫辟秽的铮友，安魂正魄的护持。千年香文化历史悠久，见证了华夏文明绵延不息的文化脉搏。让我们以香学古籍《香谱》入手，走进香气氤氲的香学世界。本次香学课堂，我们以香学起源发展为主要脉络，窥探各代、各家，品味各门、各派，感受香在历史各处的延绵之味，感悟风雨沧桑中这“一炷香”所带来的温暖。\n本次香文化课堂，开在烟台市博物馆，将与大家一起聊一下香文化以及其发展史。",
        // "preStartTime":1529820000000,
        // "preEndTime":1529825400000,
        // "screenDirection":1,
        // "resources":[{"resourceId":320810,"resourceType":1},{"resourceId":646972,"resourceType":1},{"resourceId":202961,"resourceType":1},{"resourceId":340632,"resourceType":1}]
        // }:CraftsLive}
        craftsLive.setScreenDirection(1);
        craftsLive.setAnchorId(416905L);
        craftsLive.setPic("T3I9xTBgdT1R49Ip6K");
        craftsLive.setTitle("杜掌柜博物馆课堂之香文化史");
        craftsLive.setShowDesc("世事有过现，熏性无变迁”数千年风雨相伴，袅袅香烟氤氲，寄托的是我们对天地的敬畏之心，对先贤往圣的敬佩之情。炉热香暖外，静对一炉香");
        craftsLive.setPreStartTime(Utils.getCurrentMillis()+200000);
        craftsLive.setPreEndTime(Utils.getCurrentMillis()+500000);
        craftsLive.setTemplateJsonStr("[{\"attrs\":{\"content\":\"啦额咯咯\"},\"key\":\"label\"},{\"attrs\":{\"content\":\"[{\\\"h\\\":2560,\\\"url\\\":\\\"T3uaCTBCKT1R49Ip6K\\\",\\\"w\\\":1440}]\",\"type\":\"l1\"},\"key\":\"image\"}]");
        List<LiveResource> resources=new ArrayList<>();
        LiveResource resource=new LiveResource();
        resource.setResourceId(320810L);
        resource.setResourceType(1);
        resources.add(resource);
        LiveResource resource1=new LiveResource();
        resource1.setResourceId(337461L);
        resource1.setResourceType(1);
        resources.add(resource1);
        LiveResource resource2=new LiveResource();
        resource2.setResourceId(7600L);
        resource2.setResourceType(0);
        resources.add(resource2);

//        craftsLive.setResources(resources);
        craftsLive.setVideoCoverDuration(1000);
        craftsLive.setVideoCoverPic("4444444444");
        craftsLive.setVideoCoverUrl("sefwefwewewe");
        craftsLive.setLiveType(LiveEnum.LiveType.TREASURE_TYPE.getCode());

    }

    @Test
    @Rollback(false)
    public void craftsLiveAdd() throws Exception {

        Long id=craftsLiveServiceV2.craftsLiveAdd(craftsLive);
        LOGGER.info(craftsLive.getTitle()+"的id为：{}",id);



    }

    @Test
    @Rollback(false)
    public void craftsLiveUpdate() throws Exception {
        craftsLive.setLid(3653L);
//        craftsLive.setPreStartTime(1529668800000L);
//        craftsLive.setPreEndTime(1529672400000L);
        craftsLive.setTitle("麦克麦克");
        craftsLiveServiceV2.craftsLiveUpdate(craftsLive);



    }

    @Test
    @Rollback(false)
    public void craftsLiveResourceManage() throws Exception {
        List<LiveResource> resources=new ArrayList<>();
        LiveResource resource=new LiveResource();
        resource.setResourceType(1);
        resource.setResourceId(320810L);
        resources.add(resource);
        craftsLiveServiceV2.craftsLiveResourceManage(3461L,resources);

    }

    @Test
    @Rollback(false)
    public void craftsLiveResourceReSort() throws Exception {
        List<LiveResource> resources=new ArrayList<>();
//        LiveResource resource=new LiveResource();
//        resource.setResourceType(1);
//        resource.setResourceId(320810L);
        LiveResource resource1=new LiveResource();
        resource1.setResourceType(1);
        resource1.setResourceId(337461L);
        resources.add(resource1);
        LiveResource resource2=new LiveResource();
        resource2.setResourceType(1);
        resource2.setResourceId(320810L);
        resources.add(resource2);


        craftsLiveServiceV2.craftsLiveResourceReSort(3641L,resources);
    }

    @Test
    public void getUnstartList() throws Exception {
        LiveSearch liveSearch=new LiveSearch();
        liveSearch.setPage(1);
        liveSearch.setLimit(10);
        liveSearch.setUid(416905L);
        liveSearch.setState(1);
        BaseList<CraftsLive4List> unstartList = craftsLiveServiceV2.getUnstartList(liveSearch);
        LOGGER.info("unstartList==>{}",unstartList);
    }

    @Test
    public void getEndList() throws Exception {
        LiveSearch liveSearch=new LiveSearch();
        liveSearch.setPage(1);
        liveSearch.setLimit(10);
        liveSearch.setUid(416905L);
        liveSearch.setState(3);
        BaseList<CraftsLive4List> unstartList = craftsLiveServiceV2.getEndList(liveSearch);
        LOGGER.info("endList==>{}",unstartList);
    }

    @Test
    public void getInProgressLiveByUid() throws Exception {
        CraftsLive craftsLive = craftsLiveServiceV2.getInProgressLiveByUid(131470L);
        LOGGER.info("craftsLive==>{}",craftsLive);
    }

    @Test
    public void getLiveDetail() throws Exception {
        CraftsLiveDetail liveDetail = craftsLiveServiceV2.getLiveDetail(3643L, 416905L);
        LOGGER.info("liveDetail==>{}",liveDetail);
    }

    @Test
    public void listItemResource() throws Exception {
        List<ItemResource> itemResources = craftsLiveServiceV2.listItemResource(3631L);
        LOGGER.info("itemResources=> {}",itemResources);
    }

    @Test
    @Rollback(false)
    public void liveAutoOnline() throws Exception {
        // TODO: 2018/6/22

        craftsLiveServiceV2.liveAutoOnline(3641L,1);
    }

    @Test
    public void listCraftsItems() throws Exception {
        ResourceSearch resourceSearch = new ResourceSearch();
        resourceSearch.setPage(1);
        resourceSearch.setLimit(20);
        resourceSearch.setCraftsUid(190639L);
        resourceSearch.setLid(3631L);
        ItemResourcePackage itemResourcePackage = craftsLiveServiceV2.listCraftsItems(resourceSearch);
        LOGGER.info("itemResourcePackage==>{}",itemResourcePackage);
    }

    @Test
    public void getSelectedItems() throws Exception {
        List<ItemResource> selectedItems = craftsLiveServiceV2.getSelectedItems(3631L);
        LOGGER.info("selectedItems==>{}",selectedItems);
    }

    @Test
    public void getCraftsLive() throws Exception {
        CraftsLive craftsLive = craftsLiveServiceV2.getCraftsLive(3668L);
        LOGGER.info("craftsLive==>{}",craftsLive);

    }

    @Test
    public void getServicePhoneNumber() throws Exception {
        Map<String, String> servicePhoneNumber = craftsLiveServiceV2.getServicePhoneNumber();
        LOGGER.info("servicePhoneNumber{}",servicePhoneNumber);
    }

    @Test
    public void getPushPullUrl() throws Exception {
        CraftsLivePPUrl pushPullUrl = craftsLiveServiceV2.getPushPullUrl(3631L);
        LOGGER.info("pushPullUrl{}",pushPullUrl);

    }

    @Test
    public void getLiveStatus() throws Exception {
        CraftsLive craftsLive = craftsLiveServiceV2.getLiveStatus(3631L);
        LOGGER.info("craftsLive==>{}",craftsLive);

    }

}
