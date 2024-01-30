package activityrecord.web.view.user;

import activityrecord.web.view.CommonGenericView;

public class SqlEditView extends CommonGenericView {
  public SqlEditView() {
    super("page", "/jsp/sqleditor/CreateSql.jsp", new Integer[] { Integer.valueOf(0) });
  }
}
