package com.gausssoft.commands;

import activityrecord.ract.DetailChoiceTO;
import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.client.MessageTO;
import com.gausssoft.service.SessionFacade;
import com.gausssoft.services.ServiceException;
import com.gausssoft.services.SessionFactory;
import java.util.List;

public class GetObjectDetail implements ICommand {
  private String name;
  
  public GetObjectDetail() {
    setName("GetObjectDetail");
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
      String activitycode = (String)request.getParameter("activitycode");
      String objectcode = (String)request.getParameter("objectcode");
      responseContext.setResultCode(ResultCode.FAIL);
      if (session.validateSessionUser(usercode)) {
        List<DetailChoiceTO> resp = session.getObjectDetail(resourcecode, activitycode, objectcode);
        responseContext.setValue(resp);
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
