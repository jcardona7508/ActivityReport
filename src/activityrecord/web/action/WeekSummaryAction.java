package activityrecord.web.action;

import activityrecord.data.GraphDataTO;
import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.AmountWeeklyHours;
import activityrecord.ract.PercentsPerTimePeriod;
import activityrecord.ract.PeriodType;
import activityrecord.ract.TimePeriod;
import activityrecord.ract.Week;
import activityrecord.ract.WorkPerTimePeriod;
import com.gausssoft.GausssoftException;
import common.web.ClientInfo;
import common.web.action.GenericAction;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.serie.DateSerieArrayListImplementation;
import gausssoft.graph.serie.DateValue;
import gausssoft.graph.serie.NumericSerieArrayListImplementation;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.system.DateSC;

public class WeekSummaryAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    Session session = event.getRequest().getSession();
    EventResponse response = new EventResponse();
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    if (resource != null) {
      ApplicationEvent modelEvent = new ApplicationEvent("getWeekSummary", 
          event.getRequest(), 
          event.getApplicationContext());
      modelEvent.getRequest().setAttribute("resourceCode", 
          resource.getResourceCode());
      response.setModelEvent(modelEvent);
    } 
    return response;
  }
  
  public void doEnd(ApplicationEvent event, EventResponse response) {
    EventResponse modelResponse = response.getModelResponse();
    DateSC currTime = null;
    Session session = event.getRequest().getSession();
    String dateFormatStr = (String)session.getAttribute("DATEFORMAT");
    if (dateFormatStr == null)
      dateFormatStr = "yyyy/MM/dd"; 
    String currentDateStr = (String)session.getAttribute("CURRENTDATE");
    if (currentDateStr != null)
      try {
        currTime = new DateSC(currentDateStr, dateFormatStr);
      } catch (ParseException ex) {
        Logger.getLogger(activityrecord.web.action.WeekSummaryAction.class.getName()).log(Level.SEVERE, (String)null, ex);
        currTime = new DateSC();
        currTime.setFormat(dateFormatStr);
      }  
    ActivityRecordCalendar calendar = new ActivityRecordCalendar(currTime);
    calendar.setDateFormat(new SimpleDateFormat(dateFormatStr));
    HashMap parameters = (HashMap)session.getAttribute("RACT_PARAMETERS");
    Locale locale = (Locale)session.getAttribute("LOCALE");
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    PeriodType periodType = resource.getView();
    DateSC minDate = DateSC.fromString((String)parameters.get("RFROM"), 
        "yyyy/MM/dd", new DateSC());
    DateSC maxDate = DateSC.fromString((String)parameters.get("RTO"), 
        "yyyy/MM/dd", new DateSC());
    try {
      if (modelResponse != null) {
        ClientInfo clientInfo = (ClientInfo)session.getAttribute("CLIENTINFO");
        if (modelResponse.getAttribute("SUMMARY") != null) {
          UserSpace userSpace;
          int resolution;
          DateSC startDate;
          int periods;
          GraphDataTO transferObject;
          try {
            resolution = Integer.parseInt(clientInfo.getScreenWidth());
          } catch (NumberFormatException e) {
            resolution = 0;
          } 
          Dimension dimension = new Dimension(resolution - 10, 80);
          if (periodType == PeriodType.DAY) {
            AmountWeeklyHours summary = (AmountWeeklyHours)modelResponse.getAttribute("SUMMARY");
            DateSerieArrayListImplementation dateDataSerie = new DateSerieArrayListImplementation(
                (DateSC[])summary.getFecha().toArray(
                  (Object[])new DateSC[summary.getFecha().size()]));
            NumericSerieArrayListImplementation numericDataSerie = new NumericSerieArrayListImplementation(
                (Number[])summary.getCantidad().toArray(
                  (Object[])new Double[summary.getCantidad().size()]));
            double userHeight = numericDataSerie.getMaxValue().getValue().doubleValue();
            userSpace = new UserSpace(dimension, 
                0.0D, 
                0.0D, (
                resolution - 10), 
                16.0D, 
                0.0D, 0.0D, 0.0D, 0.0D);
            Week week = new Week(calendar.getCurrentTime());
            dateDataSerie.setDateFormat("dd");
            transferObject = new GraphDataTO(week, 
                dimension, 
                userSpace, 
                dateDataSerie, 
                numericDataSerie, 
                minDate, 
                maxDate);
            periods = numericDataSerie.size();
            startDate = ((DateValue)dateDataSerie.get(0)).getValue();
          } else {
            int titleHeight;
            calendar.setTime((Date)minDate);
            calendar.gotoFirstDayOfPeriod(periodType);
            int sizeInDays = calendar.getSmallPeriodsCount(PeriodType.MONTH, periodType, minDate) * 30;
            if (minDate.diference(calendar.getCurrentTime()).longValue() > (sizeInDays / 2))
              calendar.goNextTimePeriod(periodType); 
            minDate = calendar.getCurrentTime();
            minDate.setFormat("yyyy/MM/dd");
            maxDate = DateSC.fromString((String)parameters.get("RTO"), 
                "yyyy/MM/dd", new DateSC());
            calendar.setTime((Date)maxDate);
            calendar.gotoLastDayOfPeriod(periodType);
            if (calendar.getCurrentTime().diference(maxDate).longValue() > (sizeInDays / 2))
              calendar.goPreviousTimePeriod(periodType); 
            maxDate = calendar.getCurrentTime();
            maxDate.setFormat("yyyy/MM/dd");
            calendar.setTime((Date)currTime);
            PercentsPerTimePeriod summary = (PercentsPerTimePeriod)modelResponse.getAttribute("SUMMARY");
            if (periodType == PeriodType.YEAR) {
              titleHeight = 12;
            } else {
              titleHeight = 24;
            } 
            userSpace = new UserSpace(dimension, 
                0.0D, 
                0.0D, (
                resolution - 10), 
                80.0D, 
                0.0D, titleHeight, 0.0D, 12.0D);
            TimePeriod currentPeriod = new TimePeriod(calendar.getCurrentTime(), periodType);
            TimePeriod bigCurrentPeriod = new TimePeriod(calendar.getCurrentTime(), PeriodType.YEAR);
            transferObject = new GraphDataTO(currentPeriod, 
                dimension, 
                userSpace, 
                summary, 
                minDate, 
                maxDate, 
                bigCurrentPeriod, 
                locale);
            periods = summary.getWorkPeriods().size();
            startDate = ((WorkPerTimePeriod)summary.getWorkPeriods().get(0)).getDate();
          } 
          String htmlMap = getImageMap(periods, (userSpace.getDevice()).width, 
              userSpace.getTopMargin(), 
              userSpace.getDevice().getHeight() - userSpace.getBottomMargin(), 
              startDate, periodType, locale);
          session.setAttribute("SUMMARYIMAGE", "summary.png");
          session.setAttribute("GRAPHINFO", transferObject);
          session.setAttribute("SUMMARYMAP", htmlMap);
          response.setResultCode(0);
        } 
      } 
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
    } 
  }
  
  private String getImageMap(int periods, double totalWidth, double top, double bottom, DateSC startDate, PeriodType periodType, Locale locale) {
    StringBuilder st = new StringBuilder();
    ActivityRecordCalendar cal = new ActivityRecordCalendar(startDate);
    cal.setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
    double step = totalWidth / periods;
    for (int i = 0; i < periods; i++) {
      String title, date = cal.getCurrentTime().toString();
      switch (periodType) {
        case YEAR:
          title = Integer.toString(cal.get(1));
          break;
        case DAY:
          title = cal.getCurrentTime().toString();
          break;
        default:
          title = String.valueOf(cal.getTimePeriod(periodType, locale)) + "/" + cal.get(1);
          break;
      } 
      st.append("<area title=\"" + title + "\" target=\"_top\" href=\"javascript:changeDate('','" + date + "')\" shape=\"rect\" coords=\"" + (step * i) + "," + top + "," + (step * (i + 1) - 1.0D) + "," + bottom + "\" />");
      cal.goNextTimePeriod(periodType);
    } 
    return st.toString();
  }
}
