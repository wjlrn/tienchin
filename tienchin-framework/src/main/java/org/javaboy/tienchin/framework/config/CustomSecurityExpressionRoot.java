package org.javaboy.tienchin.framework.config;

import org.javaboy.tienchin.common.core.domain.model.LoginUser;
import org.javaboy.tienchin.framework.security.context.PermissionContextHolder;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private AntPathMatcher matcher = new AntPathMatcher();

    public CustomSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    /**
     * 判断当前对象是否具备某一个权限
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        //获取当前登录用户所具有的权限
        Authentication authentication = this.getAuthentication();
        //这里实际上调用到的是 org.javaboy.tienchin.common.core.domain.model.LoginUser.getAuthorities 方法的返回值
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<? extends GrantedAuthority> collect = authorities.stream().filter(authority -> matcher.match(authority.getAuthority(), permission)).collect(Collectors.toList());
        PermissionContextHolder.setContext(permission);
        if(collect.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 是否具备多个权限中的任意一个权限
     * @param permissions
     * @return
     */
    public boolean hasAnyPermissions(String... permissions) {
        if(permissions == null || permissions.length == 0) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = this.getAuthentication().getAuthorities();
       for(GrantedAuthority authority : authorities) {
           for(String permission : permissions) {
               if(matcher.match(authority.getAuthority(), permission)) {
                   return true;
               }
           }
       }
       return false;
    }

    public boolean hasAllPermissions(String... permissions) {
        Collection<? extends GrantedAuthority> authorities = this.getAuthentication().getAuthorities();
        if(permissions == null || permissions.length == 0) {
            return false;
        }
        for(String permission : permissions) {
            boolean flag = false;
            for(GrantedAuthority authority : authorities) {
                if(matcher.match(authority.getAuthority(), permission)) {
                    flag = true;
                }
            }
            if(!flag) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
