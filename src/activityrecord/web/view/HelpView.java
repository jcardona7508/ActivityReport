package activityrecord.web.view;

import common.web.view.GenericHtmlView;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class HelpView extends GenericHtmlView {
  public void render(ApplicationEvent event, EventResponse response) throws Exception {
    String contexto = event.getRequest().getParameter("contexto");
    switch (response.getResultCode()) {
      case 0:
      case 1:
        response.setAttribute("page", "/jsp/help.jsp?contexto=" + contexto);
        break;
    } 
    super.render(event, response);
  }
}
