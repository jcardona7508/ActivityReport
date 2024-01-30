package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.engine.event.SqlEvent;
import common.util.ResourceString;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class SQLCommonAction extends GenericAction {
  public static final int SQL_LIST = 0;
  
  public static final int SQL_EDIT = 1;
  
  public static final int SQL_UPDATE = 2;
  
  public static final int SQL_ADD = 3;
  
  public static final int SQL_SAVE = 4;
  
  public static final int SQL_DELETE = 5;
  
  public static final int SQL_PROBE = 6;
  
  public static final int SQL_LIST_GROUP = 7;
  
  private int sqlCommand;
  
  public SQLCommonAction(int sqlCommand) {
    this.sqlCommand = sqlCommand;
  }
  
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    ResourceString resourceString = (ResourceString)event.getRequest()
      .getSession().getAttribute("RESOURCESTRING");
    try {
      SqlEvent modelEvent = new SqlEvent(event.getRequest(), event.getApplicationContext());
      modelEvent.setEventCode(this.sqlCommand);
      response.setResultCode(1);
      response.setModelEvent((ApplicationEvent)modelEvent);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      response.setResultCode(200);
      response.setAttribute("message", resourceString.getString("unknown"));
    } 
    return response;
  }
}
