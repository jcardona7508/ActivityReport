package activityrecord.web.view;

import activityrecord.web.view.CommonGenericView;

public class ResourcesView extends CommonGenericView {
  public ResourcesView() {
    super("page", "/jsp/resources.jsp", new Integer[] { Integer.valueOf(0) });
  }
}
