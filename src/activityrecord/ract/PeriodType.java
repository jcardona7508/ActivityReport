package activityrecord.ract;

import java.io.Serializable;

public enum PeriodType implements Serializable {
  DAY(
    
    'D'),
  WEEK(
    
    'W'),
  MONTH(
    
    'M'),
  QUARTER(
    
    'Q'),
  SEMESTER(
    
    'S'),
  YEAR(
    
    'Y');
  
  private final Character abbreviation;
  
  PeriodType(char abbrevation) {
    this.abbreviation = Character.valueOf(abbrevation);
  }
  
  public static activityrecord.ract.PeriodType getPeriodType(Character abbrevation) throws Exception {
    byte b;
    int i;
    activityrecord.ract.PeriodType[] arrayOfPeriodType;
    for (i = (arrayOfPeriodType = values()).length, b = 0; b < i; ) {
      activityrecord.ract.PeriodType periodType = arrayOfPeriodType[b];
      if (periodType.abbreviation.equals(abbrevation))
        return periodType; 
      b++;
    } 
    throw new Exception("Invalid Period Type " + abbrevation);
  }
}
