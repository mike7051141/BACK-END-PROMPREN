package com.springboot.backendprompren.config.security;

import com.springboot.backendprompren.service.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final CustomUserDetailsService userDetailsService;


    //@Autowired
    //public JwtTokenProvider(UserDetailsService userDetailsService) {
   //     this.userDetailsService = userDetailsService;
   // }

    @Value("${springboot.jwt.secret}")
    private String secretKey = "secretKey";
    private  final Long tokenValidTime = 1000L*60*60;

    @PostConstruct
    protected void init(){
        logger.info("[init] 시크릿 초기화 시작");
        System.out.println("초기화 전 : "+secretKey);

        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        System.out.println("초기화 후 : "+secretKey);
        logger.info("[init] 시크릿 초기화 완료 : {}",secretKey);
    }
    // jwt 토큰 생성
    public String createToken(String account, List<String> roles){
        Claims claims = Jwts.claims().setSubject(account);
        claims.put("roles",roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date((now.getTime()+tokenValidTime)))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        logger.info("[createToken] 토큰 생성 완료",token);
        return token;
    }

    // jwt 토큰에서 회원 구별 조회
    public String getUsername(String token){
        logger.info("[getUsername] 회원 구별 조회 시작");
        String info = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody() // 토큰 본문
                .getSubject(); // 토큰에서 Subject -> 회원 구별
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }

    //jwt 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token){
        logger.info("[getAuthentication] 토큰 인증 정보 조회 시작");

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        logger.info("[getAuthenticaiton] 토큰 인증 정보 조회 완료, UserDetails userName",userDetails.getUsername());

        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
    //HTTP 헤더 정보에 X-AUTH-TOKEN 추가
    public String resolveToken(HttpServletRequest request){
        logger.info("[resolveToken]:{}",request);
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validationToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }
}
