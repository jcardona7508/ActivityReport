package activityrecord.web.view;

import activityrecord.web.view.CommonGenericView;

public class HomeView extends CommonGenericView {
  public HomeView() {
    super("page", "/jsp/home.jsp", new Integer[] { Integer.valueOf(0), Integer.valueOf(1) });
  }
}
