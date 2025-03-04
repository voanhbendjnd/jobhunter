package vn.hoidanit.jobhunter.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.Entity.Permission;
import vn.hoidanit.jobhunter.domain.Entity.Role;
import vn.hoidanit.jobhunter.domain.Entity.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import vn.hoidanit.jobhunter.util.error.PermissionException;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestUri = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> Run preHandle");
        System.out.println(">>> Path = " + path);
        System.out.println(">>> HttpMethod = " + httpMethod);
        System.out.println(">>> RequestURI = " + requestUri);
        // check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        if (email != null && !email.isEmpty()) {
            User user = this.userService.fecthUserByUserName(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream()
                            .anyMatch(it -> it.getApiPath().equals(path) && it.getMethod().equals(httpMethod));
                    if (isAllow == false) {
                        throw new PermissionException("Ban không có quyên truy cập endpoint này!");
                    }
                } else {
                    throw new PermissionException("Ban không có quyên truy cập endpoint này!");

                }
            }
        }
        return true;
    }
}
