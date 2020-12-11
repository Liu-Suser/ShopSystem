package com.shiroyk.shopsystem.filter;

import com.shiroyk.shopsystem.constant.JwtConstants;
import com.shiroyk.shopsystem.service.UserService;
import com.shiroyk.shopsystem.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;
    private final UserService userService;

    public JwtAuthorizationFilter(HandlerExceptionResolver resolver,  UserService userService) {
        this.resolver = resolver;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader(JwtConstants.HEADER_STRING);
        try {
            if (StringUtils.isEmpty(token) || !token.startsWith(JwtConstants.TOKEN_PREFIX)) {
                SecurityContextHolder.clearContext();
            } else {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (UsernameNotFoundException
                | SignatureException
                | ExpiredJwtException
                | MalformedJwtException
                | IllegalArgumentException
                | AccessDeniedException ex) {
            resolver.resolveException(request, response, null, ex);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String authorization) {

        String token = authorization.replace(JwtConstants.TOKEN_PREFIX, "");
        String username = JwtTokenUtils.getUsernameByToken(token);

        if (!StringUtils.isEmpty(username)) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, userDetails.getAuthorities());
            return userDetails.isEnabled() ? authenticationToken : null;
        }
        return null;
    }
}
