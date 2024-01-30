package activityrecord.web.action;

import activityrecord.engine.event.UpdateRactEvent;
import activityrecord.ract.ActivityReportResource;
import com.gausssoft.GausssoftException;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class UpdateAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    Session session = event.getRequest().getSession();
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    if (resource == null)
      return response; 
    try {
      UpdateRactEvent modelEvent = new UpdateRactEvent(event.getRequest(), 
          event.getApplicationContext());
      modelEvent.setEventCode(0);
      response.setModelEvent((ApplicationEvent)modelEvent);
      response.setResultCode(1);
      event.getRequest().setAttribute("save", "true");
    } catch (Exception e) {
      response.setResultCode(200);
      response.setAttribute("message", "Error indeterminado ver log");
    } 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    Session session = event.getRequest().getSession();
    if (response.getModelResponse().getResultCode() == 0) {
      session.removeAttribute("ACTIVITIES");
      response.setNextEvent(new ApplicationEvent("getActivities", event.getRequest(), 
            event.getApplicationContext()));
      response.setResultCode(0);
    } else {
      response.setResultCode(100);
      response.setAttribute("message", "Error recuperando actividades");
    } 
  }
}
