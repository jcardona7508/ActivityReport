package activityrecord.engine.action;

import com.gausssoft.GausssoftException;
import common.engine.action.DefaultHandler;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class DuplicateModelAction extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    System.out.println(event.getRequest().getAttribute("position"));
    System.out.println(event.getRequest().getAttribute("operation"));
    return null;
  }
}
