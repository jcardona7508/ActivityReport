package activityrecord.web.view;

import activityrecord.web.view.CommonGenericView;

public class DetailView extends CommonGenericView {
  public DetailView() {
    super("page", "/jsp/DailyReportDetail.jsp", new Integer[] { Integer.valueOf(0), Integer.valueOf(1) });
  }
}
