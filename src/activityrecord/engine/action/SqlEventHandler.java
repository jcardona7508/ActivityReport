package activityrecord.engine.action;

import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.engine.event.SqlEvent;
import common.sql.SQLEntry;
import common.sql.SQLManager;
import java.util.ArrayList;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.ApplicationRequest;
import siscorp.framework.application.EventResponse;

public class SqlEventHandler extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    ArrayList listSql;
    SQLEntry sQLEntry1;
    String[] selected;
    SQLEntry sql;
    String parametros;
    int idsql = 0;
    SqlEvent sqlEvent = (SqlEvent)event;
    EventResponse response = new EventResponse();
    GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext()
      .getAtribute("siscorp.framework.application.ComponentManager");
    SQLManager sqlManager = (SQLManager)manager.getComponent("activityrecord.sqleditor", 
        "activityrecord");
    switch (sqlEvent.getEventCode()) {
      case 7:
        listSql = sqlManager.getSqlGroupList();
        event.getRequest().setAttribute("sqlList", listSql);
        response.setResultCode(0);
        return response;
      case 0:
        listSql = sqlManager.getSqlList(event.getRequest().getParameter("group"));
        event.getRequest().setAttribute("sqlList", listSql);
        response.setResultCode(0);
        return response;
      case 1:
        idsql = Integer.valueOf(event.getRequest().getParameter("idsql")).intValue();
        event.getRequest().setAttribute("sql", sqlManager.findSQL(idsql));
        response.setResultCode(0);
        return response;
      case 2:
        if (event.getRequest().getParameter("idsql") != null) {
          idsql = Integer.valueOf(event.getRequest().getParameter("idsql")).intValue();
          sql = sqlManager.findSQL(idsql);
          sql.setSqlStatement(sql.getSqlStatement().replaceAll("\n", " "));
          loadFromRequest(sql, event.getRequest());
          sqlManager.updateSQL(sql);
        } 
        listSql = sqlManager.getSqlList(event.getRequest().getParameter("group"));
        event.getRequest().setAttribute("sqlList", listSql);
        response.setResultCode(0);
        return response;
      case 3:
        response.setResultCode(0);
        return response;
      case 4:
        sQLEntry1 = new SQLEntry();
        loadFromRequest(sQLEntry1, event.getRequest());
        sQLEntry1.setSqlStatement(sQLEntry1.getSqlStatement().replaceAll("\n", " "));
        sqlManager.insertSQL(sQLEntry1);
        listSql = sqlManager.getSqlList(event.getRequest().getParameter("group"));
        event.getRequest().setAttribute("sqlList", listSql);
        response.setResultCode(0);
        return response;
      case 5:
        selected = event.getRequest().getParameterValues("sqlSelected");
        if (selected != null)
          for (int i = 0; i < selected.length; i++) {
            SQLEntry sQLEntry = sqlManager.findSQL((new Integer(selected[i])).intValue());
            sqlManager.deleteSQL(sQLEntry.getId());
          }  
        listSql = sqlManager.getSqlList(event.getRequest().getParameter("group"));
        event.getRequest().setAttribute("sqlList", listSql);
        response.setResultCode(0);
        return response;
      case 6:
        if (event.getRequest().getParameter("idsql") != null) {
          idsql = Integer.valueOf(event.getRequest().getParameter("idsql")).intValue();
          sql = new SQLEntry();
          sql.setId(idsql);
          loadFromRequest(sql, event.getRequest());
        } else {
          sql = new SQLEntry();
          loadFromRequest(sql, event.getRequest());
          sql.setId(-1);
        } 
        parametros = event.getRequest().getParameter("parametros").trim();
        event.getRequest().setAttribute("sql", sql);
        event.getRequest().setAttribute("parametros", parametros);
        event.getRequest().setAttribute("db", sqlManager.getDatabase());
        event.getRequest().setAttribute("action", "probe");
        listSql = sqlManager.getSqlList(event.getRequest().getParameter("group"));
        event.getRequest().setAttribute("sqlList", listSql);
        response.setResultCode(0);
        return response;
    } 
    response.setResultCode(100);
    response.setAttribute("message", "Evento Desconocido");
    return response;
  }
  
  public void loadFromRequest(SQLEntry sql, ApplicationRequest request) {
    sql.setName((request.getParameter("nombre") == null) ? "" : request.getParameter("nombre"));
    sql.setDescripcion((request.getParameter("descripcion") == null) ? "" : request.getParameter("descripcion"));
    sql.setGroup((request.getParameter("grupo") == null) ? "none" : request.getParameter("grupo"));
    sql.setSqlStatement((request.getParameter("sql") == null) ? "" : request.getParameter("sql"));
    sql.setType((request.getParameter("typesql") == null) ? -1 : Integer.valueOf(request.getParameter("typesql")).intValue());
  }
}
