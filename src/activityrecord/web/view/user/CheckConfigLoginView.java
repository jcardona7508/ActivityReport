package activityrecord.web.view.user;

import common.web.view.GenericHtmlView;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class CheckConfigLoginView extends GenericHtmlView {
  public void render(ApplicationEvent event, EventResponse response) throws Exception {
    switch (response.getResultCode()) {
      case 100:
      case 200:
        response.setAttribute("page", "jsp/activityconfiguration.jsp");
        break;
    } 
    super.render(event, response);
  }
}
