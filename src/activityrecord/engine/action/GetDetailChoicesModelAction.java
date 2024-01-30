package activityrecord.engine.action;

import activityrecord.data.Condition;
import activityrecord.data.RactDAO;
import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.sql.SQLManager;
import java.util.List;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class GetDetailChoicesModelAction extends DefaultHandler {
  public void doStart(ApplicationEvent event) {
    String filter = "";
    String columns = event.getRequest().getParameter("columns");
    String option = event.getRequest().getParameter("option");
    filter = event.getRequest().getParameter("filter");
    if (columns != null && !columns.equals("")) {
      event.getRequest().setAttribute("columns", columns.split(","));
    } else {
      event.getRequest().setAttribute("columns", new String[0]);
    } 
    if (option != null && !option.equals("")) {
      event.getRequest().setAttribute("option", 
          new Integer(Integer.parseInt(option)));
    } else {
      event.getRequest().setAttribute("option", new Integer(2));
    } 
    if (filter != null && !filter.equals("")) {
      event.getRequest().setAttribute("filter", filter);
    } else {
      event.getRequest().setAttribute("filter", "");
    } 
  }
  
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    String activityCode, objectCode, filter = "";
    EventResponse response = new EventResponse();
    String[] columns = new String[0];
    response = new EventResponse();
    columns = (String[])event.getRequest().getAttribute("columns");
    filter = (String)event.getRequest().getAttribute("filter");
    String resourceCode = (String)event.getRequest().getAttribute("resourceCode");
    String name = event.getRequest().getParameter("name");
    if (name.indexOf("[|]") < 0) {
      activityCode = "";
      objectCode = name;
    } else {
      activityCode = name.substring(0, name.indexOf("[|]"));
      objectCode = name.substring(name.indexOf("[|]") + 3);
    } 
    int option = ((Integer)event.getRequest().getAttribute("option")).intValue();
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext()
        .getAtribute("siscorp.framework.application.ComponentManager");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      SQLManager sqlManager = (SQLManager)manager.getComponent("activityrecord.sqleditor", 
          "activityrecord");
      if (resourceCode != null) {
        Condition condition = new Condition();
        String AND = condition.createCondition(columns, option, filter);
        List list = RactDAO.getObjectDetail(resourceCode, 
            activityCode, 
            objectCode, 
            AND, 
            sqlManager);
        if (list != null && list.size() > 0)
          response.setAttribute("CHOICES", list); 
      } 
      response.setResultCode(0);
    } catch (Exception e) {
      System.out.println(e);
    } 
    return response;
  }
}
