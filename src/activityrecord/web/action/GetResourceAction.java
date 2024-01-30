package activityrecord.web.action;

import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.WeekDetail;
import com.gausssoft.GausssoftException;
import common.user.User;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class GetResourceAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    User user = null;
    Session session = event.getRequest().getSession();
    user = (User)session.getAttribute("USER");
    EventResponse response = new EventResponse();
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    if (user != null) {
      if (resource != null)
        resource = null; 
      ApplicationEvent modelEvent = new ApplicationEvent("getResource", 
          event.getRequest(), 
          event.getApplicationContext());
      modelEvent.getRequest().setAttribute("resourceCode", 
          user.getLoginName());
      response.setModelEvent(modelEvent);
    } 
    WeekDetail weekDetail = (WeekDetail)session.getAttribute("ACTIVITIES");
    if (weekDetail == null)
      response.setNextEvent(new ApplicationEvent("getActivities", 
            event.getRequest(), event.getApplicationContext())); 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    EventResponse modelResponse = response.getModelResponse();
    Session session = event.getRequest().getSession();
    if (modelResponse != null) {
      ActivityReportResource activityReportResource = (ActivityReportResource)modelResponse.getAttribute("RESOURCE");
      if (activityReportResource != null) {
        session.setAttribute("RESOURCE", activityReportResource);
        response.setResultCode(0);
      } 
    } 
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
  }
}
