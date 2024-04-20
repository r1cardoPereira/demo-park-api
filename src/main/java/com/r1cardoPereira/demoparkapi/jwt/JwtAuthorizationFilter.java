package com.r1cardoPereira.demoparkapi.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Autowired field for JwtUserDetailsService.
     */
    @Autowired
    private JwtUserDetailsService detailsService;

    /**
     * Applies the JWT authorization filter to the incoming request.
     * Checks if the JWT token is valid and if it starts with 'Bearer'.
     * If the token is invalid or expired, it logs a warning and continues the
     * filter chain.
     * Otherwise, it extracts the username from the token and sets it in the
     * request's authentication.
     * Finally, it continues the filter chain.
     *
     * @param request     the incoming HTTP servlet request
     * @param response    the outgoing HTTP servlet response
     * @param filterChain the filter chain to be executed
     * @throws ServletException if there is a servlet-related problem
     * @throws IOException      if there is an I/O problem
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = request.getHeader(JwtUtils.JTW_AUTHORIZATION);

        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            log.info("Jwt Token está nulo, vazio ou não iniciado com 'Bearer'.");
            filterChain.doFilter(request, response);

            return;
        }

        if (!JwtUtils.isTokenValid(token)) {
            log.warn("Jwt Token está inválido ou expirado.");
            filterChain.doFilter(request, response);

            return;

        }

        String username = JwtUtils.getUsernameFromToken(token);

        toAuthentication(request, username);

        filterChain.doFilter(request, response);

    }

    /**
     * Authenticates the user based on the provided username and sets the
     * authentication token in the security context.
     *
     * @param request  the HttpServletRequest object representing the current
     *                 request
     * @param username the username of the user to be authenticated
     */
    private void toAuthentication(HttpServletRequest request, String username) {

        UserDetails userDetails = detailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }

}
