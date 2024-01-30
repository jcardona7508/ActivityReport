package activityrecord.ract;

import activityrecord.ract.Activity;
import activityrecord.ract.DailyDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import siscorp.system.DateSC;

public class WeekDetail implements Serializable {
  protected DailyDetail weekSummary;
  
  protected List dailyList = new ArrayList();
  
  protected Map dailyIndex = new HashMap<>();
  
  public DailyDetail getWeekSummary() {
    return this.weekSummary;
  }
  
  public void setWeekSummary(DailyDetail weekSummary) {
    this.weekSummary = weekSummary;
  }
  
  public void addActivity(Activity activity) {
    addActivity(activity, this.weekSummary.size());
  }
  
  public void addActivity(Activity activity, int position) {
    if (this.weekSummary.add(activity, position))
      for (int i = 0; i < this.dailyList.size(); i++)
        ((DailyDetail)this.dailyList.get(i)).add(activity, position);  
  }
  
  public void addDailyDetail(DailyDetail detail, DateSC date) {
    if (!this.dailyIndex.containsKey(date)) {
      this.dailyList.add(detail);
      int position = this.dailyList.size() - 1;
      this.dailyIndex.put(date, new Integer(position));
    } else {
      int position = ((Integer)this.dailyIndex.get(date)).intValue();
      this.dailyList.set(position, detail);
    } 
  }
  
  public DailyDetail getDailyDetail(DateSC date) {
    if (this.dailyIndex.containsKey(date))
      return (DailyDetail) this.dailyList.get(((Integer)this.dailyIndex.get(date)).intValue()); 
    return null;
  }
  
  public DailyDetail getDailyDetail(int pos) {
    return (DailyDetail) this.dailyList.get(pos);
  }
  
  public int getDayCount() {
    return this.dailyList.size();
  }
  
  public void updateSubtotal(Activity activity) {
    try {
      Activity tmpActivity = (Activity)this.weekSummary.getActivity(activity).clone();
      tmpActivity.setCana(new Float(tmpActivity.getCana().floatValue() + activity.getCana().floatValue()));
      tmpActivity.setCano(new Float(tmpActivity.getCano().floatValue() + activity.getCano().floatValue()));
      this.weekSummary.set(tmpActivity);
    } catch (CloneNotSupportedException exception) {
      System.out.println("Exception " + exception);
    } 
  }
}
