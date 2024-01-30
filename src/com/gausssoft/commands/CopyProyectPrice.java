package com.gausssoft.commands;

import activityrecord.data.CopyProjectDAO;
import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.client.MessageTO;
import com.gausssoft.service.SessionFacade;
import com.gausssoft.services.ServiceException;
import com.gausssoft.services.SessionFactory;
import com.gausssoft.store.Store;
import com.google.gson.Gson;

public class CopyProyectPrice implements ICommand {
  private String name;
  
  public CopyProyectPrice() {
    setName("CopyProyectPrice");
  }
  
  public IResponseContext execute(IRequestContext request) {
    Gson gson = new Gson();
    ResponseContext responseContext = new ResponseContext();
    MessageTO message = new MessageTO();
    try {
      String sessionId = request.getSessionID();
      SessionFacade session = (SessionFacade)SessionFactory.getInstance().getSession(
          "SessionFacade", sessionId);
      String jsonString = (String)request.getParameter("jsonString");
      CopyProjectDAO copyProject = (CopyProjectDAO)gson.fromJson(jsonString, CopyProjectDAO.class);
      String usercode = (String)request.getParameter("usercode");
      String resourcecode = (String)request.getParameter("resourcecode");
      responseContext.setResultCode(ResultCode.FAIL);
      if (session.validateSessionUser(usercode)) {
        Store store = Store.getInstance();
        if (store.copyProyectPriceActivities(copyProject, resourcecode)) {
          store.execSP();
          responseContext.setResultCode(ResultCode.SUCCESS);
        } 
      } else {
        responseContext.setValue("message", message.client = "sessionexpired");
      } 
    } catch (ServiceException e) {
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
