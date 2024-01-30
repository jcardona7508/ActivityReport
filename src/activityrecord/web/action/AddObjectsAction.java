package activityrecord.web.action;

import activityrecord.web.action.CommonWebAction;
import com.gausssoft.GausssoftException;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class AddObjectsAction extends CommonWebAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    return standardExecute("addObjects", event, "loadActivities");
  }
}
