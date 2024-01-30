package activityrecord.ract;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import siscorp.system.DateSC;

public class Week implements Serializable {
  private DateSC date;
  
  private Calendar calendar;
  
  public Week(DateSC date) {
    setCalendar(new GregorianCalendar());
    setDate(date);
  }
  
  public Calendar getCalendar() {
    return this.calendar;
  }
  
  public DateSC getDate() {
    return this.date;
  }
  
  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }
  
  public void setDate(DateSC date) {
    this.date = date;
    this.calendar.setTime((Date)date);
    this.calendar.set(7, this.calendar.getFirstDayOfWeek());
  }
  
  public boolean belongsTo(DateSC date) {
    this.calendar.set(7, this.calendar.getFirstDayOfWeek());
    DateSC firstDate = new DateSC(this.calendar.getTime());
    this.calendar.add(7, 6);
    DateSC lastDate = new DateSC(this.calendar.getTime());
    this.calendar.set(7, this.calendar.getFirstDayOfWeek());
    return (date.compareTo((Date)firstDate) >= 0 && date.compareTo((Date)lastDate) <= 0);
  }
}
