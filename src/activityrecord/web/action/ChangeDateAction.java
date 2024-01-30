package activityrecord.web.action;

import activityrecord.engine.event.UpdateRactEvent;
import com.gausssoft.GausssoftException;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class ChangeDateAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    try {
      UpdateRactEvent modelEvent = new UpdateRactEvent(event.getRequest(), event.getApplicationContext());
      modelEvent.setEventCode(0);
      response.setResultCode(1);
      response.setModelEvent((ApplicationEvent)modelEvent);
    } catch (Exception e) {
      response.setResultCode(200);
      response.setAttribute("message", "Error indeterminado ver log");
    } 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    Session session = event.getRequest().getSession();
    if (response.getResultCode() == 0) {
      session.removeAttribute("ACTIVITIES");
      response.setNextEvent(new ApplicationEvent("getActivities", 
            event.getRequest(), event.getApplicationContext()));
      response.setResultCode(0);
    } else {
      response.setResultCode(100);
      response.setAttribute("message", "Error recuperando actividades");
    } 
  }
}
