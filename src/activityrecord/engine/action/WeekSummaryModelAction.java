package activityrecord.engine.action;

import activityrecord.data.RactDAO;
import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.AmountWeeklyHours;
import activityrecord.ract.PercentsPerTimePeriod;
import activityrecord.ract.PeriodType;
import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.sql.SQLManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.system.DateSC;

public class WeekSummaryModelAction extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    DateSC currTime = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.engine.action");
    EventResponse response = new EventResponse();
    Session session = event.getRequest().getSession();
    String resourceCode = (String)event.getRequest().getAttribute("resourceCode");
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
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    PeriodType periodType = resource.getView();
    DateSC fecha = calendar.getCurrentTime();
    fecha.setFormat(dateFormatString);
    logger.log(Level.FINER, "weekSummaryModelAction.execute.currentDate: " + fecha.toString());
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext()
        .getAtribute("siscorp.framework.application.ComponentManager");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      SQLManager sqlManager = (SQLManager)manager.getComponent(
          "activityrecord.sqleditor", "activityrecord");
      if (resourceCode != null) {
        if (periodType == PeriodType.DAY) {
          AmountWeeklyHours summary = RactDAO.getAmountWeeklySummary(resourceCode, fecha, 5, sqlManager);
          response.setAttribute("SUMMARY", summary);
        } else {
          int years = 4;
          if (periodType == PeriodType.MONTH)
            years = 3; 
          if (periodType == PeriodType.QUARTER)
            years = 4; 
          if (periodType == PeriodType.SEMESTER)
            years = 8; 
          if (periodType == PeriodType.YEAR)
            years = 12; 
          PercentsPerTimePeriod summary = RactDAO.getPercentsPerTimePeriodSummary(resourceCode, 
              fecha, years, sqlManager, periodType);
          response.setAttribute("SUMMARY", summary);
        } 
      } else {
        throw new RuntimeException("Resource code is null in WeekSummaryModelAction");
      } 
      response.setResultCode(0);
    } catch (Exception e) {
      System.out.println(e);
    } 
    return response;
  }
}
