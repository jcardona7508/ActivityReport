package activityrecord.engine.action;

import activityrecord.data.RactDAO;
import activityrecord.engine.event.UpdateRactEvent;
import activityrecord.ract.Activity;
import activityrecord.ract.WeekDetail;
import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.sql.SQLManager;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class AddActivitiesModelAction extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    int pos;
    Session session = event.getRequest().getSession();
    EventResponse response = new EventResponse();
    String resourceCode = (String)event.getRequest().getAttribute("resourceCode");
    String activityList = event.getRequest().getParameter("list");
    try {
      pos = Integer.parseInt(event.getRequest().getParameter("position").trim());
    } catch (NumberFormatException e) {
      pos = 1;
    } 
    String[] activityCodes = activityList.split(",");
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext()
        .getAtribute("siscorp.framework.application.ComponentManager");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      SQLManager sqlManager = (SQLManager)manager.getComponent(
          "activityrecord.sqleditor", 
          "activityrecord");
      WeekDetail weekDetail = (WeekDetail)session.getAttribute("ACTIVITIES");
      if (resourceCode != null && weekDetail != null)
        for (int i = 0; i < activityCodes.length; i++) {
          Activity activity = RactDAO.getActivity(resourceCode, activityCodes[i], sqlManager);
          weekDetail.addActivity(activity, pos);
        }  
      response.setResultCode(0);
    } catch (Exception e) {
      System.out.println(e);
    } 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse eventResponse) {
    EventResponse response = eventResponse;
    UpdateRactEvent modelEvent = new UpdateRactEvent(event.getRequest(), event.getApplicationContext());
    modelEvent.setEventCode(0);
    response.setResultCode(1);
    response.setNextEvent((ApplicationEvent)modelEvent);
  }
}
