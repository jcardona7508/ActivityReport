package com.gausssoft.web.action;

import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.system.GaussSoftException;
import com.gausssoft.system.GaussSoftSettings;
import com.gausssoft.web.JSonResponse;
import com.gausssoft.web.WebAction;
import com.gausssoft.web.WebActionMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class GetLogoImageAction extends WebAction {
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
      httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      httpResponse.setHeader("Pragma", "no-cache");
      httpResponse.setHeader("Expires", "0");
      httpResponse.setCharacterEncoding("UTF-8");
      httpResponse.setContentType("image/png");
      String workingDirectory = GaussSoftSettings.getInstance().getSection("ActivityRecordWeb").getString("WorkingPath", "UNDEFINED");
      File customLogo = new File(new File(workingDirectory, "uploadedFiles"), "gausssoftlogo.png");
      Logger.getLogger("siscorp.activityreport").log(Level.SEVERE, "Logo file: " + customLogo.getPath());
      ServletOutputStream stream = httpResponse.getOutputStream();
      BufferedImage image = ImageIO.read(customLogo);
      ImageIO.write(image, "PNG", (OutputStream)stream);
      stream.flush();
    } catch (IOException e) {
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
