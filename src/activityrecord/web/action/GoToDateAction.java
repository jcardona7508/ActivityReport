package activityrecord.web.action;

import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.web.action.ChangeDateAction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.system.DateSC;

public class GoToDateAction extends ChangeDateAction {
  public void doEnd(ApplicationEvent event, EventResponse response) {
    super.doEnd(event, response);
    DateSC currTime = null;
    if (response.getResultCode() == 0) {
      Session session = event.getRequest().getSession();
      DateSC date = new DateSC();
      String dateFormatString = (String)session.getAttribute("DATEFORMAT");
      if (dateFormatString == null)
        dateFormatString = "yyyy/MM/dd"; 
      date.setFormat(dateFormatString);
      String targetDate = event.getRequest().getParameter("fecha");
      if (targetDate != null) {
        try {
          date.setTime((new DateSC(targetDate, dateFormatString)).getTime());
        } catch (Exception e) {
          System.out.println("Fecha no valida " + e);
        } 
      } else {
        date = new DateSC();
      } 
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
      if (calendar != null)
        calendar.setTimeCalendar(date); 
      session.setAttribute("CURRENTDATE", calendar.getCurrentTime().toString());
    } 
  }
}
