package activityrecord.engine.action;

import activityrecord.data.RactDAO;
import activityrecord.ract.ActivityChoiceTO;
import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.PeriodType;
import activityrecord.ract.WeekDetail;
import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.sql.SQLManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.system.DateSC;

public class GetActivitiesModelAction extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    DateSC currTime = null;
    Session session = event.getRequest().getSession();
    Map parameters = (HashMap)session.getAttribute("RACT_PARAMETERS");
    EventResponse response = new EventResponse();
    String resourceCode = (String)event.getRequest().getAttribute("resourceCode");
    String mode = (String)event.getRequest().getAttribute("mode");
    if (mode == null)
      mode = "1day"; 
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
    DateSC date = calendar.getCurrentTime();
    date.setFormat(dateFormatString);
    calendar = (ActivityRecordCalendar)calendar.clone();
    calendar.setTimeCalendar(date);
    int days = 1;
    boolean hasCustomFields = false;
    if (!hasCustomFields && resource.getView().equals(PeriodType.DAY))
      if (mode.equalsIgnoreCase("5day")) {
        days = 5;
        calendar.set(7, 2);
      } else if (!mode.equalsIgnoreCase("1day")) {
        days = 7;
        calendar.set(7, calendar.getFirstDayOfWeek());
      }  
    if (!resource.getView().equals(PeriodType.DAY))
      calendar.gotoFirstDayOfPeriod(resource.getView()); 
    DateSC startDate = calendar.getCurrentTime();
    startDate.setFormat(dateFormatString);
    if (!resource.getView().equals(PeriodType.DAY)) {
      calendar.gotoFirstDayOfPeriod(resource.getView());
    } else {
      calendar.add(7, days - 1);
    } 
    DateSC endDate = calendar.getCurrentTime();
    endDate.setFormat(dateFormatString);
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext().getAtribute("siscorp.framework.application.ComponentManager");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      SQLManager sqlManager = (SQLManager)manager.getComponent("activityrecord.sqleditor", 
          "activityrecord");
      if (resourceCode != null) {
        WeekDetail weekDetail = RactDAO.getResourceWeekActivities(resource, startDate, 
            endDate, 
            resourceCode, 
            sqlManager);
        response.setAttribute("ACTIVITIES", weekDetail);
        List<ActivityChoiceTO> proyectos = RactDAO.getResourceActivities(resource.getCode(), "", sqlManager);
        session.setAttribute("PROJECTS", proyectos);
      } 
      response.setResultCode(0);
    } catch (Exception e) {
      System.out.println(e);
    } 
    return response;
  }
}
