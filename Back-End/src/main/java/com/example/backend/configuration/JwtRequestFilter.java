package com.example.backend.configuration;

import com.example.backend.service.JwtService;
import com.example.backend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        String jwtToken = null;
        String userName = null;
        if(header != null && header.startsWith("Bearer ")){
            jwtToken = header.substring(7);

            try {
                userName = jwtUtil.getUsernameFromToken(jwtToken);
            }catch (IllegalArgumentException e){
                System.out.println("Unable JWT");
            }catch (ExpiredJwtException e){
                System.out.println("Token is expired");
            }
        }else {
            System.out.println("Jwt token does not start with Bearer ");
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
           UserDetails userDetails = jwtService.loadUserByUsername(userName);

           if(jwtUtil.validateToken(jwtToken,userDetails)){
               UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                       new UsernamePasswordAuthenticationToken(userDetails,
                               null,
                               userDetails.getAuthorities());
                       usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                       SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
           }
        }
        filterChain.doFilter(request,response);

    }
}
