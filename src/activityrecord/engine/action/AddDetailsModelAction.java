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

public class AddDetailsModelAction extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    String activityCode, objectCode;
    Session session = event.getRequest().getSession();
    EventResponse response = new EventResponse();
    String resourceCode = (String)event.getRequest().getAttribute("resourceCode");
    String position = event.getRequest().getParameter("position").trim();
    if (position.indexOf("[|]") < 0) {
      activityCode = "";
      objectCode = position;
    } else {
      activityCode = position.substring(0, position.indexOf("[|]"));
      objectCode = position.substring(position.indexOf("[|]") + 3);
    } 
    String detailList = event.getRequest().getParameter("list");
    String[] detailCodes = detailList.split(",");
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext()
        .getAtribute("siscorp.framework.application.ComponentManager");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      SQLManager sqlManager = (SQLManager)manager.getComponent("activityrecord.sqleditor", 
          "activityrecord");
      WeekDetail weekDetail = (WeekDetail)session.getAttribute("ACTIVITIES");
      if (resourceCode != null && weekDetail != null)
        for (int i = 0; i < detailCodes.length; i++) {
          Activity activity = RactDAO.getDetail(resourceCode, 
              activityCode, 
              objectCode, 
              detailCodes[i], 
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
