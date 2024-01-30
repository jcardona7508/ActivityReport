package activityrecord.web.view;

import activityrecord.web.view.CommonGenericView;

public class ActionView extends CommonGenericView {
  public ActionView() {
    super("page", "/jsp/ReportHeader.jsp", new Integer[] { Integer.valueOf(0), Integer.valueOf(1) });
  }
}
