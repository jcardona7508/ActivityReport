package activityrecord.web.action;

import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.WeekDetail;
import com.gausssoft.GausssoftException;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class GetActivitiesAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    Session session = event.getRequest().getSession();
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    WeekDetail weekDetail = (WeekDetail)session.getAttribute("ACTIVITIES");
    if (resource != null && weekDetail == null) {
      ApplicationEvent modelEvent = new ApplicationEvent("getActivities", 
          event.getRequest(), 
          event.getApplicationContext());
      if (resource != null)
        modelEvent.getRequest().setAttribute("resourceCode", 
            resource.getResourceCode()); 
      modelEvent.getRequest().setAttribute("mode", session.getAttribute("MODE"));
      response.setModelEvent(modelEvent);
    } 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    EventResponse modelResponse = response.getModelResponse();
    Session session = event.getRequest().getSession();
    if (modelResponse != null) {
      WeekDetail weekDetail1 = (WeekDetail)modelResponse.getAttribute("ACTIVITIES");
      if (weekDetail1 != null && 
        modelResponse.getResultCode() == 0) {
        session.setAttribute("ACTIVITIES", weekDetail1);
        response.setResultCode(0);
      } else {
        session.removeAttribute("ACTIVITIES");
        response.setResultCode(100);
      } 
    } 
    WeekDetail weekDetail = (WeekDetail)session.getAttribute("ACTIVITIES");
  }
}
