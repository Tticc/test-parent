package com.tester.testercommon.util.jwt;

import com.tester.testercommon.util.endecrypt.Md5Security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

/**
 * @Author 温昌营
 * @Date
 */
public class JwtHelper {

    private static final int EXPIRED_TIME = 1800;

    private static final String SIGN_KEY = "fjidoaqnfieowqjieoqw12==";

    public static String generateJwtToken(int platform, long id) throws Exception {
        StringBuilder sb = new StringBuilder();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String encrypt = Md5Security.encrypt(SIGN_KEY);
        Key signingKey = new SecretKeySpec(encrypt.getBytes(), signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT") // 头部
                .claim("id", id)
                .setIssuer(String.valueOf(id))
                .setSubject(String.valueOf(platform))
                .signWith(signatureAlgorithm, signingKey);  // 签名
        // 过期时间

        long nowMillis = System.currentTimeMillis()-30*1000L;
        Date now = new Date(nowMillis);
        long expireTimeMs = EXPIRED_TIME*1000L;
        long expMillis = nowMillis + expireTimeMs;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);
        sb.append(builder.compact());
        String jwtToken = sb.toString();
        return jwtToken;
    }

    public static JwtDataModel getJwtDataId(String jwtToken) throws Exception {
        Long memberId,tokenExpire=null;
        Claims claims = Jwts.parser()
                .setSigningKey(Md5Security.encrypt(SIGN_KEY).getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();
        memberId = Long.parseLong(claims.getIssuer());
        tokenExpire = claims.getExpiration().getTime() / 1000;
        JwtDataModel jwtDataModel = new JwtDataModel();
        jwtDataModel.setExpiredTime(tokenExpire)
                .setId(memberId);
        return jwtDataModel;
    }
}
