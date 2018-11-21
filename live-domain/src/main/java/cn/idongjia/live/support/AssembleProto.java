package cn.idongjia.live.support;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.proto.LiveProto;
import cn.idongjia.live.restructure.domain.entity.live.LivePullUrlV;
import cn.idongjia.util.Utils;
import cn.idongjia.zoo.pojo.ZooMessage;
import cn.idongjia.zoo.proto.ZooProto;
import com.google.protobuf.ByteString;

import java.io.UnsupportedEncodingException;

public final class AssembleProto {

    /**
     * 组装下发拉流地址
     *
     * @param livePullUrl 拉流地址
     * @return 组装好的websocket信息
     */
    public static ZooProto.ZooPack assemblePushUrls(LivePullUrlV livePullUrl) {
        if (livePullUrl != null) {
            ZooProto.ZooPack.Builder zooBuilder = ZooProto.ZooPack.newBuilder();
            LiveProto.LiveUrlPush.Builder builder = LiveProto.LiveUrlPush.newBuilder();
            builder.setFlvUrl(livePullUrl.getFlvUrl() == null ? "" : livePullUrl.getFlvUrl());
            builder.setRtmpUrl(livePullUrl.getRtmpUrl() == null ? "" : livePullUrl.getRtmpUrl());
            builder.setHlsUrl(livePullUrl.getHlsUrl() == null ? "" : livePullUrl.getHlsUrl());
            zooBuilder.setService(LiveProto.LiveServiceType.LIVE_URL_PUSH_VALUE);
            zooBuilder.setTime(Utils.getCurrentSecond());
            zooBuilder.setSerialized(builder.build().toByteString());
            return zooBuilder.build();
        } else {
            return null;
        }
    }

    /**
     * 组装聊天消息
     *
     * @param message 聊天消息
     * @return 组装好的协议包
     */
    public static ZooProto.ZooPack assembleChatMessage(ZooMessage message, User user) {
        ZooProto.ZooPack.Builder packBuilder = ZooProto.ZooPack.newBuilder();
        ZooProto.ZooMsg.Builder msgBuilder = ZooProto.ZooMsg.newBuilder();
        try {
            msgBuilder.setContent(ByteString.copyFrom(message.getContent().getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        msgBuilder.setZmid(message.getZmid());
        msgBuilder.setUid(message.getUid());
        msgBuilder.setUsername(user.getUsername());
        if (!Utils.isEmpty(user.getAvatar())) {
            msgBuilder.setAvatar(user.getAvatar());
        }
        msgBuilder.setType(message.getType());
        long currentTime = Utils.getCurrentMillis();
        msgBuilder.setTime(currentTime);
        ZooProto.ZooMsgPush.Builder pushBuilder = ZooProto.ZooMsgPush.newBuilder();
        pushBuilder.addMsg(msgBuilder.build());
        packBuilder.setSerialized(pushBuilder.build().toByteString());
        packBuilder.setTime(currentTime);
        packBuilder.setService(ZooProto.ZooServiceType.SERVICE_MSG_PUSH_VALUE);
        return packBuilder.build();
    }

    /**
     * 组装直播状态变更websocket信息
     *
     * @param isSuccess 是否成功
     * @return 组装好的websoecket信息
     */
    public static ZooProto.ZooPack assembleLiveStateResp(boolean isSuccess) {
        LiveProto.LiveStateRes.Builder liveStateResBuilder = LiveProto.LiveStateRes.newBuilder();
        ZooProto.ZooRes.Builder zooResBuilder = ZooProto.ZooRes.newBuilder();
        if (isSuccess) {
            zooResBuilder.setCode(1);
        } else {
            zooResBuilder.setCode(0);
        }
        liveStateResBuilder.setRes(zooResBuilder.build());
        ZooProto.ZooPack.Builder zooBuilder = ZooProto.ZooPack.newBuilder();
        zooBuilder.setService(LiveProto.LiveServiceType.LIVE_STATE_VALUE);
        zooBuilder.setTime(Utils.getCurrentSecond());
        zooBuilder.setSerialized(liveStateResBuilder.build().toByteString());
        return zooBuilder.build();
    }

    public static ZooProto.ZooPack assembleMainResource() {
        return null;
    }

    public static ZooProto.ZooPack assembleUserItemOperationPack(String info, int operation, String username) {
        LiveProto.UserItemOperationPush push = LiveProto.UserItemOperationPush.newBuilder()
                .setInfo(info)
                .setOperation(operation)
                .setUsername(username)
                .build();
        return ZooProto.ZooPack.newBuilder()
                .setSerialized(push.toByteString())
                .setService(LiveProto.LiveServiceType.LIVE_USER_ITEM_OPERATION_VALUE)
                .setTime(System.currentTimeMillis())
                .build();
    }
}
