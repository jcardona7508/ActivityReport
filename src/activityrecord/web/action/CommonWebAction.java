package activityrecord.web.action;

import activityrecord.html.HTMLActivityChoices;
import activityrecord.html.HTMLActivityColumns;
import activityrecord.html.HTMLDetailChoices;
import activityrecord.html.HTMLDetailColumns;
import activityrecord.html.HTMLObjectChoices;
import activityrecord.html.HTMLObjectColumns;
import activityrecord.html.HTMLResourceChoices;
import activityrecord.html.HTMLResourceColumns;
import activityrecord.ract.ActivityReportResource;
import com.gausssoft.GausssoftException;
import common.util.ResourceString;
import common.web.action.GenericAction;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class CommonWebAction extends GenericAction {
  protected static final int IS_RESOURCES = 0;
  
  protected static final int IS_ACTIVITIES = 2;
  
  protected static final int IS_OBJECTS = 1;
  
  protected static final int IS_DETAILS = 3;
  
  protected EventResponse standardExecute(String applicationEventName, ApplicationEvent event, String nextEventName) throws GausssoftException {
    EventResponse response = new EventResponse();
    Session session = event.getRequest().getSession();
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    ApplicationEvent modelEvent = new ApplicationEvent(applicationEventName, 
        event.getRequest(), 
        event.getApplicationContext());
    if (nextEventName != null) {
      ApplicationEvent nextEvent = new ApplicationEvent(nextEventName, event.getRequest(), 
          event.getApplicationContext());
      response.setNextEvent(nextEvent);
    } 
    if (resource != null)
      event.getRequest().setAttribute("resourceCode", resource.getCode()); 
    response.setModelEvent(modelEvent);
    return response;
  }
  
  protected EventResponse basicExecute(ApplicationEvent event, String resourceCode, String applicationEventName) {
    EventResponse response = new EventResponse();
    event.getRequest().setAttribute("resourceCode", resourceCode);
    ApplicationEvent modelEvent = new ApplicationEvent(applicationEventName, 
        event.getRequest(), 
        event.getApplicationContext());
    response.setModelEvent(modelEvent);
    return response;
  }
  
  public Cookie getCookie(String name, HttpServletRequest request) {
    Cookie result = null;
    int i = 0;
    Cookie[] list = request.getCookies();
    if (list != null)
      while (i < list.length) {
        if (list[i].getName().equalsIgnoreCase(name)) {
          result = list[i];
          break;
        } 
        i++;
      }  
    return result;
  }
  
  public void doCommonGetEnd(ApplicationEvent event, EventResponse response, int type) {
    List choices = (List)response.getModelResponse().getAttribute("CHOICES");
    Session session = event.getRequest().getSession();
    session.setAttribute("CHOICES", null);
    session.setAttribute("COLUMNS", null);
    String language = (String)session.getAttribute("language");
    if (language == null)
      language = "EN"; 
    if (response.getModelResponse().getResultCode() == 0 && 
      choices != null) {
      HTMLResourceChoices hTMLResourceChoices;
      HTMLActivityChoices hTMLActivityChoices;
      HTMLObjectChoices hTMLObjectChoices;
      HTMLDetailChoices htmlChoices;
      HTMLResourceColumns hTMLResourceColumns;
      HTMLActivityColumns hTMLActivityColumns;
      HTMLObjectColumns hTMLObjectColumns;
      HTMLDetailColumns htmlColumns;
      ResourceString i18nTexts = (ResourceString)session.getAttribute("RESOURCESTRING");
      switch (type) {
        case 0:
          hTMLResourceChoices = new HTMLResourceChoices(choices, i18nTexts);
          hTMLResourceColumns = new HTMLResourceColumns(session);
          session.setAttribute("CHOICES", hTMLResourceChoices);
          session.setAttribute("COLUMNS", hTMLResourceColumns);
          break;
        case 2:
          hTMLActivityChoices = new HTMLActivityChoices(choices, i18nTexts);
          hTMLActivityColumns = new HTMLActivityColumns(session);
          session.setAttribute("CHOICES", hTMLActivityChoices);
          session.setAttribute("COLUMNS", hTMLActivityColumns);
          break;
        case 1:
          hTMLObjectChoices = new HTMLObjectChoices(choices, i18nTexts);
          hTMLObjectColumns = new HTMLObjectColumns(session);
          session.setAttribute("CHOICES", hTMLObjectChoices);
          session.setAttribute("COLUMNS", hTMLObjectColumns);
          break;
        case 3:
          htmlChoices = new HTMLDetailChoices(choices, i18nTexts);
          htmlColumns = new HTMLDetailColumns(session);
          session.setAttribute("CHOICES", htmlChoices);
          session.setAttribute("COLUMNS", htmlColumns);
          break;
      } 
    } 
    response.setResultCode(0);
  }
}
