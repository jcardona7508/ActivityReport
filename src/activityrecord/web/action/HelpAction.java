package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class HelpAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    response.setResultCode(0);
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) throws GausssoftException {
    super.doEnd(event, response);
    String contexto = event.getRequest().getParameter("contexto");
    if (response.getResultCode() == 0)
      event.getRequest().setAttribute("contexto", contexto); 
  }
}
