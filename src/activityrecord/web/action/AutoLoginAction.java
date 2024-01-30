package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.engine.event.UserEvent;
import common.general.text.Base64;
import common.user.User;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.system.DateSC;

public class AutoLoginAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    DateSC dateTime = new DateSC();
    String user = event.getRequest().getParameter("username");
    if (user == null || user.equals("null"))
      user = ""; 
    String key = event.getRequest().getParameter("key");
    if (key == null || key.equals("null"))
      key = ""; 
    String credencial = event.getRequest().getParameter("credencial");
    if (credencial == null || credencial.equals("null"))
      credencial = ""; 
    if ((!user.equals("") && !key.equals("") && !credencial.equals("")) || (
      !user.equals("") && key.contains("*AUTO*"))) {
      StringBuffer string = new StringBuffer(100);
      dateTime.setFormat("yyyy/MM/dd");
      String date = dateTime.toString();
      dateTime.setFormat("k");
      String time = dateTime.toString();
      string.append(user);
      string.append(key);
      string.append(date);
      string.append(time);
      String encode = (new Base64()).encode(string.toString());
      encode = encode.substring(0, credencial.length());
      if (encode.equals(credencial)) {
        UserEvent modelEvent = new UserEvent(event.getRequest(), event.getApplicationContext());
        modelEvent.setEventCode(0);
        response.setResultCode(1);
        event.getRequest().setAttribute("checkPassword", "false");
        response.setModelEvent((ApplicationEvent)modelEvent);
      } else {
        response.setResultCode(100);
        response.setAttribute("message", "AutoLoginAction.forbiddenAction");
        return response;
      } 
    } else {
      response.setResultCode(100);
      response.setAttribute("message", "AutoLoginAction.forbiddenAction");
      return response;
    } 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    if (response.getResultCode() == 0) {
      Session session = event.getRequest().getSession();
      User user = (User)response.getAttribute("user");
      session.setAttribute("USER", user);
      session.setAttribute("LOGGED", new Boolean(true));
      ApplicationEvent nextEvent = new ApplicationEvent("home", event.getRequest(), event.getApplicationContext());
      response.setNextEvent(nextEvent);
    } 
  }
}
