package activityrecord.ract;

import activityrecord.ract.PeriodType;
import java.io.Serializable;

public class ActivityReportResource implements Serializable {
  private String code;
  
  private String name;
  
  private String positionName;
  
  private boolean position;
  
  private String userName;
  
  private PeriodType view;
  
  public String getUserName() {
    return this.userName;
  }
  
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  public ActivityReportResource() {}
  
  public ActivityReportResource(String code, String name) {
    this();
    setCode(code);
    setName(name);
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setCode(String string) {
    this.code = string;
  }
  
  public void setName(String string) {
    this.name = string;
  }
  
  public boolean isPosition() {
    return this.position;
  }
  
  public void setPosition(boolean position) {
    this.position = position;
  }
  
  public String getPositionName() {
    return this.positionName;
  }
  
  public void setPositionName(String positionName) {
    this.positionName = positionName;
  }
  
  public String getResourceCode() {
    if (isPosition())
      return String.valueOf(getPositionName()) + "/" + getUserName(); 
    return getCode();
  }
  
  public void setView(PeriodType view) {
    this.view = view;
  }
  
  public PeriodType getView() {
    return this.view;
  }
  
  public boolean acceptPercentages() {
    return !getView().equals(PeriodType.DAY);
  }
}
