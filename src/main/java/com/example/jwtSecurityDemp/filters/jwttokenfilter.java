package com.example.jwtSecurityDemp.filters;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Component
public class jwttokenfilter extends OncePerRequestFilter {

    private Environment env;

    jwttokenfilter(Environment env) {
        this.env = env;
    }
    
    @Value("${secret.key}")
    String secretkey;
    @Value("${jwt.token}")
    String tokensecret;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        String token;

        System.out.println("insidet the jwt token validator "+SecurityContextHolder.getContext().getAuthentication());

        Authentication authentication1=SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication1);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            System.out.println("inside the jwttokenfilter");

            try {
                token = bearerToken.substring(7);

                String secret = env.getProperty(secretkey, tokensecret);


                SecretKey secretKey = Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();


                String username = String.valueOf(claims.get("username"));
                String authoritis = String.valueOf(claims.get("roles"));
                System.out.println(authoritis);
                Collection<GrantedAuthority> auth = convertRolesToAuthorities(authoritis);
                System.out.println(auth);
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                        auth);

                System.out.println(authentication);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String errorMessage = String.format("{\"error\":\"JWT token is expired \"}");
                response.getWriter().write(errorMessage);
                return;
            }

        }
        else
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String errorMessage = String.format("{\"error\":\"Invalid JWT token: %s\"}");
            response.getWriter().write(errorMessage);
            return;
        }

        filterChain.doFilter(request, response);


    }
    private static Collection<GrantedAuthority> convertRolesToAuthorities(String rolesString) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        String content = rolesString.substring(1, rolesString.length() - 1); // Remove brackets


        String[] roleStrings = content.split("\\},\\s*\\{");

        for (String roleString : roleStrings) {
            roleString = roleString.replaceAll("[{}]", "").trim();
            String[] keyValue = roleString.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                if ("authority".equals(key)) {
                    authorities.add(new SimpleGrantedAuthority(value));
                }
            }
        }

        return authorities;
    }


    @Override
    public boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/display") && !request.getServletPath().equals("/display_any");
    }
}
