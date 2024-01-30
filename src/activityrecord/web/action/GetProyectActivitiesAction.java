package activityrecord.web.action;

import activityrecord.data.ProjectDAO;
import activityrecord.data.RactDAO;
import activityrecord.ract.ActivityReportResource;
import com.gausssoft.GausssoftException;
import com.gausssoft.web.JSonResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.engine.GenericComponentManager;
import common.sql.SQLManager;
import common.util.ResourceString;
import common.web.action.GenericAction;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class GetProyectActivitiesAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    ResourceString resourceString = (ResourceString)event.getRequest()
      .getSession()
      .getAttribute("RESOURCESTRING");
    EventResponse response = new EventResponse();
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext().getAtribute("siscorp.framework.application.ComponentManager");
      SQLManager sqlManager = (SQLManager)manager.getComponent("activityrecord.sqleditor", "activityrecord");
      String proyectCode = event.getRequest().getParameter("proyectCode");
      String orderBy = event.getRequest().getParameter("orderBy");
      orderBy = (orderBy == null || orderBy.equals("1")) ? "A.ACTIVIDAD" : (orderBy.equals("2") ? "A.NOMBRE_ACTIVIDAD" : "A.UND_MED");
      Session session = event.getRequest().getSession();
      ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
      ProjectDAO proyect = RactDAO.getProyect(proyectCode, resource.getCode(), sqlManager, orderBy);
      session.setAttribute("PROJECT", proyect);
      response.setResultCode(0);
      HttpServletResponse httpResponse = (HttpServletResponse)event.getRequest().getAttribute("HTTP_SERVLET_RESPONSE");
      httpResponse.setHeader("Cache-Control", "no-cache,no-store");
      httpResponse.setCharacterEncoding("UTF-8");
      httpResponse.setContentType("application/json;charset=UTF-8");
      PrintWriter out = httpResponse.getWriter();
      buildJSonResponse(out, proyect);
    } catch (Exception e) {
      response.setResultCode(200);
      response.setAttribute("message", (resourceString != null) ? resourceString.getString("unknown") : ("Error:" + e.getMessage()));
    } 
    return response;
  }
  
  public void buildJSonResponse(PrintWriter out, ProjectDAO proyect) {
    JSonResponse json = new JSonResponse();
    Gson gson = (new GsonBuilder()).serializeNulls().create();
    json.status = "SUCCESS";
    json.response = proyect;
    String header = gson.toJson(json);
    out.println(header);
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) throws GausssoftException {}
}
