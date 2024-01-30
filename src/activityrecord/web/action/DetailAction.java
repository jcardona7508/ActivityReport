package activityrecord.web.action;

import activityrecord.ract.WeekDetail;
import com.gausssoft.GausssoftException;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class DetailAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    Session session = event.getRequest().getSession();
    EventResponse response = new EventResponse();
    WeekDetail weekDetail = (WeekDetail)session.getAttribute("ACTIVITIES");
    if (weekDetail == null) {
      ApplicationEvent nextEvent = new ApplicationEvent("getActivities", 
          event.getRequest(), 
          event.getApplicationContext());
      response.setNextEvent(nextEvent);
      response.setResultCode(1);
    } else {
      response.setResultCode(0);
    } 
    return response;
  }
}
