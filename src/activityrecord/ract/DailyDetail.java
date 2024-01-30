package activityrecord.ract;

import activityrecord.ract.Activity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import siscorp.system.DateSC;

public class DailyDetail implements Cloneable, Serializable {
  protected DateSC date;
  
  protected HashMap index;
  
  protected ArrayList list;
  
  protected HashMap subtotalIndex;
  
  protected Activity total;
  
  public DailyDetail(DateSC date) {
    this.list = new ArrayList();
    this.index = new HashMap<>();
    this.subtotalIndex = new HashMap<>();
    this.total = new Activity();
    this.total.setAcua("<*TOTAL*>");
    this.total.setCana(new Float(0.0F));
    this.total.setCano(new Float(0.0F));
    this.total.setType("T");
    this.list.add(this.total);
    setDate(date);
  }
  
  public boolean add(Activity activity) {
    return add(activity, this.list.size());
  }
  
  public boolean add(Activity activity, int position) {
    Activity subtotalTypeS = null;
    Activity subtotalTypeD = null;
    boolean result = false;
    String key;
    if (!this.index.containsKey(key = getActivityKey(activity))) {
      if (!activity.getType().equals("S")) {
        subtotalTypeS = new Activity();
        subtotalTypeS.setAcua(activity.getAcua());
        subtotalTypeS.setAcuo(activity.getAcua());
        subtotalTypeD = new Activity();
        subtotalTypeD.setAcua(activity.getAcua());
        subtotalTypeD.setAcuo(activity.getAcuo());
        if (!this.subtotalIndex.containsKey(getSubtotalKey(subtotalTypeS))) {
          subtotalTypeS.setActividad(activity.getActividad());
          subtotalTypeS.setCana(new Float(0.0F));
          subtotalTypeS.setCano(new Float(0.0F));
          subtotalTypeS.setType("S");
          subtotalTypeS.setFixed(false);
          subtotalTypeS.setRegistros(0);
          this.list.add(position, subtotalTypeS);
          this.subtotalIndex.put(getSubtotalKey(subtotalTypeS), 
              new Integer(position));
        } else {
          position = ((Integer)this.subtotalIndex.get(getSubtotalKey(subtotalTypeS))).intValue();
          subtotalTypeS = (Activity) this.list.get(position);
        } 
        if (activity.getType().equals("U"))
          if (!this.subtotalIndex.containsKey(getSubtotalKey(subtotalTypeD))) {
            position++;
            subtotalTypeD.setActividad(activity.getActividad());
            subtotalTypeD.setObjeto(activity.getObjeto());
            subtotalTypeD.setCana(new Float(0.0F));
            subtotalTypeD.setCano(new Float(0.0F));
            subtotalTypeD.setType("D");
            subtotalTypeD.setFixed(false);
            subtotalTypeD.setRegistros(0);
            this.list.add(position, subtotalTypeD);
            this.subtotalIndex.put(getSubtotalKey(subtotalTypeD), 
                new Integer(position));
          } else {
            position = ((Integer)this.subtotalIndex.get(getSubtotalKey(subtotalTypeD))).intValue();
            subtotalTypeD = (Activity) this.list.get(position);
          }  
        Activity tempActivity = (Activity) this.list.get(position);
        if (activity.getType().equals("D")) {
          while (tempActivity.getAcua().equals(activity.getAcua())) {
            position++;
            if (position < this.list.size()) {
              tempActivity = (Activity) this.list.get(position);
              continue;
            } 
            break;
          } 
        } else if (activity.getType().equals("U")) {
          while (getSubtotalKey(tempActivity).equals(getSubtotalKey(activity))) {
            position++;
            if (position < this.list.size()) {
              tempActivity = (Activity) this.list.get(position);
              continue;
            } 
            break;
          } 
        } 
        this.list.add(position, activity);
        subtotalTypeS.setCana(new Float(subtotalTypeS.getCana().floatValue() + 
              activity.getCana().floatValue()));
        subtotalTypeS.setCano(new Float(subtotalTypeS.getCano().floatValue() + 
              activity.getCano().floatValue()));
        subtotalTypeS.setRegistros(subtotalTypeS.getRegistros() + 1);
        if (activity.getType().equals("U")) {
          subtotalTypeD.setCana(new Float(subtotalTypeD.getCana().floatValue() + 
                activity.getCana().floatValue()));
          subtotalTypeD.setCano(new Float(subtotalTypeD.getCano().floatValue() + 
                activity.getCano().floatValue()));
          subtotalTypeD.setRegistros(subtotalTypeD.getRegistros() + 1);
        } 
      } else {
        this.list.add(position, activity);
        this.subtotalIndex.put(getSubtotalKey(activity), 
            new Integer(position));
      } 
      this.total.setCana(new Float(this.total.getCana().floatValue() + 
            activity.getCana().floatValue()));
      this.total.setCano(new Float(this.total.getCano().floatValue() + 
            activity.getCano().floatValue()));
      this.index.put(key, new Integer(position));
      rebuildIndex();
      result = true;
    } 
    return result;
  }
  
