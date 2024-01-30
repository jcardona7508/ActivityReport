package activityrecord.web.action;

import activityrecord.ract.ActivityReportResource;
import activityrecord.web.action.CommonWebAction;
import com.gausssoft.GausssoftException;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class AddResourceAction extends CommonWebAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    String resourceCode = event.getRequest().getParameter("list");
    return basicExecute(event, resourceCode, "getResource");
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    EventResponse modelResponse = response.getModelResponse();
    Session session = event.getRequest().getSession();
    if (modelResponse != null) {
      ActivityReportResource activityReportResource = (ActivityReportResource)modelResponse.getAttribute("RESOURCE");
      if (activityReportResource != null) {
        session.setAttribute("RESOURCE", activityReportResource);
        session.removeAttribute("ACTIVITIES");
        response.setNextEvent(new ApplicationEvent("getActivities", event.getRequest(), event.getApplicationContext()));
        response.setResultCode(0);
      } 
    } 
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
  }
}
