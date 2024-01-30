package activityrecord.web.view;

import activityrecord.web.view.CommonGenericView;

public class RactWeekView extends CommonGenericView {
  public RactWeekView() {
    super("page", "/jsp/semanalract.jsp", new Integer[] { Integer.valueOf(0), Integer.valueOf(1) });
  }
}
