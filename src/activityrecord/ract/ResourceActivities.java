package activityrecord.ract;

import java.util.ArrayList;
import java.util.Collection;
import siscorp.system.DateSC;

public class ResourceActivities extends ArrayList {
  protected DateSC date;
  
  public ResourceActivities(int arg0) {
    super(arg0);
  }
  
  public ResourceActivities() {}
  
  public ResourceActivities(Collection arg0) {
    super(arg0);
  }
  
  public DateSC getDate() {
    return this.date;
  }
  
  public void setDate(DateSC dateSC) {
    this.date = dateSC;
  }
}
