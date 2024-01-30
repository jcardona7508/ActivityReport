package activityrecord.ract;

import activityrecord.ract.PeriodType;
import activityrecord.ract.WorkPerTimePeriod;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import siscorp.system.DateSC;

public class PercentsPerTimePeriod implements Serializable {
  private List workPeriods = null;
  
  private PeriodType periodType;
  
  public PercentsPerTimePeriod(PeriodType periodType) {
    setPeriodType(periodType);
    setWorkPeriods(new ArrayList());
  }
  
  public void addWorkPeriod(DateSC date) {
    getWorkPeriods().add(new WorkPerTimePeriod(date));
  }
  
  public WorkPerTimePeriod getWorkPeriodItem(int index) {
    return (WorkPerTimePeriod) getWorkPeriods().get(index);
  }
  
  public WorkPerTimePeriod getWorkPeriodItem(DateSC date) {
    int size = getWorkPeriods().size();
    for (int i = 0; i < size; i++) {
      WorkPerTimePeriod item = (WorkPerTimePeriod) getWorkPeriods().get(i);
      if (item.getDate().equals(date))
        return item; 
    } 
    return null;
  }
  
  public PeriodType getPeriodType() {
    return this.periodType;
  }
  
  public void setPeriodType(PeriodType periodType) {
    this.periodType = periodType;
  }
  
  public List getWorkPeriods() {
    return this.workPeriods;
  }
  
  public void setWorkPeriods(List workPeriods) {
    this.workPeriods = workPeriods;
  }
}
