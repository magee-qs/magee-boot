package com.magee.framework.config.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 功能描述:JWT工具类
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public class JwtUtil {
    /** token有效期 7天*/
    public static final long EXPIRE_TIME = (7 * 12) * 60 * 60 * 1000;

    /**
     * 生成签名
     * */
    public static String sign(String username,String secret){
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 校验token合法性
     * */
    public static boolean verify(String token , String username, String secret){
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 从签名中获取用户名
     * */
    public static String getUserName(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        }catch (Exception e){
            log.warn(e.getMessage());
            return null;
        }
    }
}
