package com.gausssoft.web;

import com.gausssoft.web.JSonResponse;
import com.gausssoft.web.SessionToken;
import com.gausssoft.web.WebFrontController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActivityReport extends WebFrontController implements Filter {
  private static final long serialVersionUID = 1L;
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse httpResp = (HttpServletResponse)response;
    HttpServletRequest httpReq = (HttpServletRequest)request;
    if (httpReq.getRequestURI().contains("/go/")) {
      Cookie cookie = getCookie(httpReq, "authentoken");
      if (cookie == null || !checkToken(cookie.getValue())) {
        if (httpReq.getRequestURI().contains("/go/login") || httpReq.getRequestURI().contains("/go/start")) {
          chain.doFilter(request, response);
        } else {
          redirect(httpResp, httpReq);
        } 
      } else {
        chain.doFilter(request, response);
      } 
    } else if (validateRequest(httpReq.getRequestURI())) {
      chain.doFilter(request, response);
    } else {
      redirect(httpResp, httpReq);
    } 
  }
  
  private void redirect(HttpServletResponse httpResp, HttpServletRequest httpReq) throws IOException, ServletException {
    httpResp.setHeader("Cache-Control", "no-cache,no-store");
    httpResp.setContentType("application/json;charset=UTF-8");
    PrintWriter out = httpResp.getWriter();
    JSonResponse json = new JSonResponse();
    Gson gson = (new GsonBuilder()).serializeNulls().create();
    json.status = "Redirect";
    json.message = "Login";
    SessionToken sessionToken = new SessionToken();
    sessionToken.setLastUrl(httpReq.getRequestURI());
    json.response = sessionToken;
    String resp = gson.toJson(json);
    out.println(resp);
  }
  
  private boolean validateRequest(String uri) {
    if (uri.equals("/CloudLicensing/do/GetLicenseN") || uri.equals("/CloudLicensing/do/ActivateServer") || uri.equals("/CloudLicensing/do/GetActivationServer") || uri.equals("/CloudLicensing/do/CheckAlerts") || 
      uri.equals("/CloudLicensing/do/login") || uri.equals("/CloudLicensing/do/start") || uri.equals("/CloudLicensing/do/ImportLicenciasData"))
      return true; 
    System.out.println("Request not allowed: " + uri);
    return false;
  }
  
  public void init(FilterConfig filterConfig) throws ServletException {}
  
  private boolean checkToken(String cookieValue) {
    if (cookieValue.length() > 0)
      return true; 
    return false;
  }
  
  private Cookie getCookie(HttpServletRequest httpReq, String name) {
    Cookie[] cookies = httpReq.getCookies();
    if (cookies != null) {
      byte b;
      int i;
      Cookie[] arrayOfCookie;
      for (i = (arrayOfCookie = cookies).length, b = 0; b < i; ) {
        Cookie cookieTmp = arrayOfCookie[b];
        if (cookieTmp.getName().equals(name))
          return cookieTmp; 
        b++;
      } 
    } 
    return null;
  }
}
