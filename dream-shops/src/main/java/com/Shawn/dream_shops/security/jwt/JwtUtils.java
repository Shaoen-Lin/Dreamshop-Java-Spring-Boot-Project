package com.Shawn.dream_shops.security.jwt;

import com.Shawn.dream_shops.security.user.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;


import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication)
    {
        // 從 Spring Security 的 Authentication 物件中拿出登入的使用者資訊
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();
        // Principal 主要的

        // Authentication 就是一個代表使用者當前身份與權限的物件。
        // 它包含了「你是誰」、「你怎麼驗證」、「你有什麼權限」。
        // 包含幾個主要的函式：
        // getPrincipal()	回傳「使用者的主要身份資訊」。通常是一個 UserDetails 物件（例如 ShopUserDetails）。
        // getCredentials()	驗證憑證，例如密碼（通常登入後會被清空，避免洩漏）。
        // getAuthorities()	使用者的角色 / 權限清單 (GrantedAuthority 列表)。
        // isAuthenticated()	表示這個使用者是否已經通過驗證。 getDetails()

        List<String> roles = userPrincipal
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority).toList();

        // 使用 JWT (JSON Web Token) 建立一個 token
        return Jwts.builder() // build a JWT
                .setSubject(userPrincipal.getEmail()) // JSON 中 "sub": "user@example.com"
                .claim("id", userPrincipal.getId())
                .claim("roles", roles) // claim = set roles => roles
                .setIssuedAt(new Date()) // iat：現在這個時間
                .setExpiration(new Date( (new Date()).getTime() + expirationTime ))
                .signWith(key(), SignatureAlgorithm.HS256)
                // 確保 Token 是由 Server 發出的，中間沒被攔截並竄改
                // 機制：透過隨便 設置一個 Encoded 的 JwtKey 來確保這個 key 能夠被 public key 給解出正確的 String
                .compact(); //最後把整個 JWT 壓縮成字串：
    }

    private Key key()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) // 從 Client 提供的 Token 來知道 User 的名字
    {
        return Jwts.parserBuilder() // parse:解析
                .setSigningKey(key()) // 因為 SHA 256 是 對稱式加密 所以 key() 也可以用來解密
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token)
    {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }

    }
}
