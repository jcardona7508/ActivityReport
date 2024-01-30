package activityrecord.engine.action;

import activityrecord.data.RactDAO;
import activityrecord.ract.Activity;
import activityrecord.ract.WeekDetail;
import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.sql.SQLManager;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class AddObjectsModelAction extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    Session session = event.getRequest().getSession();
    EventResponse response = new EventResponse();
    String resourceCode = (String)event.getRequest().getAttribute("resourceCode");
    String activityList = event.getRequest().getParameter("list");
    String activityCode = event.getRequest().getParameter("position").trim();
    String[] objectCodes = activityList.split(",");
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext()
        .getAtribute("siscorp.framework.application.ComponentManager");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      SQLManager sqlManager = (SQLManager)manager.getComponent("activityrecord.sqleditor", 
          "activityrecord");
      WeekDetail weekDetail = (WeekDetail)session.getAttribute("ACTIVITIES");
      if (resourceCode != null && weekDetail != null)
        for (int i = 0; i < objectCodes.length; i++) {
          Activity activity = RactDAO.getObject(resourceCode, 
              activityCode, 
              objectCodes[i], 
              sqlManager);
          weekDetail.addActivity(activity);
        }  
      response.setResultCode(0);
    } catch (Exception e) {
      System.out.println(e);
    } 
    return response;
  }
}
