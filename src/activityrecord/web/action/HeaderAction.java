package activityrecord.web.action;

import activityrecord.ract.ActivityReportResource;
import com.gausssoft.GausssoftException;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class HeaderAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    Session session = event.getRequest().getSession();
    EventResponse response = new EventResponse();
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    if (resource == null) {
      ApplicationEvent nextEvent = new ApplicationEvent("getResource", event.getRequest(), event.getApplicationContext());
      response.setNextEvent(nextEvent);
      response.setResultCode(1);
    } else {
      ApplicationEvent nextEvent = new ApplicationEvent("weekSummary", event.getRequest(), event.getApplicationContext());
      response.setNextEvent(nextEvent);
      response.setResultCode(0);
    } 
    return response;
  }
}
