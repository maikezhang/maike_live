//package cn.idongjia.live.restructure.impl.login;
//
//import cn.idongjia.clan.lib.pojo.User;
//import cn.idongjia.exception.HandleException;
//import cn.idongjia.exception.Result;
//import cn.idongjia.exception.ResultCodes;
//import cn.idongjia.live.pojo.user.LoginUser;
//import cn.idongjia.live.restructure.manager.UserManager;
//import cn.idongjia.live.service.impl.login.LoginService;
//import cn.idongjia.util.MD5;
//import com.alibaba.dubbo.common.utils.StringUtils;
//import com.alibaba.dubbo.rpc.RpcContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.util.Objects;
//
///**
// * 用户登录功能
// * Created by wuqiang on 16/4/18.
// */
//@Component("loginServiceImpl")
//public class LoginServiceImpl implements LoginService {
//
//    private static final Integer MOBILE_SESSION_INTERNAL = 60 * 60 * 24 * 30;
//    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
//    private static final String ANCHOR_ID = "anchorid";
//    @Resource
//    private UserManager userManager;
//    @Resource
//    private HttpSession session;
////    @LiveShowResource
////    private HttpServletResponse httpServletResponse;
////    @LiveShowResource
////    private HttpServletRequest httpServletRequest;
//
//    @Value("#{liveConfig['tip.login']}")
//    private String loginError;
//
//    @Override
//    public Result login(LoginUser user) {
//        try {
//            Objects.requireNonNull(user, loginError);
//            Objects.requireNonNull(user.getMobile(), loginError);
//            Objects.requireNonNull(user.getPassword(), loginError);
//            String password = user.getPassword();
//
//            User userDB = userManager.getUserByMobile(user.getMobile());
//            Objects.requireNonNull(userDB, loginError);
//            if (!MD5.getPasswordMD5(userDB.getSalt(), password)
//                    .equals(userDB.getPassword())) {
//                throw new HandleException(loginError);
//            }
//            session.setAttribute(SESSION_KEY_USER, userDB);
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("LoginUser {} login succeed.", userDB.getMobile());
//            }
//            //手机用户重置session过期为1个月
//            String guest = RpcContext.getContext().getRequest(HttpServletRequest.class).getHeader(SecurityFilter.GUEST_FROM_HEADER_KEY);
//            if (!StringUtils.isEmpty(guest)
//                    && guest.equalsIgnoreCase(SecurityFilter.GUEST_FROM_MOBILE)) {
//                session.setMaxInactiveInterval(MOBILE_SESSION_INTERNAL);
//            } else {
//                //主播从web端登录,设置APPID-cookie
//                Cookie cookie = new Cookie(ANCHOR_ID, userDB.getUid().toString());
//                cookie.setPath("/");
//                RpcContext.getContext().getResponse(HttpServletResponse.class).addCookie(cookie);
//            }
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return HandleException.getResult(e.getMessage());
//        }
//        return ResultCodes.SUCCESS;
//    }
//
//    @Override
//    public Result logout() {
//        LoginUser user = (LoginUser) session.getAttribute(SESSION_KEY_USER);
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("LoginUser {} login out.", user.getUsername());
//        }
//        session.removeAttribute(SESSION_KEY_USER);
//        return ResultCodes.SUCCESS;
//    }
//
//}
