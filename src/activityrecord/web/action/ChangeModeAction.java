package activityrecord.web.action;

import activityrecord.web.action.CommonWebAction;
import com.gausssoft.GausssoftException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.framework.application.web.WebRequest;

public class ChangeModeAction extends CommonWebAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    WebRequest webRequest = (WebRequest)event.getRequest();
    try {
      Session session = event.getRequest().getSession();
      session.setAttribute("MODE", event.getRequest()
          .getParameter("mode"));
      HttpServletRequest httpRequest = webRequest.getHttpRequest();
      Cookie cookie = getCookie("mode", httpRequest);
      if (cookie != null)
        cookie.setMaxAge(0); 
      cookie = new Cookie("mode", (String)session.getAttribute("MODE"));
      cookie.setMaxAge(5184000);
      cookie.setPath("/");
      HttpServletResponse httpResponse = (HttpServletResponse)webRequest
        .getAttribute("HTTP_SERVLET_RESPONSE");
      httpResponse.addCookie(cookie);
      response.setNextEvent(new ApplicationEvent("goToDate", 
            event.getRequest(), 
            event.getApplicationContext()));
      response.setResultCode(1);
    } catch (Exception e) {
      response.setResultCode(200);
      response.setAttribute("message", "Error indeterminado ver log");
    } 
    return response;
  }
}
