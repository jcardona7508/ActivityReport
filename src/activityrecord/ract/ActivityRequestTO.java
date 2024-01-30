package activityrecord.ract;

import activityrecord.ract.ActiTO;
import java.io.Serializable;
import java.util.ArrayList;

public class ActivityRequestTO implements Serializable {
  public String usercode;
  
  public String date;
  
  public String resourcecode;
  
  public ArrayList<ActiTO> activities;
}
