package activityrecord.web.view;

import common.web.view.GenericHtmlView;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class FechaView extends GenericHtmlView {
  public void render(ApplicationEvent event, EventResponse response) throws Exception {
    String mode = (String)event.getRequest().getAttribute("mode");
    switch (response.getResultCode()) {
      case 0:
      case 1:
        if (mode.equals("day")) {
          response.setAttribute("page", "/jsp/ract.jsp");
          break;
        } 
        if (mode.equals("week"))
          response.setAttribute("page", "/jsp/semanalract.jsp"); 
        break;
    } 
    super.render(event, response);
  }
}
