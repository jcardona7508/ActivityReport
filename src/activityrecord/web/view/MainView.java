package activityrecord.web.view;

import activityrecord.web.view.CommonGenericView;

public class MainView extends CommonGenericView {
  public MainView() {
    super("page", "/jsp/login.jsp", new Integer[] { Integer.valueOf(0), Integer.valueOf(2) });
  }
}
