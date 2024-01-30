package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.engine.event.UserEvent;
import common.util.ResourceString;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class CheckConfigLoginAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    ResourceString resourceString = (ResourceString)event.getRequest()
      .getSession()
      .getAttribute("RESOURCESTRING");
    EventResponse response = new EventResponse();
    String pass = event.getRequest().getParameter("password");
    String activeLDAP = event.getRequest().getParameter("ipServer");
    if (activeLDAP == null && (pass == null || pass.equals(""))) {
      response.setResultCode(100);
      response.setAttribute("message", (resourceString != null) ? resourceString.getString("nopass") : "Debe proporcionar una clave");
      return response;
    } 
    try {
      UserEvent modelEvent = new UserEvent(event.getRequest(), 
          event.getApplicationContext());
      if (pass != null) {
        modelEvent.setEventCode(3);
      } else {
        modelEvent.setEventCode(5);
      } 
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
  
  public void doEnd(ApplicationEvent event, EventResponse response) throws GausssoftException {
    if (response.getResultCode() == 0) {
      ApplicationEvent nextEvent = new ApplicationEvent("showConfig", 
          event.getRequest(), 
          event.getApplicationContext());
      response.setNextEvent(nextEvent);
    } 
  }
}
