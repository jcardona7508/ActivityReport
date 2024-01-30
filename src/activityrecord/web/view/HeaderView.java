package activityrecord.web.view;

import common.web.view.GenericHtmlView;
import javax.servlet.http.HttpServletResponse;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class HeaderView extends GenericHtmlView {
  public void render(ApplicationEvent event, EventResponse response) throws Exception {
    HttpServletResponse httpResponse;
    switch (response.getResultCode()) {
      case 0:
      case 1:
        response.setAttribute("page", "/jsp/ReportHeader.jsp");
        httpResponse = 
          (HttpServletResponse)event.getRequest().getAttribute("HTTP_SERVLET_RESPONSE");
        httpResponse.addHeader("Cache-Control", "no-cache");
        break;
    } 
    super.render(event, response);
  }
}
