package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.engine.event.UserEvent;
import common.util.ResourceString;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class AuthenticationTestAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    ResourceString resourceString = (ResourceString)event.getRequest()
      .getSession()
      .getAttribute("RESOURCESTRING");
    EventResponse response = new EventResponse();
    response.setResultCode(0);
    String user = event.getRequest().getParameter("user");
    String pass = event.getRequest().getParameter("pass");
    if (pass == null || pass.equals("") || user == null || user.equals("")) {
      event.getRequest().setAttribute("message", (resourceString != null) ? resourceString.getString("nouser") : "Debe proporcionar un usuario y un password.");
      return response;
    } 
    try {
      UserEvent modelEvent = new UserEvent(event.getRequest(), event.getApplicationContext());
      modelEvent.setEventCode(6);
      response.setResultCode(1);
      response.setModelEvent((ApplicationEvent)modelEvent);
    } catch (Exception e) {
      response.setAttribute("message", (resourceString != null) ? resourceString.getString("unknown") : ("Error:" + e.getMessage()));
    } 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) throws GausssoftException {
    ApplicationEvent nextEvent = new ApplicationEvent("showConfig", 
        event.getRequest(), 
        event.getApplicationContext());
    response.setNextEvent(nextEvent);
  }
}
