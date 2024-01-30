package activityrecord.engine.action;

import activityrecord.data.RactDAO;
import activityrecord.ract.ActivityReportResource;
import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.sql.SQLManager;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class GetResourceModelAction extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    String resourceCode = (String)event.getRequest().getAttribute("resourceCode");
    GenericComponentManager manager = 
      (GenericComponentManager)event.getApplicationContext()
      .getAtribute("siscorp.framework.application.ComponentManager");
    if (manager == null)
      throw new GausssoftException("Component Manager = NULL "); 
    SQLManager sqlManager = 
      (SQLManager)manager.getComponent("activityrecord.sqleditor", "activityrecord");
    if (resourceCode != null && sqlManager != null) {
      ActivityReportResource resource = RactDAO.getResourceByCode(resourceCode, sqlManager);
      response.setAttribute("RESOURCE", resource);
    } else {
      throw new GausssoftException("Error in class GetResourceModelAction.  Resource Code=" + resourceCode);
    } 
    response.setResultCode(0);
    return response;
  }
}
