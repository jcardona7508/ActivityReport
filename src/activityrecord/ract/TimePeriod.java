package activityrecord.ract;

import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.PeriodType;
import java.io.Serializable;
import java.util.Date;
import siscorp.system.DateSC;

public class TimePeriod implements Serializable {
  private DateSC date;
  
  private ActivityRecordCalendar calendar;
  
  private PeriodType periodType;
  
  public TimePeriod(DateSC date, PeriodType type) {
    setPeriodType(type);
    this.calendar = new ActivityRecordCalendar();
    setDate(new DateSC(date));
  }
  
  public DateSC getStartDate() {
    this.calendar.setTime((Date)getDate());
    this.calendar.gotoFirstDayOfPeriod(getPeriodType());
    return new DateSC(this.calendar.getTime());
  }
  
  public DateSC getLastDate() {
    int month;
    this.calendar.setTime((Date)getDate());
    switch (getPeriodType()) {
      case WEEK:
        this.calendar.set(7, this.calendar.getFirstDayOfWeek());
        this.calendar.add(5, 6);
        return new DateSC(this.calendar.getTime());
      case MONTH:
        this.calendar.add(2, 1);
        this.calendar.set(5, 1);
        this.calendar.add(5, -1);
        return new DateSC(this.calendar.getTime());
      case QUARTER:
        month = this.calendar.get(2);
        this.calendar.set(2, (month / 3 + 1) * 3);
        this.calendar.set(5, 1);
        this.calendar.add(5, -1);
        return new DateSC(this.calendar.getTime());
      case SEMESTER:
        month = this.calendar.get(2);
        this.calendar.set(2, (month / 6 + 1) * 6);
        this.calendar.set(5, 1);
        this.calendar.add(5, -1);
        return new DateSC(this.calendar.getTime());
      case YEAR:
        this.calendar.set(this.calendar.get(1) + 1, 0, 1);
        this.calendar.add(5, -1);
        return new DateSC(this.calendar.getTime());
    } 
    throw new RuntimeException("Program error in getStartDate()");
  }
  
  public boolean contains(DateSC date) {
    DateSC firstDate = getStartDate();
    DateSC lastDate = getLastDate();
    return (date.compareTo((Date)firstDate) >= 0 && date.compareTo((Date)lastDate) <= 0);
  }
  
  protected ActivityRecordCalendar getCalendar() {
    return this.calendar;
  }
  
  public DateSC getDate() {
    return this.date;
  }
  
  public PeriodType getPeriodType() {
    return this.periodType;
  }
  
  protected void setPeriodType(PeriodType periodType) {
    this.periodType = periodType;
  }
  
  public void setDate(DateSC date) {
    this.date = date;
  }
}
