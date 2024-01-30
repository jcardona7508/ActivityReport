package com.gausssoft.client;

import com.gausssoft.client.ActivityTO;
import com.gausssoft.client.CustomFieldTO;
import java.util.ArrayList;
import java.util.List;

public class WeekDetailTO {
  public List<ActivityTO> fixed;
  
  public List<ActivityTO> unFixed;
  
  public List<ActivityTO> activities;
  
  public String[] customTitles = new String[] { "", "", "", "", "" };
  
  public List<CustomFieldTO> customFields;
  
  public String periodType;
  
  public String dobl;
  
  public String editMode;
  
  public String maxTime;
  
  public String percentLimit;
  
  public String showActivityCode;
  
  public WeekDetailTO() {
    this.fixed = new ArrayList<>();
    this.unFixed = new ArrayList<>();
    this.activities = new ArrayList<>();
    this.customFields = new ArrayList<>();
    this.editMode = "F";
    this.percentLimit = "F";
    this.showActivityCode = "T";
  }
}
