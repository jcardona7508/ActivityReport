package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class GetChoicesAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    response.setResultCode(0);
  }
}
