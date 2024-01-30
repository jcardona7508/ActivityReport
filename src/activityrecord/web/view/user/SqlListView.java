package activityrecord.web.view.user;

import activityrecord.web.view.CommonGenericView;

public class SqlListView extends CommonGenericView {
  public SqlListView() {
    super("page", "/jsp/sqleditor/ListSql.jsp", new Integer[] { Integer.valueOf(0) });
  }
}
