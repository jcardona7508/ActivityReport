package activityrecord.web.action;

import com.gausssoft.GausssoftException;
import common.util.ResourceString;
import common.web.action.GenericAction;
import java.io.IOException;
import java.io.InputStream;
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

public class ChangeLanguageAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    WebRequest webRequest = (WebRequest)event.getRequest();
    HttpServletRequest h = webRequest.getHttpRequest();
    int posCookie = 0;
    boolean bandera = false;
    WebContext context = (WebContext)event.getApplicationContext();
    String applicationName = context.getInitParameter("ApplicationName", 
        "WebApplication");
    Cookie[] cookies = h.getCookies();
    for (int i = 0; i < cookies.length; i++) {
      if (cookies[i].getName().equals("language")) {
        bandera = true;
        posCookie = i;
        break;
      } 
    } 
    if (bandera)
      if (cookies[posCookie].getName().equalsIgnoreCase("language")) {
        ResourceString resourceString;
        Session session = event.getRequest().getSession();
        session.setAttribute("language", event.getRequest().getParameter("select"));
        HttpServletResponse res = (HttpServletResponse)webRequest.getAttribute("HTTP_SERVLET_RESPONSE");
        Cookie cookie = null;
        cookie = cookies[posCookie];
        cookie.setMaxAge(0);
        res.addCookie(cookie);
        String language = event.getRequest().getParameter("select");
        Cookie cookie2 = new Cookie("language", language);
        res = (HttpServletResponse)webRequest.getAttribute("HTTP_SERVLET_RESPONSE");
        cookie2.setMaxAge(5184000);
        cookie2.setPath("/");
        res.addCookie(cookie2);
        ResourceManager webResource = event.getApplicationContext().getResourceManager();
        session.setAttribute("LOCALE", new Locale(language.toLowerCase()));
        InputStream inputResourceString = webResource.getResourceAsStream("/" + applicationName + 
            "Resources/", "ResourceString" + session.getAttribute("language") + 
            ".properties");
        try {
          resourceString = new ResourceString(inputResourceString);
        } catch (IOException exception) {
          System.out.println("Exception " + exception);
          throw new GausssoftException("Error loading Resource String  File", 
              exception);
        } 
        session.setAttribute("RESOURCESTRING", resourceString);
      }  
    EventResponse response = new EventResponse();
    response.setResultCode(2);
    return response;
  }
}
