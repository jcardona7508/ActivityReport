package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.engine.event.UserEvent;
import common.util.ResourceString;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class UnInstallApplicationAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    ResourceString resourceString = (ResourceString)event.getRequest()
      .getSession()
      .getAttribute("RESOURCESTRING");
    EventResponse response = new EventResponse();
    try {
      UserEvent modelEvent = new UserEvent(event.getRequest(), 
          event.getApplicationContext());
      modelEvent.setEventCode(9);
      response.setResultCode(1);
      response.setModelEvent((ApplicationEvent)modelEvent);
      if (event.getRequest().getAttribute("message") != null)
        response.setAttribute("message", event.getRequest().getAttribute("message")); 
    } catch (Exception e) {
      response.setResultCode(200);
      response.setAttribute("message", (resourceString != null) ? resourceString.getString("unknown") : ("Error:" + e.getMessage()));
    } 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) throws GausssoftException {}
}
