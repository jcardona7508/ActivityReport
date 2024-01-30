package activityrecord.web.action;

import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class CalendarAction extends GenericAction {
  public void doEnd(ApplicationEvent event, EventResponse response) {
    response.setResultCode(0);
  }
}
