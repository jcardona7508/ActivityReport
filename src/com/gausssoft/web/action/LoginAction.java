package com.gausssoft.web.action;

import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.system.GaussSoftException;
import com.gausssoft.web.JSonResponse;
import com.gausssoft.web.SessionToken;
import com.gausssoft.web.WebAction;
import com.gausssoft.web.WebActionMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class LoginAction extends WebAction {
  protected Node getRoot(Document document, IResponseContext response) {
    Node root = document.createElement("message");
    String message = response.getExceptionMessage();
    if (message == null)
      message = "Exception"; 
    root.setTextContent(message);
    return root;
  }
  
  public void execute(WebActionMapping action, IRequestContext request, IResponseContext response) throws GaussSoftException {
    try {
      SessionToken sessionToken = new SessionToken();
      Cookie cookie = new Cookie("authentoken", sessionToken.getId());
      cookie.setPath("/");
      cookie.setMaxAge(86400);
      HttpServletResponse httpResponse = (HttpServletResponse)response.getResponse();
      httpResponse.addCookie(cookie);
      HttpServletRequest httpRequest = (HttpServletRequest)request.getRequest();
      String acceptType = httpRequest.getHeader("Accept").toLowerCase();
      httpResponse.setHeader("Cache-Control", "no-cache,no-store");
      httpResponse.setCharacterEncoding("UTF-8");
      if (acceptType.indexOf("application/json") >= 0) {
        httpResponse.setContentType("application/json;charset=UTF-8");
        PrintWriter out = httpResponse.getWriter();
        buildJSonResponse(out, response);
      } else if (acceptType.indexOf("application/xml") >= 0) {
        httpResponse.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = httpResponse.getWriter();
        buildXMLResponse(out, response);
      } else {
        System.out.println("Unknown application request....");
      } 
    } catch (GaussSoftException ex) {
      Logger.getLogger(WebAction.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } catch (IOException ex) {
      Logger.getLogger(WebAction.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }
  
  private void buildJSonResponse(PrintWriter out, IResponseContext response) {
    JSonResponse json = new JSonResponse();
    Gson gson = (new GsonBuilder()).serializeNulls().create();
    json.status = getResultStatus();
    json.message = response.getValue("message");
    json.response = response.getValue("com.gausssoft.defaultValue");
    String header = gson.toJson(json);
    out.println(header);
  }
  
  private void buildXMLResponse(PrintWriter out, IResponseContext response) throws GaussSoftException {
    Document document = createXMLDocument("XMLResponse");
    document.getDocumentElement().setAttribute("status", getResultStatus());
    document.getDocumentElement().appendChild(getRoot(document, response));
    out.println(documentToString(document));
  }
}
