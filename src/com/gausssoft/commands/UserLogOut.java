package com.gausssoft.commands;

import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.security.exception.SecurityException;
import com.gausssoft.security.messages.SecurityMessages;
import com.gausssoft.security.service.ISecuritySessionFacade;
import com.gausssoft.services.ServiceException;
import com.gausssoft.services.SessionFactory;

public class UserLogOut implements ICommand {
  private String name = "UserLogOut";
  
  public void dispose() {}
  
  public IResponseContext execute(IRequestContext context) {
    ISecuritySessionFacade session = null;
    ResponseContext responseContext = new ResponseContext();
    SecurityMessages messages = new SecurityMessages();
    responseContext.setResultCode(ResultCode.SUCCESS);
    try {
      String sessionId = context.getSessionID();
      session = (ISecuritySessionFacade)SessionFactory.getInstance().getSession(
          "SecuritySessionFacade", sessionId);
      boolean closed = SessionFactory.getInstance().close(sessionId);
      if (!closed) {
        responseContext.setValue("message", messages.getMessage("SessionExpired"));
        session.saveLog(messages.getMessage("JoinSystem"), responseContext.getResultCode().toString(), messages.getMessage("SessionExpired"));
      } else {
        responseContext.setValue("message", messages.getMessage("EndSession"));
        session.saveLog(messages.getMessage("JoinSystem"), responseContext.getResultCode().toString(), messages.getMessage("EndSession"));
      } 
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (SecurityException se) {
      se.printStackTrace();
      responseContext.setExceptionMessage(se.getMessage());
      responseContext.setResultCode(ResultCode.EXCEPTION);
    } 
    return (IResponseContext)responseContext;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}
