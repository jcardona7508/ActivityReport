package activityrecord.web.action;

import activityrecord.web.action.CommonWebAction;
import com.gausssoft.GausssoftException;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class GetResourcesChoicesAction extends CommonWebAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    return standardExecute("getResources", event, null);
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    doCommonGetEnd(event, response, 0);
  }
}
