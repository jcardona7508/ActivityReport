package com.gausssoft.commands;

import activityrecord.data.ProjectDAO;
import activityrecord.ract.ActivityReportResource;
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

public class SaveProyectPriceActivities implements ICommand {
  private String name;
  
  public SaveProyectPriceActivities() {
    setName("SaveProyectPriceActivities");
  }
  
  public IResponseContext execute(IRequestContext request) {
    boolean resp = false;
    Gson gson = new Gson();
    ResponseContext responseContext = new ResponseContext();
    MessageTO message = new MessageTO();
    try {
      String sessionId = request.getSessionID();
      SessionFacade session = (SessionFacade)SessionFactory.getInstance().getSession(
          "SessionFacade", sessionId);
      String jsonString = (String)request.getParameter("jsonString");
      ProjectDAO newProject = (ProjectDAO)gson.fromJson(jsonString, ProjectDAO.class);
      String usercode = (String)request.getParameter("usercode");
      String resourcecode = (String)request.getParameter("resourcecode");
      String orderBy = (String)request.getParameter("orderBy");
      orderBy = (orderBy == null || orderBy.equals("1")) ? "A.ACTIVIDAD" : (orderBy.equals("2") ? "A.NOMBRE_ACTIVIDAD" : "A.UND_MED");
      responseContext.setResultCode(ResultCode.FAIL);
      if (session.validateSessionUser(usercode)) {
        Store store = Store.getInstance();
        ActivityReportResource resource = store.getResourceByCode(resourcecode);
        ProjectDAO project = store.getProject(newProject.proyecto, resourcecode, orderBy);
        project.updateActivitiesValues(newProject);
        resp = store.updateProject(project, resource.getCode(), true);
        responseContext.setValue(Boolean.valueOf(resp));
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
