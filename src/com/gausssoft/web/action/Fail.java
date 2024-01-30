package com.gausssoft.web.action;

import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.web.action.Action;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Fail extends Action {
  protected Node getRoot(Document document, IResponseContext response) {
    Element root = document.createElement("message");
    String message = response.getValue("message").toString();
    if (message == null)
      message = "Fail"; 
    root.setTextContent(message);
    return root;
  }
  
  protected String getResultStatus() {
    return ResultCode.FAIL.toString();
  }
  
  protected Object getResponseValue(IResponseContext response) {
    String message = (String)response.getValue("message");
    if (message == null)
      message = "Exception"; 
    return message;
  }
}
