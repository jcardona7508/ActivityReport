package activityrecord.web.action;

import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.PeriodType;
import activityrecord.web.action.ChangeDateAction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.system.DateSC;

public class CommonChangeDateAction extends ChangeDateAction {
  public static final int BACK = 1;
  
  public static final int FORWARD = 2;
  
  public static final int TIME_UNIT = 10;
  
  public static final int TIME_PERIOD = 20;
  
  public static final int BIG_TIME_PERIOD = 40;
  
  private int direction;
  
  private int unit;
  
  public CommonChangeDateAction(int direction, int unit) {
    this.direction = direction;
    this.unit = unit;
    Logger logger = Logger.getLogger("activityrecord.web.action");
    logger.log(Level.FINER, "CommonChangeDateAction.direction: " + direction);
    logger.log(Level.FINER, "CommonChangeDateAction.unit: " + unit);
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    super.doEnd(event, response);
    DateSC currTime = null;
    Logger logger = Logger.getLogger("activityrecord.web.action");
    if (response.getResultCode() == 0) {
      Session session = event.getRequest().getSession();
      ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
      PeriodType timeUnit = resource.getView();
      String dateFormatString = (String)session.getAttribute("DATEFORMAT");
      if (dateFormatString == null)
        dateFormatString = "yyyy/MM/dd"; 
      String currentDateStr = (String)session.getAttribute("CURRENTDATE");
      if (currentDateStr != null)
        try {
          currTime = new DateSC(currentDateStr, dateFormatString);
        } catch (ParseException ex) {
          Logger.getLogger("siscorp.activityreport").log(Level.SEVERE, (String)null, ex);
          currTime = new DateSC();
          currTime.setFormat(dateFormatString);
        }  
      ActivityRecordCalendar calendar = new ActivityRecordCalendar(currTime);
      calendar.setDateFormat(new SimpleDateFormat(dateFormatString));
      if (calendar != null) {
        logger.log(Level.FINER, "CommonChangeDateAction.doEnd().calendar(a): " + calendar.getCurrentTime().toString());
        switch (this.direction + this.unit) {
          case 11:
            calendar.goPreviousTimePeriod(timeUnit);
            break;
          case 21:
            if (timeUnit == PeriodType.DAY) {
              calendar.goPreviousWeek();
              break;
            } 
            calendar.goPreviousTimePeriod(PeriodType.YEAR);
            break;
          case 41:
            calendar.goPreviousMonth();
            break;
          case 12:
            calendar.goNextTimePeriod(timeUnit);
            break;
          case 22:
            if (timeUnit == PeriodType.DAY) {
              calendar.goNextWeek();
              break;
            } 
            calendar.goNextTimePeriod(PeriodType.YEAR);
            break;
          case 42:
            calendar.goNextMonth();
            break;
          default:
            throw new RuntimeException(
                "Error: CommonChangeDateAction created with unexpected values.");
        } 
        logger.log(Level.FINER, "CommonChangeDateAction.doEnd().calendar(b): " + calendar.getCurrentTime().toString());
        session.setAttribute("CURRENTDATE", calendar.getCurrentTime().toString());
      } 
    } 
  }
}
