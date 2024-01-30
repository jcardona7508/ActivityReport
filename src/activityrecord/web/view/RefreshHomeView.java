package activityrecord.web.view;

import activityrecord.web.view.CommonGenericView;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class RefreshHomeView extends CommonGenericView {
  public RefreshHomeView() {
    super("page", "/activityreportdoc/html/refreshhome.html", new Integer[] { Integer.valueOf(3) });
  }
  
  public void render(ApplicationEvent event, EventResponse response) throws Exception {
    if (response.getResultCode() == 0)
      response.setResultCode(3); 
    super.render(event, response);
  }
}
