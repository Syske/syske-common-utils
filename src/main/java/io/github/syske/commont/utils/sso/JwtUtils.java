package io.github.syske.commont.utils.sso;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;


import io.github.syske.commont.utils.sso.entity.User;
import io.github.syske.commont.utils.sso.exception.AuthorizationException;
import io.github.syske.commont.utils.sso.exception.OtherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * @program: syske-common-utils
 * @description: jwt工具类
 * @author: syske
 * @create: 2020-03-14 22:26
 */
public class JwtUtils {
    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private JwtUtils() {}
    /**
     * Description: 生成一个jwt字符串
     *
     * @param user    用户
     * @param timeOut 超时时间（单位s）
     * @return java.lang.String
     * @author fanxb
     * @date 2019/3/4 17:26
     */
    public static String encode(User user, String secret, long timeOut) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                //设置过期时间为一个小时
                .withExpiresAt(new Date(System.currentTimeMillis() + timeOut))
                //设置负载
                .withClaim("username", user.getUsername())
                .withClaim("name", user.getName())
                .sign(algorithm);
        return token;
    }

    /**
     * Description: 解密jwt
     *
     * @param token  token
     * @param secret secret
     * @return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               com.auth0.jwt.interfaces.Claim>
     * @author fanxb
     * @date 2019/3/4 18:14
     */
    public static Map<String, Claim> decode(String token, String secret) {
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(secret)) {
            logger.info("token：" + token + " , secret:" + secret);
            throw new AuthorizationException("用户状态校验失败:会话已过期，请重新登陆");
        }
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = jwtVerifier.verify(token);
        } catch (TokenExpiredException e) {
            logger.error("会话已过期，请重新登陆:", e);
            throw new AuthorizationException("会话已过期，请重新登陆");
        }
        return decodedJWT.getClaims();
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static User getUser(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            User user = new User();
            user.setUsername(jwt.getClaim("username").asString());
            user.setName(jwt.getClaim("name").asString());
            return user;
        } catch (JWTDecodeException e) {
            logger.error("获取用户名信息失败：", e);
            throw new OtherException("获取用户名信息失败");
        }
    }
}