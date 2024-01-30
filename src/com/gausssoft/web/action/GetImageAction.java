package com.gausssoft.web.action;

import activityrecord.data.GraphDataTO;
import com.gausssoft.GausssoftException;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.system.GaussSoftException;
import com.gausssoft.web.JSonResponse;
import com.gausssoft.web.SessionToken;
import com.gausssoft.web.WebAction;
import com.gausssoft.web.WebActionMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gausssoft.drawing.Canvas;
import gausssoft.drawing.Drawable;
import gausssoft.drawing.DrawingException;
import gausssoft.graph.Graph;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class GetImageAction extends WebAction {
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
      httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      httpResponse.setHeader("Pragma", "no-cache");
      httpResponse.setHeader("Expires", "0");
      httpResponse.setCharacterEncoding("UTF-8");
      httpResponse.setContentType("image/png");
      GraphDataTO transferObject = (GraphDataTO)response.getValue("transferObject");
      Graph graph = (Graph)response.getValue("graph");
      ServletOutputStream stream = httpResponse.getOutputStream();
      Canvas canvas = new Canvas();
      canvas.setDefaultTransform(transferObject.userSpace);
      canvas.addObject((Drawable)graph);
      canvas.setSize(transferObject.dimension);
      canvas.export((OutputStream)stream);
      stream.flush();
    } catch (DrawingException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (GausssoftException e) {
      e.printStackTrace();
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
