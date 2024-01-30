package activityrecord.web.action;

import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class MainAction extends GenericAction {
  public void doEnd(ApplicationEvent event, EventResponse response) {
    String autoLoginValue = "";
    Boolean autoLogin = Boolean.valueOf(false);
    autoLoginValue = (String)event.getRequest().getSession().getAttribute("AUTOLOGIN");
    if (autoLoginValue != null && autoLoginValue.equals("T"))
      autoLogin = Boolean.valueOf(true); 
    if (autoLogin.booleanValue()) {
      ApplicationEvent nextEvent = new ApplicationEvent("login", event.getRequest(), event.getApplicationContext());
      response.setNextEvent(nextEvent);
    } 
    response.setResultCode(0);
  }
}
