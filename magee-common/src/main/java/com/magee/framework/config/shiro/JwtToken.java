package com.magee.framework.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class JwtToken implements AuthenticationToken {
    private static final long serialVersionUID = 1L;
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
