//package cn.idongjia.live.service.impl.login;
//
//import cn.idongjia.exception.Result;
//import cn.idongjia.live.pojo.user.LoginUser;
//import cn.idongjia.live.pojo.user.LoginUser;
//import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//
///**
// * 登录接口
// * Created by wuqiang on 16/4/19.
// */
//
//@Produces({ContentType.APPLICATION_JSON_UTF_8})
//@Path("user")
//public interface LoginService {
//
//    String SESSION_KEY_USER = "LOGINING-USER-KEY";
//
//    @POST
//    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
//    @Path("login")
//    Result login(LoginUser user);
//
//    @Path("logout")
//    @POST
//    Result logout();
//
//}
