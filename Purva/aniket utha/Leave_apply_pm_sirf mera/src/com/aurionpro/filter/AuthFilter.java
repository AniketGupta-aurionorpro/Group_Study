package com.aurionpro.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet Filter implementation class AuthFilter
 */
@WebFilter("/manager/*")
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        // Allow access to the login page and its resources
        String loginURI = httpRequest.getContextPath() + "/login.jsp";
        String testLoginURI = httpRequest.getContextPath() + "/manager/testLogin";
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI) || httpRequest.getRequestURI().equals(testLoginURI);

        if (loggedIn || isLoginRequest) {
            chain.doFilter(request, response);
        } else {
            // User is not logged in, redirect to login page
            httpResponse.sendRedirect(loginURI);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    public void destroy() {
        // Cleanup code if needed
    }
}