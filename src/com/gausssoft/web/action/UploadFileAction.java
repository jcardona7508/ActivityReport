package com.gausssoft.web.action;

import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.system.GaussSoftException;
import com.gausssoft.web.WebAction;
import com.gausssoft.web.WebActionMapping;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class UploadFileAction extends WebAction {
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
      HttpServletResponse httpResponse = (HttpServletResponse)response.getResponse();
      httpResponse.setHeader("Cache-Control", "no-cache,no-store");
      httpResponse.setCharacterEncoding("UTF-8");
      httpResponse.setContentType("application/json;charset=UTF-8");
      PrintWriter out = httpResponse.getWriter();
      out.println("{\"success\":true}");
    } catch (IOException ex) {
      Logger.getLogger(WebAction.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }
}
