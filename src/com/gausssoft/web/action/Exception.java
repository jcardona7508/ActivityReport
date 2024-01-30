package com.gausssoft.web.action;

import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.web.action.Action;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Exception extends Action {
  protected Node getRoot(Document document, IResponseContext response) {
    Node root = document.createElement("message");
    String message = response.getExceptionMessage();
    if (message == null)
      message = "Exception"; 
    root.setTextContent(message);
    return root;
  }
  
  protected String getResultStatus() {
    return ResultCode.EXCEPTION.toString();
  }
  
  protected Object getResponseValue(IResponseContext response) {
    String message = response.getExceptionMessage();
    if (message == null)
      message = "Exception"; 
    return message;
  }
}
