package com.gausssoft.commands;

import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.client.MessageTO;
import com.gausssoft.configuration.service.ConfigurationSessionFacade;
import com.gausssoft.services.ServiceException;
import com.gausssoft.services.SessionFactory;

public class Start implements ICommand {
  private String name;
  
  public Start() {
    setName("Start");
  }
  
  public IResponseContext execute(IRequestContext request) {
    ResponseContext responseContext = new ResponseContext();
    MessageTO message = new MessageTO();
    try {
      responseContext.setResultCode(ResultCode.FAIL);
      String sessionId = request.getSessionID();
      ConfigurationSessionFacade session = (ConfigurationSessionFacade)SessionFactory.getInstance().getSession(
          "ConfSessionFacade", sessionId);
      if (session.isInstalled("ActivityRecordWeb")) {
        responseContext.setValue(Boolean.valueOf(true));
        responseContext.setResultCode(ResultCode.SUCCESS);
      } else {
        responseContext.setValue(Boolean.valueOf(false));
        responseContext.setResultCode(ResultCode.FAIL);
        message.server = "Application ActivityRecordWeb";
      } 
    } catch (ServiceException|com.gausssoft.configuration.ConfigurationException e) {
      responseContext.setValue("message", message.client = e.getMessage());
      responseContext.setExceptionMessage(e.getMessage());
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
  
  public void dispose() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
