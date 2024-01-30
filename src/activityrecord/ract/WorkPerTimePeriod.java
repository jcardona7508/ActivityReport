package activityrecord.ract;

import gausssoft.graph.StringValue;
import gausssoft.graph.serie.DualValue;
import gausssoft.graph.serie.Value;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import siscorp.system.DateSC;

public class WorkPerTimePeriod implements Serializable {
  private DateSC date;
  
  private List workItems;
  
  public WorkPerTimePeriod(DateSC date) {
    this.date = date;
    this.workItems = new ArrayList();
  }
  
  public void addWorkItem(double quantity, String type) {
    DualValue value = new DualValue(Double.valueOf(quantity), (Value)new StringValue(type));
    this.workItems.add(value);
  }
  
  public void accumulateWorkItem(double quantity, String type) {
    for (int i = 0; i < this.workItems.size(); i++) {
      DualValue item = (DualValue) this.workItems.get(i);
      if (item.getDualValue().getDisplayValue().equals(type)) {
        DualValue dualValue = new DualValue(Double.valueOf(quantity + item.getValue().doubleValue()), 
            (Value)new StringValue(type));
        this.workItems.set(i, dualValue);
        return;
      } 
    } 
    DualValue value = new DualValue(Double.valueOf(quantity), (Value)new StringValue(type));
    this.workItems.add(value);
  }
  
  public double calculateTotalWork() {
    double total = 0.0D;
    for (int i = 0; i < this.workItems.size(); i++) {
      DualValue item = (DualValue) this.workItems.get(i);
      total += item.getValue().doubleValue();
    } 
    return total;
  }
  
  public DateSC getDate() {
    return this.date;
  }
  
  public List getWorkItems() {
    return this.workItems;
  }
}
