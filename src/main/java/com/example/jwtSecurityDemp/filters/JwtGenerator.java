package com.example.jwtSecurityDemp.filters;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;


@Component
public class JwtGenerator  {


    @Value("${secret.key}")
    String secretcontant;

    @Value("${jwt.token}")
    String jwttokensecret;


    private Environment env;

    public JwtGenerator(Environment env) {
        this.env = env;
    }
    public String generatorjwt()
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        if(authentication!=null)
        {
            String username=authentication.getName();
            String secret=env.getProperty(secretcontant,secretcontant);
            SecretKey secretKey= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            System.out.println("authentication"+ authentication);

            String jwt= Jwts.builder().setSubject("jwttoken").setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+60000))
                    .claim("username",username).claim("roles",authentication.getAuthorities()).signWith(secretKey).compact();


            System.out.println(username+" username is ");
            return jwt;


        }

        else
            throw new RuntimeException("user is not found");
    }
}
