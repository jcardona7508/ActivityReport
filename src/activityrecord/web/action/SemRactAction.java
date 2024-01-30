package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.user.User;
import common.web.action.GenericAction;
import siscorp.dbmanagement.DatabaseSC;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class SemRactAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    Session session = event.getRequest().getSession();
    User user = (User)session.getAttribute("USER");
    GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext().getAtribute("siscorp.framework.application.ComponentManager");
    if (user == null)
      return response; 
    if (manager == null)
      throw new GausssoftException("Component Manager is NULL ", new NullPointerException()); 
    DatabaseSC db = manager.getDatabase("activityrecord");
    event.getRequest().setAttribute("BD", db);
    event.getRequest().setAttribute("mode", "week");
    event.getRequest().setAttribute("fecha", event.getRequest().getParameter("fecha"));
    response.setResultCode(1);
    return response;
  }
}
