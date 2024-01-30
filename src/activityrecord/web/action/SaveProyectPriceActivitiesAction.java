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
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.framework.application.web.WebRequest;

public class SaveProyectPriceActivitiesAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    String line = null;
    boolean resp = false;
    Gson gson = new Gson();
    StringBuffer jb = new StringBuffer();
    ResourceString resourceString = (ResourceString)event.getRequest()
      .getSession()
      .getAttribute("RESOURCESTRING");
    EventResponse response = new EventResponse();
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext().getAtribute("siscorp.framework.application.ComponentManager");
      SQLManager sqlManager = (SQLManager)manager.getComponent("activityrecord.sqleditor", "activityrecord");
      WebRequest webRequest = (WebRequest)event.getRequest();
      BufferedReader reader = webRequest.getHttpRequest().getReader();
      while ((line = reader.readLine()) != null)
        jb.append(line); 
      String jsonString = jb.toString();
      ProjectDAO newProject = (ProjectDAO)gson.fromJson(jsonString, ProjectDAO.class);
      Session session = event.getRequest().getSession();
      ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
      response.setResultCode(0);
      String orderBy = event.getRequest().getParameter("orderBy");
      orderBy = (orderBy == null || orderBy.equals("1")) ? "A.ACTIVIDAD" : (orderBy.equals("2") ? "A.NOMBRE_ACTIVIDAD" : "A.UND_MED");
      ProjectDAO project = RactDAO.getProyect(newProject.proyecto, resource.getCode(), sqlManager, orderBy);
      project.updateActivitiesValues(newProject);
      resp = RactDAO.updateProject(project, sqlManager, resource.getCode(), true);
      response.setResultCode(0);
      HttpServletResponse httpResponse = (HttpServletResponse)event.getRequest().getAttribute("HTTP_SERVLET_RESPONSE");
      httpResponse.setHeader("Cache-Control", "no-cache,no-store");
      httpResponse.setCharacterEncoding("UTF-8");
      httpResponse.setContentType("application/json;charset=UTF-8");
      PrintWriter out = httpResponse.getWriter();
      buildJSonResponse(out, resp);
    } catch (Exception e) {
      response.setResultCode(200);
      response.setAttribute("message", (resourceString != null) ? resourceString.getString("unknown") : ("Error:" + e.getMessage()));
    } 
    return response;
  }
  
  public void buildJSonResponse(PrintWriter out, boolean resp) {
    JSonResponse json = new JSonResponse();
    Gson gson = (new GsonBuilder()).serializeNulls().create();
    json.status = "SUCCESS";
    json.response = Boolean.valueOf(resp);
    String header = gson.toJson(json);
    out.println(header);
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) throws GausssoftException {}
}
