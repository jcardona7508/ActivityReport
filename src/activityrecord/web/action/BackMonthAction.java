package activityrecord.web.action;

import activityrecord.web.action.CommonChangeDateAction;

public class BackMonthAction extends CommonChangeDateAction {
  public BackMonthAction() {
    super(1, 40);
  }
}
