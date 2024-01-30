package com.gausssoft.commands;

import activityrecord.ract.ActivityRequestTO;
import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.client.MessageTO;
import com.gausssoft.service.SessionFacade;
import com.gausssoft.services.ServiceException;
import com.gausssoft.services.SessionFactory;
import com.google.gson.Gson;

public class SaveActivities implements ICommand {
  private String name;
  
  public SaveActivities() {
    setName("SaveActivities");
  }
  
  public IResponseContext execute(IRequestContext request) {
    ResponseContext responseContext = new ResponseContext();
    MessageTO message = new MessageTO();
    try {
      Gson gson = new Gson();
      String sessionId = request.getSessionID();
      SessionFacade session = (SessionFacade)SessionFactory.getInstance().getSession(
          "SessionFacade", sessionId);
      String jsonString = (String)request.getParameter("jsonString");
      ActivityRequestTO activityRequestTO = (ActivityRequestTO)gson.fromJson(jsonString, ActivityRequestTO.class);
      responseContext.setResultCode(ResultCode.FAIL);
      if (session.validateSessionUser(activityRequestTO.usercode)) {
        session.doUpdateRact(activityRequestTO);
        responseContext.setResultCode(ResultCode.SUCCESS);
      } else {
        responseContext.setValue("message", message.client = "sessionexpired");
      } 
    } catch (ServiceException|com.gausssoft.GausssoftException e) {
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
