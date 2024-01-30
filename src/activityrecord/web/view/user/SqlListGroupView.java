package activityrecord.web.view.user;

import activityrecord.web.view.CommonGenericView;

public class SqlListGroupView extends CommonGenericView {
  public SqlListGroupView() {
    super("page", "/jsp/sqleditor/ListSqlGroup.jsp", new Integer[] { Integer.valueOf(0) });
  }
}