  public Object clone() throws CloneNotSupportedException {
    activityrecord.ract.DailyDetail copy = (activityrecord.ract.DailyDetail)super.clone();
    copy.list = new ArrayList(this.list.size());
    Iterator<Activity> iterator = this.list.listIterator();
    while (iterator.hasNext())
      copy.list.add(((Activity)iterator.next()).clone()); 
    iterator = this.index.keySet().iterator();
    copy.index = new HashMap<>(this.index.size());
    while (iterator.hasNext()) {
      Object key = iterator.next();
      Integer value = (Integer)this.index.get(key);
      copy.index.put(key, new Integer(value.intValue()));
    } 
    iterator = this.subtotalIndex.keySet().iterator();
    copy.subtotalIndex = new HashMap<>(this.subtotalIndex.size());
    while (iterator.hasNext()) {
      Object key = iterator.next();
      Integer value = (Integer)this.subtotalIndex.get(key);
      copy.subtotalIndex.put(key, new Integer(value.intValue()));
    } 
    copy.total = (Activity) copy.list.get(0);
    if (this.date != null) {
      copy.date = new DateSC(this.date);
    } else {
      copy.date = null;
    } 
    return copy;
  }
  
  public boolean contains(Activity activity) {
    return this.index.containsKey(getActivityKey(activity));
  }
  
  public boolean contains(String key) {
    return this.index.containsKey(key);
  }
  
  public Activity getActivity(Activity activity) {
    return getActivity(getActivityKey(activity));
  }
  
  public Activity getActivity(String key) {
    return (Activity) this.list.get(((Integer)this.index.get(key)).intValue());
  }
  
  public Activity getActivity(int pos) {
    return (Activity) this.list.get(pos);
  }
  
  public DateSC getDate() {
    return this.date;
  }
  
  public void setDate(DateSC date) {
    this.date = date;
  }
  
  public boolean set(Activity activity) {
    boolean result = false;
    String key;
    if (this.index.containsKey(key = getActivityKey(activity))) {
      int position = ((Integer)this.index.get(key)).intValue();
      Activity tempActivity = (Activity) this.list.get(position);
      if (!activity.getType().equals("S")) {
        Activity subtotal;
        if (activity.getType().equals("U")) {
          subtotal = (Activity) this.list.get(((Integer)this.subtotalIndex.get(getSubtotalKey(activity))).intValue());
        } else {
          subtotal = (Activity) this.list.get(((Integer)this.subtotalIndex.get(String.valueOf(activity.getAcua()) + activity.getAcua())).intValue());
        } 
        subtotal.setCana(new Float(subtotal.getCana().floatValue() + 
              activity.getCana().floatValue()));
        subtotal.setCano(new Float(subtotal.getCano().floatValue() + 
              activity.getCano().floatValue()));
        subtotal.setCana(new Float(subtotal.getCana().floatValue() - 
              tempActivity.getCana()
              .floatValue()));
        subtotal.setCano(new Float(subtotal.getCano().floatValue() - 
              tempActivity.getCano()
              .floatValue()));
      } 
      this.total.setCana(new Float(this.total.getCana().floatValue() + 
            activity.getCana().floatValue()));
      this.total.setCano(new Float(this.total.getCano().floatValue() + 
            activity.getCano().floatValue()));
      this.total.setCana(new Float(this.total.getCana().floatValue() - 
            tempActivity.getCana()
            .floatValue()));
      this.total.setCano(new Float(this.total.getCano().floatValue() - 
            tempActivity.getCano()
            .floatValue()));
      this.list.set(position, activity);
      result = true;
    } 
    return result;
  }
  
  public String toString() {
    if (this.list != null)
      return this.list.toString(); 
    return "[]";
  }
  
  protected String getActivityKey(Activity activity) {
    return String.valueOf(activity.getAcua()) + activity.getAcuo() + activity.getAcu4();
  }
  
  protected String getSubtotalKey(Activity activity) {
    return String.valueOf(activity.getAcua()) + activity.getAcuo();
  }
  
  protected void rebuildIndex() {
    int position = 0;
    this.index = new HashMap<>(this.list.size());
    this.subtotalIndex = new HashMap<>(this.list.size());
    Iterator<Activity> iterator = this.list.listIterator();
    while (iterator.hasNext()) {
      Activity activity = iterator.next();
      this.index.put(getActivityKey(activity), 
          new Integer(position));
      if (activity.getType().equals("S") || (activity.getType().equals("D") && activity.getRegistros() > 0))
        this.subtotalIndex.put(getSubtotalKey(activity), 
            new Integer(position)); 
      position++;
    } 
    this.total = (Activity) this.list.get(0);
  }
  
  public int size() {
    return this.list.size();
  }
}
