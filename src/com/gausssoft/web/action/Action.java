package com.gausssoft.web.action;

import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.system.GaussSoftException;
import com.gausssoft.web.JSonResponse;
import com.gausssoft.web.WebAction;
import com.gausssoft.web.WebActionMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Action extends WebAction {
  protected Node getRoot(Document document, IResponseContext response) {
    Node root = document.createElement("response");
    Node child = document.createElement("ActivityReport");
    root.appendChild(child);
    return root;
  }
  
  public void execute(WebActionMapping action, IRequestContext request, IResponseContext response) throws GaussSoftException {
    HttpServletResponse httpResponse = (HttpServletResponse)response.getResponse();
    try {
      httpResponse.setHeader("Cache-Control", "no-cache,no-store");
      httpResponse.setCharacterEncoding("UTF-8");
      httpResponse.setContentType("application/json;charset=UTF-8");
      PrintWriter out = httpResponse.getWriter();
      buildJSonResponse(out, response);
    } catch (IOException ex) {
      Logger.getLogger(WebAction.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }
  
  protected void buildJSonResponse(PrintWriter out, IResponseContext response) {
    JSonResponse json = new JSonResponse();
    Gson gson = (new GsonBuilder()).serializeNulls().create();
    json.status = getResultStatus();
    json.message = response.getValue("message");
    json.response = response.getValue("com.gausssoft.defaultValue");
    String header = gson.toJson(json);
    out.println(header);
  }
}
