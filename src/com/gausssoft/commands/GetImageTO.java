package com.gausssoft.commands;

import activityrecord.data.GraphDataTO;
import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.client.MessageTO;
import com.gausssoft.service.SessionFacade;
import com.gausssoft.services.ServiceException;
import com.gausssoft.services.SessionFactory;

public class GetImageTO implements ICommand {
  private String name;
  
  public GetImageTO() {
    setName("GeImageTO");
  }
  
  public IResponseContext execute(IRequestContext request) {
    ResponseContext responseContext = new ResponseContext();
    MessageTO message = new MessageTO();
    try {
      String sessionId = request.getSessionID();
      SessionFacade session = (SessionFacade)SessionFactory.getInstance().getSession(
          "SessionFacade", sessionId);
      String resourcecode = (String)request.getParameter("resourcecode");
      String usercode = (String)request.getParameter("usercode");
      String date = (String)request.getParameter("date");
      String screenwidth = (String)request.getParameter("screenwidth");
      String screenheight = (String)request.getParameter("screenheight");
      responseContext.setResultCode(ResultCode.FAIL);
      if (session.validateSessionUser(usercode)) {
        GraphDataTO transferObject = session.getImage(resourcecode, date, screenwidth, screenheight);
        responseContext.setValue(transferObject);
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
