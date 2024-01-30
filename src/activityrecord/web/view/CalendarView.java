package activityrecord.web.view;

import activityrecord.web.view.CommonGenericView;

public class CalendarView extends CommonGenericView {
  public CalendarView() {
    super("page", "/jsp/calendar.jsp", new Integer[] { Integer.valueOf(0), Integer.valueOf(1) });
  }
}
