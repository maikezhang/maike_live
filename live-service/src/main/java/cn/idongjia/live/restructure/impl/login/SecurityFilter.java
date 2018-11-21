//package cn.idongjia.live.restructure.impl.login;
//
//import cn.idongjia.exception.BaseException;
//import cn.idongjia.exception.HandleException;
//import cn.idongjia.live.service.impl.login.LoginService;
//import cn.idongjia.log.Log;
//import cn.idongjia.log.LogFactory;
//import com.alibaba.dubbo.common.utils.StringUtils;
//import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
//import com.google.gson.Gson;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * 登录过滤
// * Created by wuqiang on 16/4/19.
// */
//public class SecurityFilter implements Filter {
//
//    static final String GUEST_FROM_HEADER_KEY = "guest_from";
//    static final String GUEST_FROM_MOBILE = "mobile";
//
//    private static final Log LOGGER = LogFactory.getLog(SecurityFilter.class);
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
//                         FilterChain filterChain) throws IOException, ServletException {
//        if (!(servletRequest instanceof HttpServletRequest)) {
//            response(servletResponse, new HandleException("只支持Http请求."));
//            return;
//        }
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//
//        String uri = request.getRequestURI();
//        String contextPath = request.getContextPath();
//        String guest = request.getHeader(GUEST_FROM_HEADER_KEY);
//        //登录不需要做session认证
//        HttpSession session = request.getSession();
//
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("Request URI:{}", uri);
//            LOGGER.debug("ContextPath:{}", contextPath);
//        }
//
//        //处理静态资源
//        if (!uri.equals(contextPath + "/main.html") && isStatic(uri)) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        //php或手机接口,不做登录认证
//        if (uri.contains(contextPath + "/unauth/")) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        if (uri.contains(contextPath + "/api/")) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        if (uri.contains(contextPath + "/info/")) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        if (uri.contains(contextPath + "/manage/")) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("SessionId:{}", session.getId());
//        }
//        Object o = session.getAttribute(LoginService.SESSION_KEY_USER);
//        if (null == o && !uri.endsWith("user/login")) {
//            //手机端登录验证未通过,直接退出
//            if (!StringUtils.isEmpty(guest) && guest.equalsIgnoreCase(GUEST_FROM_MOBILE)) {
//                response(servletResponse, new HandleException("登录已过期!"));
//                return;
//            }
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("Unregist login uri: {} ", uri);
//            }
//            response.sendRedirect(contextPath + "/login.html");
//        } else {
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    private void response(ServletResponse response, BaseException exception) {
//        response.setCharacterEncoding("utf-8");
//        response.setContentType(ContentType.APPLICATION_JSON_UTF_8);
//        PrintWriter out = null;
//        try {
//            out = response.getWriter();
//            out.append(new Gson().toJson(exception));
//        } catch (IOException e) {
//            LOGGER.warn("set response failed: %s", e.getMessage());
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
//    }
//
//
//    private static boolean isStatic(String uri) {
//        String regex =
//                "(.*\\.html)|.*\\.jsp|.*\\.js|.*\\.css|.*\\.map"
//                        + "|.*\\.jpg|.*\\.png|.*\\.proto|.*\\.json";
//        return uri.matches(regex);
//    }
//
//
//}
