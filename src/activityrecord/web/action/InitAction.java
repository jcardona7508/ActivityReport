package activityrecord.web.action;

import activityrecord.data.RactDAO;
import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.web.action.CommonWebAction;
import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.sql.SQLManager;
import common.util.ResourceString;
import common.web.ClientInfo;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.ResourceManager;
import siscorp.framework.application.Session;
import siscorp.framework.application.web.WebContext;
import siscorp.framework.application.web.WebRequest;

public class InitAction extends CommonWebAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    ResourceString resourceString;
    WebContext context = (WebContext)event.getApplicationContext();
    String applicationName = context.getInitParameter("ApplicationName", 
        "WebApplication");
    Session session = event.getRequest().getSession();
    session.setTimeout(7200);
    WebRequest webRequest = (WebRequest)event.getRequest();
    HttpServletRequest h = webRequest.getHttpRequest();
    if (session.getAttribute("language") == null) {
      Cookie cookie1 = getCookie("language", h);
      if (cookie1 != null) {
        session.setAttribute("language", cookie1.getValue());
      } else {
        if (event.getRequest().getParameter("select") != null) {
          cookie1 = new Cookie("language", event.getRequest().getParameter("select"));
        } else {
          cookie1 = new Cookie("language", "ES");
        } 
        cookie1.setMaxAge(5184000);
        cookie1.setPath("/");
        HttpServletResponse res = (HttpServletResponse)webRequest.getAttribute(
            "HTTP_SERVLET_RESPONSE");
        res.addCookie(cookie1);
        session.setAttribute("language", "ES");
      } 
    } 
    Cookie cookie = getCookie("mode", h);
    if (cookie != null) {
      session.setAttribute("MODE", cookie.getValue());
    } else {
      cookie = new Cookie("mode", "1day");
      cookie.setMaxAge(5184000);
      cookie.setPath("/");
      HttpServletResponse res = (HttpServletResponse)webRequest.getAttribute(
          "HTTP_SERVLET_RESPONSE");
      res.addCookie(cookie);
      session.setAttribute("MODE", "1day");
    } 
    ResourceManager webResource = event.getApplicationContext().getResourceManager();
    ClientInfo clientInfo = new ClientInfo();
    clientInfo.setScreenWidth(event.getRequest().getParameter("screenWidth"));
    clientInfo.addSupportedResolution(Integer.valueOf(800));
    clientInfo.addSupportedResolution(Integer.valueOf(1024));
    clientInfo.setScreenHeight(event.getRequest().getParameter("screenHeigth"));
    clientInfo.setNameNavigator(event.getRequest().getParameter("nameNavigator"));
    clientInfo.setNavigatorVersion(event.getRequest().getParameter("navigatorVersion"));
    clientInfo.setPlataform(event.getRequest().getParameter("plataform"));
    session.setAttribute("CLIENTINFO", clientInfo);
    InputStream inputResourceString = webResource.getResourceAsStream("/", 
        "ResourceString" + session
        .getAttribute("language") + ".properties");
    try {
      resourceString = new ResourceString(inputResourceString);
    } catch (IOException exception) {
      String lang = (String)session.getAttribute("language");
      System.out.println("Exception " + exception);
      if (lang.equals("ES"))
        throw new GausssoftException(
            "Error de inicialización del archivo de recursos de texto ", 
            exception); 
      if (lang.equals("FR"))
        throw new GausssoftException(
            "Erreur d'initialisation de fichier de ressources de chaîne", 
            exception); 
      throw new GausssoftException("Resource String File Initialization Error", 
          exception);
    } 
    session.setAttribute("RESOURCESTRING", resourceString);
    ActivityRecordCalendar calendario = new ActivityRecordCalendar();
    String dateFormat = event.getApplicationContext().getInitParameter("DisplayDateFormat");
    if (dateFormat == null)
      dateFormat = "yyyy/MM/dd"; 
    calendario.setDateFormat(new SimpleDateFormat(dateFormat));
    String lenguage = (String)session.getAttribute("language");
    session.setAttribute("DATEFORMAT", dateFormat);
    session.setAttribute("CURRENTDATE", calendario.getCurrentTime().toString());
    session.setAttribute("LOCALE", new Locale(lenguage.toLowerCase()));
    GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext().getAtribute(
        "siscorp.framework.application.ComponentManager");
    SQLManager sqlManager = (SQLManager)manager.getComponent("activityrecord.sqleditor", 
        "activityrecord");
    session.setAttribute("RACT_PARAMETERS", RactDAO.getRactParameters(sqlManager));
    session.setAttribute("WEB_RESOURCE", webResource);
    EventResponse response = new EventResponse();
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    ApplicationEvent nextEvent = new ApplicationEvent("open", event.getRequest());
    response.setNextEvent(nextEvent);
    response.setResultCode(0);
    event.getRequest().getSession().removeAttribute("ACTIVITIES");
    event.getRequest().getSession().removeAttribute("RESOURCE");
  }
}
