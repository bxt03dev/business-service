package com.buixuantruong.shopapp.security.jwt;

import com.buixuantruong.shopapp.model.User;
import com.github.javafaker.Bool;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.internal.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JWTTokenFilter extends OncePerRequestFilter {

    JWTTokenUtil jwtTokenUtil;
    UserDetailsService userDetailsService;

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            if(isByPassToken(request)){
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                final String token = authHeader.substring(7);
                final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
                if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    User existingUser = (User) userDetailsService.loadUserByUsername(phoneNumber);
                    if(jwtTokenUtil.validateToken(token, existingUser)){
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        existingUser,
                                        null,
                                        existingUser.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }


    private Boolean isByPassToken(@NotNull HttpServletRequest request){
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of("/api/v1/products", "GET"),
                Pair.of("/api/v1/categories", "GET"),
                Pair.of("/api/v1/users/register", "POST"),
                Pair.of("/api/v1/users/login", "POST"));

        for(Pair<String, String> byPassToken : byPassTokens){
            if(request.getServletPath().contains(byPassToken.getLeft()) && request.getMethod().equals(byPassToken.getRight())){
                return true;
            }
        }
        return false;
    }
}
